package com.projects.paymentsduedemo.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class DaysToDateConverterTest {
    private static final LocalDateTime NOW = LocalDateTime.of(2023, 10, 20, 12, 34, 56);
    private static final LocalDateTime NOW_PLUS_7_DAYS = LocalDateTime.of(2023, 10, 27, 12, 34, 56);
    private static final String DEFAULT_ZONE_ID = "America/Denver";

    @Mock
    private Clock clock;

    @InjectMocks
    private DaysToDateConverter underTest;

    @BeforeEach
    void setUp() {
        Clock fixedClock = Clock.fixed(NOW.atZone(ZoneId.of(DEFAULT_ZONE_ID)).toInstant(), ZoneId.of(DEFAULT_ZONE_ID));
        given(clock.instant()).willReturn(fixedClock.instant());
        given(clock.getZone()).willReturn(fixedClock.getZone());
    }

    @Test
    void shouldGetNowPlusDays() {
        // when
        LocalDateTime result = underTest.toNowPlusDays(7);

        // then
        assertThat(result).isEqualTo(NOW_PLUS_7_DAYS);
    }
}
