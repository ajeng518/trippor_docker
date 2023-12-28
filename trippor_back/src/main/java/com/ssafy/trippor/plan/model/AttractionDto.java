package com.ssafy.trippor.plan.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
@ApiModel(value = "AttractionDto : 게시글정보", description = "게시글의 상세 정보를 나타낸다.")
public class AttractionDto {
	@ApiModelProperty(value = "계획번호")
	private int plan_no;
	@ApiModelProperty(value = "관광지 아이디")
	private String content_id;
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
    @ApiModelProperty(value = "계획 내 관광지 순서번호")
	int order_num;
	@ApiModelProperty(value = "관광지 코멘트")
	private String memo;
}
