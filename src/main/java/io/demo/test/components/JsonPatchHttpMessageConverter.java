package io.demo.test.components;

import java.io.IOException;

import javax.json.Json;
import javax.json.JsonPatch;
import javax.json.JsonReader;

import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.stereotype.Component;


@Component
public class JsonPatchHttpMessageConverter extends AbstractHttpMessageConverter<JsonPatch> {

	public JsonPatchHttpMessageConverter() {
		super(MediaType.valueOf("application/json-patch+json"));
	}

	@Override
	protected boolean supports(Class<?> clazz) {
		
		return JsonPatch.class.isAssignableFrom(clazz);
	
	}

	@Override
	protected JsonPatch readInternal(Class<? extends JsonPatch> clazz, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
	
		try (JsonReader reader = Json.createReader(inputMessage.getBody())) {
	        return Json.createPatch(reader.readArray());
	    } catch (Exception e) {
	        throw new HttpMessageNotReadableException(e.getMessage(), inputMessage);
	    }
	
	}

	@Override
	protected void writeInternal(JsonPatch jsonPatch, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
		
	}

}
