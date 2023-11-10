package com.gopang.dto;

import com.gopang.entity.Qna;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Getter
@Setter
public class QnaFormDto {

    private Long id;

    @NotBlank(message = "제목을 작성해 주시길 바랍니다.")
    private String title;

    @NotBlank(message = "내용을 작성해 주시길 바랍니다.")
    private String content;

    @Column(updatable = false, columnDefinition = "DATETIME")
    private LocalDateTime createDate;

    private Long views;

    private String createBy;

    private static ModelMapper modelMapper = new ModelMapper();

    public Qna createQna(){
        Qna qna = modelMapper.map(this, Qna.class);
        qna.setViews(0L); // views를 0으로 초기화
        return qna;
    }

}
