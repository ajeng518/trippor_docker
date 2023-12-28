import 'vuetify/styles'
import { createVuetify } from 'vuetify'
import * as components from 'vuetify/components'
import * as directives from 'vuetify/directives' 

import './assets/bootstrap.css';
import { createApp } from "vue";
import { createPinia } from "pinia";

import piniaPluginPersistedstate from "pinia-plugin-persistedstate";

// import 'bootstrap/dist/css/bootstrap.css';

import App from "./App.vue";
import router from "./router";

// import "bootstrap/dist/minty/bootstrap.min.css";

import "bootstrap";

const app = createApp(App);
const pinia = createPinia();
const vuetify = createVuetify({
    components,
    directives,
  })

pinia.use(piniaPluginPersistedstate);

app.use(pinia);
// app.use(createPinia().use(piniaPluginPersistedstate));
app.use(router);

app.use(vuetify);

app.mount("#app");
