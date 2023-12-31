package com.example.userservice.domain.visitor.util;

import com.example.userservice.global.config.redis.util.VisitorRedisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Component
public class ViewCountUtil {

    private final VisitorRedisUtil visitorRedisUtil;


    public boolean isDuplicatedAccess(String ipAddress, String domainName) {
        return visitorRedisUtil.getData(ipAddress + "_" + domainName) != null;
    }

    public void setDuplicateAccess(String ipAddress, String domainName){
        visitorRedisUtil.setData(ipAddress + "_" + domainName, 1L, 1L, TimeUnit.DAYS);
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