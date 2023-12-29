package com.gopang.repository;

import com.gopang.entity.Offer;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface OfferRepository extends JpaRepository<Offer,Long>, QuerydslPredicateExecutor<Offer>, OfferRepositoryCustom {
    boolean existsByPath(String path);

    @EntityGraph(value = "Offer.withAll", type = EntityGraph.EntityGraphType.LOAD)
    Offer findByPath(String path);

    @EntityGraph(attributePaths = "managers")
    Offer findStudyWithManagersByPath(String path);

    @EntityGraph(attributePaths = "members")
    Offer findOfferWithMembersByPath(String path);
}
