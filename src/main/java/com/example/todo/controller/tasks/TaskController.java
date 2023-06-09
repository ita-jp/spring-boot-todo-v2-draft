package com.example.todo.controller.tasks;

import com.example.todo.service.tasks.TaskEntity;
import com.example.todo.service.tasks.TaskService;
import com.example.todo.service.tasks.TaskStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @GetMapping
    public String index(TaskQueryForm query, Model model) {
        model.addAttribute("taskList", taskService.find(query.toEntity()));
        model.addAttribute("taskQuery", query.toDTO());
        return "tasks/list";
    }


    @GetMapping("/creationForm")
    public String showCreationForm(@ModelAttribute TaskForm form, Model model) {
        model.addAttribute("formMethod", "post");
        model.addAttribute("formAction", "/tasks");
        model.addAttribute("buttonName", "作成");
        return "tasks/form";
    }

    @PostMapping
    public String create(@Validated TaskForm form, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "tasks/form";
        }
        taskService.create(new TaskEntity(null, form.summary(), form.description(), TaskStatus.valueOf(form.status())));
        return "redirect:/tasks";
    }

    @GetMapping("/{id}")
    public String showDetail(@PathVariable Long id, Model model) {
        var taskOpt = taskService.findById(id).map(TaskDTO::toDTO);
        if (taskOpt.isEmpty()) {
            return "redirect:/";
        }
        model.addAttribute("task", taskOpt.get());
        return "tasks/detail";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") long id) {
        taskService.delete(id);
        return "redirect:/tasks";
    }

    @GetMapping("/{id}/editForm")
    public String showEditForm(@PathVariable("id") long id, Model model) {
        var form = taskService.findById(id)
                .map(TaskForm::toForm)
                .orElseThrow(() -> new IllegalArgumentException("Invalid task Id:" + id));

        model.addAttribute("taskForm", form);
        model.addAttribute("formMethod", "put");
        model.addAttribute("formAction", "/tasks/" + id);
        model.addAttribute("buttonName", "更新");
        return "tasks/form";
    }

    @PutMapping("/{id}")
    public String update(@PathVariable("id") long id, @Validated TaskForm form, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "tasks/form";
        }

        taskService.update(form.toEntity(id));
        return "redirect:/tasks/{id}";
    }
}
