package com.gopang.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
public class OfferDescriptionForm {

    @NotBlank
    @Length(max=100)
    private String shortDescription;

    @NotBlank
    private String FullDescription;
}
