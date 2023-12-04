package com.gopang.repository;

import com.gopang.entity.BoardImg;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardImgRepository extends JpaRepository<BoardImg, Long> {

    List<BoardImg> findByBoardIdOrderByIdAsc(Long boardMainId);

}
