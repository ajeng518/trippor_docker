<script setup>
import CommentFormItem from '../comment/item/CommentFormItem.vue';
import CommentListItem from '../comment/item/CommentListItem.vue';

import { ref, onMounted } from "vue";
import { useRoute, useRouter } from "vue-router";
import { detailArticle, deleteArticle } from "@/api/board";
import { write, listComment, deleteParent } from "@/api/comment";
import { useMemberStore } from "@/stores/member";
import { storeToRefs } from "pinia";
import { httpStatusCode } from "@/util/http-status";

const route = useRoute();
const router = useRouter();
const memberStore = useMemberStore();

// const articleno = ref(route.params.articleno);
const { articleno } = route.params;
const { getUserInfo } = memberStore;
const { userInfo } = storeToRefs(memberStore);

const article = ref({});
const isPossibleUser = ref(false);

onMounted(() => {
  getArticle();
  getComment();
  // checkValiableUser();
});

const getArticle = () => {
  // const { articleno } = route.params;
  console.log(articleno + "번글 얻으러 가자!!!");
  detailArticle(
    articleno,
    ({ data }) => {
      article.value = data;
      console.log("수정, 삭제할 권한이 있는 사용자가 맞는지 확인?", article.value.userId);

      let token = sessionStorage.getItem("accessToken");
      getUserInfo(token);

      if (userInfo.value.userId == 'admin' || userInfo.value.userId == article.value.userId) {
        console.log("관리자 또는 작성자네용 ㅎㅎ");
        isPossibleUser.value = true;
      }
    },
    (error) => {
      console.error(error);
    }
  );
};


function moveList() {
  router.push({ name: "article-list" });
}

function moveModify() {
  router.push({ name: "article-modify", params: { articleno } });
}

function onDeleteArticle() {
  // const { articleno } = route.params;
  console.log(articleno + "번글 삭제하러 가자!!!");

  if (confirm("정말 삭제하시나요?")) {
    deleteArticle(
      articleno,
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



////////////////////////댓글////////////////////////
const comments = ref({
  // userId: "",
  // userName: "",
  // registerTime: "",
  // context: "",
  // parentId: "",
  // article: "",
})

const getComment = () => {
  console.log("처음 댓글 가져오기")
  listComment(
    articleno,
    ({ data }) => {
      comments.value = data;
    },
    (error) => {
      console.error(error);
    }
  );
}

const writeComment = (newComment) => {
  console.log("댓글 등록하자", newComment.value);
  write(
    newComment.value,
    (response) => {
      if (response.status == httpStatusCode.CREATE) {
        alert("댓글 등록!!");
        console.log("댓글 등록 성공", response.data.commentList);
        comments.value = response.data.commentList;
        console.log(comments.value);
        // listComment(
        //   articleno,
        //   ({data}) => {
        //     comments.value = data;
        //   },
        //   (error) => {
        //     console.error(error);
        //   }
        // )
      }
    },
    (error) => {
      console.error(error);
    }
  )
}


// const delComment = (target) => {
//     console.log("댓글 삭제할거임!!!!!!");
//     console.log( target.value.parentId);
//     deleteParent(
//       target.value.parentId,
//         (response) => {
//             if (response.status == httpStatusCode.OK) {
//                 alert('삭제되었습니다.');
//                 comments.value = [];
//                 comments.value = response.data.commentList;
//                 console.log("네?", comments.value);
//                 // children.value =
//             }
//         },
//         (error) => {
//             console.error(error);
//             alert(error.message);
//         }
//     )

// }









</script>

<template>
  <div class="container">
    <div class="row justify-content-center">

      <div class="col-lg-10 text-start">
        <div class="row my-2">
          <h2 class="text-secondary pr-5 pt-5 pb-3">{{ article.articleNo }}. {{ article.subject }}</h2>
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
                <span class="fw-bold">{{ article.userName }}</span> <br />
                <span class="text-secondary fw-light">
                  {{ article.registerTime }}
                </span>
              </p>
            </div>
          </div>
          <div class="col-md-4 align-self-center text-end">
            <label for="commentSize" class="mr-1">조회수 :</label>
            <span class="badge rounded-pill bg-secondary"> {{ article.hit }}</span>
          </div>
          <div class="divider mb-3"></div>
          <div class="text-secondary">
            {{ article.content }}
          </div>
          <div class="divider mt-3 mb-3"></div>
          <div class="d-flex justify-content-end">
            <button type="button" class="btn btn-outline-primary mb-3" @click="moveList">
              글목록
            </button>
            <button v-show="isPossibleUser" type="button" class="btn btn-outline-success mb-3 ms-1" @click="moveModify">
              글수정
            </button>
            <button v-show="isPossibleUser" type="button" class="btn btn-outline-danger mb-3 ms-1"
              @click="onDeleteArticle">
              글삭제
            </button>
          </div>
        </div>
        <div>
          <h6><strong> 댓글: <span class="badge rounded-pill bg-secondary"> {{ comments.length }}</span></strong></h6>
          <CommentFormItem :articleno="articleno" type="parent" @regist="writeComment"></CommentFormItem>
          <!-- <CommentList :articleno="articleno" :list="comments"></CommentList> -->
          <!-- @removeComment="delComment" -->
          <CommentListItem v-for="commentDetail in comments" :commentDetail="commentDetail" :key="commentDetail.parentId">
          </CommentListItem>

        </div>
      </div>

    </div>
  </div>
</template>

<style scoped></style>
