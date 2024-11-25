package com.example.capture_demo.controller;

import com.example.capture_demo.service.CaptchaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;

@Controller
public class LoginController {

    private static final String USERNAME = "admin";
    private static final String PASSWORD = "12345678";
    private final CaptchaService captchaService;

    @Autowired
    public LoginController(CaptchaService captchaService) {
        this.captchaService = captchaService;
    }

    /**
     * 显示验证码图片
     */
    @GetMapping("/captcha")
    public void getCaptcha(HttpServletResponse response) throws IOException {
        // 生成验证码文本
        String captchaText = captchaService.generateCaptchaText();
        // 将验证码存储到 Session 中
        captchaService.storeCaptchaInSession(captchaText);
        // 生成验证码图像
        BufferedImage captchaImage = captchaService.generateCaptchaImage(captchaText);
        // 设置响应类型为图片
        response.setContentType("image/png");
        // 将验证码图像写入响应流
        javax.imageio.ImageIO.write(captchaImage, "PNG", response.getOutputStream());
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String login(
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam String captcha,
            HttpSession session,
            Model model
    ) {
        if (captchaService.validateCaptcha(captcha)) {
            if (USERNAME.equals(username) && PASSWORD.equals(password)) {
                session.setAttribute("flag1", "success");  // 存储用户名到 session
                return "redirect:/login2";
            }else{
                model.addAttribute("error", "用户名或密码错误");
                return "login";
            }
        } else {
            model.addAttribute("error", "验证码错误");
            return "login";


        }


    }



}
