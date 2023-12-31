package com.example.userservice.domain.visitor.service;

public interface VisitorService {

    void increaseHomeViewCount(String ipAddress);

    Long readHomeViewCount();

    Long readHomeTotalViewCount();

}
