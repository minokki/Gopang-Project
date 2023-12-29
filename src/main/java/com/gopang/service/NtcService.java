package com.gopang.service;

import com.gopang.dto.NtcFormDto;
import com.gopang.dto.NtcSearchDto;
import com.gopang.entity.Ntc;
import com.gopang.repository.NtcRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class NtcService {
    private final NtcRepository ntcRepository;

    /* 공지사항 저장 */
    public Long saveCommunityNtc(NtcFormDto ntcFormDto) {
        Ntc ntc = ntcFormDto.createNtc();
        ntcRepository.save(ntc);
        return ntc.getId();
    }

    /* 공지사항 READ */
    @Transactional(readOnly = true)
    public NtcFormDto getCommunityNtc(Long ntcId) {
        Ntc ntc = ntcRepository.findById(ntcId).orElseThrow(EntityExistsException::new);

        NtcFormDto ntcFormDto = new NtcFormDto();
        ntcFormDto.setId(ntc.getId());
        ntcFormDto.setTitle(ntc.getTitle());
        ntcFormDto.setContent(ntc.getContent());
        ntcFormDto.setCreateBy(ntc.getCreateBy());
        ntcFormDto.setCreateDate(ntc.getRegTime());

        return ntcFormDto;
    }

    /* 공지사항 UPDATE */
    public Long updateCommunityNtc(NtcFormDto ntcFormDto) throws Exception {
        Ntc ntc = ntcRepository.findById(ntcFormDto.getId()).orElseThrow(EntityExistsException::new);
        ntc.updateNtc(ntcFormDto);
        return ntc.getId();
    }

    /* 공지사항 PAGING */
    @Transactional(readOnly = true)
    public Page<Ntc> getNtcPage(NtcSearchDto ntcSearchDto, Pageable pageable) {
        return ntcRepository.getNtcPage(ntcSearchDto, pageable);
    }

    /* 공지사항 조회수 */
    public Ntc viewNtc(Long ntcId) {
        Optional<Ntc> optionalNtc = ntcRepository.findById(ntcId);

        if (optionalNtc.isPresent()) {
            Ntc ntc = optionalNtc.get();

            // 조회수 증가
            ntc.setViews(ntc.getViews() + 1L);
            ntcRepository.save(ntc);

            return ntc;
        } else {
            throw new EntityNotFoundException("게시물을 찾을 수 없습니다."); // 예외 처리
        }
    }

    public void deleteNtc(Long ntcId) {
        ntcRepository.deleteById(ntcId);
    }
}
