package com.yigit.social_media.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.yigit.social_media.model.User;
import com.yigit.social_media.enums.RoleTypes;
import java.util.List;
import java.util.Optional;
public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findAllByRole(RoleTypes role);

  Optional<User> findByUsername(String username);

boolean existsByUsername(String username);

boolean existsByEmail(String email);

List<User> findByUsernameContainingIgnoreCase(String query);
}
