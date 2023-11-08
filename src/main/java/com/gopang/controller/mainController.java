package com.gopang.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class mainController {

    /* 메인페이지 이동 */
    @GetMapping("/")
    public String home() {
        return "index";
    }

}
