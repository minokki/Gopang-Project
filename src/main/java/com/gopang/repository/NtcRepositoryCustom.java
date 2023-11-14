package com.gopang.repository;

import com.gopang.dto.NtcSearchDto;
import com.gopang.entity.Ntc;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NtcRepositoryCustom {
    Page<Ntc> getNtcPage(NtcSearchDto ntcSearchDto, Pageable pageable);
}
