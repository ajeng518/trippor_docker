<script setup>
const props = defineProps({ article: Object });

// console.log(props.article.attractions);
</script>

<template>
  <!-- <tr class="text-center">
    <th scope="row">{{ props.article.plan_no }}</th>
    <td class="text-start">
      <router-link :to="{ name: 'plan-view', params: { plan_no: props.article.plan_no } }"
        class="article-title link-dark">
        {{ props.article.subject }}
                                              </router-link>
                                            </td>
                                                                                                                                                                                          <td>{{ props.article.user_id }}</td>
                                                                                                                                                                                          <td>{{ props.article.hit }}</td>
                                                                                                                                                                                          <td>{{ props.article.register_time }}</td>
                                                                                                                                                                                        </tr> -->
  <router-link :to="{ name: 'plan-view', params: { plan_no: props.article.plan_no } }" class="article-title link-dark">
    <v-card class="mb-4" max-width="344">
      <v-img :src="props.article.attractions[0].image" height="200px" cover></v-img>

      <v-card-title>
        {{ props.article.plan_no }}. {{ props.article.subject }}
      </v-card-title>

      <v-card-subtitle>
        <div class="row mb-2">
          <div class="col-1">
            {{ props.article.user_id }}
          </div>
          <div class="col-11 text-right">
            {{ props.article.register_time }}
          </div>
        </div>
        <v-spacer></v-spacer>
        <div class="text-right mb-2">
          👁️: {{ props.article.hit }} ❤️‍🔥: {{ props.article.favorite }}
        </div>
      </v-card-subtitle>
    </v-card>
  </router-link>
</template>
  
<style scoped>
a {
  text-decoration: none;
}
</style>