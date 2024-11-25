package com.example.capture_demo.service;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.util.Properties;

@Service
public class Captcha2Service extends CaptchaService {

    private final DefaultKaptcha captchaProducer;

    @Autowired
    private RedisService redisService;

    @Autowired
    public Captcha2Service() {
        Properties properties = new Properties();
        properties.setProperty("kaptcha.border", "yes");
        properties.setProperty("kaptcha.border.color", "105,179,90");
        properties.setProperty("kaptcha.textproducer.font.color", "black");
        properties.setProperty("kaptcha.image.width", "200");
        properties.setProperty("kaptcha.image.height", "50");
        properties.setProperty("kaptcha.textproducer.font.size", "40");
        properties.setProperty("kaptcha.textproducer.char.length", "4");
        properties.setProperty("kaptcha.textproducer.char.string", "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789");

        Config config = new Config(properties);
        captchaProducer = new DefaultKaptcha();
        captchaProducer.setConfig(config);
    }

    /**
     * 生成验证码文本
     */
    public String generateCaptchaText() {
        return captchaProducer.createText();
    }

    /**
     * 生成验证码图像
     */
    public BufferedImage generateCaptchaImage(String text) {
        return captchaProducer.createImage(text);
    }

    /**
     * 存储验证码到 Session
     */
    public void storeCaptchaInSession(String key,String captchaText) {
        System.out.println(key + captchaText);
        redisService.write("capture."+key,captchaText);
    }

    /**
     * 验证用户输入的验证码
     */
    public boolean validateCaptcha(String uuid, String userInputCaptcha) {
        System.out.println(uuid + userInputCaptcha);
        String read = redisService.read("capture." + uuid);
        System.out.println(read);
        redisService.delete("capture." + uuid);
        if(read == null){
            return false;
        }
        if(read.equalsIgnoreCase(userInputCaptcha)){
            return true;
        }else{
            return false;
        }

    }

}
