package com.ssafy.trippor.plan.model;

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
@ApiModel(value = "PlanRequestDto : 계획 정보", description = "계획의 상세 정보를 나타낸다.")
public class PlanResponseDto {
    @ApiModelProperty(value = "계획번호")
    private int plan_no;
    @ApiModelProperty(value = "작성자 아이디")
    private String user_id;
    @ApiModelProperty(value = "좋아요")
    private int favorite;
    @ApiModelProperty(value = "조회수")
    private int hit;
    @ApiModelProperty(value = "작성일")
    private String register_time;
    @ApiModelProperty(value = "제목")
    private String subject;
    @ApiModelProperty(value = "제목")
    private List<AttractionDto> attractions;
}
