package com.tibco.ma.model.core;

import javax.xml.bind.annotation.XmlRootElement;

import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@XmlRootElement
@JsonInclude(Include.NON_NULL)
@JsonPropertyOrder(alphabetic = true)
@Document(collection = "rest_query_type")
public enum QueryType {
	/*
	 * e.g. where={"score":{"$gte":1000,"$lte":3000}}
	 */
	LT("$lt"), // Less Than
	LTE("$lte"), // Less Than Or Equal To
	GT("$gt"), // Greater Than
	GTE("$gte"), // Greater Than Or Equal To
	NE("$ne"), // Not Equal To
	/*
	 * e.g. where={"score":{"$in":[1,3,5,7,9]}}
	 */
	IN("$in"), // Contained In
	NIN("$nin"), // Not Contained in
	/*
	 * e.g. where={"score":{"$exists":true}}
	 */
	EXIST("$exists"), // A value is set for the key
	/*
	 * e.g. where={"hometown":{"$select":{"query":{"className":"Team","where":{"winPct":{"$gt":0.5}}},"key":"city"}}}
	 */
	SELECT("$select"), // This matches a value for a key in the result of a different query
	DONTSELECT("$dontSelect"), // Requires that a key's value not match a value for a key in the result of a different query
	/*
	 * e.g. where={"arrayKey":{"$all":[2,3,4]}}
	 */
	ALL("$all"), // Contains all of the given values
	/*
	 * e.g. where={"name":{"$regex":"^Big Daddy"}}
	 */
	REGULAR("$regex"), // Requires that a key's value match a regular expression
	/*
	 * e.g. where={"post":{"$inQuery":{"where":{"image":{"$exists":true}},"className":"Post"}}}
	 */
	INQUERY("$inQuery"), // default limit of 100 and maximum limit of 1000, find relatedId
	/*
	 * e.g. where={"post":{"$notInQuery":{"where":{"image":{"$exists":true}},"className":"Post"}}}
	 */
	NINQUERY("$notInQuery"), // default limit of 100 and maximum limit of 1000, find relatedId
	/*
	 * e.g. where={"$relatedTo":{"object":{"__type":"Pointer","className":"Post","objectId":"8TOXdXf3tz"},"key":"likes"}}
	 */
	RELATETO("$relatedTo"), // object, key
	/*
	 * e.g. where={"$or":[{"wins":{"$gt":150}},{"wins":{"$lt":5}}]}
	 */
	OR("$or"),
	/*
	 * e.g. where={"$and":[{"createdAt":{"$gte":{"__type": "Date", "iso": "1433922490485"}}}, {"createdAt":{"$lte":{"__type": "Date", "iso": "1433922490485"}}}]}
	 */
	AND("$and");
	
	private String type;
	
	QueryType(String type) {
	      this.type = type;
	}
	
	public String getType() {
	      return type;
	}
	
	
	public static QueryType from(String type) {
		if (type == null) {
			return null;
		}

		if (type.equals(LT.type)) {
			return LT;
		} else if (type.equals(LTE.type)) {
			return LTE;
		} else if (type.equals(GT.type)) {
			return GT;
		} else if (type.equals(GTE.type)) {
			return GTE;
		} else if (type.equals(NE.type)) {
			return NE;
		} else if (type.equals(IN.type)) {
			return IN;
		} else if (type.equals(NIN.type)) {
			return NIN;
		} else if (type.equals(EXIST.type)) {
			return EXIST;
		} else if (type.equals(SELECT.type)) {
			return SELECT;
		} else if (type.equals(DONTSELECT.type)) {
			return DONTSELECT;
		} else if (type.equals(ALL.type)) {
			return ALL;
		} else if (type.equals(REGULAR.type)) {
			return REGULAR;
		} else if (type.equals(INQUERY.type)) {
			return INQUERY;
		} else if (type.equals(NINQUERY.type)) {
			return NINQUERY;
		} else if (type.equals(RELATETO.type)) {
			return RELATETO;
		} else if (type.equals(OR.type)) {
			return OR;
		} else if (type.equals(AND.type)) {
			return AND;
		}
		return null;
	}
}
