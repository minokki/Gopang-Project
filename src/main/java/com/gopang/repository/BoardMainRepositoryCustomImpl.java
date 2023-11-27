package com.gopang.repository;

import com.gopang.constant.Example;
import com.gopang.dto.BoardMainFormDto;
import com.gopang.dto.BoardMainImgDto;
import com.gopang.dto.BoardSearchDto;
import com.gopang.dto.MainBoardDto;
import com.gopang.entity.BoardMain;
import com.gopang.entity.BoardMainImg;
import com.gopang.entity.QBoardMain;
import com.gopang.entity.QBoardMainImg;
import com.gopang.service.BoardMainImgService;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.*;

public class BoardMainRepositoryCustomImpl implements BoardMainRepositoryCustom {

    private JPAQueryFactory queryFactory;

    public BoardMainRepositoryCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    private BooleanExpression regDtsAfter(String searchDateType) {
        LocalDateTime dateTime = LocalDateTime.now();

        if (StringUtils.equals("all", searchDateType) || searchDateType == null) {
            return null;
        } else if (StringUtils.equals("1d", searchDateType)) {
            dateTime = dateTime.minusDays(1);
        } else if (StringUtils.equals("1w", searchDateType)) {
            dateTime = dateTime.minusWeeks(1);
        } else if (StringUtils.equals("1m", searchDateType)) {
            dateTime = dateTime.minusMonths(1);
        } else if (StringUtils.equals("6m", searchDateType)) {
            dateTime = dateTime.minusMonths(6);
        }

        return QBoardMain.boardMain.regTime.after(dateTime);
    }

    private BooleanExpression searchByLike(String searchBy, String searchQuery) {
        if (StringUtils.equals("title", searchBy)) {
            return QBoardMain.boardMain.title.like("%" + searchQuery + "%");
        } else if (StringUtils.equals("createdBy", searchBy)) {
            return QBoardMain.boardMain.createBy.like("%" + searchQuery + "%");
        }
        return null;
    }
    @Override
    public Page<BoardMain> getAdminBoardMainPage(BoardSearchDto boardSearchDto, Pageable pageable) {
        List<BoardMain> content = queryFactory.selectFrom(QBoardMain.boardMain)
                .select(QBoardMain.boardMain)
                .where(regDtsAfter(boardSearchDto.getSearchDateType()),
                        searchByLike(boardSearchDto.getSearchBy(),
                                boardSearchDto.getSearchQuery()))
                .orderBy(QBoardMain.boardMain.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory.select(Wildcard.count).from(QBoardMain.boardMain)
                .where(regDtsAfter(boardSearchDto.getSearchDateType()),
                        searchByLike(boardSearchDto.getSearchBy(), boardSearchDto.getSearchQuery()))
                .fetchOne();

        return new PageImpl<>(content, pageable, total);
    }
    private BooleanExpression boardMainTitleLike(String searchQuery){
        return StringUtils.isEmpty(searchQuery) ? null : QBoardMain.boardMain.title.like("%" + searchQuery + "%");
    }
    @Override
    public Page<MainBoardDto> getMainBoardPage(BoardSearchDto boardSearchDto, Pageable pageable) {
        QBoardMain boardMain = QBoardMain.boardMain;
        QBoardMainImg boardMainImg = QBoardMainImg.boardMainImg;

        Map<Long, MainBoardDto> boardDtoMap = new LinkedHashMap<>();

        List<Tuple> tuples = queryFactory
                .select(
                        boardMain.id,
                        boardMain.title,
                        boardMain.content,
                        boardMainImg.oriImgName,
                        boardMainImg.example
                )
                .from(boardMain)
                .leftJoin(boardMainImg).on(boardMain.id.eq(boardMainImg.boardMain.id))
                .where(boardMainTitleLike(boardSearchDto.getSearchQuery()))
                .orderBy(boardMain.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        for (Tuple tuple : tuples) {
            Long id = tuple.get(boardMain.id);
            String title = tuple.get(boardMain.title);
            String content = tuple.get(boardMain.content);
            String imgUrl = tuple.get(boardMainImg.oriImgName);
            Example example = tuple.get(boardMainImg.example);

            MainBoardDto boardDto = boardDtoMap.getOrDefault(id, new MainBoardDto());
            boardDto.setId(id);
            boardDto.setTitle(title);
            boardDto.setContent(content);

            if (Example.BEFORE.equals(example)) {
                boardDto.setImgUrlBefore(imgUrl);
            } else if (Example.AFTER.equals(example)) {
                boardDto.setImgUrlAfter(imgUrl);
            }

            boardDtoMap.put(id, boardDto);
        }

        List<MainBoardDto> content = new ArrayList<>(boardDtoMap.values());

        long total = queryFactory
                .select(Wildcard.count)
                .from(boardMain)
                .leftJoin(boardMainImg).on(boardMain.id.eq(boardMainImg.boardMain.id))
                .where(boardMainTitleLike(boardSearchDto.getSearchQuery()))
                .fetchCount();

        return new PageImpl<>(content, pageable, total);
    }


}
