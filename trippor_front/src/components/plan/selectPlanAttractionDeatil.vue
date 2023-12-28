<script setup>
import { ref, watch, computed, onMounted } from "vue";

const props = defineProps({ Attractions: Array });
const emit = defineEmits(["onAttrSelect"]);



const showselectAttr = (val) => {
    // key.value = val;
    console.log(val);
    // console.log(key.value + "선택!!!");
    emit("onAttrSelect", val);
};
</script>

<template>
    <div>
        <v-card class="mb-4" max-width="344" v-for="attraction in Attractions" @click="showselectAttr(attraction)"
            style="background-color: var(--bs-warning-border-subtle);">
            <v-img :src="attraction.image" style="height: 100px;" class="img" cover></v-img>

            <v-card-title>
                {{ attraction.title }}
            </v-card-title>
            <v-card-subtitle class="mb-2">🏠: {{ attraction.addr }}</v-card-subtitle>
            <v-spacer></v-spacer>
            <v-card-subtitle class="mb-2">💬: {{ attraction.memo }}</v-card-subtitle>
        </v-card>
    </div>
</template>

<style scoped></style>