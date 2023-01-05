package com.cooksys.social_team_3.repositories;

import java.util.Optional;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cooksys.social_team_3.entities.User;
import com.cooksys.social_team_3.entities.Credentials;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

        List<User> findAllByDeleted(boolean deleted);

	Optional<User> findByCredentials_Username(String username);

	Optional<User> findByCredentials(Credentials credential);
  
}
