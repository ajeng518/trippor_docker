import { localAxios } from "@/util/http-commons";

const local = localAxios();

function listComment(articleNo, success, fail) {
    console.log(articleNo);
  local.defaults.headers["Authorization"] = sessionStorage.getItem("accessToken");
  local.get(`/comment/${articleNo}`, articleNo).then(success).catch(fail);
}

function write(comment, success, fail) {
    console.log("댓글을 등록합니다", comment);
    local.defaults.headers["Authorization"] = sessionStorage.getItem("accessToken");
    local.post(`/comment`, comment).then(success).catch(fail);
}

function updateParent(comment, success, fail) {
    local.put(`/comment`, comment).then(success).catch(fail);
}

function deleteParent(parentId, success, fail) {
    console.log("삭제 서버로 보내용!", parentId);
    local.delete(`/comment/${parentId}`, parentId).then(success).catch(fail);
}

function writeChild(comment, success, fail) {
    console.log("대댓글 등록합니다.", comment);
    local.post(`/comment/child`, comment).then(success).catch(fail);
}

function updateChild(comment, success, fail) {
    local.put(`/comment/child`, comment).then(success).catch(fail);
}

function deleteChild(childId, success, fail) {
    console.log("왜 missing?", childId);
    local.delete(`/comment/child/${childId}`, childId).then(success).catch(fail);
}

export {
    listComment, write, updateParent, deleteParent, writeChild, updateChild, deleteChild
};