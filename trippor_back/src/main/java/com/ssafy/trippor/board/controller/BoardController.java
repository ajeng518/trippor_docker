package com.ssafy.trippor.board.controller;

import com.ssafy.trippor.board.model.BoardDto;
import com.ssafy.trippor.board.model.BoardListDto;
import com.ssafy.trippor.board.model.service.BoardService;
import com.ssafy.trippor.util.JWTUtil;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//@CrossOrigin(origins = { "*" }, methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.POST} , maxAge = 6000)
@RestController
@RequestMapping("/board")
@Api("게시판 컨트롤러  API V1")
@Slf4j
public class BoardController {
    private BoardService boardService;

    private JWTUtil jwtUtil;

    public BoardController(BoardService boardService, JWTUtil jwtUtil) {
        super();
        this.boardService = boardService;
        this.jwtUtil = jwtUtil;
    }

    @ApiOperation(value = "게시판 글작성", notes = "새로운 게시글 정보를 입력한다.", response = Map.class)
    @PostMapping
    public ResponseEntity<?> writeArticle(
            @RequestBody @ApiParam(value = "게시글 정보.", required = true) BoardDto boardDto, HttpServletRequest request) {
        log.info("writeArticle boardDto - {}", request.getHeader("Authorization"));
        if(jwtUtil.checkToken(request.getHeader("Authorization"))) {
            log.debug("글 작성가능해요");
            try {
                boardService.writeArticle(boardDto);
//			return ResponseEntity.ok().build();
                return new ResponseEntity<Void>(HttpStatus.CREATED);
            } catch (Exception e) {
                return exceptionHandling(e);
            }
        }
        else {
            log.error("사용 불가능 토큰!!!");
            return new ResponseEntity<Void>(HttpStatus.UNAUTHORIZED);
        }
    }

    @ApiOperation(value = "게시판 글목록", notes = "모든 게시글의 정보를 반환한다.", response = List.class)
    @ApiResponses({ @ApiResponse(code = 200, message = "회원목록 OK!!"), @ApiResponse(code = 404, message = "페이지없어!!"),
            @ApiResponse(code = 500, message = "서버에러!!") })
    @GetMapping
    public ResponseEntity<?> listArticle(
            @RequestParam @ApiParam(value = "게시글을 얻기위한 부가정보.", required = true) Map<String, String> map) {
        log.info("listArticle map - {}", map);
        try {
            BoardListDto boardListDto = boardService.listArticle(map);
            HttpHeaders header = new HttpHeaders();
            header.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
            return ResponseEntity.ok().headers(header).body(boardListDto);
        } catch (Exception e) {
            return exceptionHandling(e);
        }
    }

    @ApiOperation(value = "게시판 글보기", notes = "글번호에 해당하는 게시글의 정보를 반환한다.", response = BoardDto.class)
    @GetMapping("/{articleno}")
    public ResponseEntity<BoardDto> getArticle(
            @PathVariable("articleno") @ApiParam(value = "얻어올 글의 글번호.", required = true) int articleno)
            throws Exception {
        log.info("getArticle - 호출 : " + articleno);
        boardService.updateHit(articleno);
        return new ResponseEntity<BoardDto>(boardService.getArticle(articleno), HttpStatus.OK);
    }

    @ApiOperation(value = "수정 할 글 얻기", notes = "글번호에 해당하는 게시글의 정보를 반환한다.", response = BoardDto.class)
    @GetMapping("/modify/{articleno}")
    public ResponseEntity<BoardDto> getModifyArticle(
            @PathVariable("articleno") @ApiParam(value = "얻어올 글의 글번호.", required = true) int articleno)
            throws Exception {
        log.info("getModifyArticle - 호출 : " + articleno);
        return new ResponseEntity<BoardDto>(boardService.getArticle(articleno), HttpStatus.OK);
    }

    @ApiOperation(value = "게시판 글수정", notes = "수정할 게시글 정보를 입력한다. 그리고 DB수정 성공여부에 따라 'success' 또는 'fail' 문자열을 반환한다.", response = Map.class)
    @PutMapping
    public ResponseEntity<?> modifyArticle(
            @RequestBody @ApiParam(value = "수정할 글정보.", required = true) BoardDto boardDto, HttpServletRequest request) throws Exception {
        log.info("modifyArticle - 호출 {}", boardDto);
        Map<String, Object> resultMap = new HashMap<String, Object>();
        HttpStatus status = HttpStatus.ACCEPTED;
//        log.info("modifyArticle boardDto - {}", request.getHeader("Authorization"));
        if(jwtUtil.checkToken(request.getHeader("Authorization"))) {
            try {
                boardService.modifyArticle(boardDto);
                status = HttpStatus.CREATED;
                resultMap.put("isUpdate", true);
            }
            catch (Exception e) {
                log.error("글수정 실패 : {}", e);
                resultMap.put("message", e.getMessage());
                status = HttpStatus.INTERNAL_SERVER_ERROR;
            }
        }else {
            log.error("사용 불가능 토큰!!!");
            status = HttpStatus.UNAUTHORIZED;
        }
        return new ResponseEntity<Map<String, Object>>(resultMap, status);
    }

    @ApiOperation(value = "게시판 글삭제", notes = "글번호에 해당하는 게시글의 정보를 삭제한다. 그리고 DB삭제 성공여부에 따라 'success' 또는 'fail' 문자열을 반환한다.", response = Map.class)
    @DeleteMapping("/{articleno}")
    public ResponseEntity<?> deleteArticle(@PathVariable("articleno") @ApiParam(value = "살제할 글의 글번호.", required = true) int articleno, HttpServletRequest request) throws Exception {
        log.info("deleteArticle - 호출");
        if(jwtUtil.checkToken(request.getHeader("Authorization"))) {
            try{
                boardService.deleteArticle(articleno);
                return new ResponseEntity<Void>(HttpStatus.OK);
            }
            catch (Exception e) {
                return new ResponseEntity<Void>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
//            return ResponseEntity.ok().build();
        }else {
            log.error("사용 불가능 토큰!!!");
            return new ResponseEntity<Void>(HttpStatus.UNAUTHORIZED);
        }
    }

    private ResponseEntity<String> exceptionHandling(Exception e) {
        e.printStackTrace();
        return new ResponseEntity<String>("Error : " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
