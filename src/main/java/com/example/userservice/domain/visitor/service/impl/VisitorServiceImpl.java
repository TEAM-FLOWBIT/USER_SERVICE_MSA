package com.example.userservice.domain.visitor.service.impl;


import com.example.userservice.domain.visitor.entity.Visitor;
import com.example.userservice.domain.visitor.repository.VisitorRepository;
import com.example.userservice.domain.visitor.service.VisitorService;
import com.example.userservice.domain.visitor.util.ViewCountUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class VisitorServiceImpl implements VisitorService {

    private final ViewCountUtil viewCountUtil;
    private final VisitorRepository visitorRepository;
    private final static LocalDateTime dateTime = LocalDateTime.now();
    private final static int YEAR = dateTime.getYear();
    private final static int MONTH = dateTime.getMonthValue();
    private final static int DAY = dateTime.getDayOfMonth();

    @Override
    @Transactional
    public void increaseHomeViewCount(String ipAddress) {
//        if(!viewCountUtil.isDuplicatedAccess(ipAddress, "Home")) { // 중복된 값이 있는지 확인
        viewCountUtil.increaseData("ViewCount_Home_Total"); // 해당 키에 data값 1씩 증가
        viewCountUtil.increaseData("viewCount_Home_1"+YEAR+MONTH+DAY); // 해당 키에 data값 1씩 증가
//        viewCountUtil.setDuplicateAccess(ipAddress, "Home"); //1일 동안 해당 아이피 유지
        Long totalViewCount = viewCountUtil.getViewCount("ViewCount_Home_Total");
        Visitor visitor = Visitor.builder()
                .count(totalViewCount)
                .build();
        visitorRepository.save(visitor);
//        }
    }

    @Override
    public Long readHomeViewCount() {
        Long viewCount = viewCountUtil.getViewCount("viewCount_Home_1"+YEAR+MONTH+DAY);
        return viewCount;
    }

    @Override
    public Long readHomeTotalViewCount() {
        Long viewCount = viewCountUtil.getViewCount("ViewCount_Home_Total");
        return viewCount;
    }
}
