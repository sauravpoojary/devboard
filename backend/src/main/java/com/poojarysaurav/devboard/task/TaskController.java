package com.poojarysaurav.devboard.task;

import com.poojarysaurav.devboard.auth.User;
import com.poojarysaurav.devboard.task.dto.TaskRequest;
import com.poojarysaurav.devboard.task.dto.TaskResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @GetMapping
    public ResponseEntity<List<TaskResponse>> getTasks(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(taskService.getTasksByUser(user.getId()));
    }

    @PostMapping
    public ResponseEntity<TaskResponse> createTask(
            @Valid @RequestBody TaskRequest request,
            @AuthenticationPrincipal User user) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(taskService.createTask(request, user.getId()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskResponse> updateTask(
            @PathVariable Long id,
            @Valid @RequestBody TaskRequest request,
            @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(taskService.updateTask(id, request, user.getId()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(
            @PathVariable Long id,
            @AuthenticationPrincipal User user) {
        taskService.deleteTask(id, user.getId());
        return ResponseEntity.noContent().build();
    }
}
