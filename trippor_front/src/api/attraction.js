import { attractionAxios, localAxios } from "@/util/http-commons";

const attraction = attractionAxios();
const local = localAxios();

//api값 가지고 오기(관광지 정보)
function listAttractions(param, success, fail) {
  attraction
    .get("/areaBasedList1", { params: param })
    .then(success)
    .catch(fail);
}

//api값 가지고 오기(시도, 구군구 코드)
function listSidoGugun(param, success, fail) {
  attraction.get("/areaCode1", { params: param }).then(success).catch(fail);
}

//서버로 여행계획 보내기
function insertTrip(param, success, fail) {
  local.defaults.headers["Authorization"] =
    sessionStorage.getItem("accessToken");
  local.post("/plan", param).then(success).catch(fail);
}

//서버에서 여행계획 가져오기
function getTripList(param, success, fail) {
  local.defaults.headers["Authorization"] =
    sessionStorage.getItem("accessToken");
  local.get("/plan", param).then(success).catch(fail);
}

//서버에서 여행계획 하나빼오기
function getTripplan(plan_no, success, fail) {
  local.defaults.headers["Authorization"] =
    sessionStorage.getItem("accessToken");
  local.get(`/plan/${plan_no}`).then(success).catch(fail);
}

//서버에서 내 여행계획 가져오기
function getMyplanList(user_id, success, fail) {
  local.defaults.headers["Authorization"] =
    sessionStorage.getItem("accessToken");
  local.get(`/plan/my/${user_id}`).then(success).catch(fail);
}

function deleteTrip(plan_no, success, fail) {
  local.defaults.headers["Authorization"] =
    sessionStorage.getItem("accessToken");
  local.delete(`/plan/${plan_no}`).then(success).catch(fail);
}

export {
  listAttractions,
  listSidoGugun,
  insertTrip,
  getTripList,
  getTripplan,
  deleteTrip,
  getMyplanList,
};
