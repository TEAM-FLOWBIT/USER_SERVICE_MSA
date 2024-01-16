package com.example.userservice.domain.visitor.util;

import com.example.userservice.global.config.redis.util.VisitorRedisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.Set;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Component
public class ViewCountUtil {

    private final VisitorRedisUtil visitorRedisUtil;


    public boolean isDuplicatedAccess(String ipAddress, String domainName) {
        return visitorRedisUtil.getData(ipAddress + "_" + domainName) != null;
    }

    public void setDuplicateAccess(String ipAddress, String domainName){
        long expireTime = calculateExpirationTime();
        visitorRedisUtil.setData(ipAddress + "_" + domainName, 1L, expireTime, TimeUnit.MILLISECONDS);
    }

    private long calculateExpirationTime() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime endOfDay = LocalDateTime.of(now.toLocalDate(), LocalTime.MAX);

        long remainingMilliseconds = now.until(endOfDay, ChronoUnit.MILLIS);
        return remainingMilliseconds;
    }

    public Long getViewCount(String key) {
        if(visitorRedisUtil.getData(key)==null){
            return 0L;
        }else{
            return Long.parseLong((String) visitorRedisUtil.getData(key));
        }
    }

    public Set<String> getKeySet(String domain) {
        return visitorRedisUtil.getKeySet(domain);
    }

    public void deleteData(String key) {
        visitorRedisUtil.deleteData(key);
    }

    public void increaseData(String key) {
        visitorRedisUtil.increaseData(key);
    }

}