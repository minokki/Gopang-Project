package com.gopang.dto;

import com.gopang.entity.Account;
import com.gopang.entity.Comment;
import com.gopang.entity.Qna;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentRequestDto {
    private long id;
    private String comment;
    private String createDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm"));
    private String modifiedDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm"));
    private Account account;
    private Qna qna;
    private String commentProfileImg;

    public Comment toEntity(){
        Comment comments = Comment.builder()
                .id(id)
                .comment(comment)
                .createdDate(createDate)
                .modifiedDate(modifiedDate)
                .account(account)
                .commentProfileImg(commentProfileImg)
                .qna(qna)
                .build();
        return comments;
    }

}
