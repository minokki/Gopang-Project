package com.gopang.entity;

import com.gopang.dto.QnaFormDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
@Table(name = "qna")
public class Qna extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "qna_id")
    private Long id;

    @Column(nullable = false,length = 50)
    private String title;

    @Lob
    @Column(nullable = false)
    private String content;

    @Column(columnDefinition = "integer default 0", nullable = false)
    private Long views = 0L;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    @OneToMany(mappedBy = "qna",fetch = FetchType.EAGER,cascade = CascadeType.REMOVE)
    private List<Comment> comments;

    public void updateQna(QnaFormDto qnaFormDto) {
        this.title = qnaFormDto.getTitle();
        this.content = qnaFormDto.getContent();
    }

}