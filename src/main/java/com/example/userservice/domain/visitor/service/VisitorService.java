package com.example.userservice.domain.visitor.service;

public interface VisitorService {

    Long increaseHomeViewCount(String ipAddress);

    Long readHomeViewCount();

    Long readHomeTotalViewCount();

}
