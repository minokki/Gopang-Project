package com.gopang.repository;

import com.gopang.dto.BoardSearchDto;
import com.gopang.dto.MainBoardDto;
import com.gopang.entity.BoardMain;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BoardMainRepositoryCustom {

    Page<BoardMain> getAdminBoardMainPage(BoardSearchDto boardSearchDto, Pageable pageable);

    Page<MainBoardDto> getMainBoardPage(BoardSearchDto boardSearchDto, Pageable pageable);
}

