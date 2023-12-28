<script setup>
import { ref, watch, computed, onMounted } from "vue";

const props = defineProps({ checkOption: Array });
const emit = defineEmits(["onCheckSelect"]);

const Key = ref([]);
const onSelect = () => {
    console.log(Key.value + "check.contentTypeId");
    emit("onCheckSelect", Key.value);

}

</script>

<template>
    <fieldset class="form-group">
        <div class="form-check-inline" v-for="check in checkOption" :key="check.contentTypeId" @change="onSelect">
            <input class="form-check-input" v-model="Key" type="checkbox" :value="check.contentTypeId">
            <label class="form-check-label">{{ check.name }}</label>
        </div>
    </fieldset>
</template>

<style scoped>
.form-check-input:checked {
    background-color: #f3969a;
    /* 부트스트랩의 secondary 색상 */
    border-color: #f3969a;
    /* 부트스트랩의 secondary 색상 */
}
</style>