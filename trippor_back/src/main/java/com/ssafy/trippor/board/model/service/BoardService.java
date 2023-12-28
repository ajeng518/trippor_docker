package com.ssafy.trippor.board.model.service;

import com.ssafy.trippor.board.model.BoardDto;
import com.ssafy.trippor.board.model.BoardListDto;

import java.util.Map;

public interface BoardService {
    void writeArticle(BoardDto boardDto) throws Exception;
    BoardListDto listArticle(Map<String, String> map) throws Exception;
    //	PageNavigation makePageNavigation(Map<String, String> map) throws Exception;
    BoardDto getArticle(int articleNo) throws Exception;
    void updateHit(int articleNo) throws Exception;

    void modifyArticle(BoardDto boardDto) throws Exception;
    //
    void deleteArticle(int articleNo) throws Exception;

}
