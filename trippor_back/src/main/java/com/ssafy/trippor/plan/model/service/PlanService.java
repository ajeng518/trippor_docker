package com.ssafy.trippor.plan.model.service;

import com.ssafy.trippor.plan.model.PlanDto;
import com.ssafy.trippor.plan.model.PlanResponseDto;

import java.util.List;

public interface PlanService {
	void insertPlan(PlanDto planDto) throws Exception;
	void insertAttraction(PlanDto planDto) throws Exception;
	int countPlan()throws Exception;

	List<PlanResponseDto> listPlan() throws Exception;
	PlanResponseDto getPlanById(int plan_no) throws Exception;
	void updateHit(int plan_no) throws Exception;
	void deletePlan(int planNo) throws Exception;
	void insertPlanList(List<PlanDto> list) throws Exception;
	List<PlanResponseDto>  getPlanByUserId(String user_id) throws Exception;
}
