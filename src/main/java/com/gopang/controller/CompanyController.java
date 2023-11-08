package com.gopang.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CompanyController {
    /* 회사소개 페이지 이동 */
    @GetMapping("/company/company_info")
    public String company_info() {
//        if (account != null) {
//            model.addAttribute(account);
//        }
        return "company/company_info";
    }
}
