package com.gopang.repository;

import com.gopang.constant.Example;
import com.gopang.dto.BoardSearchDto;
import com.gopang.dto.MainBoardDto;
import com.gopang.entity.Board;
import com.gopang.entity.QBoard;
import com.gopang.entity.QBoardImg;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.*;

public class BoardRepositoryCustomImpl implements BoardRepositoryCustom {

    private JPAQueryFactory queryFactory;

    public BoardRepositoryCustomImpl(EntityManager em) {
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

        return QBoard.board.regTime.after(dateTime);
    }

    private BooleanExpression searchByLike(String searchBy, String searchQuery) {
        if (StringUtils.equals("title", searchBy)) {
            return QBoard.board.title.like("%" + searchQuery + "%");
        } else if (StringUtils.equals("createdBy", searchBy)) {
            return QBoard.board.createBy.like("%" + searchQuery + "%");
        }
        return null;
    }
    @Override
    public Page<Board> getAdminBoardPage(BoardSearchDto boardSearchDto, Pageable pageable) {
        List<Board> content = queryFactory.selectFrom(QBoard.board)
                .select(QBoard.board)
                .where(regDtsAfter(boardSearchDto.getSearchDateType()),
                        searchByLike(boardSearchDto.getSearchBy(),
                                boardSearchDto.getSearchQuery()))
                .orderBy(QBoard.board.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory.select(Wildcard.count).from(QBoard.board)
                .where(regDtsAfter(boardSearchDto.getSearchDateType()),
                        searchByLike(boardSearchDto.getSearchBy(), boardSearchDto.getSearchQuery()))
                .fetchOne();

        return new PageImpl<>(content, pageable, total);
    }
    private BooleanExpression boardTitleLike(String searchQuery){
        return StringUtils.isEmpty(searchQuery) ? null : QBoard.board.title.like("%" + searchQuery + "%");
    }
    @Override
    public Page<MainBoardDto> getMainBoardPage(BoardSearchDto boardSearchDto, Pageable pageable) {
        QBoard board = QBoard.board;
        QBoardImg boardImg = QBoardImg.boardImg;

        Map<Long, MainBoardDto> boardDtoMap = new LinkedHashMap<>();

        List<Tuple> tuples = queryFactory
                .select(
                        board.id,
                        board.title,
                        board.content,
                        boardImg.oriImgName,
                        boardImg.example
                )
                .from(board)
                .leftJoin(boardImg).on(board.id.eq(boardImg.board.id))
                .where(boardTitleLike(boardSearchDto.getSearchQuery()))
                .orderBy(board.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        for (Tuple tuple : tuples) {
            Long id = tuple.get(board.id);
            String title = tuple.get(board.title);
            String content = tuple.get(board.content);
            String imgUrl = tuple.get(boardImg.oriImgName);
            Example example = tuple.get(boardImg.example);

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
                .from(board)
                .leftJoin(boardImg).on(board.id.eq(boardImg.board.id))
                .where(boardTitleLike(boardSearchDto.getSearchQuery()))
                .fetchCount();

        return new PageImpl<>(content, pageable, total);
    }


}
