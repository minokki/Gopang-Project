package com.gopang.validator;

import com.gopang.dto.OfferForm;
import com.gopang.repository.OfferRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class OfferFormValidator implements Validator {
    private  final OfferRepository offerRepository;
    @Override
    public boolean supports(Class<?> clazz) {
        return OfferForm.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        OfferForm offerForm = (OfferForm)target;
        if (offerRepository.existsByPath(offerForm.getPath())) {
         errors.rejectValue("path","wrong.path","경로를 사용할수 없습니다.");
        }
    }
}
