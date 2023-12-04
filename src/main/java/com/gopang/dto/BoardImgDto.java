package com.gopang.dto;

import com.gopang.entity.BoardImg;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

@Getter
@Setter
public class BoardImgDto {
    private Long id;

    private String imgName;

    private String oriImgName;

    private String BeforeImgUrl;

    private String AfterImgUrl;

    private static ModelMapper modelMapper = new ModelMapper();

    public static BoardImgDto ofv(BoardImg boardImg) {
        return modelMapper.map(boardImg, BoardImgDto.class);
    }
}
