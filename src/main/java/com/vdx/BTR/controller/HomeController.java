package com.vdx.BTR.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "redirect:/btr/dashboard";
    }

    @GetMapping("/dashboard")
    public String dashboard() {
        return "dashboard"; // zrobię za chwilę dashboard.html
    }
}
