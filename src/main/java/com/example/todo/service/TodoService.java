package com.example.todo.service;

import com.example.todo.exception.TodoNotFoundException;
import com.example.todo.model.TodoModel;
import com.example.todo.repository.TodoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class TodoService {

    @Autowired
    TodoRepository todoRepository;


    public List<TodoModel> getAllTodos() {
        return todoRepository.findAll();
    }

    public TodoModel getTodoById(Integer id) {
        return todoRepository.findById(id)
                .orElseThrow(() -> new TodoNotFoundException("TodoModel not found with id: " + id));
    }

    public TodoModel createTodo(TodoModel TodoModel) {
        return todoRepository.save(TodoModel);
    }

    public TodoModel updateTodo(Integer id, TodoModel updatedTodoModel) {
        TodoModel existingTodoModel = getTodoById(id);
        existingTodoModel.setTitle(updatedTodoModel.getTitle());
        existingTodoModel.setDescription(updatedTodoModel.getDescription());
        existingTodoModel.setCompleted(updatedTodoModel.isCompleted());
        return todoRepository.save(existingTodoModel);
    }

    public void deleteTodoById(Integer id) {
        TodoModel TodoModel = getTodoById(id);
        todoRepository.delete(TodoModel);
    }

}
