package com.ssafy.trippor.comment.model.mapper;

import com.ssafy.trippor.comment.model.ChildCommentDto;
import com.ssafy.trippor.comment.model.ParentCommentDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.jdbc.SQL;

import java.sql.SQLException;
import java.util.List;
@Mapper
public interface CommentMapper {
    void writeComment(ParentCommentDto parentCommentDto) throws SQLException;
    List<ParentCommentDto> listParentComment(int articleNo) throws SQLException;;
    List<ChildCommentDto> childList(int parentId) throws SQLException;;
    void writeChildComment(ChildCommentDto childCommentDto) throws SQLException;;
    ParentCommentDto getCommentById(int parentId) throws SQLException;;

    void modifyChild(ChildCommentDto childCommentDto) throws SQLException;
    void deleteChild(int childId) throws SQLException;

    void deleteParent(int parentId) throws SQLException;
    void modifyParent(ParentCommentDto parentCommentDto) throws SQLException;
    ChildCommentDto getChildById(int childId) throws SQLException;
}
