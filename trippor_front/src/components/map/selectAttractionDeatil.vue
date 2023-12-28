<script setup>
import { ref, watch, computed, onMounted } from "vue";

const props = defineProps({ Attractions: Array });
const emit = defineEmits(["onAttrSelect"]);
const noimage = "@/assets/noimage.png";


const showselectAttr = (val) => {
    // key.value = val;
    console.log(val);
    // console.log(key.value + "선택!!!");
    emit("onAttrSelect", val);
};
</script>

<template>
    <v-card class="mx-auto mb-3" v-for="attraction in Attractions" @click="showselectAttr(attraction)" max-width="400">
        <v-img class="align-end text-white" height="200" :src="attraction.firstimage || noimage" cover>
            <v-card-title>{{ attraction.title }}</v-card-title>
        </v-img>
        <v-card-text>
            <div>{{ attraction.addr1 }}</div>
        </v-card-text>
    </v-card>
</template>

<style scoped>
.scrollable-container {
    max-height: 650px;
    overflow-y: scroll;
    margin-bottom: 10px;
}
</style>