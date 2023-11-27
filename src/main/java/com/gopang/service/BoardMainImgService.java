package com.gopang.service;

import com.gopang.entity.BoardMainImg;
import com.gopang.file.S3Uploader;
import com.gopang.repository.BoardMainImgRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;

@Service
@RequiredArgsConstructor
@Transactional
public class BoardMainImgService {

    private final BoardMainImgRepository boardMainImgRepository;
    private final S3Uploader s3Uploader;

    /* 이미지 저장 */
    public void saveBoardMainImg(BoardMainImg boardMainImg, MultipartFile multipartFile) throws IOException {
        String oriImgName = multipartFile.getOriginalFilename();
        String imgName = "";
        String imgUrl = "";

        if (!StringUtils.isEmpty(oriImgName)) {
            // S3에 이미지 업로드
            imgName = s3Uploader.uploadFiles(multipartFile, "board");
            imgUrl = "/board/" + imgName;
        }

        // 상품 이미지 정보 저장
        boardMainImg.updateBoardMainImg(oriImgName, imgName, imgUrl);
        boardMainImgRepository.save(boardMainImg);
    }

    /* 이미지 UPDATE */
    public void updateBoardMainImg(Long boardMainImgId, MultipartFile multipartFile) throws IOException {
        if (!multipartFile.isEmpty()) {
            BoardMainImg savedBoardMainImg = boardMainImgRepository.findById(boardMainImgId).orElseThrow(EntityNotFoundException::new);

            // 기존 이미지 삭제
            if (savedBoardMainImg.getImgUrl() != null) {
                s3Uploader.deleteFile(savedBoardMainImg.getImgUrl());
            }

            // S3에 새 이미지 업로드
            String newImgName = s3Uploader.uploadFiles(multipartFile, "board");
            String newImgUrl = "/images/board/" + newImgName;

            // 이미지 정보 업데이트
            savedBoardMainImg.updateBoardMainImg(multipartFile.getOriginalFilename(), newImgName, newImgUrl);
        }
    }
}