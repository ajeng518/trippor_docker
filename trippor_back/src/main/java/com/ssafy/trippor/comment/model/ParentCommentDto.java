package com.ssafy.trippor.comment.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
@ApiModel(value = "ParentCommentDto : 댓글정보", description = "댓글의 상세 정보를 나타낸다.")
public class ParentCommentDto {
    @ApiModelProperty(value = "댓글 아이디")
    private int parentId;
    @ApiModelProperty(value = "게시글 번호")
    private int articleNo;
    @ApiModelProperty(value = "작성자 아이디")
    private String userId;
    @ApiModelProperty(value = "작성자 이름")
    private String userName;
    @ApiModelProperty(value = "댓글 내용")
    private String context;
    @ApiModelProperty(value = "작성일")
    private String registerTime;
    @ApiModelProperty(value = "수정일")
    private String updateTime;
    @ApiModelProperty(value = "삭제일")
    private String deleteTime;
    @ApiModelProperty(value = "대댓글")
    private List<ChildCommentDto> childComments;
}
