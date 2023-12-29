package com.gopang.controller;

import com.gopang.account.CurrentUser;
import com.gopang.dto.OfferForm;
import com.gopang.entity.Account;
import com.gopang.entity.Offer;
import com.gopang.repository.OfferRepository;
import com.gopang.service.OfferService;
import com.gopang.validator.OfferFormValidator;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Controller
@RequiredArgsConstructor
public class OfferController {

    private final OfferService offerService;
    private final ModelMapper modelMapper;
    private final OfferFormValidator offerFormValidator;
    private final OfferRepository offerRepository;

    @InitBinder("offerForm")
    public void offerFormInitBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(offerFormValidator);
    }

    @GetMapping("/offer/new")
    public String newOfferForm(@CurrentUser Account account, Model model) {
        model.addAttribute(account);
        model.addAttribute(new OfferForm());
        return "offer/offerForm";
    }

    @PostMapping("/offer/new")
    public String newOfferSubmit(@CurrentUser Account account, @Valid OfferForm offerForm, Errors errors) {
        if (errors.hasErrors()) {
            return "/offer/offerForm";
        }

        Offer offer = offerService.createNewOffer(modelMapper.map(offerForm, Offer.class), account);
        return "redirect:/offer/" + offer.getPath();
    }

    @GetMapping("/offer/{path}")
    public String offerDetail(@CurrentUser Account account, @PathVariable String path, Model model) {
        if( account != null) {
            model.addAttribute(account);
        }
        Offer offer = offerRepository.findByPath(path);
        model.addAttribute("account", account);
        model.addAttribute("offer", offer);
        return "offer/offerDetail";
    }

    @GetMapping("/offer/{path}/members")
    public String offerMembers(@CurrentUser Account account, @PathVariable String path, Model model) {
        Offer offer = offerRepository.findByPath(path);
        model.addAttribute("account", account);
        model.addAttribute("offer", offer);
        return "offer/members";
    }

    @GetMapping("/offer/{path}/join")
    public String joinOffer(@CurrentUser Account account, @PathVariable String path) {
        Offer offer = offerRepository.findOfferWithMembersByPath(path);
        offerService.addMember(offer, account);
        return "redirect:/offer/" + offer.getPath();
    }

    @GetMapping("/offer/{path}/leave")
    public String leaveOffer(@CurrentUser Account account, @PathVariable String path) {
        Offer offer = offerRepository.findOfferWithMembersByPath(path);
        offerService.removeMember(offer, account);
        return "redirect:/offer/" + offer.getPath();
    }
}
