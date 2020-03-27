package io.demo.test.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import io.demo.test.entities.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, String> {

	@Query("SELECT user FROM UserEntity AS user WHERE user.email LIKE :email")
	public UserEntity getUserByEmail(@Param("email") String email);

}
