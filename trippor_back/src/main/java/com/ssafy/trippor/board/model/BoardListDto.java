package com.ssafy.trippor.board.model;

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
@ApiModel(value = "BoardListDto : 게시글 목록 & 페이지 정보", description = "게시글 목록과 현재 페이지와 전체 페이지 정보를 나타낸다.")
public class BoardListDto {

    @ApiModelProperty(value = "글목록")
    private List<BoardDto> articles;
    @ApiModelProperty(value = "현재 페이지번호")
    private int currentPage;
    @ApiModelProperty(value = "전체 페이지 수")
    private int totalPageCount;

}

