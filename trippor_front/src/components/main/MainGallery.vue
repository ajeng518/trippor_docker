<script setup>
import { ref } from 'vue';

const themes = ref([
  "caping",
  "flight",
  "mountain",
  "ocean",
  "cloud",
  "Sunset",
  "paradise",
  "festival",
  "trip",
  "scenery",
  "nature",
  "travel"
])

</script>
<template>
  <div class="container-fluid">
    <div class="row gy-4 justify-content-center">
      <div class="col-xl-3 col-lg-4 col-md-6" v-for="theme in themes" :key="theme">
        <div class="gallery-item h-100">
          <!-- <img :src="`https://source.unsplash.com/random/300/?${theme}`" class="img-fluid" alt="" style="height: 30rem; width: 30rem;"> -->
          <v-card class="mx-auto img-fluid" :image="`https://source.unsplash.com/random/300/?${theme}`"
            style="height: 30rem; width: 30rem;" theme="dark"
            hover ></v-card>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped></style>