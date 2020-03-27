package io.demo.test.services.impl;

import java.lang.reflect.ParameterizedType;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import javax.json.JsonPatch;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.demo.test.entities.GenericEntity;
import io.demo.test.exceptions.BadRequestException;
import io.demo.test.exceptions.ForeignKeyIntegrityViolationException;
import io.demo.test.exceptions.MissingRequiredArgumentException;
import io.demo.test.exceptions.ResourceNotFoundException;
import io.demo.test.exceptions.UniqueConstraintViolationException;
import io.demo.test.services.GenericService;
import io.demo.test.utils.JsonPatchUtil;

public abstract class GenericServiceImpl<T extends GenericEntity> implements GenericService<T> {

	protected final JpaRepository<T, String> repository;

	protected final ObjectMapper objectMapper;

	protected final Validator validator;

	public GenericServiceImpl(JpaRepository<T, String> repository, ObjectMapper objectMapper, Validator validator) {
		super();
		this.repository = repository;
		this.objectMapper = objectMapper;
		this.validator = validator;
	}

	@Override
	public T findById(String id) {

		if (id == null || id.trim().equals(""))
			throw new MissingRequiredArgumentException();
		Optional<T> entity = this.repository.findById(id);
		if (!entity.isPresent())
			throw new ResourceNotFoundException(id);
		return entity.get();

	}

	@Override
	public Collection<T> findAll() {

		return this.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));

	}

	@Override
	public Collection<T> findAll(Sort sort) {

		return this.repository.findAll(sort);

	}

	private T persist(T t) {

		try {
			return this.repository.saveAndFlush(t);
		}
		catch (DataIntegrityViolationException e) {
			if (e.getMostSpecificCause() instanceof SQLException
					&& ((SQLException) e.getMostSpecificCause()).getSQLState().equals("23505"))
				throw new UniqueConstraintViolationException(e.getMostSpecificCause());
			throw new BadRequestException(e);
		}

	}

	@Transactional
	@Override
	public T create(T t) {

		t.setId(UUID.randomUUID().toString());
		return this.persist(t);

	}

	@Transactional
	@Override
	public T partialUpdate(JsonPatch jsonPatch, String id) {

		T existingEntity = this.findById(id);
		@SuppressWarnings("unchecked")
		T patchedEntity = JsonPatchUtil.patch(jsonPatch, existingEntity,
				(Class<T>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0],
				this.objectMapper);
		patchedEntity.setId(existingEntity.getId());
		patchedEntity.setCreatedAt(existingEntity.getCreatedAt());
		patchedEntity.setUpdatedAt(existingEntity.getUpdatedAt());
		Set<ConstraintViolation<T>> violations = this.validator.validate(patchedEntity);
		if (!violations.isEmpty())
			throw new ConstraintViolationException(violations);
		return this.persist(patchedEntity);

	}

	@Transactional
	@Override
	public void delete(String id) {

		if (id == null || id.trim().equals(""))
			throw new MissingRequiredArgumentException();
		try {
			this.repository.deleteById(id);
			this.repository.flush();
		}
		catch (EmptyResultDataAccessException e) {
			throw new ResourceNotFoundException(id);
		}
		catch (DataIntegrityViolationException e) {
			if (e.getMostSpecificCause() instanceof SQLException
					&& ((SQLException) e.getMostSpecificCause()).getSQLState().equals("23503"))
				throw new ForeignKeyIntegrityViolationException(e.getMostSpecificCause());
			throw new BadRequestException(e);
		}

	}

}
