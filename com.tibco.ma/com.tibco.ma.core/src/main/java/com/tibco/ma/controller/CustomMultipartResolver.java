package com.tibco.ma.controller;

import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUpload;
import org.apache.commons.fileupload.FileUploadBase;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.tibco.ma.listener.FileUploadProgressListener;
/**
 * 
 * @author qiankun<a href="mailto:kuqian@tibco-support.com;qiankun</a>
 * @version $Id$
 * @date 2015/10/13
 */
public class CustomMultipartResolver extends CommonsMultipartResolver {
	
	public CustomMultipartResolver(){
		super();
	}
	
	public CustomMultipartResolver(ServletContext servletContext){
		super();
		setServletContext(servletContext);
	}
	
	private HttpServletRequest request;
		
	/**
	 * 
	 * @param fileItemFactory
	 * @return
	 */
	protected FileUpload newFileUpload(FileItemFactory fileItemFactory){
		ServletFileUpload upload =new ServletFileUpload(fileItemFactory);
		upload.setSizeMax(-1);
		if(request!=null){
			HttpSession session =request.getSession();
			FileUploadProgressListener progressListener = new FileUploadProgressListener(session);
			upload.setProgressListener(progressListener);	
		}
		return upload;
	}
	
	/**
	 * 
	 * @param request
	 * @return
	 * @throws MultipartException
	 */
	public MultipartHttpServletRequest resolveMultipart(HttpServletRequest request) throws MultipartException{
		this.request = request;
        return super.resolveMultipart(request);  
	}
	
	/**
	 * 
	 * @param request
	 * @return
	 * @throws MultipartException
	 */
	@Override
	public MultipartParsingResult parseRequest(HttpServletRequest request) throws MultipartException{
		HttpSession session=request.getSession();
		String encoding =determineEncoding(request);
		FileUpload fileUpload =prepareFileUpload(encoding);
		FileUploadProgressListener progressListener = new FileUploadProgressListener(session);
		fileUpload.setProgressListener(progressListener);
		try {
			List<FileItem> fileItems = ((ServletFileUpload) fileUpload).parseRequest(request);
			return parseFileItems(fileItems, encoding);
		} catch (FileUploadBase.SizeLimitExceededException ex) {
			throw new MaxUploadSizeExceededException(fileUpload.getSizeMax(), ex);
		} catch (FileUploadException ex) {
			throw new MultipartException("Could not parse multipart servlet request", ex);
		}
		
	}
	

}
