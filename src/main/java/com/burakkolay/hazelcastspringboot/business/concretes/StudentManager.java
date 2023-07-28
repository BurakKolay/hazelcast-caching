package com.burakkolay.hazelcastspringboot.business.concretes;

import com.burakkolay.hazelcastspringboot.business.abstracts.StudentService;
import com.burakkolay.hazelcastspringboot.entities.Student;
import com.burakkolay.hazelcastspringboot.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class StudentManager implements StudentService {
    private final StudentRepository repository;

    @Override
    public List<Student> getAll() {
        return repository.findAll();
    }

    @Override
    public Student getById(UUID id) {
        return repository.findById(id).orElseThrow();
    }

    @Override
    public Student add(Student student) {
        return repository.save(student);
    }

    @Override
    public Student update(UUID id, Student student) {
        return repository.save(student);
    }

    @Override
    public void delete(UUID id) {
        repository.deleteById(id);
    }
}
