package com.gopang.controller;

import com.gopang.account.CurrentUser;
import com.gopang.dto.MainBoardDto;
import com.gopang.dto.OfferSearchDto;
import com.gopang.entity.Account;
import com.gopang.entity.Ntc;
import com.gopang.entity.Offer;
import com.gopang.repository.OfferRepository;
import com.gopang.service.OfferService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@RequiredArgsConstructor
@Controller
public class MainController {
    private final OfferRepository offerRepository;
    private final OfferService offerService;
    /* 메인페이지 이동 */
    @GetMapping("/")
    public String home(@CurrentUser Account account, Model model) {
        if( account != null) {
            model.addAttribute(account);
        }
        return "index";
    }
    @GetMapping(value = {"/offerMain","/offerMain/{page}"})
    public String offerMainView(@CurrentUser Account account, OfferSearchDto offerSearchDto, @PathVariable("page") Optional<Integer> page, Model model) {
        if( account != null) {
            model.addAttribute(account);
        }
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 6);
        Page<Offer> offer = offerService.getOfferPage(offerSearchDto, pageable);
        model.addAttribute("offer", offer);
        model.addAttribute("offerSearchDto", offerSearchDto);
        model.addAttribute("maxPage",5);
        return "offer/offerMain";
    }

}
