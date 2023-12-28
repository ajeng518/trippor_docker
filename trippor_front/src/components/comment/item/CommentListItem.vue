<script setup>
import { ref } from 'vue';
import { deleteParent } from '../../../api/comment';
import { deleteChild, writeChild } from '@/api/comment';
import { httpStatusCode } from '@/util/http-status';

import CommentFormItem from './CommentFormItem.vue';
import ChildComment from './ChildComment.vue';

const props = defineProps({ commentDetail: Object });

// console.log(props.commentDetail);


const parent = ref({
    parentId: props.commentDetail.parentId,
    userName: props.commentDetail.userName,
    userId: props.commentDetail.userId,
    context: props.commentDetail.context,
    registerTime: props.commentDetail.registerTime,
    deleteTime: props.commentDetail.deleteTime,
    childComments: props.commentDetail.childComments,
});

const children = ref({});
const show = ref(false);

children.value = props.commentDetail.childComments;
// console.log(parent.value.deleteTime);

const removeChild = (target) => {
    console.log("대댓글 삭제하자", target.childId);
    deleteChild(
        target.childId,
        (response) => {
            console.log("삭제하면 뭘 받나요?", response.status);
            let { data } = response;
            if (response.status == httpStatusCode.OK) {
                alert("댓글 삭제!!");
                parent.value = data.parentComment;
                console.log("children 변경", parent.value);
                children.value = parent.value.childComments;
            }
        },
        (error) => {
            console.error(error);
        }
    )
}

const removeParent = () => {
    console.log("댓글 삭제할거임!!!!!!");
    console.log(parent.value.parentId);
    deleteParent(
        parent.value.parentId,
        (response) => {
            let { data } = response
            if (response.status == httpStatusCode.OK) {
                alert('삭제되었습니다.');
                parent.value = data.parentComment;
                children.value = data.parentComment.childComments;
                console.log("네?", data);
                // children.value = 
            }
        },
        (error) => {
            console.error(error);
            alert(error.message);
        }
    )

}

const createChild = (newChild) => {
    console.log("새로운 답글 달기!", newChild);
    writeChild(
        newChild.value,
        (response) => {
            let { data } = response;
            if (response.status == httpStatusCode.CREATE) {
                alert("댓글 등록!!");
                console.log("등록하고 댓글을 받아온다.", data);
                parent.value = data.childList;
                children.value = data.childList.childComments;
                show.value = !show;
            }
        }
    )
}

// const emit = defineEmits(['removeComment']);

// const removeParent = () => {
//     console.log("부모 vue로 삭제할 부모 댓글 보내요", parent);
//     emit('removeComment', parent);
// }

</script>

<template>
    <div class="card bg-light mb-3">
        <div class="card-header">
            <div class="d-flex justify-content-between align-items-center">
                <div class="d-flex align-items-center">
                    <v-avatar class="mr-2">
                        <v-img
                            src="https://raw.githubusercontent.com/Tarikul-Islam-Anik/Animated-Fluent-Emojis/master/Emojis/Smilies/Kissing%20Face%20with%20Closed%20Eyes.png"
                            alt="Kissing Face with Closed Eyes" />
                    </v-avatar>
                    <span class="fw-bold mr-5">{{ parent.userName }}</span>
                    <p style="display: flex; align-items: center; margin: 0;">{{ parent.registerTime }}</p>

                </div>
                <button v-if="parent.deleteTime == null" @click="show = !show">댓글달기</button>
            </div>
        </div>
        <div class="card-body">
            <p class="card-text">{{ parent.context }}</p>
            <div class="d-flex justify-content-between align-items-center mb-2 mr-4">
                <button class="btn btn-danger" v-if="parent.deleteTime == null" type="button"
                    @click="removeParent">삭제</button>
            </div>
            <Transition>
                <CommentFormItem v-if="show" type="child" :parentId="parent.parentId" @registChild="createChild">
                </CommentFormItem>
            </Transition>
            <ChildComment v-for="child in children" :key="child.childId" :child="child" @deleteChildComment="removeChild">
            </ChildComment>
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

.btn-danger {
    margin-left: auto;
}

/* 수정된 스타일 */
.d-flex {
    display: flex;
    align-items: center;
}
</style>
  