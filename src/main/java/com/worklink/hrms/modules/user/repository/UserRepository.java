package com.worklink.hrms.modules.user.repository;

import com.worklink.hrms.modules.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    // Update user status by employee_id
    @Modifying
    @Query("UPDATE User u SET u.status = :status WHERE u.employeeId = :employeeId")
    void updateStatusByEmployeeId(@Param("employeeId") Long employeeId, @Param("status") User.UserStatus status);

    // Find user by employee_id
    Optional<User> findByEmployeeId(Long employeeId);

    List<User> findByRole(User.UserRole role);

    List<User> findByStatus(User.UserStatus status);

    boolean existsByEmail(String email);
}