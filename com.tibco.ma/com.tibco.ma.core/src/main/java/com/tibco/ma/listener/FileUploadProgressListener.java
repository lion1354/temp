package com.tibco.ma.listener;

import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.ProgressListener;
import org.apache.log4j.Logger;

import com.tibco.ma.common.log.Log;
import com.tibco.ma.model.Progress;
/**
 * 
 * @author qiankun<a href="mailto:kuqian@tibco-support.com;qiankun</a>
 * @version $Id$
 * @date 2015/10/13
 */
public class FileUploadProgressListener implements ProgressListener {

	private Logger logger = Logger.getLogger(FileUploadProgressListener.class);

	private HttpSession session;

	public FileUploadProgressListener() {
	}

	public FileUploadProgressListener(HttpSession session) {
		this.session = session;
		Progress status = new Progress();
		session.setAttribute("upload_Progress", status);
	}

	@Log(operate = "upload", modelName = "upload")
	@Override
	public void update(long pBytesRead, long pContentLength, int pItems) {
		Progress status = (Progress) session.getAttribute("upload_Progress");
		status.setBytesRead(pBytesRead);
		status.setContentLength(pContentLength);
		status.setItems(pItems);
		session.setAttribute("upload_Progress", status);
		logger.debug(pItems + "  " + System.currentTimeMillis());

	}

}
