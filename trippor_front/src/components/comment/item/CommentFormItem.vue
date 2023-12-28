<script setup>
import { ref, watch } from 'vue';
import { useMemberStore } from '@/stores/member';
import { storeToRefs } from 'pinia';
import { useRoute, useRouter } from 'vue-router';

const router = useRouter();
const route = useRoute();

const memberStore = useMemberStore();
const { userInfo } = storeToRefs(memberStore);

const props = defineProps({ articleno: String, type: String, parentId: Number })
const emit = defineEmits(['regist', 'registChild']);

console.log("부모 props", props.parentId);

const comment = ref({
  userId: userInfo.value.userId,
  context: "",
  articleNo: props.articleno,
})

const childCommet = ref({
  userId: comment.value.userId,
  parentId: props.parentId,
  context: "",
})

const contentErrMsg = ref("");

watch(
  () => comment.value.context,
  (value) => {
    let len = value.length;
    if (len == 0 || len > 500) {
      contentErrMsg.value = "내용을 확인해 주세요!!!";
    } else contentErrMsg.value = "";
  },
  { immediate: true }
);

function onSubmit() {
  // event.preventDefault();
  if (contentErrMsg.value) {
    alert(contentErrMsg.value);
  } else {
    console.log("댓글일까 답글일까?", props.type);
    props.type === "parent" ? registComment() : registChildComment();
  }
}


const registComment = () => {
  console.log("부모로 새로운 comment 보낸다", comment);
  emit('regist', comment);
  comment.value.context = "";

}

const registChildComment = () => {
  childCommet.value.context = comment.value.context;
  console.log("대댓글 작성", childCommet);
  emit('registChild', childCommet);
  comment.value.context = "";
}

</script>

<template>
  <form @submit.prevent="onSubmit">
    <div class="form-group">
      <fieldset>
        <div class="card">
          <fieldset disabled="" class="mr-4 ml-4">
              <label class="form-label" for="disabledInput"></label>
              <input v-model="comment.userId" class="form-control" id="disabledInput" type="text" placeholder="작성자 ID"
                disabled="">
          </fieldset>
          <fieldset class="mr-4 ml-4 mb-5">
            <label for="exampleTextarea" class="form-label mt-4"></label>
            <textarea class="form-control" id="exampleTextarea" rows="3" style="height: 70px;"
              v-model="comment.context"></textarea>
          </fieldset>
        </div>
        <div class="mt-2 mb-5">
          <button type="submit" class="btn btn-primary" v-if="props.type === 'parent'">댓글 등록
          </button>
          <button type="submit" class="btn btn-primary" v-if="props.type === 'child'">답글 등록
          </button>
        </div>
      </fieldset>
    </div>
  </form>
</template>

<style scoped></style>