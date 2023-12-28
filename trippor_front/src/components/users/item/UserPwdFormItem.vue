<script setup>
import { ref } from "vue";

const emit = defineEmits(["changePwd"]);

const saveNewPwd = () => {
  console.log("비밀번호를 변경하자");
  emit("changePwd", pwd);
  pwd.value.currentPwd = ""; // 변경 후 초기화?
};

const pwd = ref({
  userId: "",
  currentPwd: "",
  newPwd: "",
});

const newPwdConfirm = ref("")

const isSame = ref(false);

const confirmPwd = () => {
  // 새 비밀번호와 새 비밀번호가 같은지 확인한다.
  if (pwd.value.newPwd === newPwdConfirm.value) isSame.value = true;
  else isSame.value = false;
  console.log("비밀번호 확인", isSame.value);
};
</script>

<template>
  <div data-bs-theme="light" id="edit-password">
    <form>
      <div class="form-group">
        <input class="form-control" type="password" placeholder="현재 비밀번호" v-model="pwd.currentPwd" />
      </div>
      <div class="form-group">
        <input class="form-control" type="password" placeholder="새 비밀번호" v-model="pwd.newPwd" />
      </div>
      <!-- <div>
        <input type="password" placeholder="새 비밀번호 확인" v-model="newPwdConfirm" @input="confirmPwd" />
      </div> -->
      <div class="form-group has-danger" v-if="!isSame && newPwdConfirm != ''">
        <input type="password" 
        v-model="newPwdConfirm" 
        @input="confirmPwd" class="form-control is-invalid" 
        id="inputInvalid"
        placeholder="새 비밀번호 확인"
        >
        <div class="invalid-feedback"
        >비밀번호가 일치 하지 않습니다.</div>
      </div>
      <div class="form-group has-success" v-else-if="isSame && newPwdConfirm != ''">
        <input class="form-control is-valid" type="password" placeholder="새 비밀번호 확인"  v-model="newPwdConfirm" />
        <div class="valid-feedback">변경가능합니다. 😝</div>
      </div>
      <div class="form-group" v-else>
        <input class="form-control" type="password" placeholder="새 비밀번호 확인"  v-model="newPwdConfirm" />
      </div>

      <button v-if="isSame && pwd.currentPwd != ''" class="btn btn-outline-secondary" type="button" @click="saveNewPwd">
        저장하기
      </button>
    </form>
  </div>
</template>

<style scoped></style>
