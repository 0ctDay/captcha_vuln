package com.example.capture_demo.controller;
import java.util.Optional;
import java.util.UUID;

import com.example.capture_demo.service.Captcha2Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;

@Controller
public class Login2Controller {

    private static final String USERNAME = "admin";
    private static final String PASSWORD = "12345678";
    private final Captcha2Service captchaService;
    @GetMapping("/captcha2")
    public void getCaptcha(HttpServletResponse response) throws IOException {
        // 生成验证码文本
        String captchaText = captchaService.generateCaptchaText();
        String key = UUID.randomUUID().toString();


        // 创建 Cookie
        Cookie cookie = new Cookie("uuid",key);
        // 设置 Cookie 的有效时间（以秒为单位），例如 3600 秒 = 1 小时
        cookie.setMaxAge(3600);
        // 设置 Cookie 的作用域（可选）
        cookie.setPath("/");
        response.addCookie(cookie);


        // 保存验证码到非关系型数据库
        captchaService.storeCaptchaInSession(key,captchaText);



        // 生成验证码图像
        BufferedImage captchaImage = captchaService.generateCaptchaImage(captchaText);
        // 设置响应类型为图片
        response.setContentType("image/png");
        // 将验证码图像写入响应流
        javax.imageio.ImageIO.write(captchaImage, "PNG", response.getOutputStream());
    }
    @Autowired
    public Login2Controller(Captcha2Service captchaService) {

        this.captchaService = captchaService;
    }

    @GetMapping("/login2")
    public String loginPage(HttpSession session) {
        String flag1 = (String)session.getAttribute("flag1");
        if (flag1 != null) {
            return "login2";
        } else {
            // 用户未登录，重定向回登录页面
            return "redirect:/login";
        }

    }

    @PostMapping("/login2")
    public String login(
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam String captcha,
            @CookieValue(value = "uuid", required = false) Optional<String> uuid,
            HttpSession session,
            Model model
    ) {
        String flag1 = (String)session.getAttribute("flag1");
        if (flag1 == null) {return "redirect:/login";}

        if ( !uuid.isPresent() || captchaService.validateCaptcha(uuid.get(),captcha)) {
            if (USERNAME.equals(username) && PASSWORD.equals(password)) {
                session.setAttribute("flag2", "success");  // 存储用户名到 session
                return "redirect:/login3";
            }else{
                model.addAttribute("error", "用户名或密码错误");
                return "login2";
            }
        } else {
            model.addAttribute("error", "验证码错误");
            return "login2";
        }


    }

}
