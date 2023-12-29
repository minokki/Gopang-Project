package com.gopang.controller;

import com.gopang.account.CurrentUser;
import com.gopang.dto.OfferDescriptionForm;
import com.gopang.dto.OfferPathForm;
import com.gopang.dto.OfferTitleForm;
import com.gopang.entity.Account;
import com.gopang.entity.Offer;
import com.gopang.service.OfferService;
import com.gopang.validator.OfferPathFormValidator;
import com.gopang.validator.OfferTitleFormValidator;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequestMapping("/offer/{path}/setting")
@RequiredArgsConstructor
public class OfferSettingController {

    private final OfferService offerService;
    private final ModelMapper modelMapper;
    private final OfferPathFormValidator offerPathFormValidator;
    private final OfferTitleFormValidator offerTitleFormValidator;



    /* 유효성 검증 */
    @InitBinder("offerPathForm") //해당 모델에 초기화 바인더 설정
    public void pathInitBinder(WebDataBinder webDataBinder){
        webDataBinder.addValidators(offerPathFormValidator);
        // addValidators() 메소드를 사용하여 유효성 검증기를 등록하면, 해당 폼 데이터의 유효성 검증을 수행할 수 있음
    }
    @InitBinder("offerTitleForm") //해당 모델에 초기화 바인더 설정
    public void titleInitBinder(WebDataBinder webDataBinder){
        webDataBinder.addValidators(offerTitleFormValidator);
        // addValidators() 메소드를 사용하여 유효성 검증기를 등록하면, 해당 폼 데이터의 유효성 검증을 수행할 수 있음
    }

    @GetMapping("/description")
    public String viewOfferSetting(@CurrentUser Account account, @PathVariable String path, Model model) {
        Offer offer = offerService.getOfferUpdate(account, path);
        OfferDescriptionForm offerDescriptionForm = modelMapper.map(offer, OfferDescriptionForm.class);
        model.addAttribute("account", account);
        model.addAttribute("offer", offer);
        model.addAttribute("offerDescriptionForm", offerDescriptionForm);
        return "offer/setting/description";
    }

    /*작업소개 수정*/
    @PostMapping("/description")
    public String updateOfferInfo(@CurrentUser Account account, @PathVariable String path, @Valid OfferDescriptionForm offerDescriptionForm
            , Errors errors, Model model, RedirectAttributes attributes) {
        Offer offer = offerService.getOfferUpdate(account, path);
        if (errors.hasErrors()) {
            model.addAttribute("account", account);
            model.addAttribute("offer", offer);
            return "offer/setting/description";
        }
        offerService.updateOfferDescription(offer, offerDescriptionForm);
        attributes.addFlashAttribute("message", "작업 소개를 수정했습니다.");
        return "redirect:/offer/" + offer.getPath() + "/setting/description";
    }

    /* 배너 get매핑*/
    @GetMapping("/banner")
    public String offerImageForm(@CurrentUser Account account, @PathVariable String path, Model model) {
        Offer offer = offerService.getOfferUpdate(account, path);
        model.addAttribute("account", account);
        model.addAttribute("offer", offer);
        return "offer/setting/banner";
    }

    @PostMapping("/banner")
    public String offerImageSubmit(@CurrentUser Account account, @PathVariable String path, String image, RedirectAttributes attributes) {
        Offer offer = offerService.getOfferUpdate(account, path);

        offerService.updateOfferImage(offer, image);
        attributes.addFlashAttribute("message", "배너를 수정했습니다.");
        return "redirect:/offer/" + offer.getPath() + "/setting/banner";
    }

    /* 배너 사용하기*/
    @PostMapping("/banner/enable")
    public String enableOfferBanner(@CurrentUser Account account, @PathVariable String path) {
        Offer offer = offerService.getOfferUpdate(account, path);
        offerService.enableOfferBanner(offer);
        return "redirect:/offer/" + offer.getPath() + "/setting/banner";
    }

    /* 배너 끄기 */
    @PostMapping("/banner/disable")
    public String disableOfferBanner(@CurrentUser Account account, @PathVariable String path) {
        Offer offer = offerService.getOfferUpdate(account, path);
        offerService.disableOfferBanner(offer);
        return "redirect:/offer/" + offer.getPath() + "/setting/banner";
    }

    /* 작업 탭 get매핑*/
    @GetMapping("/offer")
    public String offerSettingView(@CurrentUser Account account, @PathVariable String path, Model model) {
        Offer offer = offerService.getOfferUpdate(account, path);
        model.addAttribute("account", account);
        model.addAttribute("offer", offer);
        return "offer/setting/work";
    }
    /* 작업신청 시작 */
    @PostMapping("/offer/publish")
    public String publishOffer(@CurrentUser Account account, @PathVariable String path, RedirectAttributes attributes) {
        Offer offer = offerService.getOfferUpdate(account, path);
        offerService.publish(offer);
        attributes.addFlashAttribute("message", "작업신청을 공개 했습니다.");
        return "redirect:/offer/" + offer.getPath() + "/setting/offer";
    }
    /* 작업신청 중단 */
    @PostMapping("/offer/close")
    public String closeOffer(@CurrentUser Account account, @PathVariable String path, RedirectAttributes attributes) {
        Offer offer = offerService.getOfferUpdate(account, path);
        offerService.close(offer);
        attributes.addFlashAttribute("message", "작업신청을 공개 했습니다.");
        return "redirect:/offer/" + offer.getPath() + "/setting/offer";
    }

    /* 작업신청 주소 변경 */
    @PostMapping("/offer/path")
    public String updateOfferPath(@CurrentUser Account account, @PathVariable String path, Model model,
                                  @Valid @ModelAttribute("offerPathForm") OfferPathForm offerPathForm,BindingResult result,
                                  RedirectAttributes attributes) {
        Offer offer = offerService.getOfferUpdate(account, path);
        if (result.hasErrors()) {
            model.addAttribute("account", account);
            model.addAttribute("offer", offer);
            model.addAttribute("offerPathError", "해당 스터디 경로는 사용할 수 없습니다. 다른 값을 입력하세요.");

            return "offer/setting/work";
        }

        offerService.updateOfferPath(offer, offerPathForm.getNewPath());
        attributes.addFlashAttribute("message", "작업신청 경로를 수정했습니다.");
        return "redirect:/offer/" + offer.getPath() + "/setting/offer";
    }

    /* 작업신청 제목 수정 */
    @PostMapping("offer/title")
    public String updateOfferTitle(@CurrentUser Account account, @PathVariable String path, Model model,
                                   @Valid @ModelAttribute("offerTitleForm") OfferTitleForm offerTitleForm, BindingResult result,
                                   RedirectAttributes attributes) {
        Offer offer = offerService.getOfferUpdate(account, path);
        if (result.hasErrors()) {
            model.addAttribute("account", account);
            model.addAttribute("offer", offer);
            model.addAttribute("offerTitleError", "해당 제목은 사용할 수 없습니다. 다른 값을 입력하세요.");

            return "offer/setting/work";
        }
        offerService.updateOfferTitle(offer,offerTitleForm.getNewTitle());
        attributes.addFlashAttribute("message", "작업신청 제목을 수정했습니다.");
        return "redirect:/offer/" + offer.getPath() + "/setting/offer";
    }

    /* 작업신청 삭제 */
    @PostMapping("offer/remove")
    public String removeOffer(@CurrentUser Account account, @PathVariable String path, Model model) {
        Offer offer = offerService.getOfferUpdate(account, path);
        offerService.remove(offer);
        return "redirect:/offerMain";
    }



}
