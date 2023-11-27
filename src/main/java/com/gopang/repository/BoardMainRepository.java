package com.gopang.repository;

import com.gopang.entity.BoardMain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface BoardMainRepository extends JpaRepository<BoardMain, Long>, QuerydslPredicateExecutor<BoardMain>, BoardMainRepositoryCustom {


}