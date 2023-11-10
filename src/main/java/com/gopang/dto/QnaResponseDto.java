package com.gopang.dto;

import com.gopang.entity.Qna;
import lombok.Getter;

import javax.persistence.Column;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class QnaResponseDto {

    private Long id;
    private String title;
    private String writer;
    private String content;
    @Column(updatable = false, columnDefinition = "DATETIME")
    private LocalDateTime createDate;
    private List<CommentResponseDto> comments;

    public QnaResponseDto(Qna qna) {
        this.id = qna.getId();
        this.title = qna.getTitle();
        this.writer=qna.getCreateBy();
        this.createDate = qna.getRegTime();
        this.content=qna.getContent();
        this.comments = qna.getComments().stream().map(CommentResponseDto::new).collect(Collectors.toList());
    }
}
