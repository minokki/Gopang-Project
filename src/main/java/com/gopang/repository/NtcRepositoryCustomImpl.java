package com.gopang.repository;

import com.gopang.dto.NtcSearchDto;
import com.gopang.entity.Ntc;
import com.gopang.entity.QNtc;
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

public class NtcRepositoryCustomImpl implements NtcRepositoryCustom {
    private JPAQueryFactory queryFactory;

    public NtcRepositoryCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    private BooleanExpression regDtsAfter(String searchDateType) {
        LocalDateTime dateTime = LocalDateTime.now();

        if (StringUtils.equals("all",searchDateType) || searchDateType == null){
            return null;
        }else if(StringUtils.equals("1d",searchDateType)){
            dateTime = dateTime.minusDays(1);
        } else if (StringUtils.equals("1w", searchDateType)) {
            dateTime = dateTime.minusWeeks(1);
        } else if (StringUtils.equals("1m", searchDateType)) {
            dateTime = dateTime.minusMonths(1);
        } else if (StringUtils.equals("6m", searchDateType)) {
            dateTime = dateTime.minusMonths(6);
        }
        return QNtc.ntc.regTime.after(dateTime);
    }

    private BooleanExpression searchByLike(String searchBy, String searchQuery) {
        if (StringUtils.equals("title", searchBy)) {
            return QNtc.ntc.title.like("%" + searchQuery + "%");
        } else if (StringUtils.equals("createBy", searchBy)) {
            return QNtc.ntc.createBy.like("%" + searchQuery + "%");
        }
        return null;
    }
    @Override
    public Page<Ntc> getNtcPage(NtcSearchDto ntcSearchDto, Pageable pageable) {
        List<Ntc> results = queryFactory
                .selectFrom(QNtc.ntc)
                .where(regDtsAfter(ntcSearchDto.getSearchDateType()),
                        searchByLike(ntcSearchDto.getSearchBy(),
                                ntcSearchDto.getSearchQuery()))
                .orderBy(QNtc.ntc.isImportant.desc(), QNtc.ntc.id.desc()) // isImportant 내림차순, id 내림차순
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory.select(Wildcard.count).from(QNtc.ntc)
                .where(regDtsAfter(ntcSearchDto.getSearchDateType()),
                        searchByLike(ntcSearchDto.getSearchBy(), ntcSearchDto.getSearchQuery()))
                .fetchOne();

        return new PageImpl<>(results, pageable, total);
    }
}
