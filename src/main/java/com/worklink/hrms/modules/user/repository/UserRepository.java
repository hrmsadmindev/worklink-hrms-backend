package com.worklink.hrms.modules.user.repository;

import com.worklink.hrms.modules.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    List<User> findByRole(User.UserRole role);

    List<User> findByStatus(User.UserStatus status);

    boolean existsByEmail(String email);
}