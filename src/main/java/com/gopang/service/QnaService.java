package com.gopang.service;

import com.gopang.dto.QnaFormDto;
import com.gopang.dto.QnaResponseDto;
import com.gopang.repository.QnaRepository;
import com.gopang.dto.QnaSearchDto;
import com.gopang.entity.Qna;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class QnaService {

    private final QnaRepository qnaRepository;

    /* Q&A SAVE */
    public Long saveCommunityQna(QnaFormDto qnaFormDto) {
        Qna qna = qnaFormDto.createQna();
        qnaRepository.save(qna);
        return qna.getId();
    }

    /* Q&A READ */
    @Transactional(readOnly = true)
    public QnaFormDto getCommunityQna(Long qnaId) {
        Qna qna = qnaRepository.findById(qnaId).orElseThrow(EntityExistsException::new);

        QnaFormDto qnaFormDto = new QnaFormDto();
        qnaFormDto.setId(qna.getId());
        qnaFormDto.setTitle(qna.getTitle());
        qnaFormDto.setContent(qna.getContent());
        qnaFormDto.setCreateDate(qna.getRegTime());
        qnaFormDto.setCreateBy(qna.getCreateBy());

        return qnaFormDto;
    }
    /* Q&A UPDATE */
    public Long updateCommunityQna(QnaFormDto qnaFormDto) {
        Qna qna = qnaRepository.findById(qnaFormDto.getId()).orElseThrow(EntityNotFoundException::new);
        qna.updateQna(qnaFormDto);
        return qna.getId();
    }

    /* Q&A PAGING*/
    @Transactional(readOnly = true)
    public Page<Qna> getQnaPage(QnaSearchDto qnaSearchDto, Pageable pageable) {
        return qnaRepository.getQnaPage(qnaSearchDto, pageable);
    }

    /* Q&A 댓글 */
    public QnaResponseDto getQnaById(Long qnaId) {
        Qna qna = qnaRepository.findById(qnaId).orElseThrow(EntityExistsException::new);

        return new QnaResponseDto(qna);
    }
    /* 조회수 */
    @Transactional
    public Qna viewQna(Long qnaId) {
        Optional<Qna> optionalQna = qnaRepository.findById(qnaId);

        if (optionalQna.isPresent()) {
            Qna qna = optionalQna.get();

            // 조회수 증가
            qna.setViews(qna.getViews() + 1L);
            qnaRepository.save(qna);

            return qna;
        } else {
            throw new EntityNotFoundException("게시물을 찾을 수 없습니다."); // 예외 처리
        }
    }

}
