package io.demo.test.entities;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class GenericEntity implements Serializable {

	private static final long serialVersionUID = -3875555654366769757L;

	@Id
	protected String id;

	@CreatedDate
	@Column(nullable = false)
	protected LocalDateTime createdAt;

	@LastModifiedDate
	@Column(nullable = false)
	protected LocalDateTime updatedAt;

	public GenericEntity() {
		super();
	}

	public GenericEntity(String id, LocalDateTime createdAt, LocalDateTime updatedAt) {
		super();
		this.id = id;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@JsonProperty(access = Access.READ_ONLY)
	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	@JsonProperty(access = Access.READ_ONLY)
	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}

}
