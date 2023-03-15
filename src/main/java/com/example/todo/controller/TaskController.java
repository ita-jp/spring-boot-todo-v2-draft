package com.example.todo.controller;

import com.example.todo.service.TaskEntity;
import com.example.todo.service.TaskService;
import com.example.todo.service.TaskStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @GetMapping("/creationForm")
    public String showCreationForm() {
        return "tasks/creationForm";
    }

    @PostMapping
    public String create(@Validated TaskForm form, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return showCreationForm();
        }
        taskService.create(new TaskEntity(null, form.title(), TaskStatus.valueOf(form.status())));
        return "redirect:/";
    }
}
