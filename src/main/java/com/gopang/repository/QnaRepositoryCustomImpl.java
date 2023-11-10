package com.gopang.repository;

import com.gopang.dto.QnaSearchDto;
import com.gopang.entity.QQna;
import com.gopang.entity.Qna;
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

public class QnaRepositoryCustomImpl implements QnaRepositoryCustom{

    private JPAQueryFactory queryFactory;
    public QnaRepositoryCustomImpl(EntityManager em) {
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
        return QQna.qna.regTime.after(dateTime);
    }

    private BooleanExpression searchByLike(String searchBy, String searchQuery) {
        if (StringUtils.equals("title", searchBy)) {
            return QQna.qna.title.like("%" + searchQuery + "%");
        } else if (StringUtils.equals("createBy", searchBy)) {
            return QQna.qna.createBy.like("%" + searchQuery + "%");
        }
        return null;
    }

    @Override
    public Page<Qna> getQnaPage(QnaSearchDto qnaSearchDto, Pageable pageable) {
        List<Qna> results = queryFactory
                .selectFrom(QQna.qna)
                .where(regDtsAfter(qnaSearchDto.getSearchDateType()),
                        searchByLike(qnaSearchDto.getSearchBy(), qnaSearchDto.getSearchQuery()))
                .orderBy(QQna.qna.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory.select(Wildcard.count).from(QQna.qna)
                .where(regDtsAfter(qnaSearchDto.getSearchDateType()),
                        searchByLike(qnaSearchDto.getSearchBy(), qnaSearchDto.getSearchQuery()))
                .fetchOne();

        return new PageImpl<>(results, pageable, total);
    }
}

