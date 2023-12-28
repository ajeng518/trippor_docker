<script setup>
import { Alert } from "bootstrap";
import { ref, watch, onMounted } from "vue";

var map;
const polyline = ref([]);
const positions = ref([]);
const markers = ref([]);
const selectmarkers = ref([]);
const noimage = "@//assets/noimage.png";
const SelectAttractionList = ref([]);
const emit = defineEmits(["onSelectedList"]);
const props = defineProps({ attraction: Array, selectAttraction: Object });

watch(
  () => props.attraction.value,
  () => {
    positions.value = [];
    props.attraction.forEach((attraction) => {
      let obj = {};
      obj.latlng = new kakao.maps.LatLng(attraction.mapy, attraction.mapx);
      obj.title = attraction.title;
      obj.firstimage = attraction.firstimage || noimage;
      obj.contentid = attraction.contentid;
      obj.contenttypeid = attraction.contenttypeid;
      obj.addr1 = attraction.addr1;
      positions.value.push(obj);
    });
    loadMarkers();
  },
  { deep: true }
);

//다른 색 마커와
watch(
  () => SelectAttractionList.value,
  () => {
    // console.log("attractionlist에 들어옵니까?");
    // console.log(SelectAttractionList.value);
    // positions.value = [];
    // SelectAttractionList.forEach((attraction) => {
    //   let obj = {};
    //   console.log(attraction);
    //   // obj.latlng = new kakao.maps.LatLng(attraction.mapy, attraction.mapx);
    //   obj.latlng = attraction.latlng;
    //   obj.title = attraction.title;
    //   obj.firstimage = attraction.firstimage;
    //   obj.contentid = attraction.contentid;
    //   obj.contenttypeid = attraction.contenttypeid;
    //   positions.value.push(obj);
    // });

    togoMarkers();
    togoLine();
  },
  { deep: true }
);

watch(
  () => props.selectAttraction.value,
  () => {
    // 이동할 위도 경도 위치를 생성합니다
    var moveLatLon = new kakao.maps.LatLng(props.selectAttraction.mapy, props.selectAttraction.mapx);
    // 지도 중심을 부드럽게 이동시킵니다
    // 만약 이동할 거리가 지도 화면보다 크면 부드러운 효과 없이 이동합니다
    map.panTo(moveLatLon);
  },
  { deep: true }
);

onMounted(() => {
  if (window.kakao && window.kakao.maps) {
    initMap();
  } else {
    const script = document.createElement("script");
    script.src = `//dapi.kakao.com/v2/maps/sdk.js?autoload=false&appkey=${import.meta.env.VITE_KAKAO_MAP_SERVICE_KEY
      }&libraries=services,clusterer`;
    /* global kakao */
    script.onload = () => kakao.maps.load(() => initMap());
    document.head.appendChild(script);
  }
});

const initMap = () => {
  const container = document.getElementById("map");
  const options = {
    center: new kakao.maps.LatLng(33.450701, 126.570667),
    level: 3,
  };
  map = new kakao.maps.Map(container, options);

  // loadMarkers();
};

//추가 버튼을 눌렀을때 선택 리스트로 넣기
const handleButtonClick = (val) => {
  console.log(`아직 보내기 전임`)
  console.log(val);
  console.log(val.latlng);
  SelectAttractionList.value.push(val);
  emit("onSelectedList", SelectAttractionList.value);
};

