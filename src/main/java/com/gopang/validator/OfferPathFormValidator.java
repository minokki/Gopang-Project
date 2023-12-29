package com.gopang.validator;

import com.gopang.dto.OfferForm;
import com.gopang.dto.OfferPathForm;
import com.gopang.repository.OfferRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class OfferPathFormValidator implements Validator {
    private final OfferRepository offerRepository;
    @Override
    public boolean supports(Class<?> clazz) {
       return OfferPathForm.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        OfferPathForm offerPathForm = (OfferPathForm)target;
        if (offerRepository.existsByPath(offerPathForm.getNewPath())) {
            errors.rejectValue("newPath","wrong.path","경로를 사용할수 없습니다.");
        }

    }
}
