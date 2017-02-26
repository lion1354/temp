package com.tibco.ma.common;

import java.io.IOException;

import org.bson.types.ObjectId;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class ObjectIdJsonSerializer extends JsonSerializer<ObjectId> {
	@Override
	public void serialize(ObjectId objId, JsonGenerator jsonGenerator,
			SerializerProvider serializerProvider) throws IOException,
			JsonProcessingException {
		if (objId == null) {
			jsonGenerator.writeNull();
		} else {
			jsonGenerator.writeString(objId.toString());
		}
	}
}