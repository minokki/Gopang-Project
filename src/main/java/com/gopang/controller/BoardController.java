package com.gopang.controller;

import com.gopang.account.CurrentUser;
import com.gopang.dto.BoardFormDto;
import com.gopang.dto.BoardSearchDto;
import com.gopang.dto.MainBoardDto;
import com.gopang.entity.Account;
import com.gopang.entity.Board;
import com.gopang.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class BoardController {
    private final BoardService boardService;
    /* 작업소개 페이지 이동 */
    @GetMapping(value = "/board/info")
    public String boardInfo(@CurrentUser Account account, Model model){
        if( account != null) {
            model.addAttribute(account);
        }
        return "board/board_info";
    }
    /* 시공사례 SAVE(GET) */
    @GetMapping(value = "/board/new")
    public String boardForm(@CurrentUser Account account,Model model) {
        model.addAttribute("boardFormDto", new BoardFormDto());
        model.addAttribute(account);
        return "board/board_form";
    }

    /* 시공사레 SAVE(POST) */
    @PostMapping(value = "/admin/board/write")
    public String boardNew(@Valid BoardFormDto boardFormDto, BindingResult bindingResult, Model model,
                               @RequestParam("boardImgFile") List<MultipartFile> multipartFiles) {
        if (bindingResult.hasErrors()) {
            return "board/board_form";
        }
        if (multipartFiles.get(0).isEmpty() && boardFormDto.getId() == null) {
            model.addAttribute("errorMessage", "이미지1, 이미지2 등록을 해주시길 바랍니다.");
            return "board/board_form";
        }
        if (multipartFiles.get(1).isEmpty() && boardFormDto.getId() == null) {
            model.addAttribute("errorMessage", "이미지1, 이미지2 등록을 해주시길 바랍니다.");
            return "board/board_form";
        }
        try {
            boardService.saveBoard(boardFormDto, multipartFiles);
        } catch (Exception e) {
            model.addAttribute("errorMessage", "등록중 에러 발생");
            return "board/board_form";
        }
        return "redirect:/board/main";
    }

    /* 시공사례 수정(GET) */
    @GetMapping("/admin/board/{boardId}")
    public String boardUpdategGet(@CurrentUser Account account, @PathVariable("boardId") Long boardId, Model model) {
        try {
            BoardFormDto boardFormDto = boardService.getBoardDtl(boardId);
            model.addAttribute(account);
            model.addAttribute("boardFormDto", boardFormDto);
        } catch (EntityNotFoundException e) {
            model.addAttribute("errorMessage", "존재하지 않는 게시글");
            model.addAttribute("boardFormDto", new BoardFormDto());
            return "board/board_form";
        }
        return "board/board_form";
    }

    /* 시공사례 수정(POST) */
    @PostMapping("/admin/board/{boardId}")
    public String boardUpdatePost(@Valid BoardFormDto boardFormDto, BindingResult bindingResult,
                                      @RequestParam("boardImgFile") List<MultipartFile> multipartFiles, Model model) {
        if (bindingResult.hasErrors()) {
            return "board/board_form";
        }

        if (multipartFiles.get(0).isEmpty() && boardFormDto.getId() == null) {
            model.addAttribute("errorMessage", "첫번째 이미지는 필수값");
            return "board/board_form";
        }

        try {
            boardService.updateBoard(boardFormDto, multipartFiles);
        } catch (Exception e) {
            model.addAttribute("errorMessage", "게시글 수정중 에러 발생");
            return "board/board_form";
        }
        return "redirect:/board/{boardId}";
    }

    /*시공사레 목록 */
    @GetMapping(value ={ "/board/main", "/board/main/{page}"})
    public String getBoard(@CurrentUser Account account, BoardSearchDto boardSearchDto,@PathVariable("page") Optional<Integer> page, Model model) {
        if( account != null) {
            model.addAttribute(account);
        }
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 12);
        Page<MainBoardDto> board = boardService.getMainBoardPage(boardSearchDto, pageable);
        model.addAttribute("board", board);
        model.addAttribute("boardSearchDto", boardSearchDto);
        model.addAttribute("maxPage", 5);

        return "board/board_main";
    }

    /* 시공사례 DETAIL */
    @GetMapping(value = "/board/{boardId}")
    public String boardDtl(@CurrentUser Account account, Model model, @PathVariable("boardId") Long boardId) {
        if( account != null) {
            model.addAttribute(account);
        }
        Board board = boardService.viewBoard(boardId);
        BoardFormDto boardFormDto = boardService.getBoardDtl(boardId);
        model.addAttribute("board", boardFormDto);
        model.addAttribute("boardViews", board);
        return "board/board_detail";
    }
}
