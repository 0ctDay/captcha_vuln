package com.example.capture_demo.service;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.util.Properties;

@Service
public class CaptchaService {

    private final DefaultKaptcha captchaProducer;

    @Autowired
    public CaptchaService() {
        Properties properties = new Properties();
        properties.setProperty("kaptcha.border", "yes");
        properties.setProperty("kaptcha.border.color", "105,179,90");
        properties.setProperty("kaptcha.textproducer.font.color", "black");
        properties.setProperty("kaptcha.image.width", "200");
        properties.setProperty("kaptcha.image.height", "50");
        properties.setProperty("kaptcha.textproducer.char.space", "14");
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
    public void storeCaptchaInSession(String captchaText) {
        // 获取 HttpSession
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpSession session = attributes.getRequest().getSession();
        System.out.println(captchaText);
        // 将生成的验证码存储到 session 中
        session.setAttribute("captcha", captchaText);
    }

    /**
     * 验证用户输入的验证码
     */
    public boolean validateCaptcha(String userInputCaptcha) {
        // 获取 HttpSession
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpSession session = attributes.getRequest().getSession();

        // 从 session 中获取存储的验证码
        String storedCaptcha = (String) session.getAttribute("captcha");

        // 验证输入的验证码是否与存储的验证码一致，忽略大小写
        return storedCaptcha != null && storedCaptcha.equalsIgnoreCase(userInputCaptcha);
    }

}
