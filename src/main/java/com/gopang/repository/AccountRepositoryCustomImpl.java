package com.gopang.repository;

import com.gopang.dto.MemberSearchDto;
import com.gopang.entity.Account;
import com.gopang.entity.QAccount;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;

public class AccountRepositoryCustomImpl implements AccountRepositoryCustom{

    private JPAQueryFactory queryFactory;

    public AccountRepositoryCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    private BooleanExpression regDtsAfter(String searchDateType) {
        LocalDateTime dateTime = LocalDateTime.now();

        if (StringUtils.equals("all", searchDateType) || searchDateType == null) {
            return null;
        } else if (StringUtils.equals("1d", searchDateType)) {
            dateTime = dateTime.minusDays(1);
        } else if (StringUtils.equals("1w", searchDateType)) {
            dateTime = dateTime.minusWeeks(1);
        } else if (StringUtils.equals("1m", searchDateType)) {
            dateTime = dateTime.minusMonths(1);
        } else if (StringUtils.equals("6m", searchDateType)) {
            dateTime = dateTime.minusMonths(6);
        }

        return QAccount.account.regTime.after(dateTime);
    }
    private BooleanExpression searchByLike(String searchBy, String searchQuery) {
        if (StringUtils.equals("nickname", searchBy)) {
            return QAccount.account.nickname.like("%" + searchQuery + "%");
        } else if (StringUtils.equals("email", searchBy)) {
            return QAccount.account.email.like("%" + searchQuery + "%");
        }
        return null;
    }

    @Override
    public Page<Account> getAdminMemberPage(MemberSearchDto memberSearchDto, Pageable pageable) {

        List<Account> content = queryFactory
                .selectFrom(QAccount.account)
                .where(regDtsAfter(memberSearchDto.getSearchDateType()),
                        searchByLike(memberSearchDto.getSearchBy(), memberSearchDto.getSearchQuery()))
                .orderBy(QAccount.account.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory.select(Wildcard.count).from(QAccount.account)
                .where(regDtsAfter(memberSearchDto.getSearchDateType()),
                        searchByLike(memberSearchDto.getSearchBy(), memberSearchDto.getSearchQuery()))
                .fetchOne();

        return new PageImpl<>(content,pageable,total);
    }
}
