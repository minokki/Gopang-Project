package com.gopang.repository;

import com.gopang.dto.OfferSearchDto;
import com.gopang.entity.Offer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OfferRepositoryCustom {
    Page<Offer> getOfferPage(OfferSearchDto offerSearchDto, Pageable pageable);
}
