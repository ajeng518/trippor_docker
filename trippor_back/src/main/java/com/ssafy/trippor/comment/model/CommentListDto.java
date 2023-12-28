package com.ssafy.trippor.comment.model;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
@ApiModel(value = "CommentListDto : 댓글 목록 & 페이지 정보", description = "댓글 목록과 현재 페이지와 전체 페이지 정보를 나타낸다.")
public class CommentListDto {

}
