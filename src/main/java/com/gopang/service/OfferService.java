package com.gopang.service;

import com.gopang.entity.Account;
import com.gopang.entity.Offer;
import com.gopang.repository.OfferRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class OfferService {

    private final OfferRepository offerRepository;

    public Offer createNewOffer(Offer offer, Account account) {
        Offer newoOffer = offerRepository.save(offer);
        newoOffer.addManager(account);

        return newoOffer;
    }
}
