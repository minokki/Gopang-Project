package com.gopang.service;

import com.gopang.dto.OfferDescriptionForm;
import com.gopang.dto.OfferSearchDto;
import com.gopang.entity.Account;
import com.gopang.entity.Offer;
import com.gopang.repository.OfferRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class OfferService {

    private final OfferRepository offerRepository;
    private final ModelMapper modelMapper;

    public Offer createNewOffer(Offer offer, Account account) {
        Offer newoOffer = offerRepository.save(offer);
        newoOffer.addManager(account);

        return newoOffer;
    }

    public Offer getOfferUpdate(Account account, String path) {
        Offer offer = this.getOffer(path);
        if (!account.isManageOf(offer)){
            throw new IllegalArgumentException("해당 기능을 사용할 수 없습니다.");
        }
        return offer;

    }

    private Offer getOffer(String path) {
        Offer offer = offerRepository.findByPath(path);
        if (offer == null){
            throw new IllegalArgumentException(path + "에 해당하는 스터디가 없습니다.");
        }
        return offer;
    }

    public void updateOfferDescription(Offer offer, OfferDescriptionForm offerDescriptionForm) {
        modelMapper.map(offerDescriptionForm,offer);
        offerRepository.save(offer);
    }

    public void updateOfferImage(Offer offer, String image) {
        offer.setImage(image);
        offerRepository.save(offer);
    }

    public void enableOfferBanner(Offer offer) {
        offer.setUserBanner(true);
        offerRepository.save(offer);
    }

    public void disableOfferBanner(Offer offer) {
        offer.setUserBanner(false);
        offerRepository.save(offer);
    }
    public void publish(Offer offer) {
        offer.publish();
        offerRepository.save(offer);
    }

    public void close(Offer offer) {
        offer.close();
        offerRepository.save(offer);
    }
    @Transactional(readOnly = true)
    public Page<Offer> getOfferPage(OfferSearchDto offerSearchDto, Pageable pageable) {
        return offerRepository.getOfferPage(offerSearchDto, pageable);
    }

    public void updateOfferPath(Offer offer, String newPath) {
        offer.setPath(newPath);
        offerRepository.save(offer);
    }

    public void updateOfferTitle(Offer offer, String title) {
        offer.setTitle(title);
        offerRepository.save(offer);
    }

    public void remove(Offer offer) {
        if (offer.isRemovable()){
            offerRepository.delete(offer);
        }else {
            throw new IllegalArgumentException("게시글을 삭제할수 없습니다.");
        }

    }

    public void addMember(Offer offer, Account account) {
        offer.addMember(account);
        offerRepository.save(offer);
    }

    public void removeMember(Offer offer, Account account) {
        offer.removeMember(account);
        offerRepository.save(offer);
    }
}
