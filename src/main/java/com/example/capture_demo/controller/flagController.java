package com.example.capture_demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;
import java.io.FileInputStream;

@Controller
public class flagController {

    @GetMapping("/welcome")
    public String welcomePage(HttpSession session, Model model) {
        // 从 session 中获取已登录的用户信息
        String flag1 = (String) session.getAttribute("flag1");
        String flag2 = (String) session.getAttribute("flag2");
        String flag3 = (String) session.getAttribute("flag3");
        if (flag2 != null && flag1 != null && flag3 != null) {
            String flag = "";
            try {
                FileInputStream fileInputStream = new FileInputStream("/tmp/flag");
                byte[] bytes = new byte[fileInputStream.available()];
                fileInputStream.read(bytes);
                flag = new String(bytes);
            }
            catch (Exception e){
                flag = "flag配置失败, 请联系管理员";
            }
            // 用户已登录，显示欢迎页面
            model.addAttribute("username", flag);
            return "welcome";
        } else {
            // 用户未登录，重定向回登录页面
            return "redirect:/login3";
        }
    }
}
