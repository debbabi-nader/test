package io.demo.test.services;

import java.util.Collection;

import javax.json.JsonPatch;

import org.springframework.data.domain.Sort;

import io.demo.test.entities.GenericEntity;


public interface GenericService<T extends GenericEntity> {

	public T findById(String id);

	public Collection<T> findAll();
	
	public Collection<T> findAll(Sort sort);
	
	public T create(T t);
	
	public T partialUpdate(JsonPatch jsonPatch, String id);
	
	public void delete(String id);
	
}
