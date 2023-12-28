<script setup>
import { ref, onMounted } from "vue";
import { listSidoGugun, listAttractions, insertTrip } from "@/api/attraction";
import { httpStatusCode } from "@/util/http-status";
import { storeToRefs } from "pinia";
import { useMemberStore } from "@/stores/member";

import VKakaoMap from "@/components/common/VKakaoMap.vue";
import VSelect from "@/components/common/VSelect.vue";
import VPageNavigation from '@/components/common/VPageNavigation.vue';
import VCheck from "../components/common/VCheck.vue";

import selectAttractionDeatil from "../components/map/selectAttractionDeatil.vue";
import Test from "@/components/map/Test.vue";

const memberStore = useMemberStore();
const { userInfo } = storeToRefs(memberStore);
const { VITE_OPEN_API_SERVICE_KEY } = import.meta.env;

const currentPage = ref(0);
const totalPage = ref(0);

const sidoList = ref([{ name: "시도선택", code: "" }]);
const gugunList = ref([{ name: "구군선택", code: "" }]);
const Attractions = ref([]);
const selectAttraction = ref({});
const SelectAttractionList = ref([]);


const contentTypes = ref([
  { name: "관광지", contentTypeId: "12" },
  { name: "문화시설", contentTypeId: "14" },
  { name: "축제공연행사", contentTypeId: "15" },
  { name: "여행코스", contentTypeId: "25" },
  { name: "레포츠", contentTypeId: "28" },
  { name: "숙박", contentTypeId: "32" },
  { name: "쇼핑", contentTypeId: "38" },
  { name: "음식점", contentTypeId: "39" },
]);

const selectType = ref([]);
const selectFlag = ref(false);
const notflag = ref(false);
const subject = ref("");

const param = ref({
  serviceKey: VITE_OPEN_API_SERVICE_KEY,
  numOfRows: 200,
  MobileOS: "ETC",
  MobileApp: "trippor",
  _type: "json"
});

onMounted(() => {
  getSidoList();
});

const getSidoList = () => {

  listSidoGugun(
    param.value,
    ({ data }) => {
      let options = [];
      options.push({ name: "시도선택", code: "" });
      data.response.body.items.item.forEach((sido) => {
        options.push({ name: sido.name, code: sido.code });
      });
      sidoList.value = options;
      // flag.value = false;
    }, (err) => {
      console.log(err);
    }
  );
};

const onChangeSido = (val) => {
  param.value.areaCode = val;
  param.value.arrange = null;

  // if (param.value.sigunguCode != null) {
  //   console.log(flag.value);
  // }

  param.value.sigunguCode = null;
  param.value.contentTypeId = null;
  listSidoGugun(
    param.value,
    ({ data }) => {
      let options = [];
      options.push({ name: "구군선택", code: "" });
      data.response.body.items.item.forEach((gugun) => {
        options.push({ name: gugun.name, code: gugun.code });
      });
      // console.log("어어?? 구군을 누른적이 잇군요 당신은 다시 구군 초기화로 가야합니다")
      // flag.value = true;
      gugunList.value = options;
      getAttractions();
    }, (err) => {
      console.log(err);
    }
  );
};

const onChangeGugun = (val) => {
  // param.value.areaCode = val.value;
  // param.value.areaCode = param.value.areaCode;
  param.value.sigunguCode = val;
  // flag.value = false;
  getAttractions();
};

const getAttractions = () => {
  param.value.arrange = 'Q';
  param.value.contentTypeId = null;
  listAttractions(
    param.value,
    ({ data }) => {
      // console.log(data);
      const datas = data.response.body;
      totalPage.value = parseInt(data.response.body.toftalCount / data.response.body.numORows);
      Attractions.value = data.response.body.items.item;
    }
  )
  console.log(Attractions);
};

