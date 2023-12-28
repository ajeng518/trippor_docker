<script setup>
import { Alert } from "bootstrap";
import { ref, watch, onMounted } from "vue";

var map;
const polyline = ref([]);
const positions = ref([]);
const markers = ref([]);
const selectmarkers = ref([]);
const noimage = "@/assets/noimage.png";

// const SelectAttractionList = ref([]);
// const emit = defineEmits(["onSelectedList"]);
const props = defineProps({ attraction: Array, selectAttraction: Object });

onMounted(async () => {
    if (window.kakao && window.kakao.maps) {
        console.log("초기에 맵 로드는 하니:::");
        initMap();

        // watchfunction();
    } else {
        console.log("ㅋㅋㅋ여기는? 먼저 와야하지 않겟나..?");

        const script = document.createElement("script");
        script.src = `//dapi.kakao.com/v2/maps/sdk.js?autoload=false&appkey=${import.meta.env.VITE_KAKAO_MAP_SERVICE_KEY}&libraries=services,clusterer`;
        /* global kakao */
        script.onload = async () => {
            await kakao.maps.load(() => {
                console.log(`init하기 전이지롱`);
                initMap();
                console.log(`init햇지롱`);
                watchfunction();

            });
            console.log("로드 완료했다람쥐렁이!!!!!!!!")

        }
        document.head.appendChild(script);



        console.log("왜 안되냐고 맵아")
        // watchfunction();
    }
});

const initMap = () => {
    const container = document.getElementById("map");
    const options = {
        center: new kakao.maps.LatLng(35.7, 127.5),
        level: 8,
    };
    map = new kakao.maps.Map(container, options);
    // watchfunction();/
    // loadMarkers();
};

const watchfunction = () => {
    positions.value = [];
    console.log("들어오냐고!!");

    props.attraction.forEach((attr) => {
        console.log(attr);
        console.log(attr.latitude + "!!");
        console.log(attr.longitude);
        let obj = {};
        obj.latlng = new kakao.maps.LatLng(attr.longitude, attr.latitude);
        console.log(obj.latlng);
        obj.title = attr.title;
        obj.firstimage = attr.image || noimage;
        obj.contentid = attr.content_id;
        // obj.contenttypeid = attr.contenttypeid;
        obj.addr1 = attr.addr;
        positions.value.push(obj);
    });
    console.log(positions.value);

    loadMarkers();
    togoLine();
};

// watch(
//     () => props.attraction,
//     () => {
//         positions.value = [];
//         console.log("들어오냐고!!");

//         props.attraction.forEach((attr) => {
//             console.log(attr);
//             console.log(attr.latitude + "!!");
//             console.log(attr.longitude);
//             let obj = {};
//             obj.latlng = new kakao.maps.LatLng(attr.longitude, attr.latitude);
//             console.log(obj.latlng);
//             obj.title = attr.title;
//             obj.firstimage = attr.image;
//             obj.contentid = attr.content_id;
//             // obj.contenttypeid = attr.contenttypeid;
//             obj.addr1 = attr.addr;
//             positions.value.push(obj);
//         });
//         loadMarkers();
//     },
//     { deep: true }
// );

//다른 색 마커와
// watch(
//   () => SelectAttractionList.value,
//   () => {
//     // console.log("attractionlist에 들어옵니까?");
//     // console.log(SelectAttractionList.value);
//     // positions.value = [];
//     // SelectAttractionList.forEach((attraction) => {
//     //   let obj = {};
//     //   console.log(attraction);
//     //   // obj.latlng = new kakao.maps.LatLng(attraction.mapy, attraction.mapx);
//     //   obj.latlng = attraction.latlng;
//     //   obj.title = attraction.title;
//     //   obj.firstimage = attraction.firstimage;
//     //   obj.contentid = attraction.contentid;
//     //   obj.contenttypeid = attraction.contenttypeid;
//     //   positions.value.push(obj);
//     // });

//     togoMarkers();
//     togoLine();
//   },
//   { deep: true }
// );

watch(
    () => props.selectAttraction.value,
    () => {
        // 이동할 위도 경도 위치를 생성합니다
        var moveLatLon = new kakao.maps.LatLng(props.selectAttraction.longitude, props.selectAttraction.latitude);
        // 지도 중심을 부드럽게 이동시킵니다
        // 만약 이동할 거리가 지도 화면보다 크면 부드러운 효과 없이 이동합니다
        map.panTo(moveLatLon);
    },
    { deep: true }
);



//추가 버튼을 눌렀을때 선택 리스트로 넣기
// const handleButtonClick = (val) => {
//     console.log(`아직 보내기 전임`)
//     console.log(val);
//     console.log(val.latlng);
//     SelectAttractionList.value.push(val);
//     emit("onSelectedList", SelectAttractionList.value);
// };

