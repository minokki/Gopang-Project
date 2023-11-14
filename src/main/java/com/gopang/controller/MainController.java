package com.gopang.controller;

import com.gopang.account.CurrentUser;
import com.gopang.entity.Account;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    /* 메인페이지 이동 */
    @GetMapping("/")
    public String home(@CurrentUser Account account, Model model) {
        if( account != null) {
            model.addAttribute(account);
        }
        return "index";
    }

    /* 오시는길 페이지 이동 */
    @GetMapping("/community/companyMap")
    public String company_map(@CurrentUser Account account, Model model) {
        if( account != null) {
            model.addAttribute(account);
        }
        return "community/community_companyMap";
    }

}
