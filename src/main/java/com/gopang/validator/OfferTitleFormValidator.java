package com.gopang.validator;

import com.gopang.dto.OfferTitleForm;
import com.gopang.repository.OfferRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class OfferTitleFormValidator implements Validator {
    private final OfferRepository offerRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return OfferTitleForm.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        OfferTitleForm offerTitleForm = (OfferTitleForm) target;
        if (offerRepository.existsByPath(offerTitleForm.getNewTitle())) {
            errors.rejectValue("newTitle","wrong.path","제목을 사용할수 없습니다.");
        }
    }
}
