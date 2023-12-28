<script setup>
const { title, subtitle } = defineProps([
    'title', 'subtitle'
])
</script>


<template>
    <v-hover>
        <template v-slot:default="{ isHovering, props }">

            <v-card v-bind="props" 
            :color="isHovering ? 'var(--bs-secondary)' : 'var(--bs-gray)'"
             elevation="18">
                <v-card-item>
                    <v-card-title style="color: var(--bs-gray-200);">{{ title }}</v-card-title>
                    <v-card-subtitle class="card-text" style="color: var(--bs-gray-200);">{{ subtitle
                    }}</v-card-subtitle>
                    <img 
                    src="https://raw.githubusercontent.com/Tarikul-Islam-Anik/Animated-Fluent-Emojis/master/Emojis/Smilies/Grinning%20Face%20with%20Smiling%20Eyes.png"
                    alt="Grinning Face with Smiling Eyes" width="200" height="200" />
                </v-card-item>
            </v-card>
        </template>
    </v-hover>
</template>



<style scoped></style>