package com.example.todo.controller;

import com.example.todo.model.TodoModel;
import com.example.todo.service.TodoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TodoController.class)
class TodoControllerTest {

    private MockMvc mockMvc;

    @MockBean
    private TodoService todoService;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new TodoController(todoService)).build();
    }

    @Test
    void getAllTodos_ReturnsListOfTodos() throws Exception {
        // Arrange
        TodoModel todo1 = new TodoModel(1, "Title 1", "Description 1", false);
        TodoModel todo2 = new TodoModel(2, "Title 2", "Description 2", true);
        List<TodoModel> todoList = Arrays.asList(todo1, todo2);

        when(todoService.getAllTodos()).thenReturn(todoList);

        // Act & Assert
        mockMvc.perform(get("/api/todos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].title").value("Title 1"))
                .andExpect(jsonPath("$[1].title").value("Title 2"));

        verify(todoService, times(1)).getAllTodos();
    }

    @Test
    void getTodoById_ValidId_ReturnsTodo() throws Exception {
        // Arrange
        TodoModel todo = new TodoModel(1, "Title", "Description", false);
        when(todoService.getTodoById(1)).thenReturn(todo);

        // Act & Assert
        mockMvc.perform(get("/api/todos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Title"))
                .andExpect(jsonPath("$.description").value("Description"));

        verify(todoService, times(1)).getTodoById(1);
    }

    @Test
    void createTodo_ValidTodo_ReturnsCreatedTodo() throws Exception {
        // Arrange
        TodoModel todo = new TodoModel(1, "New Title", "New Description", false);
        when(todoService.createTodo(any(TodoModel.class))).thenReturn(todo);

        // Act & Assert
        mockMvc.perform(post("/api/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\": \"New Title\", \"description\": \"New Description\", \"completed\": false}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("New Title"))
                .andExpect(jsonPath("$.description").value("New Description"));

        verify(todoService, times(1)).createTodo(any(TodoModel.class));
    }

    @Test
    void updateTodo_ValidId_ReturnsUpdatedTodo() throws Exception {
        // Arrange
        TodoModel updatedTodo = new TodoModel(1, "Updated Title", "Updated Description", true);
        when(todoService.updateTodo(eq(1), any(TodoModel.class))).thenReturn(updatedTodo);

        // Act & Assert
        mockMvc.perform(put("/api/todos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\": \"Updated Title\", \"description\": \"Updated Description\", \"completed\": true}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Title"))
                .andExpect(jsonPath("$.description").value("Updated Description"))
                .andExpect(jsonPath("$.completed").value(true));

        verify(todoService, times(1)).updateTodo(eq(1), any(TodoModel.class));
    }

    @Test
    void deleteTodoById_ValidId_ReturnsNoContent() throws Exception {
        // Act & Assert
        mockMvc.perform(delete("/api/todos/1"))
                .andExpect(status().isNoContent());

        verify(todoService, times(1)).deleteTodoById(1);
    }

}