package com.example.userservice.global.config.redis.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Service
@Slf4j
public class VisitorRedisUtil {

    private final StringRedisTemplate redisTemplate;

    public void setData(String key, Object value, Long time, TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(key, value.toString(), time, timeUnit);
    }

    public Object getData(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public Set<String> getKeySet(String domain) {
        return redisTemplate.keys(domain);
    }

    public void deleteData(String key) {
        redisTemplate.delete(key);
    }

    public void increaseData(String key) {
        redisTemplate.opsForValue().increment(key);
    }

}