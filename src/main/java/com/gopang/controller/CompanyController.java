package com.gopang.controller;

import com.gopang.account.CurrentUser;
import com.gopang.entity.Account;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CompanyController {
    /* 회사소개 페이지 이동 */
    @GetMapping("/company/greeting")
    public String get_company_greeting(@CurrentUser Account account, Model model) {
        if (account != null) {
            model.addAttribute(account);
        }
        return "company/company_greeting";
    }

    /* 회사소개 페이지 이동 */
    @GetMapping("/company/info")
    public String get_company_info(@CurrentUser Account account, Model model) {
        if (account != null) {
            model.addAttribute(account);
        }
        return "company/company_info";
    }

    /* 오시는길 페이지 이동 */
    @GetMapping("/company/map")
    public String get_company_map(@CurrentUser Account account, Model model) {
        if (account != null) {
            model.addAttribute(account);
        }
        return "company/company_companyMap";
    }
}
