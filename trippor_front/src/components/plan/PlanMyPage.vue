<script setup>
import { ref, onMounted } from 'vue';
import { useRouter, useRoute } from 'vue-router';
import { getMyplanList } from "@/api/attraction";
import TheHeadingPage from "../layout/TheHeadingPage.vue";

import VSelect from "@/components/common/VSelect.vue";
import PlanListItem from "@/components/plan/item/PlanListItem.vue";
import VPageNavigation from "@/components/common/VPageNavigation.vue";

const router = useRouter();
const route = useRoute();

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
    getMyArticleList();
    console.log("제발", route.params.user_id);
});

const getMyArticleList = () => {
    console.log("서버에서 계획 목록 얻어오자!!!", route.params.user_id);
    getMyplanList(
        route.params.user_id,
        ({ data }) => {
            articles.value = data;
            console.log(data);
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

</script>

<template>
    <div class="container">
        <div class="row justify-content-center">
            <div class="col-lg-10 mt-5 mb-5">
                <TheHeadingPage title="나의 여행"></TheHeadingPage>
            </div>
            <div class="col-lg-10">
                <div class="container-fluid">
                    <div class="row gy-4" v-for="article in articles">
                        <PlanListItem class="col-xl-3 col-lg-4 col-md-6" :key="article.plan_no" :article="article">
                        </PlanListItem>
                    </div>
                    <VPageNavigation :current-page="currentPage" :total-page="totalPage" @pageChange="onPageChange">
                    </VPageNavigation>
                </div>
            </div>
        </div>
    </div>
</template>

<style scoped></style>