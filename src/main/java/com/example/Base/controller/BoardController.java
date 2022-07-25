package com.example.Base.controller;

import com.example.Base.domain.dto.BoardDTO;
import com.example.Base.service.BoardServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/boards")
public class BoardController {

    private final BoardServiceImpl boardService;

    @PostMapping("/new")
    public ResponseEntity createBoard(@RequestBody BoardDTO boardDTO){
        return ResponseEntity.created(URI.create("/boards/new")).body(boardService.create(boardDTO));
    }

    @GetMapping("/board")
    public ResponseEntity findAllBoard(){
        return ResponseEntity.ok().body(boardService.findAll());
    }

    @GetMapping("/board/{id}")
    public ResponseEntity findOneBoard(@PathVariable Long id){
        return ResponseEntity.ok().body(boardService.findOne(id));
    }

    @PutMapping("/board/{id}")
    public ResponseEntity updateBoard(@RequestBody BoardDTO boardDTO ,@PathVariable Long id){
        return ResponseEntity.created(URI.create("/boards/board/update")).body(boardService.update(boardDTO, id));
    }

    @DeleteMapping("/board/{id")
    public ResponseEntity deleteBoard(@PathVariable Long id){
        return ResponseEntity.ok().body(boardService.delete(id ));
    }
}
