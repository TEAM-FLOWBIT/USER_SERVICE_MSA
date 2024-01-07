package com.example.userservice.domain.visitor.service.impl;


import com.example.userservice.domain.visitor.entity.Visitor;
import com.example.userservice.domain.visitor.repository.VisitorRepository;
import com.example.userservice.domain.visitor.service.VisitorService;
import com.example.userservice.domain.visitor.util.ViewCountUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class VisitorServiceImpl implements VisitorService {

    private final ViewCountUtil viewCountUtil;
    private final VisitorRepository visitorRepository;

    @Override
    @Transactional
    public void increaseHomeViewCount(String ipAddress) {
        LocalDateTime currentDate = getCurrentDate();
        int year = currentDate.getYear();
        int month = currentDate.getMonthValue();
        int day = currentDate.getDayOfMonth();

        if(!viewCountUtil.isDuplicatedAccess(ipAddress, "Ip_Deadline")) { // 중복된 값이 있는지 확인
        viewCountUtil.increaseData("ViewCount_Home_Total"); // 해당 키에 data값 1씩 증가
        viewCountUtil.increaseData("ViewCount_Home"+year+month+day); // 해당 키에 data값 1씩 증가
        viewCountUtil.setDuplicateAccess(ipAddress, "Ip_Deadline"); //1일 동안 해당 아이피 유지
        Long totalViewCount = viewCountUtil.getViewCount("ViewCount_Home_Total");
        Visitor visitor = Visitor.builder()
                .count(totalViewCount)
                .visitorIp(ipAddress)
                .visitDate(currentDate)
                .build();
        visitorRepository.save(visitor);
        }
    }

    @Override
    public Long readHomeViewCount() {
        LocalDateTime currentDate = getCurrentDate();
        int year = currentDate.getYear();
        int month = currentDate.getMonthValue();
        int day = currentDate.getDayOfMonth();

        Long viewCount = viewCountUtil.getViewCount("ViewCount_Home"+year+month+day);
        return viewCount;
    }

    @Override
    public Long readHomeTotalViewCount() {
        Long viewCount = viewCountUtil.getViewCount("ViewCount_Home_Total");
        return viewCount;
    }

    private LocalDateTime getCurrentDate() {
        return LocalDateTime.now();
    }
}
