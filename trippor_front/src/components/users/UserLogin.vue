<script setup>
import { ref } from "vue";
import { storeToRefs } from "pinia";
import { useRouter } from "vue-router";
import { useMemberStore } from "@/stores/member";
import { useMenuStore } from "@/stores/menu";

const router = useRouter();

const memberStore = useMemberStore();

const { isLogin } = storeToRefs(memberStore);
const { userLogin, getUserInfo } = memberStore;
const { changeMenuState } = useMenuStore();

const loginUser = ref({
  userId: "",
  userPwd: "",
});

const login = async () => {
  console.log("login ing!!!! !!!");
  await userLogin(loginUser.value);
  let token = sessionStorage.getItem("accessToken");
  console.log("111. ", token);
  console.log("isLogin: ", isLogin.value);
  if (isLogin.value) {
    console.log("로그인 성공아닌가???");
    getUserInfo(token);
    changeMenuState();
    router.push("/");
  }
  else {
    console.log("로그인 실패...");
    // alert("아이디와 패스워드를 확인해주세요.");

    loginUser.value = {userId: "", userPwd: "",}; // form 초기화
  }
};

const moveToJoin = () => {
  router.push({ name: 'user-join' });
}

const moveToFindPwd = () => {
  router.push({ name: 'user-find-pwd' });
}

</script>

<template>
  <div class="container">
    <div class="row justify-content-center">
      <div class="col-lg-10">
        <h2 class="my-3 py-3 shadow-sm bg-light text-center">
          <mark class="orange">로그인</mark>
        </h2>
      </div>
      <div class="col-lg-10">
        <form>
          <div class="form-check mb-3 float-end">
            <input class="form-check-input" type="checkbox" />
            <label class="form-check-label" for="saveid"> 아이디저장 </label>
          </div>
          <div class="mb-3 text-start">
            <label for="userid" class="form-label">아이디 : </label>
            <input
              type="text"
              class="form-control"
              v-model="loginUser.userId"
              placeholder="아이디..."
            />
          </div>
          <div class="mb-3 text-start">
            <label for="userpwd" class="form-label">비밀번호 : </label>
            <input
              type="password"
              class="form-control"
              v-model="loginUser.userPwd"
              @keyup.enter="login"
              placeholder="비밀번호..."
            />
          </div>
          <div class="col-auto text-center">
            <button type="button" class="btn btn-outline-primary mb-3" @click="login">
              로그인
            </button>
            <button type="button" class="btn btn-outline-success ms-1 mb-3" @click="moveToJoin">회원가입</button>
            <button type="button" class="btn btn-outline-danger ms-1 mb-3" @click="moveToFindPwd">비밀번호 찾기</button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<style scoped></style>
