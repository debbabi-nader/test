package io.demo.test.utils;

import javax.json.JsonException;
import javax.json.JsonPatch;
import javax.json.JsonStructure;
import javax.json.JsonValue;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.demo.test.entities.GenericEntity;
import io.demo.test.exceptions.JsonPatchException;


public class JsonPatchUtil {

	private JsonPatchUtil() {
		
	}
	
	public static <T extends GenericEntity> T patch(JsonPatch jsonPatch, T targetEntity, Class<T> targetEntityClass, ObjectMapper objectMapper) {
		
		JsonStructure targetJson = objectMapper.convertValue(targetEntity, JsonStructure.class);
		JsonValue patchedJson;
		
		try {
			patchedJson = jsonPatch.apply(targetJson);
		} catch (JsonException e) {
			throw new JsonPatchException(e);
		}
		
		return objectMapper.convertValue(patchedJson, targetEntityClass);

	}
	
}
