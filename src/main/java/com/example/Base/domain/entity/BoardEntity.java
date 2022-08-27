package com.example.Base.domain.entity;

import com.example.Base.domain.dto.BoardDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "board")
public class BoardEntity {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String content;

    public BoardDTO toDTO(){
        BoardDTO boardDTO = BoardDTO.builder()
                .id(id)
                .title(title)
                .content(content)
                .build();
        return boardDTO;
    }
}
