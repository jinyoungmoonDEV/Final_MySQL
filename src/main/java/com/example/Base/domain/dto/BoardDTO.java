package com.example.Base.domain.dto;

import com.example.Base.domain.entity.BoardEntity;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class BoardDTO {
    private Long id;

    private String title;

    private String content;

    public BoardEntity toEntity() {
        BoardEntity boardEntity = BoardEntity.builder()
                .id(id)
                .title(title)
                .content(content)
                .build();
        return boardEntity;
    }
}