const loadMarkers = () => {
    // 현재 표시되어있는 marker들이 있다면 map에 등록된 marker를 제거한다.
    deleteMarkers();

    // 마커 이미지를 생성합니다
    //   const imgSrc = require("@/assets/map/markerStar.png");
    // 마커 이미지의 이미지 크기 입니다
    //   const imgSize = new kakao.maps.Size(24, 35);
    //   const markerImage = new kakao.maps.MarkerImage(imgSrc, imgSize);
    // 마커 이미지를 생성합니다
    const imgSrc = "https://raw.githubusercontent.com/Tarikul-Islam-Anik/Animated-Fluent-Emojis/master/Emojis/Smilies/Purple%20Heart.png";
    // 마커 이미지의 이미지 크기 입니다
    const imgSize = new kakao.maps.Size(48, 54);
    const markerImage = new kakao.maps.MarkerImage(imgSrc, imgSize);

    // 마커를 생성합니다
    markers.value = [];
    positions.value.forEach((position) => {
        console.log(position.latlng);
        console.log("@@@@@@@@@@@@@@@@@@@@@")
        const marker = new kakao.maps.Marker({
            map: map, // 마커를 표시할 지도
            position: position.latlng, // 마커를 표시할 위치
            image: markerImage,
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
                `<div class="overlayWrap" style="height:300px, weight:400px">
          <img class="overlayImg" src="${position.firstimage}" style="height: 100px; weight: 200px"/>
          <div class="accommInfoWrap">
            <p class="accommName">${position.title}</p>
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

// const togoMarkers = () => {
//     deleteselectMarkers();//수정해야함요....전용 삭제로
//     // deletelastMark();//기본 마크였던 것을 선택마크로 바궈야하니까 기본마크를 지워줄게용
//     // for (var i = 0; i < markers.length; i++){
//     //   if(markers.value[i].contentid===)
//     // }
//     // 마커 이미지를 생성합니다
//     const imgSrc = "https://raw.githubusercontent.com/Tarikul-Islam-Anik/Animated-Fluent-Emojis/master/Emojis/Travel%20and%20places/Luggage.png";
//     // 마커 이미지의 이미지 크기 입니다
//     const imgSize = new kakao.maps.Size(48, 54);
//     const markerImage = new kakao.maps.MarkerImage(imgSrc, imgSize);


//     selectmarkers.value = [];
//     SelectAttractionList.value.forEach((position) => {
//         const selectmarker = new kakao.maps.Marker({
//             map, // 마커를 표시할 지도
//             position: position.latlng, // 마커를 표시할 위치
//             image: markerImage,
//             contentid: position.contentid,
//             title: position.title,
//         });
//         selectmarkers.value.push(selectmarker);
//         // console.log(targetMarker.title);

//         //------------------------------------------------------------------------------------------------
//         // const mapVue = this;

//         // 마커 클릭 이벤트
//         kakao.maps.event.addListener(selectmarker, 'click', () => {
//             const content =
//                 `<div class="overlayWrap" style="height:300px, weight:400px">
//           <p style="color:red;">선택됨</p>
//           <img class="overlayImg" src="${position.firstimage}" style="height: 100px; weight: 200px"/>
//           <div class="accommInfoWrap">
//             추가됨.
//             <p class="accommName">${position.title}</p>
//           </div>
//         </div>`;

//             const parser = new DOMParser();
//             const doc = parser.parseFromString(content, 'text/html');

//             const infowindow = new kakao.maps.InfoWindow({
//                 removable: true,
//                 content: doc.body.firstChild, // 파싱된 DOM 요소를 넘김
//             });

//             infowindow.open(map, marker);
//             // 버튼 클릭 이벤트
//             const button = infowindow.getContent().querySelector('.btn');
//             button.addEventListener('click', () => {
//                 // console.log(position);
//                 // handleButtonClick(position);
//             });

//         });
//         // kakao.maps.event.addListener(marker, 'mouseover', () => { infowindow.open(map, marker) });
//         // kakao.maps.event.addListener(marker, 'mouseout', () => { infowindow.close(map, marker) });

//     });
//     // 4. 지도를 이동시켜주기
//     // 배열.reduce( (누적값, 현재값, 인덱스, 요소)=>{ return 결과값}, 초기값);
//     // const bounds = selectmarkers.value.reduce(
//     //   (bounds, position) => bounds.extend(position.latlng),
//     //   new kakao.maps.LatLngBounds()
//     // );
//     // map.setBounds(bounds);
// }

const deleteMarkers = () => {
    if (markers.value.length > 0) {
        markers.value.forEach((marker) => marker.setMap(null));
    }
};
// const deleteselectMarkers = () => {
//     if (selectmarkers.value.length > 0) {
//         selectmarkers.value.forEach((marker) => {
//             marker.setMap(null);
//         });
//     }
// }

//togoLine 함수 수정
const togoLine = () => {
    togoDeleteLine(); // 이전 polyline 제거

    const linePath = [];
    for (var i = 0; i < positions.value.length; i++) {
        linePath.push(positions.value[i].latlng);
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
</style>