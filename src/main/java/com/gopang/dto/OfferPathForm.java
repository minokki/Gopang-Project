package com.gopang.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class OfferPathForm {
    @NotBlank
    @Length(min=3, max=20)
    @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-zA-Z0-9]{3,20}$")
    private String newPath;
}
