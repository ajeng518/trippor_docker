package com.ssafy.trippor.member.controller;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ssafy.trippor.member.model.MemberDto;
import com.ssafy.trippor.member.model.service.MemberService;
import com.ssafy.trippor.util.JWTUtil;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

@CrossOrigin(origins = {"*"}, methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE}, maxAge = 6000)
@RestController
@RequestMapping("/user")
@Slf4j
public class MemberController {

	private MemberService memberService;
	private JWTUtil jwtUtil;

	public MemberController(MemberService memberService, JWTUtil jwtUtil) {
		super();
		this.memberService = memberService;
		this.jwtUtil = jwtUtil;
	}

	@ApiOperation(value = "로그인", notes = "아이디와 비밀번호를 이용하여 로그인 처리.")
	@PostMapping("/login")
	public ResponseEntity<Map<String, Object>> login(
			@RequestBody @ApiParam(value = "로그인 시 필요한 회원정보(아이디, 비밀번호).", required = true) MemberDto memberDto) {
		log.debug("login user : {}", memberDto);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		HttpStatus status = HttpStatus.ACCEPTED;
		try {
			MemberDto loginUser = memberService.login(memberDto);
			if (loginUser != null) {

				if(loginUser.getDeletedTime() != null){
					log.debug("로그인 에러 발생 탈퇴한 회원!! ");
					resultMap.put("message", "탈퇴한 회원입니다.");
					status = HttpStatus.INTERNAL_SERVER_ERROR;
				}
				else {
					String accessToken = jwtUtil.createAccessToken(loginUser.getUserId());
					String refreshToken = jwtUtil.createRefreshToken(loginUser.getUserId());
					log.debug("access token : {}", accessToken);
					log.debug("refresh token : {}", refreshToken);

//				발급받은 refresh token을 DB에 저장.
					memberService.saveRefreshToken(loginUser.getUserId(), refreshToken);

//				JSON으로 token 전달.
					resultMap.put("access-token", accessToken);
					resultMap.put("refresh-token", refreshToken);

					status = HttpStatus.CREATED;
				}
			} else {
				resultMap.put("message", "아이디 또는 패스워드를 확인해주세요.");
				status = HttpStatus.UNAUTHORIZED;
			}
		} catch (Exception e) {
			log.debug("로그인 에러 발생 : {}", e);
			resultMap.put("message", "로그인 실패!" );
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return new ResponseEntity<Map<String, Object>>(resultMap, status);
	}

	@ApiOperation(value = "회원인증", notes = "회원 정보를 담은 Token을 반환한다.", response = Map.class)
	@GetMapping("/info/{userId}")
	public ResponseEntity<Map<String, Object>> getInfo(
			@PathVariable("userId") @ApiParam(value = "인증할 회원의 아이디.", required = true) String userId,
			HttpServletRequest request) {
//		logger.debug("userId : {} ", userId);
		Map<String, Object> resultMap = new HashMap<>();
		HttpStatus status = HttpStatus.ACCEPTED;
		if (jwtUtil.checkToken(request.getHeader("Authorization"))) {
			log.info("사용 가능한 토큰!!!");
			try {
//				로그인 사용자 정보.
				MemberDto memberDto = memberService.userInfo(userId);
				log.debug("회원정보 조회인데 여기서 img_path가 잘 안가요.." , memberDto.getImgPath());
				resultMap.put("userInfo", memberDto);
				status = HttpStatus.OK;
			} catch (Exception e) {
				log.error("정보조회 실패 : {}", e);
				resultMap.put("message", e.getMessage());
				status = HttpStatus.INTERNAL_SERVER_ERROR;
			}
		} else {
			log.error("사용 불가능 토큰!!!");
			status = HttpStatus.UNAUTHORIZED;
		}
		return new ResponseEntity<Map<String, Object>>(resultMap, status);
	}

	@ApiOperation(value = "로그아웃", notes = "회원 정보를 담은 Token을 제거한다.", response = Map.class)
	@GetMapping("/logout/{userId}")
	public ResponseEntity<?> removeToken(@PathVariable("userId") @ApiParam(value = "로그아웃할 회원의 아이디.", required = true) String userId) {
		Map<String, Object> resultMap = new HashMap<>();
		HttpStatus status = HttpStatus.ACCEPTED;
		try {
			memberService.delRefreshToken(userId);
			status = HttpStatus.OK;
		} catch (Exception e) {
			log.error("로그아웃 실패 : {}", e);
			resultMap.put("message", e.getMessage());
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return new ResponseEntity<Map<String, Object>>(resultMap, status);

	}

	@ApiOperation(value = "Access Token 재발급", notes = "만료된 access token을 재발급받는다.", response = Map.class)
	@PostMapping("/refresh")
	public ResponseEntity<?> refreshToken(@RequestBody @ApiParam(value = "토큰 재발급 시 필요한 회원정보(아이디, 비밀번호).", required = true) MemberDto memberDto, HttpServletRequest request)
			throws Exception {
		Map<String, Object> resultMap = new HashMap<>();
		HttpStatus status = HttpStatus.ACCEPTED;
		String token = request.getHeader("refreshToken");
		log.debug("token : {}, memberDto : {}", token, memberDto);
		if (jwtUtil.checkToken(token)) {
			if (token.equals(memberService.getRefreshToken(memberDto.getUserId()))) {
				String accessToken = jwtUtil.createAccessToken(memberDto.getUserId());
				log.debug("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
				log.debug("token : {}", accessToken);
				log.debug("정상적으로 액세스토큰 재발급!!!");
				resultMap.put("access-token", accessToken);
				status = HttpStatus.CREATED;
			}
		} else {
			log.debug("리프레쉬토큰도 사용불가!!!!!!!");
			status = HttpStatus.UNAUTHORIZED;
		}
		return new ResponseEntity<Map<String, Object>>(resultMap, status);
	}

	@ApiOperation(value = "회원등록", notes = "회원의 정보를 받아 처리.")
	@PostMapping("/join")
	public ResponseEntity<?> joinUser(@RequestBody @ApiParam(value = "회원 가입시 필요한 회원정보(아이디, 비밀번호).", required = true) MemberDto memberDto) {
		log.debug("joinUser user : {}", memberDto);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		HttpStatus status = HttpStatus.ACCEPTED;
		try {
			memberDto.setJoinDate(LocalDate.now().toString());
			memberService.joinUser(memberDto);
			status = HttpStatus.CREATED;
		} catch (Exception e) {
			log.debug("회원가입 에러 발생 : {}", e);
			resultMap.put("message", e.getMessage());
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return new ResponseEntity<Map<String, Object>>(resultMap, status);
	}

	@ApiOperation(value = "아이디 중복 확인", notes = "같은 아이디를 가진 유저가 몇명인지 반환", response = Map.class)
	@GetMapping("/{userId}")
	public ResponseEntity<?> idCheck(@PathVariable("userId") @ApiParam(value = "중복 확인 할 아이디.", required = true) String userId) {
		log.debug("idCheck userid : {}", userId);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		HttpStatus status = HttpStatus.ACCEPTED;
		try {
			int cnt = memberService.idCheck(userId);
			resultMap.put("userCount", cnt);
			status = HttpStatus.OK;
		} catch (Exception e) {
			log.error("중복 확인 실패 : {}", e);
			resultMap.put("message", e.getMessage());
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return new ResponseEntity<Map<String, Object>>(resultMap, status);
	}

	@ApiOperation(value = "비밀번호", notes = "같은 아이디를 가진 유저가 몇명인지 반환", response = Map.class)
	@PostMapping("/find/pwd")
	public ResponseEntity<?> findPwd(@RequestBody @ApiParam(value = "비밀번호를 찾을 회원의 정보", required = true) MemberDto memberDto) {
		log.debug("findPwd userid : {}", memberDto);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		HttpStatus status = HttpStatus.ACCEPTED;
		try {
			MemberDto user = memberService.findByEmail(memberDto);
			
//			if(cnt == 1) { // 아이디와 이메일이 일치하는 유저가 한 명 존재할 때 비밀번호를 리셋하여 전송

//				String pwd = memberService.createNewPassword(memberDto.getEmailId(), memberDto.getEmailDomain());

				resultMap.put("responseUser", user);
				status = HttpStatus.OK;

		} catch (Exception e) {
			log.error("비밀번호 찾기 실패 : {}", e);
			resultMap.put("message", e.getMessage());
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return new ResponseEntity<Map<String, Object>>(resultMap, status);
	}

	@ApiOperation(value = "회원정보수정", notes = "회원정보를 수정합니다.", response = Map.class)
	@PutMapping(value = "/update")
	public ResponseEntity<?> userModify(@RequestBody @ApiParam(value = "회원 정보 수정시 필요한 회원정보", required = true) MemberDto memberDto) {
		log.debug("userModify user : {}", memberDto);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		HttpStatus status = HttpStatus.ACCEPTED;
		try {
			memberService.updateUser(memberDto);
			MemberDto modifiedUser = memberService.userInfo(memberDto.getUserId());
			resultMap.put("userInfo", modifiedUser);
			status = HttpStatus.OK;
		} catch (Exception e) {
			log.error("회원 정보 수정 실패 : {}", e);
			resultMap.put("message", e.getMessage());
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return new ResponseEntity<Map<String, Object>>(resultMap, status);
	}

	@ApiOperation(value = "비밀번호 확인", notes = "비밀번호가 일치하는 지 확인", response = Map.class)
	@PostMapping("/check")
	public ResponseEntity<?> pwdCheck(@RequestBody @ApiParam(value = "비밀번호를 확인할 사용자의 아이디와 비밀번호", required = true) MemberDto memberDto) {
		log.debug("pwdCheck userid : {}", memberDto.getUserId());
		Map<String, Object> resultMap = new HashMap<String, Object>();
		HttpStatus status = HttpStatus.ACCEPTED;
		try {
			MemberDto loginUser = memberService.login(memberDto);
			if (loginUser != null)
				resultMap.put("isCorrectPwd", true); // 비밀번호가 일치한다.
			else
				resultMap.put("isCorrectPwd", false); // 비밀번호가 일치하지 않는다.
			status = HttpStatus.OK;
		} catch (Exception e) {
			log.error("비밀번호 일치 확인 실패 : {}", e);
			resultMap.put("message", e.getMessage());
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return new ResponseEntity<Map<String, Object>>(resultMap, status);
	}

	@ApiOperation(value = "회원정보삭제(탈퇴)", notes = "회원정보를 삭제합니다.", response = Map.class)
	@DeleteMapping(value = "/{userid}")
	public ResponseEntity<?> userDelete(@PathVariable("userid") String userId, HttpServletRequest request) {
		log.debug("userDelete userid : {}", userId);
		System.out.println(userId);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		HttpStatus status = HttpStatus.ACCEPTED;
		if (jwtUtil.checkToken(request.getHeader("Authorization"))) {
			try {
				memberService.deleteUser(userId);
				MemberDto memberDto = memberService.userInfo(userId);
				if (memberDto.getDeletedTime() != null) {
					resultMap.put("isDelete", true); // 회원삭제 됨.
				} else {
					resultMap.put("isDelete", false); // 회원삭제 되지 않음.
				}
				status = HttpStatus.OK;
			} catch (Exception e) {
				log.error("회원 정보 삭제 실패 : {}", e);
				resultMap.put("message", e.getMessage());
				status = HttpStatus.INTERNAL_SERVER_ERROR;
			}
		} else {
			log.error("사용 불가능 토큰!!!");
			status = HttpStatus.UNAUTHORIZED;
		}
		return new ResponseEntity<Map<String, Object>>(resultMap, status);
	}


}
