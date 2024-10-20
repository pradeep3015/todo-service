package com.example.todo.service;

import com.example.todo.exception.TodoNotFoundException;
import com.example.todo.model.TodoModel;
import com.example.todo.repository.TodoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TodoServiceTest {

    @Mock
    private TodoRepository todoRepository;

    @InjectMocks
    private TodoService todoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Initialize mocks
    }

    @Test
    void getAllTodos_ReturnsListOfTodos() {
        // Arrange
        TodoModel todo1 = new TodoModel(1, "Title 1", "Description 1", false);
        TodoModel todo2 = new TodoModel(2, "Title 2", "Description 2", true);
        List<TodoModel> todoList = Arrays.asList(todo1, todo2);

        when(todoRepository.findAll()).thenReturn(todoList);

        // Act
        List<TodoModel> result = todoService.getAllTodos();

        // Assert
        assertEquals(2, result.size());
        verify(todoRepository, times(1)).findAll();
    }

    @Test
    void getTodoById_ValidId_ReturnsTodo() {
        // Arrange
        TodoModel todo = new TodoModel(1, "Title", "Description", false);
        when(todoRepository.findById(1)).thenReturn(Optional.of(todo));

        // Act
        TodoModel result = todoService.getTodoById(1);

        // Assert
        assertNotNull(result);
        assertEquals("Title", result.getTitle());
        verify(todoRepository, times(1)).findById(1);
    }

    @Test
    void getTodoById_InvalidId_ThrowsTodoNotFoundException() {
        // Arrange
        when(todoRepository.findById(1)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(TodoNotFoundException.class, () -> todoService.getTodoById(1));
        verify(todoRepository, times(1)).findById(1);
    }

    @Test
    void createTodo_SavesAndReturnsTodo() {
        // Arrange
        TodoModel todo = new TodoModel(1, "Title", "Description", false);
        when(todoRepository.save(todo)).thenReturn(todo);

        // Act
        TodoModel result = todoService.createTodo(todo);

        // Assert
        assertNotNull(result);
        assertEquals(todo.getTitle(), result.getTitle());
        verify(todoRepository, times(1)).save(todo);
    }

    @Test
    void updateTodo_ValidId_UpdatesAndReturnsTodo() {
        // Arrange
        TodoModel existingTodo = new TodoModel(1, "Old Title", "Old Description", false);
        TodoModel updatedTodo = new TodoModel(1, "New Title", "New Description", true);
        when(todoRepository.findById(1)).thenReturn(Optional.of(existingTodo));
        when(todoRepository.save(existingTodo)).thenReturn(existingTodo);

        // Act
        TodoModel result = todoService.updateTodo(1, updatedTodo);

        // Assert
        assertEquals("New Title", result.getTitle());
        assertEquals("New Description", result.getDescription());
        assertTrue(result.isCompleted());
        verify(todoRepository, times(1)).findById(1);
        verify(todoRepository, times(1)).save(existingTodo);
    }

    @Test
    void deleteTodoById_ValidId_DeletesTodo() {
        // Arrange
        TodoModel todo = new TodoModel(1, "Title", "Description", false);
        when(todoRepository.findById(1)).thenReturn(Optional.of(todo));

        // Act
        todoService.deleteTodoById(1);

        // Assert
        verify(todoRepository, times(1)).delete(todo);
    }

    @Test
    void deleteTodoById_InvalidId_ThrowsTodoNotFoundException() {
        // Arrange
        when(todoRepository.findById(1)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(TodoNotFoundException.class, () -> todoService.deleteTodoById(1));
        verify(todoRepository, times(1)).findById(1);
    }

}