import { createRouter, createWebHistory } from "vue-router";
import TheMainView from "../views/TheMainView.vue";
import TheElectricChargingStationView from "@/views/TheElectricChargingStationView.vue";
import TheAttractionListView from "@/views/TheAttractionListView.vue";
// import TheTripPlanListView from "@/views/TheTripPlanListView.vue";
// import TheBoardView from "../views/TheBoardView.vue";

import { storeToRefs } from "pinia";
import { useMemberStore } from "@/stores/member";

const onlyAuthUser = async (to, from, next) => {
  const memberStore = useMemberStore();
  const { userInfo, isValidToken } = storeToRefs(memberStore);
  const { getUserInfo } = memberStore;

  // let loginUser = {};

  let token = sessionStorage.getItem("accessToken");

  if (userInfo.value != null && token) {
    // loginUser =
    await getUserInfo(token);
    console.log("로그인한 사용자 정보 새로 get함?", userInfo);
  }
  if (!isValidToken.value || userInfo.value === null) {
    // if (!loginUser.isValidToken ||  loginUser.userInfo === null) { // 흠...
    next({ name: "user-login" });
  } else {
    next();
  }
};

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: "/",
      name: "main",
      component: TheMainView,
    },
    {
      path: "/attraction",
      name: "attraction",
      // beforeEnter: onlyAuthUser,
      component: TheAttractionListView,
    },
    {
      path: "/user",
      name: "user",
      component: () => import("@/views/TheUserView.vue"),
      children: [
        {
          path: "login",
          name: "user-login",
          component: () => import("@/components/users/UserLogin.vue"),
        },
        {
          path: "join",
          name: "user-join",
          component: () => import("@/components/users/UserRegister.vue"),
        },
        {
          path: "mypage",
          name: "user-mypage",
          beforeEnter: onlyAuthUser,
          component: () => import("@/components/users/UserMyPage.vue"),
        },
        {
          path: "findpwd",
          name: "user-find-pwd",
          component: () => import("@/components/users/UserFindPwd.vue"),
        },
      ],
    },
    {
      path: "/board",
      name: "board",
      // component: TheBoardView,
      // route level code-splitting
      // this generates a separate chunk (About.[hash].js) for this route
      // which is lazy-loaded when the route is visited.
      component: () => import("../views/TheBoardView.vue"),
      redirect: { name: "article-list" },
      children: [
        {
          path: "list",
          name: "article-list",
          component: () => import("@/components/boards/BoardList.vue"),
        },
        {
          path: "view/:articleno",
          name: "article-view",
          beforeEnter: onlyAuthUser,
          component: () => import("@/components/boards/BoardDetail.vue"),
        },
        {
          path: "write",
          name: "article-write",
          beforeEnter: onlyAuthUser,
          component: () => import("@/components/boards/BoardWrite.vue"),
        },
        {
          path: "modify/:articleno",
          name: "article-modify",
          beforeEnter: onlyAuthUser,
          component: () => import("@/components/boards/BoardModify.vue"),
        },
      ],
    },
    {
      path: "/plan",
      name: "plan",
      component: () => import("@/views/TheTripPlanListView.vue"),
      redirect: { name: "plan-list" },
      children: [
        {
          path: "list",
          name: "plan-list",
          component: () => import("@/components/plan/PlanList.vue"),
        },
        {
          path: "view/:plan_no",
          name: "plan-view",
          beforeEnter: onlyAuthUser,
          component: () => import("@/components/plan/PlanDetail.vue"),
        },
        {
          path: "view/:user_id",
          name: "plan-mypage",
          beforeEnter: onlyAuthUser,
          component: () => import("@/components/plan/PlanMyPage.vue"),
        },
        // {
        //   path: "write",
        //   name: "article-write",
        //   beforeEnter: onlyAuthUser,
        //   component: () => import("@/components/boards/BoardWrite.vue"),
        // },
        // {
        //   path: "modify/:articleno",
        //   name: "article-modify",
        //   beforeEnter: onlyAuthUser,
        //   component: () => import("@/components/boards/BoardModify.vue"),
        // },
      ],
    },
    {
      path: "/estations",
      name: "estations",
      // beforeEnter: onlyAuthUser,
      component: TheElectricChargingStationView,
    },
    {
      path: "/todos",
      name: "todos",
      component: () => import("@/views/TheTodoView.vue"),
    },
  ],
});

export default router;
