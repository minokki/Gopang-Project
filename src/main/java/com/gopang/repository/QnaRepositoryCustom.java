package com.gopang.repository;

import com.gopang.dto.QnaSearchDto;
import com.gopang.entity.Qna;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface QnaRepositoryCustom {
    Page<Qna> getQnaPage(QnaSearchDto qnaSearchDto, Pageable pageable);
}
