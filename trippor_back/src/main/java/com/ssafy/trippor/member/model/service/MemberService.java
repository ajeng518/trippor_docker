package com.ssafy.trippor.member.model.service;

import com.ssafy.trippor.member.model.MemberDto;

import java.sql.SQLException;

public interface MemberService {

	MemberDto login(MemberDto memberDto) throws Exception;
	MemberDto userInfo(String userId) throws Exception;
	void saveRefreshToken(String userId, String refreshToken) throws Exception;
	Object getRefreshToken(String userId) throws Exception;
	void delRefreshToken(String userId) throws Exception;
	void joinUser(MemberDto memberDto) throws Exception;
	int idCheck(String userId) throws Exception;
    void updateUser(MemberDto memberDto) throws Exception;
	void deleteUser(String userId) throws Exception;
	MemberDto resetPassword(String newPwd) throws SQLException;
	MemberDto findByEmail(MemberDto memberDto) throws SQLException;
}
