<script setup>
import {ref} from 'vue';
import { storeToRefs } from "pinia";
import { useMemberStore } from "@/stores/member";

const memberStore = useMemberStore();
const { isDuplicate } = storeToRefs(memberStore); // 아이디 중복 여부(중복이 없으면 false, 중복이 존재하면 true)
const { idConfirm, userJoin } = memberStore;

const newUser = ref({
  userId: "",
  userPwd: "",
  userName: "",
  emailId: "",
  emailDomain: "",
});

// const userId = ref("");

const confirmPwd = ref("");
const isPossiblePwd = ref(false); // 비밀번호가 동일한지 확인용

const idCheck = async() => { // 아이디 중복 확인 호출
  console.log("중복 확인 할 겁니다..." + newUser.value.userId);
  await idConfirm(newUser.value.userId);

    if(isDuplicate.vale){ 
    console.log("같아유!!!");
    }
};

const pwdCheck = async() => {
  if(newUser.value.userPwd == confirmPwd.value) {
    isPossiblePwd.value = true;
  }
  else {
    isPossiblePwd.value = false;
  }

  console.log("비밀번호 확인: ", isPossiblePwd.value);
};

const join = async() => {

  if( newUser.value.userId == "" ) {
    alert("아이디를 입력해주세요.");
  }
  else if( newUser.value.userPwd == "" ) {
    alert("비밀번호를 입력해주세요.");
  }
  else if( newUser.value.emailId == "" || newUser.value.emailDomain == "") {
    alert("이메일 주소를 입력해주세요.");
  }
  else if( newUser.value.userName == "" ) {
    alert("이름을 입력해주세요.");
  }
  else if( isDuplicate.vale || !isPossiblePwd.value) { // 중복이거나 비밀번호가 일치하지 않으면?
    console.log(isDuplicate.value , isPossiblePwd.value);
    alert("아이디와 비밀번호를 확인하세요");
  }
  else if(!isDuplicate.value && isPossiblePwd.value){
    await userJoin(newUser.value);

  }
};

const resetForm = () =>{ // 초기화
  newUser.value = {
  userId: "",
  userPwd: "",
  userName: "",
  emailId: "",
  emailDomain: "",
};

confirmPwd.value = "";

isPossiblePwd.value = false; // 비밀번호가 동일한지 확인용

}

</script>

<template>
  <div class="container">
    <div class="row justify-content-center">
      <div class="col-lg-10">
        <h2 class="my-3 py-3 shadow-sm bg-light text-center">
          <mark class="orange">회원가입</mark>
        </h2>
      </div>
      <div class="col-lg-10 text-start">
        <form>
          <div class="mb-3">
            <label for="username" class="form-label">이름 : </label>
            <input type="text" class="form-control" placeholder="이름..." 
            v-model="newUser.userName"/>
          </div>
          <div class="mb-3">
            <label v-if="!isDuplicate" for="userid" class="form-label">아이디 : </label>
            <label v-else-if="isDuplicate" for="userid" class="form-label">아이디 : 사용할 수 없습니다.</label>
            <input type="text" class="form-control" placeholder="아이디..." 
            v-model="newUser.userId" @input="idCheck"/>
          </div>
          <div class="mb-3">
            <label for="userpwd" class="form-label">비밀번호 : </label>
            <input type="password" class="form-control" placeholder="비밀번호..."
            v-model="newUser.userPwd" />
          </div>
          <div class="mb-3">
            <label for="pwdcheck" class="form-label">비밀번호확인 : </label>
            <input type="password" class="form-control" id="pwdcheck" placeholder="비밀번호확인..." 
            v-model="confirmPwd" @input="pwdCheck"/>
          </div>
          <div class="mb-3">
            <label for="emailid" class="form-label">이메일 : </label>
            <div class="input-group">
              <input type="text" class="form-control" placeholder="이메일아이디" 
              v-model="newUser.emailId"/>
              <span class="input-group-text">@</span>
              <select class="form-select" aria-label="이메일 도메인 선택"
              v-model="newUser.emailDomain">
                <option selected>선택</option>
                <option value="ssafy.com">싸피</option>
                <option value="google.com">구글</option>
                <option value="naver.com">네이버</option>
                <option value="kakao.com">카카오</option>
              </select>
            </div>
          </div>
          <div class="col-auto text-center">
            <button type="button" class="btn btn-outline-primary mb-3" @click="join">회원가입</button>
            <button type="button" class="btn btn-outline-success ms-1 mb-3" @click="resetForm">초기화</button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<style scoped></style>
