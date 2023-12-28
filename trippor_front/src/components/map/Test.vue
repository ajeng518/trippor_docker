<script setup>
import draggable from 'vuedraggable';
import { ref, onMounted } from "vue";
const emit = defineEmits(['onAttrSelect']);
// 
const noimage = "@/assets/noimage.png";
const props = defineProps({ attractions: Array });
const memo = ref("");
// const Attractions = ([{
//   contentid: 1,
//   firstimage: "",
//   title: "asdf"
// }])
const subject = ref("");

const log = () => {
  //   emit('updatelist', props.attractions);
  console.log("!!!!!!!!!!!!!!!!!!!!!!!!!!!" + props.attractions);
  console.log("testtttt+++  : " + subject.value);
  // console.log(props.attractions);
};

const onClick = (val) => {
  console.log(val);
  // console.log(key.value + "선택!!!");
  emit("onAttrSelect", val);
}

const inputSubject = () => {
  // console.log("제목 입력하자마자 다 동기화 해드립니다람쥐");
  // if (subject.value.length > 0) {
  //   for (var i = 0; i < props.attractions.length; i++){
  //     props.attractions[i].subject = subject.value;
  //     // console.log(props.attractions[i].subject.value);
  //   }
  // }
  emit("inputSubject", subject.value);

}
</script>

<template>
  <div>
    <div>
      <!-- <h3>드래그&드랍으로 리스트를 이용해보세용</h3> -->
      <input type="text" class="form-control mt-3 mb-2" placeholder="계획 제목 적으새우.." id="inputDefault" v-model="subject"
        @change="inputSubject"><br>

      <!-- <draggable :list="attractions" @change="log">

                                                                            <template #item="{ element }">
                                                                              <div class="list-group-item">
                                                                                <div @click="onClick(element)" :key="element">
                                                                                  <img class="stationImage" :src="element.firstimage" alt="No Image" />
                                                                                  <div @blur="now">
                                                                                    {{ element.title }}
                                                                                  </div>
                                                                                </div>
                                                                                <div>
                                                                                  <input type="text" v-model="element.memo" class="form-control" placeholder="관광지 메모 작성..." id="inputDefault">
                                                                                </div>
                                                                              </div>
                                                                            </template>
                                                                          </draggable> -->

      <draggable :list="attractions" @change="log" class="scrollable-container">

        <template #item="{ element }">
          <v-card class="mx-auto mb-3" max-width="400">
            <!-- <div class="list-group-item"> -->
            <v-img class="align-end text-white" @click="onClick(element)" :key="element" height="200"
              :src="element.firstimage || noimage" cover>
              <v-card-title>{{ element.title }}</v-card-title>
            </v-img>
            <!-- <div @click="onClick(element)" :key="element">
                                                              <img class="stationImage" :src="element.firstimage" alt="No Image" />
                                                              <div @blur="now">-->
            <v-card-text>
              <div>{{ element.addr1 }}</div>
            </v-card-text>
            <!-- </div> -->
            <!-- </div> -->
            <div>
              <input type="text" v-model="element.memo" class="form-control" placeholder="관광지 메모 작성..." id="inputDefault">
            </div>
            <!-- </div> -->
          </v-card>
        </template>
      </draggable>

    </div>
  </div>
</template>

<style scoped>
.stationImage {
  width: 120px;
  height: 100px;
}

.scrollable-container {
  max-height: 650px;
  overflow-y: scroll;
  margin-bottom: 10px;
}
</style>