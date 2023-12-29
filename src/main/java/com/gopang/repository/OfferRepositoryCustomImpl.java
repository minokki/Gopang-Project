package com.gopang.repository;

import com.gopang.dto.OfferSearchDto;
import com.gopang.entity.Offer;
import com.gopang.entity.QOffer;
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

public class OfferRepositoryCustomImpl implements OfferRepositoryCustom{
    private JPAQueryFactory queryFactory;
    public OfferRepositoryCustomImpl(EntityManager em) {
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
        return QOffer.offer.regTime.after(dateTime);
    }
    private BooleanExpression searchByLike(String searchBy, String searchQuery) {
        if (StringUtils.equals("title", searchBy)) {
            return QOffer.offer.title.like("%" + searchQuery + "%");
        } else if (StringUtils.equals("createBy", searchBy)) {
            return QOffer.offer.createBy.like("%" + searchQuery + "%");
        }
        return null;
    }
    @Override
    public Page<Offer> getOfferPage(OfferSearchDto offerSearchDto, Pageable pageable) {
        List<Offer> results = queryFactory
                .selectFrom(QOffer.offer)
                .where(regDtsAfter(offerSearchDto.getSearchDateType()),
                        searchByLike(offerSearchDto.getSearchBy(),
                        offerSearchDto.getSearchQuery()))
                .orderBy(QOffer.offer.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory.select(Wildcard.count).from(QOffer.offer)
                .where(regDtsAfter(offerSearchDto.getSearchDateType()),
                        searchByLike(offerSearchDto.getSearchBy(),offerSearchDto.getSearchQuery()))
                .fetchOne();

        return new PageImpl<>(results,pageable,total);
    }
}
