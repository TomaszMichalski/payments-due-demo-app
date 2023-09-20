package com.projects.paymentsduedemo.util;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class DaysToDateConverter {
    private final Clock clock;

    public LocalDateTime toNowPlusDays(int days) {
        return LocalDateTime.now(clock).plusDays(days);
    }
}
