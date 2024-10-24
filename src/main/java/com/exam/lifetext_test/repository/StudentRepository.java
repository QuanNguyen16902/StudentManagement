package com.exam.lifetext_test.repository;

import com.exam.lifetext_test.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {
}
