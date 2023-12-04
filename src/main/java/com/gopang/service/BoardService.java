package com.gopang.service;

import com.gopang.constant.Example;
import com.gopang.dto.BoardFormDto;
import com.gopang.dto.BoardImgDto;
import com.gopang.dto.BoardSearchDto;
import com.gopang.dto.MainBoardDto;
import com.gopang.entity.Board;
import com.gopang.entity.BoardImg;
import com.gopang.repository.BoardImgRepository;
import com.gopang.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class BoardService {

    private final BoardImgRepository boardImgRepository;
    private final BoardRepository boardRepository;
    private final BoardImgService boardImgService;

    /* 시공사례 등록 */
    public Long saveBoard(BoardFormDto boardFormDto, List<MultipartFile> multipartFiles) throws Exception {

        //게시글 등록
        Board board = boardFormDto.createBoard();
        boardRepository.save(board);

        //이미지 등록
        for (int i = 0; i < multipartFiles.size(); i++) {
            BoardImg boardImg = new BoardImg();
            boardImg.setBoard(board);
            if (i == 0) {
                boardImg.setExample(Example.BEFORE);  //첫번재 사진 BEFORE
            } else {
                boardImg.setExample(Example.AFTER); //두번째 사진 AFTER
            }
            boardImgService.saveBoardImg(boardImg, multipartFiles.get(i));
        }
        return board.getId();
    }

    /* 시공사례 조회 */
    @Transactional(readOnly = true)
    public BoardFormDto getBoardDtl(Long boardId) {
        // boardId에 해당하는 Board 엔티티 조회
        Board board = boardRepository.findById(boardId).orElseThrow(EntityNotFoundException::new);

        // boardId에 해당하는 BoardImg 엔티티들 조회
        List<BoardImg> boardImgList = boardImgRepository.findByBoardIdOrderByIdAsc(boardId);

        // BoardFormDto에 필요한 정보 설정
        BoardFormDto boardFormDto = new BoardFormDto();
        boardFormDto.setId(board.getId());
        boardFormDto.setTitle(board.getTitle());
        boardFormDto.setContent(board.getContent());
        boardFormDto.setCreateDate(board.getRegTime());
        boardFormDto.setCreateBy(board.getCreateBy());

        // BoardImg 엔티티의 example 값을 기준으로 이미지들을 분류하여 설정
        for (BoardImg boardImg : boardImgList) {
            BoardImgDto boardImgDto = BoardImgDto.ofv(boardImg);
            if (Example.BEFORE.equals(boardImg.getExample())) {
                boardImgDto.setBeforeImgUrl(boardImg.getOriImgName());
                boardFormDto.setBeforeImgUrl(boardImg.getOriImgName());
            } else if (Example.AFTER.equals(boardImg.getExample())) {
                boardImgDto.setAfterImgUrl(boardImg.getOriImgName());
                boardFormDto.setAfterImgUrl(boardImg.getOriImgName());
            }
            // 이미지 정보를 리스트에 추가
            boardFormDto.getBoardImgDtoList().add(boardImgDto);
        }
        return boardFormDto;
    }

    /* 시공사례 UPDATE */
    public Long updateBoard(BoardFormDto boardFormDto, List<MultipartFile> multipartFiles) throws Exception {

        Board board = boardRepository.findById(boardFormDto.getId()).orElseThrow(EntityNotFoundException::new);
        board.updateBoard(boardFormDto);
        List<Long> boardImgIds = boardFormDto.getBoardImgIds();

        //이미지 등록
        for (int i = 0; i < multipartFiles.size(); i++) {
            boardImgService.updateBoardImg(boardImgIds.get(i), multipartFiles.get(i));
        }
        return board.getId();
    }

    /* 시공사례 PAGING(관리자) */
    public Page<Board> getAdminBoardPage(BoardSearchDto boardSearchDto, Pageable pageable) {
        return boardRepository.getAdminBoardPage(boardSearchDto, pageable);
    }

    /* 시공사례 PAGING */
    @Transactional(readOnly = true)
    public Page<MainBoardDto> getMainBoardPage(BoardSearchDto boardSearchDto, Pageable pageable) {
        return boardRepository.getMainBoardPage(boardSearchDto, pageable);
    }

    /* 시공사레 조회수 */
    public Board viewBoard(Long BoardId) {
        Optional<Board> optionalBoard = boardRepository.findById(BoardId);

        if (optionalBoard.isPresent()) {
            Board board = optionalBoard.get();

            // 조회수 증가
            board.setViews(board.getViews() + 1L);
            boardRepository.save(board);

            return board;
        } else {
            throw new EntityNotFoundException("게시물을 찾을 수 없습니다."); // 예외 처리
        }
    }
}
