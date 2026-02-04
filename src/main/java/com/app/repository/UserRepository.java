package com.app.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.app.entity.UserDetails;


@Repository
public interface UserRepository extends CrudRepository<UserDetails, String> {
	UserDetails findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
}
