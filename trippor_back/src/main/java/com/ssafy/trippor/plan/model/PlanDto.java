package com.ssafy.trippor.plan.model;

import java.util.List;

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
@ApiModel(value = "PlanDto : 계획 정보", description = "계획의 상세 정보를 나타낸다.")
public class PlanDto {
	@ApiModelProperty(value = "계획번호")
	private int plan_no;
	@ApiModelProperty(value = "작성자 아이디")
	private String user_id;
	@ApiModelProperty(value = "좋아요")
	private int favorite;
	@ApiModelProperty(value = "작성일")
	private String register_time;
    @ApiModelProperty(value = "제목")
    private String subject;
//    @ApiModelProperty(value = "리스트")
//    private List<AttractionDto> attractions;
    //------------------------------------
    @ApiModelProperty(value = "관광지 아이디")
    private String contentid;
    @ApiModelProperty(value = "관광지 이름")
    private String title;
    @ApiModelProperty(value = "관광지 사진")
    private String image;
    @ApiModelProperty(value = "관광지 주소")
    private String addr;
    @ApiModelProperty(value = "관광지 위도")
    private double latitude;
    @ApiModelProperty(value = "관광지 경도")
    private double longitude;
    @ApiModelProperty(value = "메모")
    private  String memo;
    @ApiModelProperty(value = "계획 내 관광지 순서 번호")
    int order_num;
}
