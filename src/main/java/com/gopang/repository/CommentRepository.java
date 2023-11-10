package com.gopang.repository;

import com.gopang.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Optional<Comment> findByQnaIdAndId(Long id, Long qnaId);

}
