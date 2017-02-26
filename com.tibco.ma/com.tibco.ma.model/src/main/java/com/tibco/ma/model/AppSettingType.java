package com.tibco.ma.model;

import javax.xml.bind.annotation.XmlRootElement;

import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
/**
 * 
 * @author qiankun<a href="mailto:kuqian@tibco-support.com;qiankun</a>
 * @version $Id$
 * @date 2015/10/13
 */
@XmlRootElement
@JsonInclude(Include.NON_NULL)
@JsonPropertyOrder(alphabetic = true)
@Document(collection = "app_setting_type")
public enum AppSettingType {
	String, Number, Boolean, Date, File, GeoPoint, Array, Object
}
