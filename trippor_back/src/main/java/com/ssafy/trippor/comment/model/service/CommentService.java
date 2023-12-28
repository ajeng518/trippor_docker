package com.ssafy.trippor.comment.model.service;

import com.ssafy.trippor.comment.model.ChildCommentDto;
import com.ssafy.trippor.comment.model.ParentCommentDto;

import java.sql.SQLException;
import java.util.List;

public interface CommentService {
    /////////Parent
    void writeComment(ParentCommentDto commentDto) throws Exception;
    void deleteParent(int parentId) throws Exception;
    void modifyParent(ParentCommentDto parentCommentDto) throws Exception;
    List<ParentCommentDto> listComment(int articleNo) throws Exception;
    ParentCommentDto getCommentById(int parentId) throws Exception;
    /////////Child
    List<ChildCommentDto> childList(int parentId) throws Exception;
    void writeChildComment(ChildCommentDto childCommentDto) throws Exception;
    void modifyChild(ChildCommentDto childCommentDto) throws Exception;
    void deleteChild(int childId) throws Exception;
    ChildCommentDto getChildById(int childId) throws Exception;
}
