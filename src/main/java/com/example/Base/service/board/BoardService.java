package com.example.Base.service.board;

import com.example.Base.domain.dto.BoardDTO;
import com.example.Base.domain.entity.BoardEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface BoardService {
    BoardEntity create(BoardDTO boardDTO);
    BoardEntity findOne(Long id);
    List<BoardEntity> findAll();
    BoardEntity update(BoardDTO boardDTO, Long id);
    String delete(Long id);
}
