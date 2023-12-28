<script setup>
import { Card } from 'ant-design-vue';
import { message } from 'ant-design-vue';
import { UploadOutlined } from '@ant-design/icons-vue';
import { BaseTransitionPropsValidators, ref } from 'vue';
import { storeToRefs } from "pinia";
import { useRouter } from 'vue-router';
import { useMemberStore } from "@/stores/member";
import { useMenuStore } from "@/stores/menu";
import UserPwdFormItem from "@/components/users/item/UserPwdFormItem.vue";


const router = useRouter();

const memberStore = useMemberStore();
const { userInfo } = storeToRefs(memberStore);
const { updateUserName, updateUserPwd, removeUser, getUserInfo } = memberStore;
const menuStore = useMenuStore();
const { changeMenuState } = menuStore;
const show = ref(false);

console.log("여기는 마이페이지: ", userInfo);

const editUser = ref({
  userId: userInfo.value.userId,
  userName: userInfo.value.userName,
  userPwd: userInfo.value.userPwd,
  imgPath: userInfo.value.imgPath,
});

// console.log("외않데", editUser);

const modifyUserInfo = () => {
  console.log("수정할 회원 정보: ", editUser);
  updateUserName(editUser.value);
}

const saveNewPwd = (pwd) => {
  pwd.value.userId = userInfo.value.userId;
  // pwd.value.token = sessionStorage.getItem("accessToken");
  console.log("변경할 비밀번호들...", pwd.value);
  updateUserPwd(pwd.value);
}

const deleteUser = () => {
  console.log("탈퇴할꺼임!!!");
  if (confirm("정말 탈퇴하실건가요?? 꺼이꺼이..😞.. 흐그극😭.. 흐국뚜흐국뚜리😢")) {
    removeUser(userInfo.value.userId);
    changeMenuState();
    router.push("/"); //Main으로 이동
  }
  console.log("탈퇴 취소!!");
}

const test = ref("");

// const handleChange = () => {
//   console.log(test);
// }

</script>

<template>
  <div class="mb-3">
    <div class="card-body">
      <h4 class="card-title">프로필</h4>
      <v-row justify="center">
        <v-img src="https://raw.githubusercontent.com/Tarikul-Islam-Anik/Animated-Fluent-Emojis/master/Emojis/Smilies/Face%20with%20Peeking%20Eye.png" width="150" height="150" alt="noimage" class="mt-5 mb-4" />
      </v-row>
      <p class="card-text">{{ editUser.userId }} </p>
      <div class="form-container">
        <div class="form-group mt-4">
          <label for="readOnlyInput" class="col-form-label">
            <div>이메일:</div>
          </label>
          <div class="col-sm-12">
            <input class="form-control" id="readOnlyInput" type="email"
              :value="`${userInfo.emailId}@${userInfo.emailDomain}`" readonly>
          </div>
        </div>

        <div class="form-group mt-2">
          <label for="name" class="col-form-label">
            <div>닉네임:</div>
          </label>
          <div class="col-sm-12">
            <input class="form-control" id="name" type="text" placeholder="변경할 닉네임을 입력해주세요" v-model="editUser.userName">
          </div>
        </div>
      </div>
      <button type="button" class="btn btn-outline-info mt-4" @click="modifyUserInfo">수정</button>
    </div>
  </div>

  <div class="mb-3">
    <div class="card-body">
      <div class="accordion" id="accordionExample">
        <div class="accordion-item">
          <h2 class="accordion-header" id="headingOne">
            <button class="accordion-button collapsed" type="button" data-bs-toggle="collapse"
              data-bs-target="#collapseOne" aria-expanded="false" aria-controls="collapseOne" @click="show = !show">
              비밀번호 변경
            </button>
          </h2>
          <div id="collapseOne" class="accordion-collapse collapse" aria-labelledby="headingOne"
            data-bs-parent="#accordionExample" style="">
            <div class="accordion-body">
              <UserPwdFormItem v-if="show" @change-pwd="saveNewPwd"></UserPwdFormItem>
            </div>
          </div>
        </div>
      </div>
    </div>

    <div class="mt-3">
      <div class="card-body">
        <div class="accordion" id="accordionExample">
          <div class="accordion-item">
            <h2 class="accordion-header" id="headingTwo">
              <button class="accordion-button collapsed" type="button" data-bs-toggle="collapse"
                data-bs-target="#collapseTwo" aria-expanded="false" aria-controls="collapseTwo">
                탈퇴하기
              </button>
            </h2>
            <div id="collapseTwo" class="accordion-collapse collapse" aria-labelledby="headingTwo"
              data-bs-parent="#accordionExample">
              <div class="accordion-body">
                <button type="button" class="btn btn-outline-danger mt-2" @click="deleteUser">탈퇴</button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.v-enter-active,
.v-leave-active {
  transition: opacity 0.5s ease;
}

.v-enter-from,
.v-leave-to {
  opacity: 0;
}

.card-body{
  margin: 0 auto;
  width: 500px;
  border: 1px solid #ddd;
  padding: 20px;
}

.form-container{
  width: 200px;
  margin: 0 auto;
}
.form-control{
  text-align: center;
}

</style>
