package com.gopang.entity;

import com.gopang.dto.BoardFormDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "board")
@Getter
@Setter
@ToString
public class Board extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Long id;

    @Column(nullable = false,length = 50)
    private String title;

    @Lob
    @Column(nullable = false)
    private String content;

    @Column(columnDefinition = "integer default 0", nullable = false)
    private Long views = 0L;

    public void updateBoard(BoardFormDto boardMainFormDto) {
        this.title = boardMainFormDto.getTitle();
        this.content = boardMainFormDto.getContent();
    }
}
