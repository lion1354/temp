package com.tibco.ma.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.mongodb.DB;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import com.tibco.ma.common.HttpUtil;
import com.tibco.ma.common.ImageUtils;
import com.tibco.ma.common.StringUtil;
import com.tibco.ma.model.MongoDBConstants;
import com.tibco.ma.service.CoreGridFSService;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

@Controller
@RequestMapping("ma/1/")
public class ShowController {

	@Resource
	private CoreGridFSService coreGridFSService;
	@Autowired
	protected MongoTemplate mongoTemplate;

	private static final int DEFAULT_BUFFER_SIZE = 20480;
	private static final long DEFAULT_EXPIRE_TIME = 604800000L; // a week
	private static final String MULTIPART_BOUNDARY = "MULTIPART_BYTERANGES";

	@ApiOperation(value = "show", notes = "show")
	@RequestMapping(value = "/show", method = RequestMethod.GET)
	public void show(
			@ApiParam(value = "fileUrl", required = true) @RequestParam(value = "fileUrl", required = true) String fileUrl,
			@ApiParam(value = "thumb", required = false) @RequestParam(value = "thumb", required = false) String thumb,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String rootPath = ImageUtils.getParentPath(request);

		File f = new File(rootPath, fileUrl);

		FileInputStream input = new FileInputStream(f);
		ServletOutputStream output = response.getOutputStream();

		// byte[] buf = new byte[input.available()];
		byte[] buffer = new byte[1024];
		int length;
		while ((length = input.read(buffer)) != -1) {
			output.write(buffer, 0, length);
		}
		output.flush();
		output.close();
		input.close();
	}

	@ApiOperation(value = "show image", notes = "show image")
	@RequestMapping(value = "showImage", method = RequestMethod.GET)
	public void showImage(
			@ApiParam(value = "fileUrl") @RequestParam("fileUrl") String fileURL,
			HttpServletResponse response) throws IOException {
		Query query = new Query();
		query.addCriteria(Criteria.where(MongoDBConstants.DOCUMENT_ID).is(new ObjectId(fileURL)));
		GridFSDBFile gridFSDBFile = coreGridFSService.querySFDBFileOne(query);
		if (gridFSDBFile != null) {
			OutputStream sos = response.getOutputStream();
			gridFSDBFile.writeTo(sos);
			sos.flush();
			sos.close();
		}

	}

	@ApiOperation(value = "show result", notes = "show result")
	@RequestMapping(value = "showRes", method = RequestMethod.GET)
	public void showRes(
			@ApiParam(value = "fileUrl") @RequestParam("fileUrl") String fileURL,
			@ApiParam(value = "model") @RequestParam("model") String model,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		processRequest(fileURL, model, request, response);
	}

	@ApiOperation(value = "show rest resource", notes = "show rest resource")
	@RequestMapping(value = "shoRestResource", method = RequestMethod.GET)
	public void showRestResource(
			@ApiParam(value = "fileUrl") @RequestParam("fileUrl") String fileURL,
			@ApiParam(value = "model") @RequestParam("model") String model,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		processRequest(fileURL, model, request, response);
	}

	private void processRequest(String fileURL, String model,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		DB db = mongoTemplate.getDb();
		GridFS gridFS = new GridFS(db, model);

		GridFSDBFile file = (GridFSDBFile) gridFS.find(new ObjectId(fileURL));

		if (null == file) {
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}

		InputStream dataStream = file.getInputStream();

		long length = file.getLength();
		String fileName = file.getFilename();
		Date lastModifiedObj = file.getUploadDate();

		if (StringUtil.isEmpty(fileName) || lastModifiedObj == null) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return;
		}
		long lastModified = lastModifiedObj.getTime();
		String contentType = file.getContentType();

		if (ImageUtils.checkImageByName(file.getFilename())) {
			String[] s = file.getFilename().split("\\.");
			contentType = "image/" + s[s.length - 1];
			response.setContentType(contentType);
		}

		// Validate request headers for caching
		// ---------------------------------------------------

		// If-None-Match header should contain "*" or ETag. If so, then return
		// 304.
		String ifNoneMatch = request.getHeader("If-None-Match");
		if (ifNoneMatch != null && HttpUtil.matches(ifNoneMatch, fileName)) {
			response.setHeader("ETag", fileName); // Required in 304.
			response.sendError(HttpServletResponse.SC_NOT_MODIFIED);
			return;
		}

		// If-Modified-Since header should be greater than LastModified. If so,
		// then return 304.
		// This header is ignored if any If-None-Match header is specified.
		long ifModifiedSince = request.getDateHeader("If-Modified-Since");
		if (ifNoneMatch == null && ifModifiedSince != -1 && ifModifiedSince + 1000 > lastModified) {
			response.setHeader("ETag", fileName); // Required in 304.
			response.sendError(HttpServletResponse.SC_NOT_MODIFIED);
			return;
		}

		// Validate request headers for resume
		// ----------------------------------------------------

		// If-Match header should contain "*" or ETag. If not, then return 412.
		String ifMatch = request.getHeader("If-Match");
		if (ifMatch != null && !HttpUtil.matches(ifMatch, fileName)) {
			response.sendError(HttpServletResponse.SC_PRECONDITION_FAILED);
			return;
		}

		// If-Unmodified-Since header should be greater than LastModified. If
		// not, then return 412.
		long ifUnmodifiedSince = request.getDateHeader("If-Unmodified-Since");
		if (ifUnmodifiedSince != -1 && ifUnmodifiedSince + 1000 <= lastModified) {
			response.sendError(HttpServletResponse.SC_PRECONDITION_FAILED);
			return;
		}