const loadMarkers = () => {
  // 현재 표시되어있는 marker들이 있다면 map에 등록된 marker를 제거한다.
  deleteMarkers();

  // 마커 이미지를 생성합니다
  //   const imgSrc = require("@/assets/map/markerStar.png");
  // 마커 이미지의 이미지 크기 입니다
  //   const imgSize = new kakao.maps.Size(24, 35);
  //   const markerImage = new kakao.maps.MarkerImage(imgSrc, imgSize);

  // 마커를 생성합니다
  markers.value = [];
  positions.value.forEach((position) => {
    const marker = new kakao.maps.Marker({
      map: map, // 마커를 표시할 지도
      position: position.latlng, // 마커를 표시할 위치
      title: position.title, // 마커의 타이틀, 마커에 마우스를 올리면 타이틀이 표시됨.
      clickable: true, // // 마커를 클릭했을 때 지도의 클릭 이벤트가 발생하지 않도록 설정합니다
      contentid: position.contentid,
      // image: markerImage, // 마커의 이미지
    });
    markers.value.push(marker);

    //------------------------------------------------------------------------------------------------
    // const mapVue = this;

    // 마커 클릭 이벤트
    kakao.maps.event.addListener(marker, 'click', () => {
      const content =
        `<div class="overlayWrap" style="weight:400px;">
          <img class="overlayImg" src="${position.firstimage}" style="height: 200px; weight: 200px;"/>
          <div class="accommInfoWrap">
            <h4 class="accommName">${position.title}</h4>
            <input type="button" class="btnn btn btn-warning" value="추가"/>
          </div>
        </div>`;

      const parser = new DOMParser();
      const doc = parser.parseFromString(content, 'text/html');

      const infowindow = new kakao.maps.InfoWindow({
        removable: true,
        content: doc.body.firstChild, // 파싱된 DOM 요소를 넘김
      });

      infowindow.open(map, marker);
      // 버튼 클릭 이벤트

      const button = infowindow.getContent().querySelector('.btnn');
      button.addEventListener('click', () => {
        // console.log(position);
        // console.log(`아직 보내기 전임`)
        // console.log(val);
        // console.log(val.latlng);
        SelectAttractionList.value.push(position);
        emit("onSelectedList", SelectAttractionList.value);
        infowindow.close(map, marker);
        marker.setMap(null);
      });
    });
    // kakao.maps.event.addListener(marker, 'mouseover', () => { infowindow.open(map, marker) });
    // kakao.maps.event.addListener(marker, 'mouseout', () => { infowindow.close(map, marker) });
  });

  // 4. 지도를 이동시켜주기
  // 배열.reduce( (누적값, 현재값, 인덱스, 요소)=>{ return 결과값}, 초기값);
  const bounds = positions.value.reduce(
    (bounds, position) => bounds.extend(position.latlng),
    new kakao.maps.LatLngBounds()
  );
  map.setBounds(bounds);
};

const deletelastMark = () => {
  // const targetMarker = markers.value.filter((marker) => {
  //   marker.contentid === nowposition.contentid;
  //   console.log(marker.contentid)
  //   console.log(nowposition.contentid)
  //   // console.log(marker.contentid + ",,," + selectmarker.contentid);
  // });
  // if (targetMarker != null)
  //   targetMarker.setMap(null);
  // nowmarker.setMap(null);
};

const togoMarkers = () => {
  deleteselectMarkers();//수정해야함요....전용 삭제로
  // deletelastMark();//기본 마크였던 것을 선택마크로 바궈야하니까 기본마크를 지워줄게용
  // for (var i = 0; i < markers생.length; i++){
  //   if(markers.value[i].contentid===)
  // }
  // 마커 이미지를 성합니다
  const imgSrc = "https://raw.githubusercontent.com/Tarikul-Islam-Anik/Animated-Fluent-Emojis/master/Emojis/Smilies/Purple%20Heart.png";
  // 마커 이미지의 이미지 크기 입니다
  const imgSize = new kakao.maps.Size(48, 54);
  const markerImage = new kakao.maps.MarkerImage(imgSrc, imgSize);


  selectmarkers.value = [];
  SelectAttractionList.value.forEach((position) => {
    const selectmarker = new kakao.maps.Marker({
      map, // 마커를 표시할 지도
      position: position.latlng, // 마커를 표시할 위치
      image: markerImage,
      contentid: position.contentid,
      title: position.title,
    });
    selectmarkers.value.push(selectmarker);
    // console.log(targetMarker.title);

    //------------------------------------------------------------------------------------------------
    // const mapVue = this;

    // 마커 클릭 이벤트
    kakao.maps.event.addListener(selectmarker, 'click', () => {
      const content =
        `<div class="overlayWrap" style="weight:400px;">
          <img class="overlayImg" src="${position.firstimage}" style="height: 200px; weight: 200px;"/>
          <div class="accommInfoWrap">
            <h4 class="accommName">${position.title}</h4>
            <p>추가됨</p>
          </div>
        </div>`;

      const parser = new DOMParser();
      const doc = parser.parseFromString(content, 'text/html');

      const infowindow = new kakao.maps.InfoWindow({
        removable: true,
        content: doc.body.firstChild, // 파싱된 DOM 요소를 넘김
      });

      infowindow.open(map, selectmarker);
      // 버튼 클릭 이벤트
      // const button = infowindow.getContent().querySelector('.btn');
      // button.addEventListener('click', () => {
      //   // console.log(position);
      //   // handleButtonClick(position);
      // });

    });
    // kakao.maps.event.addListener(marker, 'mouseover', () => { infowindow.open(map, marker) });
    // kakao.maps.event.addListener(marker, 'mouseout', () => { infowindow.close(map, marker) });

  });
  // 4. 지도를 이동시켜주기
  // 배열.reduce( (누적값, 현재값, 인덱스, 요소)=>{ return 결과값}, 초기값);
  // const bounds = selectmarkers.value.reduce(
  //   (bounds, position) => bounds.extend(position.latlng),
  //   new kakao.maps.LatLngBounds()
  // );
  // map.setBounds(bounds);
}

