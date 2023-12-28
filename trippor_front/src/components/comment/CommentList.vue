<script setup>
import CommentListItem from './item/CommentListItem.vue';
import { onMounted, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { listComment } from '@/api/comment';

const props = defineProps({ articleno: String, list: Object });

onMounted(() => {
    console.log("댓글 가져오자");
    getComment();
})

const comments = ref([]);
comments.value = props.list;

const getComment = () => {
    console.log(props.articleno);
    listComment( 
        props.articleno,
        ({data}) => {
            comments.value = data;
        },
        (error) => {
            console.error(error);
        }
     );
}

</script>

<template>
    <div>
        <CommentListItem v-for="comment in comments"
        :comment="comment">
        </CommentListItem>
    </div>
</template>

<style scoped>
    
</style>