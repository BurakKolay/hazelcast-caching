package com.burakkolay.hazelcastspringboot.api.controllers;

import com.burakkolay.hazelcastspringboot.business.abstracts.StudentService;
import com.burakkolay.hazelcastspringboot.entities.Student;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/students")
@CacheConfig(cacheNames = "students")
public class StudentController {
    private final StudentService service;

    @GetMapping
    @Cacheable(value = "student_list")
    public List<Student> getAll(){
        return service.getAll();
    }

    @GetMapping("/{id}")
    @Cacheable(key = "#id")
    public Student getById(@PathVariable UUID id){
        return service.getById(id);
    }

    @PostMapping
    @CacheEvict(value = "student_list",allEntries = true)
    public Student add(@RequestBody Student student){
        return service.add(student);
    }

    @PutMapping("/{id}")
    @CachePut(key = "#id")
    @CacheEvict(value = "student_list",allEntries = true)
    public Student update(@PathVariable UUID id,@RequestBody Student student ){
        return service.update(id,student);
    }

    @DeleteMapping("/{id}")
    @CachePut(key = "#id")
    @CacheEvict(value = "student_list",allEntries = true)
    public void delete(@PathVariable UUID id){
        service.delete(id);
    }
}
