<script setup>
import { ref } from 'vue';
import { useRouter } from "vue-router";
import { findPwd } from "@/api/user";
import { httpStatusCode } from "@/util/http-status";

const router = useRouter();

const newUser = ref({
  userId: "",
  emailId: "",
  emailDomain: "",
});

const isFind = ref(false);
const showPwd = ref({});

const findPwdByEmail = async () => {

  if (newUser.value.userId == "") {
    alert("아이디를 입력해주세요.");
  }
  else if (newUser.value.emailId == "" || newUser.value.emailDomain == "") {
    alert("이메일 주소를 입력해주세요.");
  }
  else if (newUser.value.userName == "") {
    alert("이름을 입력해주세요.");
  }
  else {
    console.log(newUser);
    await findPwd(
      newUser.value,
      (response) => {
        console.log(response);
        if (response.status == httpStatusCode.OK) {
          let { data } = response;
          console.log("비밀번호 찾기 완료", data);
          if (data.responseUser.deletedTime != null)
            alert("탈퇴한 회원입니다.");
          else {
            alert("비밀번호 찾기 완료!!");
            isFind.value = true; // 비밀번호 보이는 창 보여주기
            showPwd.value = data.responseUser;
            resetForm();
          }
        }
      },
      (error) => {
        console.error(error);
        alert("비밀번호 찾기 실패");
      }
    )
  }
};

const resetForm = () => { // 초기화
  newUser.value = {
    userId: "",
    emailId: "",
    emailDomain: "",
  };
}

const moveToLogin = () => {
  router.push({ name: 'user-login' });
}

</script>

<template>
  <div class="container">
    <div class="row justify-content-center">
      <div class="col-lg-10">
        <h2 class="my-3 py-3 shadow-sm bg-light text-center">
          <mark class="orange">비밀번호 찾기</mark>
        </h2>
      </div>
      <div class="col-lg-10 text-start">
        <form>
          <div class="mb-3">
            <label for="userid" class="form-label">아이디 : </label>
            <input type="text" class="form-control" placeholder="아이디..." v-model="newUser.userId" />
          </div>
          <div class="mb-3">
            <label for="emailid" class="form-label">이메일 : </label>
            <div class="input-group">
              <input type="text" class="form-control" placeholder="이메일아이디" v-model="newUser.emailId" />
              <span class="input-group-text">@</span>
              <select class="form-select" aria-label="이메일 도메인 선택" v-model="newUser.emailDomain">
                <option selected>선택</option>
                <option value="ssafy.com">싸피</option>
                <option value="google.com">구글</option>
                <option value="naver.com">네이버</option>
                <option value="kakao.com">카카오</option>
              </select>
            </div>
          </div>
          <div class="col-auto text-center">
            <button type="button" class="btn btn-outline-primary mb-3" @click="findPwdByEmail">비밀번호 찾기</button>
            <button type="button" class="btn btn-outline-success ms-1 mb-3" @click="moveToLogin">로그인</button>
          </div>
        </form>
        <div v-show="isFind">
          <!-- TODO: card로 바꾸기 -->
          <div class="card">
            <div class="card-body">
              <h4>{{ showPwd.userName }}의 비밀번호는 {{ showPwd.userPwd }} 입니다.</h4>
            </div>
          </div>
        </div>

      </div>
    </div>
  </div>
</template>

<style scoped></style>
