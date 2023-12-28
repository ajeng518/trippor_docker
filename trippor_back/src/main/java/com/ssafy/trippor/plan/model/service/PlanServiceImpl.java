package com.ssafy.trippor.plan.model.service;

import com.ssafy.trippor.plan.model.PlanDto;
import com.ssafy.trippor.plan.model.PlanResponseDto;
import com.ssafy.trippor.plan.model.mapper.PlanMapper;

import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class PlanServiceImpl implements PlanService{
	private PlanMapper planMapper;

	@Autowired
	public PlanServiceImpl(PlanMapper planMapper) {
		super();
		this.planMapper = planMapper;
	}

	@Override
	public void insertPlanList(List<PlanDto> list) throws Exception {
		boolean first = true;
		int plan_no = 0;
		for(PlanDto plan : list){
			if (first) {
				//Plan 저장
				planMapper.insertPlan(plan);
				plan_no = plan.getPlan_no();

				//Attraction 저장
				plan.setPlan_no(plan_no);
				planMapper.insertAttraction(plan);
				first = false;
				continue;
			}
			log.debug("plan no를 반환하나요? {} ", plan_no );
			plan.setPlan_no(plan_no);
			planMapper.insertAttraction(plan);
		}
	}

	@Override
	public void insertPlan(PlanDto planDto) throws Exception {
		planMapper.insertPlan(planDto);
	}

	@Override
	public void insertAttraction(PlanDto planDto) throws Exception {
		planMapper.insertAttraction(planDto);
	}

	@Override
	public int countPlan() throws Exception {
		return planMapper.countPlan();
	}

	@Override

	public List<PlanResponseDto> listPlan() throws Exception {
		List<PlanResponseDto> list = planMapper.listPlan();
		for(PlanResponseDto plan : list){
			plan.setAttractions(planMapper.listAttraction(plan.getPlan_no()));
		}
		return list;
	}

	@Override
	public PlanResponseDto getPlanById(int plan_no) throws Exception {
		PlanResponseDto planDto =  planMapper.getPlanById(plan_no);
		planDto.setAttractions(planMapper.listAttraction(planDto.getPlan_no()));
		return planDto;
	}

	@Override
	public List<PlanResponseDto>  getPlanByUserId(String user_id) throws Exception {
		List<PlanResponseDto>  list = planMapper.getPlanByUserId(user_id);
		for (PlanResponseDto planDto : list) {
			planDto.setAttractions(planMapper.listAttraction(planDto.getPlan_no()));
		}
		return list;
	}

	@Override
	public void updateHit(int plan_no) throws Exception {
		planMapper.updateHit(plan_no);
	}

	@Override
	public void deletePlan(int plan_no) throws Exception {
		planMapper.deletePlan(plan_no);
	}

}
