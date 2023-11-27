package com.gopang.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@Transactional
@RequiredArgsConstructor
@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final AccountRepository accountRepository;
    private final QnaRepository qnaRepository;

    /* 댓글 작성 */
    public Long commentSave(String nickname, Long id, CommentRequestDto dto) {
        Account account = accountRepository.findByNickname(nickname);

        Qna qna = qnaRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("댓글 쓰기 실패: 해당 게시글이 존재하지 않습니다." + id));
        dto.setAccount(account);
        dto.setQna(qna);

        Comment comment = dto.toEntity();
        commentRepository.save(comment);

        return dto.getId();
    }

    /* 댓글 UPDATE */
    public void commentUpdate(Long qnaId, Long commentId, CommentRequestDto dto) {

        Comment comment = commentRepository.findByQnaIdAndId(qnaId, commentId)
                .orElseThrow(() -> new EntityNotFoundException("댓글을 찾을 수 없습니다."));
        // CommentRequestDto에서 업데이트할 내용을 가져와서 엔티티에 반영
        comment.update(dto.getComment());
    }

    /* 댓글 DELETE */
    public void commentDelete(Long qnaId, Long commentId) {
        Comment comment = commentRepository.findByQnaIdAndId(qnaId, commentId).orElseThrow(() ->
                new IllegalArgumentException("해당 댓글이 존재하지 않습니다. id=" + commentId));
        commentRepository.delete(comment);
    }
}
