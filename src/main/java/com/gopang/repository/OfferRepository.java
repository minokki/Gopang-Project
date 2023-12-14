package com.gopang.repository;

import com.gopang.entity.Offer;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public interface OfferRepository extends JpaRepository<Offer,Long> {
    boolean existsByPath(String path);

    @EntityGraph(value = "Offer.withAll", type = EntityGraph.EntityGraphType.LOAD)
    Offer findByPath(String path);
}
