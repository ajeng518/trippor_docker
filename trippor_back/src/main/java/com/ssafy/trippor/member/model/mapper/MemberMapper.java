package com.ssafy.trippor.member.model.mapper;

import java.sql.SQLException;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.trippor.member.model.MemberDto;

@Mapper
public interface MemberMapper {

	MemberDto login(MemberDto memberDto) throws SQLException;
	MemberDto userInfo(String userId) throws SQLException;
	void saveRefreshToken(Map<String, String> map) throws SQLException;
	Object getRefreshToken(String userid) throws SQLException;
	void deleteRefreshToken(Map<String, String> map) throws SQLException;
	void joinUser(MemberDto memberDto) throws SQLException;
    int idCheck(String userId) throws SQLException;
	void updateUser(MemberDto memberDto) throws SQLException;
	void deleteUser(String userId) throws SQLException;
	MemberDto resetPassword(String newPwd) throws SQLException;
	MemberDto findByEmail(MemberDto memberDto) throws SQLException;
}
