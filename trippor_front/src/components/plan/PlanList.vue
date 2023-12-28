<script setup>
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { getTripList } from "@/api/attraction.js";
import TheHeadingPage from "../layout/TheHeadingPage.vue";

import VSelect from "@/components/common/VSelect.vue";
import PlanListItem from "@/components/plan/item/PlanListItem.vue";
import VPageNavigation from "@/components/common/VPageNavigation.vue";

const router = useRouter();
const selectFlag = ref("board");
const selectOption = ref([
  { name: "검색조건", code: "" },
  { name: "글번호", code: "plan_no" },
  { name: "제목", code: "subject" },
  { name: "작성자아이디", code: "user_id" },
]);

const articles = ref([]);
const currentPage = ref(1);
const totalPage = ref(0);
const { VITE_ARTICLE_LIST_SIZE } = import.meta.env;
const param = ref({
  pgno: currentPage.value,
  spp: VITE_ARTICLE_LIST_SIZE,
  key: "",
  word: "",
});

onMounted(() => {
  getArticleList();
});

const changeKey = (val) => {
  console.log("BoarList에서 선택한 조건 : " + val);
  param.value.key = val;
};

const getArticleList = () => {
  console.log("서버에서 계획 목록 얻어오자!!!", param.value);
  getTripList(
    param.value,
    ({ data }) => {
      articles.value = data;
      // currentPage.value = data.currentPage;
      // totalPage.value = data.totalPageCount;
    },
    (error) => {
      console.error(error);
      console.log("리스트를 못 얻어왔어용가리...");
    }
  );
};

const onPageChange = (val) => {
  console.log(val + "번 페이지로 이동 준비 끝!!!");
  currentPage.value = val;
  param.value.pgno = val;
  getArticleList();
};

const moveWrite = () => {
  router.push({ name: "article-write" });
};
</script>

<template>
  <div class="container">
    <div class="row justify-content-center">
      <div class="col-lg-10 mt-5 mb-5">
        <TheHeadingPage title="여행 계획" subtitle="🧳 여러분의 여행을 공유해주세요!! 🧳"></TheHeadingPage>
      </div>
      <div class="col-lg-10">
        <div class="row align-self-center mb-2">
          <div class="col-md-2 text-start">
            <!-- <button type="button" class="btn btn-outline-primary btn-sm" @click="moveWrite">
                                  글쓰기
                                </button> -->

          </div>
          <div class="col-md-5 offset-5">
            <form class="d-flex">
              <VSelect :selectOption="selectOption" :flag="selectFlag" @onKeySelect="changeKey" />
              <div class="input-group input-group-sm ms-1">
                <input type="text" class="form-control" v-model="param.word" placeholder="검색어..." />
                <button class="btn btn-dark" type="button" @click="getArticleList">검색</button>
              </div>
            </form>
          </div>
        </div>
        <div class="container-fluid">
          <div class="row gy-4">
            <PlanListItem class="col-xl-3 col-lg-4 col-md-6" v-for="article in articles" :key="article.plan_no"
              :article="article"></PlanListItem>
          </div>
          <VPageNavigation :current-page="currentPage" :total-page="totalPage" @pageChange="onPageChange">
          </VPageNavigation>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped></style>