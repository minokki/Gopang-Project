package com.gopang.dto;

import com.gopang.entity.BoardMainImg;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

@Getter
@Setter
public class BoardMainImgDto {
    private Long id;

    private String imgName;

    private String oriImgName;

    private String BeforeImgUrl;

    private String AfterImgUrl;

    private static ModelMapper modelMapper = new ModelMapper();

    public static BoardMainImgDto ofv(BoardMainImg boardMainImg) {
        return modelMapper.map(boardMainImg, BoardMainImgDto.class);
    }
}
