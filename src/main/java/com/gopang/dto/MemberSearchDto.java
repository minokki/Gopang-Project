package com.gopang.dto;

import lombok.Data;

@Data
public class MemberSearchDto {
    private String searchDateType;

    private String searchBy;

    private String searchQuery = "";
}
