package com.ssafy.trippor.plan.model.mapper;

import com.ssafy.trippor.plan.model.AttractionDto;
import com.ssafy.trippor.plan.model.PlanDto;
import com.ssafy.trippor.plan.model.PlanResponseDto;
import org.apache.ibatis.annotations.Mapper;
import java.sql.SQLException;
import java.util.List;

@Mapper
public interface PlanMapper {
	void insertPlan(PlanDto planDto)throws SQLException;
	void insertAttraction(PlanDto planDto)throws SQLException;
	int countPlan() throws SQLException;
	List<PlanResponseDto> listPlan() throws SQLException;
	PlanResponseDto getPlanById(int plan_no) throws SQLException;
	List<AttractionDto> listAttraction(int plan_no) throws SQLException;
    void updateHit(int plan_no) throws SQLException;
	void deletePlan(int plan_no) throws SQLException;

	List<PlanResponseDto> getPlanByUserId(String userId) throws SQLException;
}
