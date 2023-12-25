package com.example.userservice.domain.visitor.fixture;

import java.time.LocalDateTime;

public class VisitorLocalDateTime {

    public final static LocalDateTime dateTime = LocalDateTime.now();
    public final static int YEAR = dateTime.getYear();
    public final static int MONTH = dateTime.getMonthValue();
    public final static int DAY = dateTime.getDayOfMonth();

}