const deletetripList = (attraction) => {
  // console.log(`총길이는..?${SelectAttractionList.value.length}`);
  for (var i = 0; i < SelectAttractionList.value.length; i++) {
    // console.log(SelectAttractionList.value[i]);
    if (SelectAttractionList.value[i].contentid === attraction.contentid) {
      // console.log("들어옵니까용???")
      // console.log(value[i].cotentid)
      SelectAttractionList.value.splice(i, 1);
    }
  }
  // var options = [];
  // options.push(attraction);
  // SelectAttractionList.value = [...SelectAttractionList.value, ...options];
  // console.log(SelectAttractionList.value);
};

const onselectShowAttr = (val) => {
  console.log("TheAttractionListView로 넘어왔습니다람쥐");
  console.log(val);
  selectAttraction.value = val;
}

const onChangeType = (val) => {
  if (val.length === 0) {//체크박스에 선택이 하나도 없다면...
    getAttractions();
  } else {
    Attractions.value = [];
    for (var i = 0; i < val.length; i++) {
      console.log(val[i]);
      param.value.contentTypeId = val[i];
      var options = [];
      listAttractions(
        param.value,
        ({ data }) => {
          // const datas = data.response.body;
          const count = data.response.body.numOfRows;
          const dataAll = data.response.body.items;
          totalPage.value += parseInt(data.response.body.totalCount / data.response.body.numORows);
          for (var j = 0; j < count; j++) {
            options.push(dataAll.item[j]);
            // console.log(data.response.body.items.item[j]);
          }
          Attractions.value = [...Attractions.value, ...options];
        }, (err) => {
          console.log(err);
        }
      )
    }
  }
}

const onPageChange = (val) => {
  // console.log(val + '번 페이지로 이동 준비 끝!!!');
  // currentPage.value = val;
  // param.value.pageNo = val;
  getAttractions();
};

const changeSelectList = (val) => {

  console.log("@@@@@@@@@@@@@@@@@@")
  console.log(SelectAttractionList.value);
  // console.log(val);
  // SelectAttractionList.value.push(val);
  SelectAttractionList.value = val;
}

const inputTitle = (val) => {
  console.log("Attractionview 로 왔어용가리" + val);
  subject.value = val;
}

const savetripList = () => {
  // alert("이제 backend로 가보자보자고");
  console.log("subject:: " + subject.value);
  console.log(SelectAttractionList.value);
  if (SelectAttractionList.value.length > 0) {
    const sendToBack = ref([]);
    // console.log(userInfo.value);

    var i = 1;
    SelectAttractionList.value.forEach((select) => {
      // console.log(select);
      const temp = {
        user_id: userInfo.value.userId,
        contentid: select.contentid,
        title: select.title,
        subject: subject.value,
        image: select.firstimage,
        addr: select.addr1,
        memo: select.memo,
        latitude: select.latlng.La,
        longitude: select.latlng.Ma,
        order_num: i,
      };
      i++;
      sendToBack.value.push(temp);
    });
    // console.log(sendToBack.value);
    insertTrip(
      sendToBack.value,
      (response) => {
        let msg = "계획 등록 처리시 문제 발생했습니다.";
        if (response.status == httpStatusCode.CREATE) msg = "계획 등록이 완료되었습니다.!!!! :)";
        alert(msg);
      }
    )
  }
}

</script>

