package com.example.userservice.domain.visitor.controller;

import com.example.userservice.domain.visitor.service.VisitorService;
import com.example.userservice.global.common.CommonResDto;
import com.example.userservice.global.helper.IpHelper;
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

    // 원래는 ip로 확인해주는 것이 맞지만 현재 istio 내부에서 계속해서 x forwared 값을 127.0.0.6으로
    //반환을 해주기 때문에 ip는 중복체크하지않고 호출하면 방문자 수 계속 늘려주는 방식으로 결정한다.
    @PostMapping(value = "")
    public ResponseEntity<?> increaseVisitor(HttpServletRequest request) {
        log.info("방문자 수 post");
        String clientIpAddr = IpHelper.getClientIpAddr(request);
        visitorService.increaseHomeViewCount(clientIpAddr);
        return new ResponseEntity<>(
                new CommonResDto<>(1,"방문자 카운트 증가",""), HttpStatus.OK
        );
    }

    @GetMapping(value = "")
    public ResponseEntity<?> toDayVisitorCheck() {
        log.info("방문자 수 get");
        Long viewCount = visitorService.readHomeViewCount();
        return new ResponseEntity<>(
                new CommonResDto<>(1,"일일 방문자 확인하기",viewCount), HttpStatus.OK
        );
    }
    @GetMapping(value = "/total-view")
    public ResponseEntity<?> totalVisitorCheck() {
        log.info("방문자 수 get");
        Long viewCount = visitorService.readHomeTotalViewCount();
        return new ResponseEntity<>(
                new CommonResDto<>(1,"총 방문자 확인하기",viewCount), HttpStatus.OK
        );
    }

}
