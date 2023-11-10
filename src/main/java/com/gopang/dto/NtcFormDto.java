package com.gopang.dto;

import com.gopang.entity.Ntc;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Getter
@Setter
public class NtcFormDto {

    private Long id;

    @NotBlank(message = "제목을 작성해 주시길 바랍니다.")
    private String title;

    @NotBlank(message = "내용을 작성해 주시길 바랍니다.")
    private String content;

    @Column(updatable = false, columnDefinition = "DATETIME")
    private LocalDateTime createDate;

    private String createBy;

    private Long views;

    private String isImportant;

    private static ModelMapper modelMapper = new ModelMapper();

    public Ntc createNtc(){
        Ntc ntc = modelMapper.map(this, Ntc.class);
        ntc.setViews(0l);
        return ntc;
    }
}
