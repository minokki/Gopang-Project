package com.gopang.ApiController;

import com.gopang.account.CurrentUser;
import com.gopang.dto.CommentRequestDto;
import com.gopang.entity.Account;
import com.gopang.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class CommentApiController {
    private final CommentService commentService;

    /* 댓글 SAVE */
    @PostMapping("/qna/{qnaId}/comments")
    public ResponseEntity commentSave(@PathVariable Long qnaId, @RequestBody CommentRequestDto dto,
                                      @CurrentUser Account account) {

        return ResponseEntity.ok(commentService.commentSave(account.getNickname(), qnaId, dto));
    }

    /* 댓글 UPDATE */
    @PutMapping({"/qna/{qnaId}/comments/{commentId}"})
    public ResponseEntity<Long> update(@PathVariable Long qnaId, @PathVariable Long commentId, @RequestBody CommentRequestDto dto) {
        commentService.commentUpdate(qnaId, commentId, dto);
        return ResponseEntity.ok(commentId);
    }

    /* 댓글 DELETE */
    @DeleteMapping({"/qna/{qnaId}/comments/{commentId}"})
    public ResponseEntity<Long> delete(@PathVariable Long qnaId, @PathVariable Long commentId) {
        commentService.commentDelete(qnaId, commentId);
        return ResponseEntity.ok(commentId);
    }
}
