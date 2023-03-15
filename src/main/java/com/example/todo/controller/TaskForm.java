package com.example.todo.controller;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TaskForm(
        @NotBlank
        String summary,
        String description,
        @NotNull
        String status
) {
}