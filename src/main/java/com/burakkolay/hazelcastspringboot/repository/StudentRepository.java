package com.burakkolay.hazelcastspringboot.repository;

import com.burakkolay.hazelcastspringboot.entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface StudentRepository extends JpaRepository<Student, UUID> {
}
