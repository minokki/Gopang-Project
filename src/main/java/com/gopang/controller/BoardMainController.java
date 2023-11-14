package com.gopang.controller;

import com.gopang.account.CurrentUser;
import com.gopang.entity.Account;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class BoardMainController {
    /* 작업소개 페이지 이동 */
    @GetMapping(value = "/boardMain/info")
    public String boardMainInfo(@CurrentUser Account account, Model model){
        if( account != null) {
            model.addAttribute(account);
        }
        return "boardMain/board_info";
    }
}
