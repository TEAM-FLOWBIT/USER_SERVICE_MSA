package com.example.userservice.domain.visitor.controller;

import com.example.userservice.domain.visitor.service.VisitorService;
import com.example.userservice.global.common.CommonResDto;
import com.example.userservice.global.helper.IpHelper;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/visitor")
public class VisitorController {

    private final VisitorService visitorService;

    @Operation(
            description = "사용자의 IP 별 방문자수를 카운팅하는 API"
    )
    @PostMapping(value = "")
    public ResponseEntity<?> increaseVisitor(HttpServletRequest request) {
        log.info("방문자 수 post");
        String clientIpAddr = IpHelper.getClientIpAddr(request);
        Long result = visitorService.increaseHomeViewCount(clientIpAddr);
        return new ResponseEntity<>(
                new CommonResDto<>(1,"방문자 카운트 증가",result), HttpStatus.OK
        );
    }

    @Operation(
            description = "일일 방문자를 확인하는 API"
    )
    @GetMapping(value = "")
    public ResponseEntity<?> toDayVisitorCheck() {
        log.info("방문자 수 get");
        Long viewCount = visitorService.readHomeViewCount();
        return new ResponseEntity<>(
                new CommonResDto<>(1,"일일 방문자 확인하기",viewCount), HttpStatus.OK
        );
    }
    @Operation(
            description = "총 방문자 수를 조회하는 API"
    )
    @GetMapping(value = "/total-view")
    public ResponseEntity<?> totalVisitorCheck() {
        log.info("방문자 수 get");
        Long viewCount = visitorService.readHomeTotalViewCount();
        return new ResponseEntity<>(
                new CommonResDto<>(1,"총 방문자 확인하기",viewCount), HttpStatus.OK
        );
    }

}
