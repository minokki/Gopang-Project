package com.gopang.repository;

import com.gopang.dto.MemberSearchDto;
import com.gopang.entity.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AccountRepositoryCustom {
    Page<Account> getAdminMemberPage(MemberSearchDto memberSearchDto, Pageable pageable);
}

