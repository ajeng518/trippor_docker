<script setup>
import { useMenuStore } from "@/stores/menu";
import { storeToRefs } from "pinia";
import { useMemberStore } from "@/stores/member";
import { useRouter } from "vue-router";
import { onMounted } from "vue";

const router = useRouter();

const menuStore = useMenuStore();
const memberStore = useMemberStore();
// 반응형을 유지하면서 스토어에서 속성을 추출하려면, storeToRefs()를 사용
// https://pinia.vuejs.kr/core-concepts/
const { menuList } = storeToRefs(menuStore);
const { changeMenuState } = menuStore;
const { userLogout } = memberStore;
const { isLogin } = storeToRefs(memberStore);
const { getUserInfo } = memberStore;
const { userInfo } = storeToRefs(memberStore);

onMounted(() => {

  let token = sessionStorage.getItem("accessToken");
  if (isLogin.value) {
    getUserInfo(token);
    console.log(userInfo);
  }

});

const logout = () => {
  console.log("로그아웃!!!!");
  userLogout();
  changeMenuState();
};
</script>

<template>
  <nav class="navbar navbar-expand-lg bg-dark bg-body-tertiary sticky-top" data-bs-theme="light">
    <div class="container-fluid">
      <router-link :to="{ name: 'main' }" class="navbar-brand">
        <img
          src="https://raw.githubusercontent.com/Tarikul-Islam-Anik/Animated-Fluent-Emojis/master/Emojis/Animals/Penguin.png"
          alt="Penguin" height="100" />
      </router-link>
      <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarScroll"
        aria-controls="navbarScroll" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
      </button>
      <div class="collapse navbar-collapse" id="navbarScroll">
        <ul class="navbar-nav me-auto my-2 my-lg-0 navbar-nav-scroll" style="--bs-scroll-height: 100px">
          <li class="nav-item">
            <router-link :to="{ name: 'plan-mypage', params: { user_id: userInfo.userId } }" class="nav-link">마이
              플랜</router-link>
          </li>
          <li class="nav-item">
            <!-- <a class="nav-link" href="#">여행계획 plan-mypage</a> -->
            <router-link :to="{ name: 'plan' }" class="nav-link">여행 계획</router-link>
          </li>
          <!-- <li class="nav-item">
                                                        <a class="nav-link" href="#">커뮤니티</a>
                                                      </li> -->
          <!-- <li class="nav-item dropdown">
                                                    <a class="nav-link dropdown-toggle" href="#" role="button" data-bs-toggle="dropdown" aria-expanded="false">
                                                      HELP DESK
                                                    </a>
                                                    <ul class="dropdown-menu">
                                                      <li><a class="dropdown-item" href="#">공지사항</a></li>
                                                      <li><a class="dropdown-item" href="#">QnA</a></li>
                                                    </ul>
                                                  </li> -->
          <li class="nav-item">
            <router-link :to="{ name: 'attraction' }" class="nav-link">관광지 출력</router-link>
          </li>
          <li class="nav-item">
            <router-link :to="{ name: 'board' }" class="nav-link">커뮤니티</router-link>
          </li>
        </ul>
        <ul class="navbar-nav ms-auto my-2 my-lg-0 navbar-nav-scroll" style="--bs-scroll-height: 100px">




          <template v-for="menu in menuList" :key="menu.routeName">
            <template v-if="menu.show">
              <template v-if="menu.routeName === 'user-logout'">
                <li class="nav-item">
                  <router-link to="/" @click.prevent="logout" class="nav-link">{{
                    menu.name
                  }}</router-link>
                </li>
              </template>
              <template v-else>
                <li class="nav-item">
                  <router-link :to="{ name: menu.routeName }" class="nav-link">{{
                    menu.name
                  }}</router-link>
                </li>
              </template>
            </template>
          </template>
        </ul>
      </div>
    </div>
  </nav>
</template>

<style scoped>
img {
  height: 100px;
}
</style>
