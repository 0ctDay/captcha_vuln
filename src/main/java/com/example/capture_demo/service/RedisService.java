package com.example.capture_demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RedisService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    // 写入数据
    public void write(String key, String value) {
        stringRedisTemplate.opsForValue().set(key, value, 60, TimeUnit.SECONDS);
    }

    // 读取数据
    public String read(String key) {
        return stringRedisTemplate.opsForValue().get(key);
    }

    // 删除数据
    public void delete(String key) {
        stringRedisTemplate.delete(key);
    }
}