		// Validate and process range
		// -------------------------------------------------------------

		// Prepare some variables. The full Range represents the complete file.
		Range full = new Range(0, length - 1, length);
		List<Range> ranges = new ArrayList<Range>();

		// Validate and process Range and If-Range headers.
		String range = request.getHeader("Range");
		if (range != null) {

			// Range header should match format "bytes=n-n,n-n,n-n...". If not,
			// then return 416.
			if (!range.matches("^bytes=\\d*-\\d*(,\\d*-\\d*)*$")) {
				response.setHeader("Content-Range", "bytes */" + length);
				response.sendError(HttpServletResponse.SC_REQUESTED_RANGE_NOT_SATISFIABLE);
				return;
			}

			String ifRange = request.getHeader("If-Range");
			if (ifRange != null && !ifRange.equals(fileName)) {
				try {
					long ifRangeTime = request.getDateHeader("If-Range");
					if (ifRangeTime != -1) {
						ranges.add(full);
					}
				} catch (IllegalArgumentException ignore) {
					ranges.add(full);
				}
			}

			// If any valid If-Range header, then process each part of byte
			// range.
			if (ranges.isEmpty()) {
				for (String part : range.substring(6).split(",")) {
					// Assuming a file with length of 100, the following
					// examples returns bytes at:
					// 50-80 (50 to 80), 40- (40 to length=100), -20
					// (length-20=80 to length=100).
					long start = StringUtil.sublong(part, 0, part.indexOf("-"));
					long end = StringUtil.sublong(part, part.indexOf("-") + 1,
							part.length());

					if (start == -1) {
						start = length - end;
						end = length - 1;
					} else if (end == -1 || end > length - 1) {
						end = length - 1;
					}

					// Check if Range is syntactically valid. If not, then
					// return 416.
					if (start > end) {
						response.setHeader("Content-Range", "bytes */" + length);
						response.sendError(HttpServletResponse.SC_REQUESTED_RANGE_NOT_SATISFIABLE);
						return;
					}

					// Add range.
					ranges.add(new Range(start, end, length));
				}
			}
		}

		// Prepare and initialize response
		// --------------------------------------------------------

		// Get content type by file name and set content disposition.
		String disposition = "inline";

		// If content type is unknown, then set the default value.
		if (contentType == null) {
			contentType = "application/octet-stream";
		} else if (!contentType.startsWith("image")) {
			// Else, expect for images, determine content disposition. If
			// content type is supported by
			// the browser, then set to inline, else attachment which will pop a
			// 'save as' dialogue.
			String accept = request.getHeader("Accept");
			disposition = accept != null
					&& HttpUtil.accepts(accept, contentType) ? "inline"
					: "attachment";
		}

		// Initialize response.
		response.reset();
		response.setBufferSize(DEFAULT_BUFFER_SIZE);
		response.setHeader("Content-Disposition", disposition + ";filename=\""
				+ fileName + "\"");
		response.setHeader("Accept-Ranges", "bytes");
		response.setHeader("ETag", fileName);
		response.setDateHeader("Last-Modified", lastModified);
		response.setDateHeader("Expires", System.currentTimeMillis()
				+ DEFAULT_EXPIRE_TIME);

		// Send requested file (part(s)) to client
		// ------------------------------------------------

		// Prepare streams.
		InputStream input = null;
		OutputStream output = null;

		try {
			// Open streams.
			input = new BufferedInputStream(dataStream);
			output = new BufferedOutputStream(response.getOutputStream());

			if (ranges.isEmpty() || ranges.get(0) == full) {

				// Return full file.
				Range r = full;
				response.setContentType(contentType);
				response.setHeader("Content-Range", "bytes " + r.start + "-"
						+ r.end + "/" + r.total);
				response.setHeader("Content-Length", String.valueOf(r.length));
				copy(input, output, length, r.start, r.length);

			} else if (ranges.size() == 1) {

				// Return single part of file.
				Range r = (Range) ranges.get(0);
				response.setContentType(contentType);
				response.setHeader("Content-Range", "bytes " + r.start + "-"
						+ r.end + "/" + r.total);
				response.setHeader("Content-Length", String.valueOf(r.length));
				response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT); // 206.

				// Copy single part range.
				copy(input, output, length, r.start, r.length);

			} else {

				// Return multiple parts of file.
				response.setContentType("multipart/byteranges; boundary="
						+ MULTIPART_BOUNDARY);
				response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT); // 206.

				// Copy multi part range.
				for (Range r : ranges) {

					// Copy single part range of multi part range.
					copy(input, output, length, r.start, r.length);
				}

			}
		} finally {
			// Gently close streams.
			close(output);
			close(input);
			close(dataStream);
		}

	}

	private static void close(Closeable resource) {
		if (resource != null) {
			try {
				resource.close();
			} catch (IOException ignore) {
			}
		}
	}

	private static void copy(InputStream input, OutputStream output,
			long inputSize, long start, long length) throws IOException {
		byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
		int read;

		if (inputSize == length) {
			// Write full range.
			while ((read = input.read(buffer)) > 0) {
				output.write(buffer, 0, read);
				output.flush();
			}
		} else {
			input.skip(start);
			long toRead = length;

			while ((read = input.read(buffer)) > 0) {
				if ((toRead -= read) > 0) {
					output.write(buffer, 0, read);
					output.flush();
				} else {
					output.write(buffer, 0, (int) toRead + read);
					output.flush();
					break;
				}
			}
		}
	}

	protected class Range {
		long start;
		long end;
		long length;
		long total;

		public Range(long start, long end, long total) {
			this.start = start;
			this.end = end;
			this.length = end - start + 1;
			this.total = total;
		}
	}

}
