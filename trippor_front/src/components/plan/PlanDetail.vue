<script setup>
import { ref, onMounted } from "vue"
import { useRoute, useRouter } from "vue-router";
// import { detailArticle, deleteArticle } from "@/api/board";
import { getTripplan, deleteTrip } from "@/api/attraction.js";
import { useMemberStore } from "@/stores/member";
import { storeToRefs } from "pinia";
import { httpStatusCode } from "@/util/http-status";
import VKakaoMap2 from "../common/VKakaoMap2.vue";

import selectPlanAttractionDeatil from "@/components/plan/selectPlanAttractionDeatil.vue";

const route = useRoute();
const router = useRouter();
const memberStore = useMemberStore();

// const articleno = ref(route.params.articleno);
const { plan_no } = route.params;
const { getUserInfo } = memberStore;
const { userInfo } = storeToRefs(memberStore);

const article = ref({});
const isPossibleUser = ref(false);
const clicknow = ref({});

onMounted(() => {
  getArticle();
  // checkValiableUser();
});

const getArticle = () => {
  // const { articleno } = route.params;
  console.log(plan_no + "번글 얻으러 가자!!!");
  getTripplan(
    plan_no,
    ({ data }) => {
      article.value = data;
      console.log("수정, 삭제할 권한이 있는 사용자가 맞는지 확인?", article.value.user_id);

      let token = sessionStorage.getItem("accessToken");
      getUserInfo(token);

      if (userInfo.value.userId == 'admin' || userInfo.value.userId == article.value.user_id) {
        console.log("관리자 또는 작성자네용 ㅎㅎ");
        isPossibleUser.value = true;
      }
    },
    (error) => {
      console.error(error);
    }
  );
};


const moveList = () => {
  router.push({ name: "plan-list" });
}

//수정 보류
// function moveModify() {
//   router.push({ name: "article-modify", params: { articleno } });
// }

const onDeleteArticle = () => {
  // const { articleno } = route.params;
  console.log(plan_no + "번글 삭제하러 가자!!!");

  if (confirm("정말 삭제하시나요?")) {
    deleteTrip(
      plan_no,
      (response) => {
        if (response.status == httpStatusCode.OK) moveList();
      },
      (error) => {
        console.error(error);
      }
    );
  }
  else {
    console.log("삭제 취소");
  }
}

const selectattr = (val) => {
  clicknow.value = val;
}




</script>

<template>
  <div class="container">
    <div class="row justify-content-center">
      <div class="col-lg-10 text-start">
        <div class="row my-2">
          <h2 class="text-warning">{{ article.plan_no }}. {{ article.subject }}</h2>
        </div>
        <div class="row">
          <div class="col-md-8">
            <div class="clearfix align-content-center">
              <v-avatar>
                <v-img
                  src="https://raw.githubusercontent.com/Tarikul-Islam-Anik/Animated-Fluent-Emojis/master/Emojis/Activities/Teddy%20Bear.png"
                  alt="no-image-borad"></v-img>
              </v-avatar>
              <p>
                <span class="fw-bold">{{ article.user_id }}</span> <br />
                <span class="text-warning fw-light">
                  {{ article.register_time }}
                </span>
              </p>
            </div>
          </div>
          <div class="col-md-4 align-self-center text-end"><label for="commentSize" class="mr-1">조회수 :</label>
            <span class="badge rounded-pill bg-warning"> {{ article.hit }}</span>
          </div>
          <div class="divider mb-3"></div>
          <div class="text-secondary">
            <div class="row">
              <div class="col-8">
                <VKakaoMap2 :attraction="article.attractions" :selectAttraction="clicknow" />
              </div>
              <div class="col-4">
                <selectPlanAttractionDeatil :Attractions="article.attractions" @onAttrSelect="selectattr" />
              </div>
            </div>
          </div>
          <div class="divider mt-3 mb-3"></div>
          <div class="d-flex justify-content-end">
            <button type="button" class="btn btn-outline-primary mb-3" @click="moveList">
              글목록
            </button>
            <!-- <button v-show="isPossibleUser" type="button" class="btn btn-outline-success mb-3 ms-1" @click="moveModify">
                                                              글수정
                                                            </button> -->
            <button v-show="isPossibleUser" type="button" class="btn btn-outline-danger mb-3 ms-1"
              @click="onDeleteArticle">
              글삭제
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped></style>