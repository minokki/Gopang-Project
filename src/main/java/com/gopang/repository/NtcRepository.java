package com.gopang.repository;

import com.gopang.entity.Ntc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface NtcRepository extends JpaRepository<Ntc,Long>, QuerydslPredicateExecutor<Ntc>, NtcRepositoryCustom{
}