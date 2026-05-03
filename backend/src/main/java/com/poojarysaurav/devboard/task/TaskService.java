package com.poojarysaurav.devboard.task;

import com.poojarysaurav.devboard.auth.User;
import com.poojarysaurav.devboard.auth.UserRepository;
import com.poojarysaurav.devboard.common.exception.ResourceNotFoundException;
import com.poojarysaurav.devboard.task.dto.TaskRequest;
import com.poojarysaurav.devboard.task.dto.TaskResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public List<TaskResponse> getTasksByUser(Long userId) {
        return taskRepository.findByUserId(userId)
                .stream().map(TaskResponse::from).toList();
    }

    public TaskResponse createTask(TaskRequest request, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userId));

        Task task = Task.builder()
                .title(request.title())
                .description(request.description())
                .status(request.status() != null ? request.status() : TaskStatus.TODO)
                .user(user)
                .build();

        return TaskResponse.from(taskRepository.save(task));
    }

    public TaskResponse updateTask(Long taskId, TaskRequest request, Long userId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found: " + taskId));

        if (!task.getUser().getId().equals(userId)) {
            throw new SecurityException("Access denied");
        }

        task.setTitle(request.title());
        task.setDescription(request.description());
        if (request.status() != null) task.setStatus(request.status());

        return TaskResponse.from(taskRepository.save(task));
    }

    public void deleteTask(Long taskId, Long userId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found: " + taskId));

        if (!task.getUser().getId().equals(userId)) {
            throw new SecurityException("Access denied");
        }

        taskRepository.delete(task);
    }
}
