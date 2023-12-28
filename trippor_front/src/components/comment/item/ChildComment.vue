<script setup>
import { ref } from 'vue';

const props = defineProps({ child: Object });
const emit = defineEmits(['deleteChildComment']);

const show = ref(false);

const showModal = () => {
    console.log("대댓글 수정");
    // show.value= !show.value;

}

const remove = () => {
    if (confirm("해당 댓글을 삭제하시겠습니까?")) {
        console.log("부모로 child 보내요", props.child);
        emit('deleteChildComment', props.child);
    }
    else {
        console.log("답글 삭제 취소");
    }
}


</script>

<template>
    <!-- <div class="card bg-secondary mb-3">
                <div class="card-header">{{ props.child.userName }}
                    <button type="button" @click="showModal">수정</button>
                    <button type="button" @click="remove">삭제</button>
                </div>
                <div class="card-body">
                    <p class="card-text">{{ props.child.context }}</p>
                </div>
            </div> -->
    <v-list lines="one">
        <v-list-item :title="props.child.userName" :subtitle="props.child.context"
            prepend-avatar="https://raw.githubusercontent.com/Tarikul-Islam-Anik/Animated-Fluent-Emojis/master/Emojis/People/Baby%20Angel.png">
            <div class="button-group">
                <button class="btn btn-outline-info mr-2" type="button" @click="showModal">수정</button>
                <button class="btn btn-outline-danger" type="button" @click="remove">삭제</button>
            </div>
        </v-list-item>
    </v-list>
</template>

<style scoped>
.button-group {
    display: flex;
    justify-content: flex-end;
}
</style>