package com.burakkolay.hazelcastspringboot.business.abstracts;

import com.burakkolay.hazelcastspringboot.entities.Student;

import java.util.List;
import java.util.UUID;

public interface StudentService {
    List<Student> getAll();
    Student getById(UUID id);
    Student add(Student student);
    Student update(UUID id, Student student);
    void delete(UUID id);
}
