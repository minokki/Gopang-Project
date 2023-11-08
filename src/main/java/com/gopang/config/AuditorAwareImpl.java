package com.gopang.config;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

//현재 로그인한 사용자 정보를 조회후 사용자 이름을 등록자와 수정자로 지정.
public class AuditorAwareImpl implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = "";
        if (authentication != null){
            userId = authentication.getName();
        }
        return Optional.of(userId);
    }
}
