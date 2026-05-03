package com.poojarysaurav.devboard.stats;

import com.poojarysaurav.devboard.auth.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/stats")
@RequiredArgsConstructor
public class StatsController {

    private final StatsService statsService;

    @GetMapping
    public ResponseEntity<Map<String, Object>> getStats(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(statsService.getStatsForUser(user.getId()));
    }
}
