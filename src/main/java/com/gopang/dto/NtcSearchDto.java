package com.gopang.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NtcSearchDto {

    private String searchDateType;

    private String searchBy;

    private String searchQuery="";

}
