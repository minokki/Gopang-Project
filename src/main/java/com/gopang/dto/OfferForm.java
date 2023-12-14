package com.gopang.dto;

import com.gopang.entity.Ntc;
import com.gopang.entity.Offer;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.modelmapper.ModelMapper;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class OfferForm {

    @NotBlank
    @Length(min=2, max = 20)
    @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-zA-Z0-9]{3,20}$")
    private String path;

    @NotBlank
    @Length(max = 50)
    private String title;

    @NotBlank
    @Length(max = 100)
    private String shortDescription;

    @NotBlank
    private String fullDescription;


}
