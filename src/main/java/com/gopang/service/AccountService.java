package com.gopang.service;

import com.gopang.account.UserAccount;
import com.gopang.config.AppProperties;
import com.gopang.constant.Role;
import com.gopang.dto.MemberSearchDto;
import com.gopang.dto.Profile;
import com.gopang.dto.SignUpForm;
import com.gopang.entity.Account;
import com.gopang.mail.EmailMessage;
import com.gopang.mail.EmailService;
import com.gopang.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.validation.Valid;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class AccountService implements UserDetailsService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final TemplateEngine templateEngine;
    private final EmailService emailService;
    private final AppProperties appProperties;


    /* 회원가입 토큰생성 */
    public Account processNewAccount(SignUpForm signUpForm) {
        Account newAccount = saveNewAccount(signUpForm);
//        newAccount.generateEmailCheckToken(); todo 이메일 체크토큰 생성
//        sentConfirmEmail(newAccount); todo 회원가입시 이메일 전송, 사용시 주석해제
        return newAccount;
    }

    /* 회원가입 */
    public Account saveNewAccount(@Valid SignUpForm signUpForm) {
        Account account = Account.builder()
                .email(signUpForm.getEmail())
                .nickname(signUpForm.getNickname())
                .password(passwordEncoder.encode(signUpForm.getPassword()))
                .role(Role.USER)
                .phone(signUpForm.getPhone())
                .userType(signUpForm.getUserType())
                .build();
        Account newAccount = accountRepository.save(account);
        return newAccount;
    }

    /* 로그인 */
    public void login(Account account) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                new UserAccount(account),
                account.getPassword(),
                new UserAccount(account).getAuthorities()); // 여기서 authorities 메서드를 이용하여 권한 추가
        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(token);
    }
    /* 인증 이메일 보내기 */
    public void sentConfirmEmail(Account newAccount) {
        Context context = new Context();
        context.setVariable("link","/check-email-token?token=" + newAccount.getEmailCheckToken() +
                "&email=" + newAccount.getEmail());
        context.setVariable("nickname",newAccount.getNickname());
        context.setVariable("linkName", "이메일 인증하기");
        context.setVariable("message","벌초박사 서비스를 사용하려면 링크를 클릭하세요.");
        context.setVariable("host",appProperties.getHost());
        String message = templateEngine.process("mail/email_link", context);

        EmailMessage emailMessage = EmailMessage.builder()
                .to(newAccount.getEmail())
                .subject("벌초박사, 회원가입 인증")
                .message(message)
                .build();
        emailService.sendEmail(emailMessage);
    }

    /* userDetails */
    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String emailOrNickname) throws UsernameNotFoundException {
        Account account = accountRepository.findByEmail(emailOrNickname);
        if(account == null){
            account = accountRepository.findByNickname(emailOrNickname);
        }

        if(account == null){
            throw new UsernameNotFoundException(emailOrNickname);
        }
        return new UserAccount(account);
    }

    /* 회원가입시 자동로그인 */
    public void completeSingUP(Account account) {
        account.completeSignUp();
        accountRepository.save(account);
        login(account);
    }

    /* 프로필 UPDATE */
    public void updateProfile(Account account, Profile profile) {
        account.setPhone(profile.getPhone());
        account.setOccupation(profile.getOccupation());
        account.setLocation(profile.getLocation());
        account.setBio(profile.getBio());
        account.setProfileImage(profile.getProfileImage());
        accountRepository.save(account);
    }

    /* 프로필 PASSWORD */
    public void updatePassword(Account account, String newPassword) {
        account.setPassword(passwordEncoder.encode(newPassword));
        accountRepository.save(account);
    }
    /* 프로필 NICKNAME */
    public void updateNickname(Account account, String nickname) {
        account.setNickname(nickname);
        accountRepository.save(account);
        login(account);
    }
    /* 이메일-로그인링크 */
    public void sendLoginLing(Account account) {
        Context context = new Context();
        context.setVariable("link","/login-by-email?token=" + account.getEmailCheckToken() + "&email=" + account.getEmail());
        context.setVariable("nickname",account.getNickname());
        context.setVariable("linkName", "이메일로 로그인하기");
        context.setVariable("message","로그인 하려면 아래 링크를 클릭하세요.");
        context.setVariable("host",appProperties.getHost());
        String message = templateEngine.process("mail/email_link", context);

        EmailMessage emailMessage = EmailMessage.builder()
                .to(account.getEmail())
                .subject("벌초박사, 로그인 링크")
                .message(message)
                .build();

        emailService.sendEmail(emailMessage);

    }

    public Page<Account> getAdminMemberPage(MemberSearchDto memberSearchDto, Pageable pageable) {
        return accountRepository.getAdminMemberPage(memberSearchDto, pageable);
    }
}
