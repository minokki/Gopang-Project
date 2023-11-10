package com.gopang.dto;

import com.gopang.entity.Comment;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
public class CommentResponseDto {
    private Long id;
    private String comment;
    private String createDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm"));
    private String modifiedDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm"));
    private String nickname;
    private Long qnaId;
    private String accountProfileImg;
    private String commentProfileImg;

    public CommentResponseDto(Comment comment) {
        this.id=comment.getId();
        this.comment = comment.getComment();
        this.createDate = comment.getCreatedDate();
        this.modifiedDate = comment.getModifiedDate();
        this.nickname = comment.getAccount().getNickname();
        this.qnaId = comment.getQna().getId();
        this.accountProfileImg=comment.getAccount().getProfileImage();
        this.commentProfileImg= comment.getCommentProfileImg();
    }
}

