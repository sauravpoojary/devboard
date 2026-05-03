package com.poojarysaurav.devboard.task.dto;

import com.poojarysaurav.devboard.task.Task;
import com.poojarysaurav.devboard.task.TaskStatus;

import java.time.Instant;

public record TaskResponse(
        Long id,
        String title,
        String description,
        TaskStatus status,
        Long userId,
        Instant createdAt,
        Instant updatedAt
) {
    public static TaskResponse from(Task task) {
        return new TaskResponse(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus(),
                task.getUser().getId(),
                task.getCreatedAt(),
                task.getUpdatedAt()
        );
    }
}
