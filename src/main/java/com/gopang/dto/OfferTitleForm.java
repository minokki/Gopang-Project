package com.gopang.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Data
public class OfferTitleForm {

    @NotBlank
    @Length(max = 50)
    public String newTitle;
}
