package com.gopang.controller;

import com.gopang.account.CurrentUser;
import com.gopang.dto.SignUpForm;
import com.gopang.entity.Account;
import com.gopang.repository.AccountRepository;
import com.gopang.service.AccountService;
import com.gopang.validator.SignUpFormValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class AccountController {

    private final SignUpFormValidator signUpFormValidator;
    private final AccountService accountService;
    private final AccountRepository accountRepository;

    /* 유효성 검증 */
    @InitBinder("signUpForm") //해당 모델에 초기화 바인더 설정
    public void initBinder(WebDataBinder webDataBinder){
        webDataBinder.addValidators(signUpFormValidator);
        // addValidators() 메소드를 사용하여 유효성 검증기를 등록하면, 해당 폼 데이터의 유효성 검증을 수행할 수 있음
    }
    /* 이용약관 이동 */
    @GetMapping("/terms")
    public String terms(){
        return "account/account_terms";
    }

    /* 로그인페이지 이동  */
    @GetMapping("/login")
    public String login(){
        return "account/account_login";
    }

    /* 회원가입 FORM(GET) */
    @GetMapping("/sign-up")
    public String signUpForm(Model model){
        model.addAttribute("signUpForm",new SignUpForm());
        return "account/account_signUp";
    }
    /* 회원가입 FORM(POST) */
    @PostMapping("/sign-up")
    public String signUpSubmit(@Valid @ModelAttribute SignUpForm signUpForm, Errors errors){
        if(errors.hasErrors()){
            return "account/account_signUp";
        }
        Account account = accountService.processNewAccount(signUpForm);
        accountService.login(account);
        return "redirect:/";
    }

    /* 이메일 토큰 */
    @GetMapping("/check-email-token")
    public String CheckEmailToken(String token, String email, Model model){
        Account account = accountRepository.findByEmail(email);
        String view = "account/account_checkedEmail";
        if(account == null){
            model.addAttribute("error","wrong.email");
            return view;
        }
        if(!account.getEmailCheckToken().equals(token)){
            model.addAttribute("error","wrong.token");
            return view;
        }
        accountService.completeSingUP(account);
        model.addAttribute("numberOfUser",accountRepository.count());
        model.addAttribute("nickname",account.getNickname());
        return view;
    }

    /* 인증메일 */
    @GetMapping("/check-email")
    public String checkEmail(@CurrentUser Account account, Model model){
        model.addAttribute("email",account.getEmail());
        accountService.sentConfirmEmail(account);
        return "account/account_checkEmail";
    }
    /* 인증메일 재전송 */
    @GetMapping("resend-confirm-email")
    public String resendConfirmEmail(@CurrentUser Account account, Model model){
        if(!account.canSendConfirmEmail()){
            model.addAttribute("error","인증 이메일은 5분에 한번만 전송할 수 있습니다.");
            model.addAttribute("email", account.getEmail());
            return "account/account_checkEmail";
        }

        accountService.sentConfirmEmail(account);
        return "redirect:/";

    }
    /* 이메일 로그인(POST) */
//    @PostMapping("/email-login")
//    public String emailLoginLink(String email, Model model, RedirectAttributes attributes) {
//        Account account = accountRepository.findByEmail(email);
//        if (account == null) {
//            model.addAttribute("error", "유효한 이메일이 아닙니다.");
//            return "account/account_emailLogin";
//        }
//
//        if (!account.canSendConfirmEmail()) {
//            model.addAttribute("error", "이메일 로그인은 1시간 뒤에 사용할 수 있습니다.");
//            return "account/email-login";
//        }

//        accountService.sendLoginLing(account);
//        attributes.addFlashAttribute("message", "이메일 인증 메일을 발송했습니다");
//        return "redirect:/email-login";
//    }

    /* 이메일 로그인 */
//    @GetMapping("/login-by-email")
//    public String loginByEmail(String token, String email, Model model) {
//        Account account = accountRepository.findByEmail(email);
//        if (account == null || !account.isValidToken(token)) {
//            model.addAttribute("error", "로그인 할 수 없습니다.");
//            return "account/account_loggedEamil";
//        }
//        accountService.login(account);
//        return "account/account_loggedEamil";
//    }

}