<template>
  <div>

  </div>
  <!-- <div class="container text-center mt-3"> -->
  <!-- <ol class="breadcrumb ">
                                                                                                                                                                                                                                                                                                                                                                                                              <li class="breadcrumb-item Primary">관광지 출력</li>
                                                                                                                                                                                                                                                                                                                                                                                                            </ol> -->
  <div class="row">
    <!-- <div class="col d-flex flex-row-reverse">
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                          <VSelect :selectOption="sidoList" @onKeySelect="onChangeSido" />
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        </div>
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        <div class="col">
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                          <VSelect :selectOption="gugunList" @onKeySelect="onChangeGugun" />
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        </div>
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        <div class="text-center">
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                          <VCheck :checkOption="contentTypes" @onCheckSelect="onChangeType" />
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        </div> -->
  </div>
  <div class="row mb-3 ">
    <!-- <div class="col "> -->
    <!-- <div class="alert alert-success" role="alert">여행지 검색</div> -->

    <!-- <div id="scrollable-container"> -->
    <div>
      <!-- <selectAttractionDeatil :Attractions="Attractions" @onAttrSelect="onselectShowAttr" /> -->
      <!-- <button class="btn btn-secondary" type="button" data-bs-toggle="offcanvas" data-bs-target="#offcanvasExample"
                                                                                                                                                                                                                                              aria-controls="offcanvasExample">
                                                                                                                                                                                                                                              여행지 검색
                                                                                                                                                                                                                                              </button> -->

      <!-- <div class="d-grid-3 gap-2"> -->
      <!-- <button class="btn btn-warning search-btn" type="button" data-bs-toggle="offcanvas"
                                                                                                data-bs-target="#offcanvasExample" aria-controls="offcanvasExample">여행지 검색</button> -->
      <!-- </div> -->
      <div class="offcanvas offcanvas-start" tabindex="-1" id="offcanvasExample" aria-labelledby="offcanvasExampleLabel">
        <div class="offcanvas-header">
          <h5 class="offcanvas-title" id="offcanvasExampleLabel">관광지 검색</h5>
          <button type="button" class="btn-close text-reset" data-bs-dismiss="offcanvas" aria-label="Close"></button>
        </div>
        <div class="offcanvas-body">
          <div class="row">
            <div class="col">
              <VSelect :selectOption="sidoList" :flag="selectFlag" @onKeySelect="onChangeSido" />
            </div>
            <div class="col mb-3">
              <VSelect :selectOption="gugunList" :flag="selectFlag" @onKeySelect="onChangeGugun" />
            </div>
            <div class="row  text-center">
              <VCheck :checkOption="contentTypes" @onCheckSelect="onChangeType" />
            </div>
          </div>
          <selectAttractionDeatil :Attractions="Attractions" @onAttrSelect="onselectShowAttr" />
          <!-- <VPageNavigation :current-page="currentPage" :total-page="totalPage" @pageChange="onPageChange">
                                                                                                                                                                                                                                                                                                                                                                                                                                                </VPageNavigation> -->
        </div>
      </div>
    </div>

    <!-- </div> -->
    <div class="col-9 kakao-map mt-2">
      <VKakaoMap :attraction="Attractions" :selectAttraction="selectAttraction" @onSelectedList="changeSelectList" />
      <div class="fixed-button">
        <button class="btn btn-warning search-btn" type="button" data-bs-toggle="offcanvas"
          data-bs-target="#offcanvasExample" aria-controls="offcanvasExample">🔍</button>
      </div>
    </div>
    <div class="col-3 mt-2">
      <div>
        <nav class="navbar navbar-expand-lg bg-light" data-bs-theme="light">
          <div class="container-fluid">
            <a class="navbar-brand" href="#">✔️ 선택한 여행계획</a>
          </div>
        </nav>
        <Test :attractions="SelectAttractionList" @onAttrSelect="deletetripList" @inputSubject="inputTitle" />
      </div>

      <!-- <template> -->
      <v-btn block color="yellow-darken-1" @click="savetripList">여행계획 저장하기</v-btn>
      <!-- </template> -->
      <!-- <div type="button" class="alert alert-primary" role="alert" @click="savetripList">여행계획 저장하기</div> -->
    </div>

  </div>
  <!-- </div> -->
</template>

<style scoped>
#bttn {
  position: relative;
  top: 100%;
  left: 5.5%;
  /* width: 100; */
  transform: translate(-50%, 100%);
  /* color: white; */
  text-shadow: 2px 2px 2px #000;
  font-size: 20px;
  text-align: center;
}

.kakao-map {
  position: relative;
  z-index: 1;
}

.search-btn {
  position: fixed;
  bottom: 75%;
  right: 28%;
  border: none;
  border-radius: 16px;
  padding: 12px;
  font-weight: bold;
  box-shadow: 0px 5px 5px gray;
  cursor: pointer;
  /* Adjust other styles as needed */
}

.fixed-button {
  position: absolute;
  z-index: 100;
}
</style>