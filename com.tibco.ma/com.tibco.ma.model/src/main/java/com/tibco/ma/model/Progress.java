package com.tibco.ma.model;

import javax.xml.bind.annotation.XmlRootElement;

import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.tibco.ma.common.NumUtil;

/**
 * 
 * @author qiankun<a href="mailto:kuqian@tibco-support.com;qiankun</a>
 * @version $Id$
 * @date 2015/10/13
 */
@XmlRootElement
@JsonInclude(Include.NON_NULL)
@JsonPropertyOrder(alphabetic = true)
@Document(collection = "progress")
public class Progress {

	/** Read the byte **/
	private long bytesRead = 0L;

	/** Read the MB **/
	private String mbRead = "0";

	/** The total number of byte **/
	private long contentLength = 0L;

	private int items;
	/** the precentage has been read **/
	private String percent;
	/** read speed **/
	private String speed;
	/** start time **/
	private long startReadTime = System.currentTimeMillis();

	public long getBytesRead() {
		return bytesRead;
	}

	public void setBytesRead(long bytesRead) {
		this.bytesRead = bytesRead;
	}

	public String getMbRead() {
		mbRead = NumUtil.divideNumber(bytesRead, 1000000);
		return mbRead;
	}

	public void setMbRead(String mbRead) {
		this.mbRead = mbRead;
	}

	public long getContentLength() {
		return contentLength;
	}

	public void setContentLength(long contentLength) {
		this.contentLength = contentLength;
	}

	public int getItems() {
		return items;
	}

	public void setItems(int items) {
		this.items = items;
	}

	public String getPercent() {
		percent = NumUtil.getPercent(bytesRead, contentLength);
		System.out.println("percent:" + percent);
		return percent;
	}

	public void setPercent(String percent) {
		this.percent = percent;
	}

	public String getSpeed() {
		speed = NumUtil.divideNumber(NumUtil.divideNumber(bytesRead * 1000, System.currentTimeMillis() - startReadTime),
				1000);
		return speed;
	}

	public void setSpeed(String speed) {
		this.speed = speed;
	}

	public long getStartReadTime() {
		return startReadTime;
	}

	public void setStartReadTime(long startReadTime) {
		this.startReadTime = startReadTime;
	}

}
