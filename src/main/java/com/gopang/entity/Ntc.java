package com.gopang.entity;

import com.gopang.dto.NtcFormDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "ntc")
@Getter
@Setter
@ToString
public class Ntc extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name= "ntc_id")
    private Long id;

    @Column(nullable = false,length = 50)
    private String title;

    @Lob
    @Column(nullable = false)
    private String content;

    @Column(columnDefinition = "integer default 0", nullable = false)
    private Long views = 0L;

    private String isImportant;

    public void updateNtc(NtcFormDto ntcFormDto) {
        this.title = ntcFormDto.getTitle();
        this.content = ntcFormDto.getContent();
        this.isImportant=ntcFormDto.getIsImportant();
    }
}
