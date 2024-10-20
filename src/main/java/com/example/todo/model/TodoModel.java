package com.example.todo.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "todo")
public class TodoModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String title;
    private String description;
    private boolean completed = false;

    public TodoModel() {
    }

    public TodoModel(Integer id, String title, String description, boolean completed) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.completed = completed;
    }
}