const deleteMarkers = () => {
  if (markers.value.length > 0) {
    markers.value.forEach((marker) => marker.setMap(null));
  }
};
const deleteselectMarkers = () => {
  if (selectmarkers.value.length > 0) {
    selectmarkers.value.forEach((marker) => {
      marker.setMap(null);
    });
  }
}

//togoLine 함수 수정
const togoLine = () => {
  togoDeleteLine(); // 이전 polyline 제거

  const linePath = [];
  for (var i = 0; i < SelectAttractionList.value.length; i++) {
    linePath.push(SelectAttractionList.value[i].latlng);
  }

  // 새로운 polyline 생성
  const newPolyline = new kakao.maps.Polyline({
    path: linePath,
    strokeWeight: 5,
    strokeColor: '#f3969a',
    strokeOpacity: 0.7,
    strokeStyle: 'solid'
  });

  // 지도에 새로운 polyline 표시
  newPolyline.setMap(map);

  // ref 값 업데이트
  polyline.value = newPolyline;
};

// togoDeleteLine 함수 수정
const togoDeleteLine = () => {
  if (polyline.value instanceof kakao.maps.Polyline) {
    polyline.value.setMap(null); // 이전 polyline 제거
    polyline.value = null; // 레퍼런스 제거
  }
};

</script>

<template>
  <div id="map"></div>
</template>

<style>
#map {
  width: 100%;
  height: 700px;
}

/* 
.overlayImg{
  width: 100px;
  height: 100px;
} */
.accommName {
  position: absolute;
  top: 50%;
  left: 50%;
  width: 100;
  transform: translate(-50%, -50%);
  color: white;
  text-shadow: 2px 2px 2px #000;
  font-size: 20px;
  text-align: center;
}

.btnn {
  --bs-btn-padding-x: 0.75rem;
  --bs-btn-padding-y: 0.375rem;
  --bs-btn-font-family: ;
  --bs-btn-font-size: 1rem;
  --bs-btn-font-weight: 400;
  --bs-btn-line-height: 1.5;
  --bs-btn-color: var(--bs-body-color);
  --bs-btn-bg: transparent;
  --bs-btn-border-width: var(--bs-border-width);
  --bs-btn-border-color: transparent;
  --bs-btn-border-radius: var(--bs-border-radius);
  --bs-btn-hover-border-color: transparent;
  --bs-btn-box-shadow: inset 0 1px 0 rgba(255, 255, 255, 0.15), 0 1px 1px rgba(0, 0, 0, 0.075);
  --bs-btn-disabled-opacity: 0.65;
  --bs-btn-focus-box-shadow: 0 0 0 0.25rem rgba(var(--bs-btn-focus-shadow-rgb), .5);
  display: inline-block;
  padding: var(--bs-btn-padding-y) var(--bs-btn-padding-x);
  font-family: var(--bs-btn-font-family);
  font-size: var(--bs-btn-font-size);
  font-weight: var(--bs-btn-font-weight);
  line-height: var(--bs-btn-line-height);
  color: var(--bs-btn-color);
  text-align: center;
  text-decoration: none;
  vertical-align: middle;
  cursor: pointer;
  -webkit-user-select: none;
  -moz-user-select: none;
  user-select: none;
  border: var(--bs-btn-border-width) solid var(--bs-btn-border-color);
  border-radius: var(--bs-btn-border-radius);
  background-color: var(--bs-btn-bg);
  transition: color 0.15s ease-in-out, background-color 0.15s ease-in-out, border-color 0.15s ease-in-out, box-shadow 0.15s ease-in-out;
  color: orange;
}

/* .btnn:hover {
  background-color: orange;
  color: white;
} */
</style>