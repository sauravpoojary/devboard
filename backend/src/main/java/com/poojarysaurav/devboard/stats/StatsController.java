package com.poojarysaurav.devboard.stats;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/stats")
@RequiredArgsConstructor
public class StatsController {

    private final StatsService statsService;

    @GetMapping
    public ResponseEntity<Map<String, Object>> getStats(@RequestParam Long userId) {
        return ResponseEntity.ok(statsService.getStatsForUser(userId));
    }
}
