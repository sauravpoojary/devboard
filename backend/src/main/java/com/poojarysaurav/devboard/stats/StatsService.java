package com.poojarysaurav.devboard.stats;

import com.poojarysaurav.devboard.task.TaskRepository;
import com.poojarysaurav.devboard.task.TaskStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class StatsService {

    private final TaskRepository taskRepository;

    public Map<String, Object> getStatsForUser(Long userId) {
        long total = taskRepository.findByUserId(userId).size();
        long todo = taskRepository.findByUserIdAndStatus(userId, TaskStatus.TODO).size();
        long inProgress = taskRepository.findByUserIdAndStatus(userId, TaskStatus.IN_PROGRESS).size();
        long done = taskRepository.findByUserIdAndStatus(userId, TaskStatus.DONE).size();

        return Map.of(
                "total", total,
                "todo", todo,
                "inProgress", inProgress,
                "done", done
        );
    }
}
