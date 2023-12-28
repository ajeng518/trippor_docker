package com.ssafy.trippor.comment.controller;

import com.ssafy.trippor.comment.model.ChildCommentDto;
import com.ssafy.trippor.comment.model.ParentCommentDto;
import com.ssafy.trippor.comment.model.service.CommentService;
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
@RequestMapping("/comment")
@Api("댓글 컨트롤러  API V1")
@Slf4j
public class CommentController {

    private CommentService commentService;

//    private JWTUtil jwtUtil;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
//        this.jwtUtil = jwtUtil;
    }

    // 댓글 생성
    @ApiOperation(value = "댓글작성", notes = "새로운 댓글 정보를 입력한다.", response = Map.class)
    @PostMapping
    public ResponseEntity<?> writeParentComment(
            @RequestBody @ApiParam(value = "댓글 정보.", required = true) ParentCommentDto parentCommentDto, HttpServletRequest request) {
        log.info("writeComment commentDto - {}", parentCommentDto.getArticleNo());
        Map<String, Object> resultMap = new HashMap<String, Object>();
        HttpStatus status = HttpStatus.ACCEPTED;
//        if(jwtUtil.checkToken(request.getHeader("Authorization"))) {
            log.debug("댓글 작성가능해요");
            try {
                commentService.writeComment(parentCommentDto);
                List<ParentCommentDto> commentListDto = commentService.listComment(parentCommentDto.getArticleNo());
                status = HttpStatus.CREATED;
                resultMap.put("commentList", commentListDto);
            }
            catch (Exception e){
                log.error("댓글 등록 실패 : {}", e);
                resultMap.put("message", e.getMessage());
                status = HttpStatus.INTERNAL_SERVER_ERROR;
            }
//        } else {
//            log.error("사용 불가능 토큰!!!");
//            return new ResponseEntity<Void>(HttpStatus.UNAUTHORIZED);
//        }
        return new ResponseEntity<Map<String, Object>>(resultMap, status);
    }

    @ApiOperation(value = "댓글목록", notes = "모든 댓글의 정보를 반환한다.", response = List.class)
    @ApiResponses({ @ApiResponse(code = 200, message = "댓글목록 OK!!"), @ApiResponse(code = 404, message = "페이지없어!!"),
            @ApiResponse(code = 500, message = "서버에러!!") })
    @GetMapping("/{articleno}")
    public ResponseEntity<?> listComment(
            @PathVariable("articleno") @ApiParam(value = "댓글을 얻기위한 게시글 번호.", required = true) int articleno, HttpServletRequest request) {
        log.info("listComment map - {}", articleno);
        Map<String, Object> resultMap = new HashMap<String, Object>();
        HttpStatus status = HttpStatus.ACCEPTED;
//        if(jwtUtil.checkToken(request.getHeader("Authorization"))) {
            try {
                List<ParentCommentDto> commentListDto = commentService.listComment(articleno);
                HttpHeaders header = new HttpHeaders();
                header.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
                return ResponseEntity.ok().headers(header).body(commentListDto);
            }
            catch (Exception e){
                log.error("댓글 등록 실패 : {}", e);
                resultMap.put("message", e.getMessage());
                status = HttpStatus.INTERNAL_SERVER_ERROR;
            }
//        } else {
//            log.error("사용 불가능 토큰!!!");
//            return new ResponseEntity<Void>(HttpStatus.UNAUTHORIZED);
//        }
        return new ResponseEntity<Map<String, Object>>(resultMap, status);
    }

    @ApiOperation(value = "댓글목록", notes = "모든 댓글의 정보를 반환한다.", response = List.class)
    @ApiResponses({ @ApiResponse(code = 200, message = "댓글목록 OK!!"), @ApiResponse(code = 404, message = "페이지없어!!"),
            @ApiResponse(code = 500, message = "서버에러!!") })
    @GetMapping("/child/{parentId}")
    public ResponseEntity<?> listChildComment(
            @PathVariable("parentId") @ApiParam(value = "대댓글을 얻기위한 부모댓글 번호.", required = true) int articleno, HttpServletRequest request) {
        log.info("listComment map - {}", articleno);
        Map<String, Object> resultMap = new HashMap<String, Object>();
        HttpStatus status = HttpStatus.ACCEPTED;
//        if(jwtUtil.checkToken(request.getHeader("Authorization"))) {
        try {
            List<ParentCommentDto> commentListDto = commentService.listComment(articleno);
            HttpHeaders header = new HttpHeaders();
            header.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
            return ResponseEntity.ok().headers(header).body(commentListDto);
        }
        catch (Exception e){
            log.error("댓글 등록 실패 : {}", e);
            resultMap.put("message", e.getMessage());
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
//        } else {
//            log.error("사용 불가능 토큰!!!");
//            return new ResponseEntity<Void>(HttpStatus.UNAUTHORIZED);
//        }
        return new ResponseEntity<Map<String, Object>>(resultMap, status);
    }

    @ApiOperation(value = "댓글수정", notes = "댓글 정보를 수정한다.", response = Map.class)
    @PutMapping
    public ResponseEntity<?> modifyComment(
            @RequestBody @ApiParam(value = "댓글 정보.", required = true) ParentCommentDto parentCommentDto, HttpServletRequest request) {
        log.info("modifyComment commentDto - {}", parentCommentDto);
        Map<String, Object> resultMap = new HashMap<String, Object>();
        HttpStatus status = HttpStatus.ACCEPTED;
//        if(jwtUtil.checkToken(request.getHeader("Authorization"))) {
        log.debug("댓글 작성가능해요");
        try {
            commentService.modifyParent(parentCommentDto);
            List<ParentCommentDto> commentListDto = commentService.listComment(parentCommentDto.getArticleNo());
//            log.debug("리스트왜 안들고와... {}: ", commentListDto);
            status = HttpStatus.CREATED;
            resultMap.put("comment", commentListDto);
        }
        catch (Exception e){
            log.error("댓글 수정 실패 : {}", e);
            resultMap.put("message", e.getMessage());
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
//        } else {
//            log.error("사용 불가능 토큰!!!");
//            return new ResponseEntity<Void>(HttpStatus.UNAUTHORIZED);
//        }
        return new ResponseEntity<Map<String, Object>>(resultMap, status);
    }

    @ApiOperation(value = "댓글삭제", notes = "댓글 정보를 삭제한다.", response = Map.class)
    @DeleteMapping("/{parentId}")
    public ResponseEntity<?> deleteComment(
            @PathVariable("parentId") @ApiParam(value = "삭제할 댓글 id.", required = true) int parentId, HttpServletRequest request) {
        log.info("deleteComment commentDto - {}", parentId);
        Map<String, Object> resultMap = new HashMap<String, Object>();
        HttpStatus status = HttpStatus.ACCEPTED;
//        if(jwtUtil.checkToken(request.getHeader("Authorization"))) {
        log.debug("댓글 삭제가능해요 {}  ", parentId);
        try {
            commentService.deleteParent(parentId);
            ParentCommentDto parentCommentDto = commentService.getCommentById(parentId);
//            List<ParentCommentDto> commentListDto = commentService.listComment(parentCommentDto.getArticleNo());
            status = HttpStatus.OK;
            resultMap.put("parentComment", parentCommentDto);
//            resultMap.put("commentList", commentListDto);
        }
        catch (Exception e){
            log.error("댓글 삭제 실패 : {}", e);
            resultMap.put("message", e.getMessage());
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
//        } else {
//            log.error("사용 불가능 토큰!!!");
//            return new ResponseEntity<Void>(HttpStatus.UNAUTHORIZED);
//        }
        return new ResponseEntity<Map<String, Object>>(resultMap, status);
    }

    @ApiOperation(value = "대댓글작성", notes = "새로운 댓글 정보를 입력한다.", response = Map.class)
    @PostMapping("/child")
    public ResponseEntity<?> writeChildComment(
            @RequestBody @ApiParam(value = "댓글 정보.", required = true) ChildCommentDto childCommentDto, HttpServletRequest request) {
        log.info("writeComment commentDto - {}", request.getHeader("Authorization"));
        Map<String, Object> resultMap = new HashMap<String, Object>();
        HttpStatus status = HttpStatus.ACCEPTED;
//        if(jwtUtil.checkToken(request.getHeader("Authorization"))) {
            log.debug("댓글 작성가능해요");
            try {
                commentService.writeChildComment(childCommentDto);
                int parentId = childCommentDto.getParentId();
                ParentCommentDto parentCommentDto = commentService.getCommentById(parentId);
//                List<ChildCommentDto> commentListDto = commentService.childList(childCommentDto.getParentId());
                status = HttpStatus.CREATED;
                resultMap.put("childList", parentCommentDto);
            }
            catch (Exception e){
                log.error("댓글 등록 실패 : {}", e);
                resultMap.put("message", e.getMessage());
                status = HttpStatus.INTERNAL_SERVER_ERROR;
            }
//        } else {
//            log.error("사용 불가능 토큰!!!");
//            return new ResponseEntity<Void>(HttpStatus.UNAUTHORIZED);
//        }
        return new ResponseEntity<Map<String, Object>>(resultMap, status);
    }

    @ApiOperation(value = "대댓글수정", notes = "댓글 정보를 수정한다.", response = Map.class)
    @PutMapping("/child")
    public ResponseEntity<?> modifyChildComment(
            @RequestBody @ApiParam(value = "댓글 정보.", required = true) ChildCommentDto childCommentDto, HttpServletRequest request) {
        log.info("modifyComment commentDto - {}", childCommentDto);
        Map<String, Object> resultMap = new HashMap<String, Object>();
        HttpStatus status = HttpStatus.ACCEPTED;
//        if(jwtUtil.checkToken(request.getHeader("Authorization"))) {
        log.debug("댓글 작성가능해요");
        try {
            commentService.modifyChild(childCommentDto);
            List<ChildCommentDto> commentListDto = commentService.childList(childCommentDto.getParentId());
            status = HttpStatus.CREATED;
            resultMap.put("comment", commentListDto);
        }
        catch (Exception e){
            log.error("댓글 수정 실패 : {}", e);
            resultMap.put("message", e.getMessage());
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
//        } else {
//            log.error("사용 불가능 토큰!!!");
//            return new ResponseEntity<Void>(HttpStatus.UNAUTHORIZED);
//        }
        return new ResponseEntity<Map<String, Object>>(resultMap, status);
    }

    @ApiOperation(value = "대댓글삭제", notes = "댓글 정보를 삭제한다.", response = Map.class)
    @DeleteMapping("/child/{childId}")
    public ResponseEntity<?> deleteChildComment(
            @PathVariable("childId") @ApiParam(value = "삭제할 댓글 id", required = true) int childId, HttpServletRequest request) {
        log.info("deleteChildComment commentDto - {}", childId);
        Map<String, Object> resultMap = new HashMap<String, Object>();
        HttpStatus status = HttpStatus.ACCEPTED;
//        if(jwtUtil.checkToken(request.getHeader("Authorization"))) {
        log.debug("댓글 작성가능해요");
        try {
            ChildCommentDto childCommentDto = commentService.getChildById(childId);
            log.debug("삭제할 child: {} -- ", childCommentDto);
            int parentId = childCommentDto.getParentId();

            commentService.deleteChild(childId);
            ParentCommentDto parentCommentDto = commentService.getCommentById(parentId);

//            List<ParentCommentDto> commentListDto = commentService.listComment(parentCommentDto.getArticleNo());
//            List<ChildCommentDto> commentListDto = commentService.childList(childCommentDto.getParentId());
            status = HttpStatus.OK;
            resultMap.put("parentComment", parentCommentDto);
        }
        catch (Exception e){
            log.error("댓글 삭제 실패 : {}", e);
            resultMap.put("message", e.getMessage());
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
//        } else {
//            log.error("사용 불가능 토큰!!!");
//            return new ResponseEntity<Void>(HttpStatus.UNAUTHORIZED);
//        }
        return new ResponseEntity<Map<String, Object>>(resultMap, status);
    }
}
