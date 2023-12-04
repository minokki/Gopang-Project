package com.gopang.dto;

import com.gopang.entity.Board;
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
public class BoardFormDto {
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
    private List<BoardImgDto> boardImgDtoList = new ArrayList<>();

    //게시글 이미지 id 저장하는 리스트
    private List<Long> boardImgIds = new ArrayList<>();

    private static ModelMapper modelMapper = new ModelMapper();
    public Board createBoard(){
        Board board = modelMapper.map(this, Board.class);
        board.setViews(0l);
        return board;
    }

    public static BoardFormDto ofv(Board board) {
        return modelMapper.map(board, BoardFormDto.class);
    }
}
