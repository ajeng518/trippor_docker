<script setup>
import { ref, watch, computed, onMounted } from "vue";

const props = defineProps({ selectOption: Array, flag: Object });
const emit = defineEmits(["onKeySelect"]);

const key = props.selectOption[0];
const onSelect = (val) => {
  if (props.flag === "board") {
    key.name = val.name;
  } else {
    key.name = val.name;
  }
  console.log(val.code + "선택!!!");
  // key.value = val.name;
  emit("onKeySelect", val.code);

};

// watch(
//   () => { props.flag.value },
//   () => {
//     console.log(props.flag.value);

//     console.log("들어와줘용가리 ㅠㅠㅠㅠ");
//     // if (props.flag.value === true) {
//     //   console.log(`~~~~~~`);
//     //   key.value = 0;
//     //   console.log(`!!!!!!!key 값 바꿨엉ㅇ`);
//     // }
//   }, { deep: true }
// );



</script>

<template>
  <!-- <select v-model="key" class="form-select form-select-sm w-50" @change="onSelect">
                                                                      <option v-for="option in selectOption" :key="option.code" :value="option.code"
                                                                        :disabled="option.code === '' ? true : false">
                                                                        {{ option.name }}
                                                                      </option>
                                                                    </select> -->

  <div class="dropdown mt-3">
    <button class="btn btn-warning dropdown-toggle" type="button" id="dropdownMenuButton" data-bs-toggle="dropdown"
      aria-expanded="false">
      {{ key.name }}
    </button>
    <ul class="dropdown-menu" aria-labelledby="dropdownMenuButton">
      <li v-for="item in selectOption" :key="item.code">
        <a class="dropdown-item" @click="onSelect(item)">{{ item.name }}</a>
      </li>
    </ul>
  </div>
</template>

<style scoped></style>
