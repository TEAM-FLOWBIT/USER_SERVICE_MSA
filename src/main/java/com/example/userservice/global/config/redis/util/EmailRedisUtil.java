package com.example.userservice.global.config.redis.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class EmailRedisUtil {
    private final StringRedisTemplate stringRedisTemplate;
    // key를 통해 value 리턴
    public List<String> getData(String key) {
        ListOperations<String, String> valueOperations = stringRedisTemplate.opsForList();
        long size = valueOperations.size(key); // Get the size of the list
        return valueOperations.range(key, 0, size - 1); // Get all elements of the list
    }
    public boolean existData(String key) {

        return Boolean.TRUE.equals(stringRedisTemplate.hasKey(key));
    }
    public void setListData(String key, Integer value,String verifyPurpose,long duration) {


        ListOperations<String, String> listOperations = stringRedisTemplate.opsForList();
        String data = value.toString() + "|" + verifyPurpose;
        Duration expireDuration = Duration.ofSeconds(duration);

        listOperations.leftPush(key, data);
        stringRedisTemplate.expire(key, expireDuration);

    }

    public void deleteData(String key) {
        log.info("delete data"+key);
        stringRedisTemplate.delete(key);
    }
}