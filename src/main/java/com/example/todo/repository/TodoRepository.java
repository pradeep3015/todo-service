package com.example.todo.repository;

import com.example.todo.model.TodoModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TodoRepository extends JpaRepository<TodoModel, Integer> {
}
