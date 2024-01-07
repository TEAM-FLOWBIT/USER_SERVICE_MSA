package com.example.userservice.domain.visitor.fixture;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class VisitorLocalDateTime {

    private static final ZoneId SEOUL_ZONE_ID = ZoneId.of("Asia/Seoul");

    public static LocalDateTime getDateTime() {
        return ZonedDateTime.now(SEOUL_ZONE_ID).toLocalDateTime();
    }

    public static int getYear() {
        return getDateTime().getYear();
    }

    public static int getMonth() {
        return getDateTime().getMonthValue();
    }

    public static int getDay() {
        return getDateTime().getDayOfMonth();
    }
}