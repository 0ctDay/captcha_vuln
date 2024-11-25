package com.example.capture_demo.controller;

import com.example.capture_demo.service.CaptchaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

@Controller
public class Login3Controller {

    private static final String USERNAME = "admin";
    private static final String PASSWORD = "12345678";
    private final CaptchaService captchaService;

    @Autowired
    public Login3Controller(CaptchaService captchaService) {
        this.captchaService = captchaService;
    }

    @GetMapping("/login3")
    public String loginPage(HttpSession session) {
        String flag2 = (String)session.getAttribute("flag2");
        if (flag2 != null) {
            return "login3";
        } else {
            // 用户未登录，重定向到上一关
            return "redirect:/login2";
        }
    }

    @PostMapping("/login3")
    public String login(
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam String captcha,
            HttpSession session,
            Model model
    ) {
        String flag2 = (String)session.getAttribute("flag2");
        if (flag2 == null) {
            return "redirect:/login2";
        }
        if (captchaService.validateCaptcha(captcha)) {
            session.setAttribute("captcha", null);
            if (USERNAME.equals(username) && PASSWORD.equals(password)) {
                session.setAttribute("flag3", "success");  // 存储用户名到 session
                return "redirect:/welcome";
            }else{
                model.addAttribute("error", "用户名或密码错误");
                return "login3";
            }
        } else {
            model.addAttribute("error", "验证码错误");
            return "login3";
        }


    }



}
