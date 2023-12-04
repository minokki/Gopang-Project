package com.gopang.repository;

import com.gopang.dto.BoardSearchDto;
import com.gopang.dto.MainBoardDto;
import com.gopang.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BoardRepositoryCustom {

    Page<Board> getAdminBoardPage(BoardSearchDto boardSearchDto, Pageable pageable);

    Page<MainBoardDto> getMainBoardPage(BoardSearchDto boardSearchDto, Pageable pageable);
}

