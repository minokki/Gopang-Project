package com.gopang.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;

@Controller
@RequiredArgsConstructor
public class AccontController {
    /* 이용약관 이동 */
    @GetMapping("/terms")
    public String terms(){
        return "account/account_terms";
    }

}
