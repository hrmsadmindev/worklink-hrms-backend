package com.worklink.hrms.common.repository;

import com.worklink.hrms.common.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {

    @Query("SELECT d FROM Department d WHERE d.status = :status OR d.status IS NULL ORDER BY d.name ASC")
    List<Department> findAllByStatusOrStatusIsNull(String status);

    @Query("SELECT d FROM Department d ORDER BY d.name ASC")
    List<Department> findAllOrderByName();
}
