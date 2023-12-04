package com.gopang.entity;

import com.gopang.constant.Example;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "board_img")
@Getter
@Setter
@NoArgsConstructor
public class BoardImg extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "board_img_id")
    private Long id;

    private String imgName; //이미지파일명

    private String oriImgName; //원본 이미지 명

    private String imgUrl; //이미지 경로

    @Enumerated(EnumType.STRING)
    private Example example;  // before,after 이미지

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    public void updateBoardImg(String imgName, String oriImgName, String imgUrl) {
        this.imgName = imgName;
        this.oriImgName = oriImgName;
        this.imgUrl = imgUrl;
    }
}
