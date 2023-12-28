package com.ssafy.trippor.comment.model.service;

import com.ssafy.trippor.comment.model.ChildCommentDto;
import com.ssafy.trippor.comment.model.ParentCommentDto;
import com.ssafy.trippor.comment.model.mapper.CommentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class CommentServiceImpl implements CommentService{

    private CommentMapper commentMapper;

    @Autowired
    public CommentServiceImpl(CommentMapper commentMapper) {
        this.commentMapper = commentMapper;
    }

    @Override
    public void writeComment(ParentCommentDto parentCommentDto) throws Exception {
        commentMapper.writeComment(parentCommentDto);
    }

    @Override
    public void deleteParent(int parentId) throws Exception {
//        parentCommentDto.setContext("삭제된 댓글입니다.");
        commentMapper.deleteParent(parentId);
    }

    @Override
    public void modifyParent(ParentCommentDto parentCommentDto) throws Exception {
        commentMapper.modifyParent(parentCommentDto);
    }

    @Override
    public List<ParentCommentDto> listComment(int articleNo) throws Exception {
        List<ParentCommentDto> list = commentMapper.listParentComment(articleNo);
        for(ParentCommentDto commentDto : list){
            commentDto.setChildComments(commentMapper.childList(commentDto.getParentId()));
        }
        return list;

//        Map<String, Object> param = new HashMap<String, Object>();
//        param.put("word", map.get("word") == null ? "" : map.get("word"));
//        int currentPage = Integer.parseInt(map.get("pgno") == null ? "1" : map.get("pgno"));
//        int sizePerPage = Integer.parseInt(map.get("spp") == null ? "20" : map.get("spp"));
//        int start = currentPage * sizePerPage - sizePerPage;
//        param.put("start", start);
//        param.put("listsize", sizePerPage);
//
//        String key = map.get("key");
//        param.put("key", key == null ? "" : key);
//        if ("user_id".equals(key))
//            param.put("key", key == null ? "" : "b.user_id");
//        List<BoardDto> list = commentMapper.listComment(param);
//
//        if ("user_id".equals(key))
//            param.put("key", key == null ? "" : "user_id");
//        int totalArticleCount = commentMapper.getTotalCommentCount(param);
//        int totalPageCount = (totalArticleCount - 1) / sizePerPage + 1;
//
//        CommentListDto commentListDto = new CommentListDto();
//        commentListDto.setArticles(list);
//        commentListDto.setCurrentPage(currentPage);
//        commentListDto.setTotalPageCount(totalPageCount);
//
//        return commentListDto;
    }

    @Override
    public ParentCommentDto getCommentById(int parentId) throws Exception {
//        commentDto.setChildComments(commentMapper.childList(commentDto.getParentId()));
        ParentCommentDto parentCommentDto = commentMapper.getCommentById(parentId);
        parentCommentDto.setChildComments(commentMapper.childList(parentCommentDto.getParentId()));
        return parentCommentDto;
    }


    @Override
    public void writeChildComment(ChildCommentDto childCommentDto)throws Exception {
        commentMapper.writeChildComment(childCommentDto);
    }

    @Override
    public List<ChildCommentDto> childList(int parentId) throws Exception {
        return commentMapper.childList(parentId);
    }

    @Override
    public void modifyChild(ChildCommentDto childCommentDto) throws Exception {
        commentMapper.modifyChild(childCommentDto);
    }

    @Override
    public void deleteChild(int childId) throws Exception {
        commentMapper.deleteChild(childId);
    }

    @Override
    public ChildCommentDto getChildById(int childId) throws Exception {
        return commentMapper.getChildById(childId);
    }

}
