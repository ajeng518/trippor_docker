package com.ssafy.trippor.plan.controller;

import com.ssafy.trippor.plan.model.PlanDto;
import com.ssafy.trippor.plan.model.PlanResponseDto;
import com.ssafy.trippor.plan.model.service.PlanService;
import com.ssafy.trippor.util.JWTUtil;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.trippor.plan.model.service.PlanService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/plan")
@Api("여행계획 컨트롤러 API V1")
@Slf4j
public class PlanController {
	private PlanService planService;
	private JWTUtil jwtUtil;
	public PlanController(PlanService planService, JWTUtil jwtUtil) {
		super();
		this.planService = planService;
		this.jwtUtil = jwtUtil;
	}
	
	@ApiOperation(value = "여행계획 등록", notes = "새로운 여행 정보를 입력한다", response = Map.class)
	@PostMapping
	public ResponseEntity<?>insertPlan(@RequestBody @ApiParam(value="여행계획 정보", required = true) List<PlanDto> list, HttpServletRequest request) {
		log.info("writeArticle planDto - {}", request.getHeader("Authorization"));
//		if(jwtUtil.checkToken(request.getHeader("Authorization"))){
		log.info("계획 저장할끄얌...!");
		try {
			planService.insertPlanList(list);

			return new ResponseEntity<Void>(HttpStatus.CREATED);
		} catch (Exception e) {
			return exceptionHandling(e);
			//넌 왜 안되닝........!!!!!!!!!!ㅜ^ㅜ
		}
	}
//		}else{
//			log.error("사용 불가 토큰이란말이용");
//			return new ResponseEntity<Void>(HttpStatus.UNAUTHORIZED);
//		}
//	}

	@ApiOperation(value = "여행 계획 글목록", notes = "모든 여행계획 정보를 반환한다.", response = List.class)
	@ApiResponses({ @ApiResponse(code = 200, message = "회원목록 OK!!"), @ApiResponse(code = 404, message = "페이지없어!!"),
			@ApiResponse(code = 500, message = "서버에러!!") })
	@GetMapping
	public ResponseEntity<?> listPlan() {
		log.info("listArticle map - {}");
		try {
			List<PlanResponseDto>  planList = planService.listPlan();
			HttpHeaders header = new HttpHeaders();
			header.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
			return ResponseEntity.ok().headers(header).body(planList);
		} catch (Exception e) {
			return exceptionHandling(e);
		}
	}

	@ApiOperation(value = "여행 계획 글 상세보기", notes = "여행계획 정보를 반환한다.", response = PlanResponseDto.class)
	@ApiResponses({ @ApiResponse(code = 200, message = "회원목록 OK!!"), @ApiResponse(code = 404, message = "페이지없어!!"),
			@ApiResponse(code = 500, message = "서버에러!!") })
	@GetMapping("/{plan_no}")
	public ResponseEntity<?> getPlanById(@PathVariable("plan_no") @ApiParam(value = "얻어올 여행계획의 글번호.", required = true) int plan_no) {
		log.info("listArticle map - {}");
		try {
			planService.updateHit(plan_no);
			PlanResponseDto  plan = planService.getPlanById(plan_no);
			HttpHeaders header = new HttpHeaders();
			header.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
			return ResponseEntity.ok().headers(header).body(plan);
		} catch (Exception e) {
			return exceptionHandling(e);
		}
	}

	@ApiOperation(value = "나의 여행 계획 글목록", notes = "내가 저장한 모든 여행계획 정보를 반환한다.", response = List.class)
	@ApiResponses({ @ApiResponse(code = 200, message = "회원목록 OK!!"), @ApiResponse(code = 404, message = "페이지없어!!"),
			@ApiResponse(code = 500, message = "서버에러!!") })
	@GetMapping("/my/{user_id}")
	public ResponseEntity<?> listPlanByUserId(@PathVariable("user_id") String user_id) {
		log.info("listPlan map - {}", user_id);
		try {
			List<PlanResponseDto>  planList = planService.getPlanByUserId(user_id);
			HttpHeaders header = new HttpHeaders();
			header.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
			return ResponseEntity.ok().headers(header).body(planList);
		} catch (Exception e) {
			return exceptionHandling(e);
		}
	}

	@ApiOperation(value = "여행 계획 글 삭제", notes = "여행계획 정보를 반환한다.", response = PlanResponseDto.class)
	@ApiResponses({ @ApiResponse(code = 200, message = "회원목록 OK!!"), @ApiResponse(code = 404, message = "페이지없어!!"),
			@ApiResponse(code = 500, message = "서버에러!!") })
	@DeleteMapping("/{plan_no}")
	public ResponseEntity<?> removePlan(
			@PathVariable("plan_no") @ApiParam(value = "얻어올 여행계획의 글번호.", required = true) int plan_no) {
		log.info("deletePlan 호출 - {}", plan_no);
		try {
			planService.deletePlan(plan_no);
			return new ResponseEntity<Void>(HttpStatus.OK);

		} catch (Exception e) {
			return new ResponseEntity<Void>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	private ResponseEntity<String> exceptionHandling(Exception e) {
		e.printStackTrace();
		return new ResponseEntity<String>("Error : " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
