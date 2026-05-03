package com.poojarysaurav.devboard.task.dto;

import com.poojarysaurav.devboard.task.TaskStatus;
import jakarta.validation.constraints.NotBlank;

public record TaskRequest(
        @NotBlank(message = "Title is required")
        String title,

        String description,

        TaskStatus status
) {}
