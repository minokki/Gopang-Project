package com.gopang.service;

import com.gopang.entity.BoardImg;
import com.gopang.file.S3Uploader;
import com.gopang.repository.BoardImgRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;

@Service
@RequiredArgsConstructor
@Transactional
public class BoardImgService {

    private final BoardImgRepository boardImgRepository;
    private final S3Uploader s3Uploader;

    /* 이미지 저장 */
    public void saveBoardImg(BoardImg boardImg, MultipartFile multipartFile) throws IOException {
        String oriImgName = multipartFile.getOriginalFilename();
        String imgName = "";
        String imgUrl = "";

        if (!StringUtils.isEmpty(oriImgName)) {
            // S3에 이미지 업로드
            imgName = s3Uploader.uploadFiles(multipartFile, "board");
            imgUrl = "/board/" + imgName;
        }

        // 상품 이미지 정보 저장
        boardImg.updateBoardImg(oriImgName, imgName, imgUrl);
        boardImgRepository.save(boardImg);
    }

    /* 이미지 UPDATE */
    public void updateBoardImg(Long boardImgId, MultipartFile multipartFile) throws IOException {
        if (!multipartFile.isEmpty()) {
            BoardImg savedBoardImg = boardImgRepository.findById(boardImgId).orElseThrow(EntityNotFoundException::new);

            // 기존 이미지 삭제
            if (savedBoardImg.getImgUrl() != null) {
                s3Uploader.deleteFile(savedBoardImg.getImgUrl());
            }

            // S3에 새 이미지 업로드
            String newImgName = s3Uploader.uploadFiles(multipartFile, "board");
            String newImgUrl = "/images/board/" + newImgName;

            // 이미지 정보 업데이트
            savedBoardImg.updateBoardImg(multipartFile.getOriginalFilename(), newImgName, newImgUrl);
        }
    }
}