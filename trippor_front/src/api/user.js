import { localAxios } from "@/util/http-commons";

const local = localAxios();

async function userConfirm(param, success, fail) {
  console.log("param", param);
  await local.post(`/user/login`, param).then(success).catch(fail);
  console.log("userConfirm ok");
}

async function findById(userid, success, fail) {
  local.defaults.headers["Authorization"] = sessionStorage.getItem("accessToken");
  await local.get(`/user/info/${userid}`).then(success).catch(fail);
}

async function tokenRegeneration(user, success, fail) {
  local.defaults.headers["refreshToken"] = sessionStorage.getItem("refreshToken"); //axios header에 refresh-token 셋팅
  console.log("token 재생성 시도" , user);
  await local.post(`/user/refresh`, user).then(success).catch(fail);
}

async function logout(userid, success, fail) {
  await local.get(`/user/logout/${userid}`).then(success).catch(fail);
}

async function idCheck(userid, success, fail){
  await local.get(`/user/${userid}`).then(success).catch(fail);
}

async function join(param, success, fail){
  console.log("회원가입 param: ", param);
  await local.post('/user/join', param).then(success).catch(fail);
}

async function updateName(user, success, fail){
  console.log("회원 이름 수정 param: ", user);
  await local.put('/user/update', user).then(success).catch(fail);
}

async function updatePwd(user, success, fail){
  console.log("회원 비밀번호 수정 param: ", user);
  local.defaults.headers["Authorization"] = sessionStorage.getItem("accessToken");
  await local.put('/user/update', user).then(success).catch(fail);
}

async function comparePwd(user, success, fail){
  await local.post('/user/check', user).then(success).catch(fail);
}

async function remove(userid, success, fail) {
  console.log("회원 탈퇴");
  local.defaults.headers["Authorization"] = sessionStorage.getItem("accessToken");
  await local.delete(`/user/${userid}`, userid).then(success).catch(fail);
  console.log("회원 탈퇴 ok");
}

async function findPwd(user, success, fail) {
  await local.post(`/user/find/pwd`, user ).then(success).catch(fail);
  
}

export { userConfirm, findById, tokenRegeneration, logout, idCheck, join, updateName, updatePwd, comparePwd, remove, findPwd };
