package com.gopang.dto;

import com.gopang.entity.BoardMain;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class BoardMainFormDto {
    private Long id;

    @NotBlank(message = "제목을 작성해주세요!")
    private String title;

    @NotBlank(message = "내용을 작성해주세요!")
    private String content;

    private String imgUrl;

    private String afterImgUrl; // after 이미지의 URL

    private String beforeImgUrl; // before 이미지의 URL

    @Column(updatable = false, columnDefinition = "DATETIME")
    private LocalDateTime createDate;

    private String createBy;

    private Long views;

    //게시글 수정시 이미지 정보 저장하는 리스트
    private List<BoardMainImgDto> boardMainImgDtoList = new ArrayList<>();

    //게시글 이미지 id 저장하는 리스트
    private List<Long> boardMainImgIds = new ArrayList<>();

    private static ModelMapper modelMapper = new ModelMapper();
    public BoardMain createBoardMain(){
        BoardMain boardMain = modelMapper.map(this, BoardMain.class);
        boardMain.setViews(0l);
        return boardMain;
    }

    public static BoardMainFormDto ofv(BoardMain boardMain) {
        return modelMapper.map(boardMain, BoardMainFormDto.class);
    }
}
