<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">

  <div id="app">
    <v-app id="inspire">

      <v-navigation-drawer v-model="drawer" app clipped>
        <ConnectionsListPanel/>
        <TypesListPanel/>
      </v-navigation-drawer>

      <v-app-bar app color="green" dark clipped-left dense>
        <v-app-bar-nav-icon @click.stop="drawer = !drawer"></v-app-bar-nav-icon>
        <div class="flex-grow-1"></div>
        <v-btn icon v-for="entry in languages" :key="entry.title" @click="changeLocale(entry.language)">
          <flag :iso="entry.flag" v-bind:squared=false />
        </v-btn>
      </v-app-bar>

      <component v-bind:is="panel"></component>

      <v-footer color="green" app>
        <span class="white--text">github.com/CrocInc/sql-boot</span>
        <v-spacer></v-spacer>
        <span class="white--text">&copy; 2019</span>
      </v-footer>
    </v-app>
  </div>

</template>

<script>
import TypesListPanel from './TypesListPanel'
import ConnectionsListPanel from './ConnectionsListPanel'
import ObjectsTablePanel from "./ObjectsTablePanel";
import i18n from '@/plugins/i18n';

export default {
  data: () => ({
    drawer: true,
    drawerRight: true,
    right: null,
    left: null,
    languages: [
      { flag: 'us', language: 'en', title: 'English' },
      { flag: 'ru', language: 'ru', title: 'Русский' },
      { flag: 'es', language: 'es', title: 'Español' }
    ]
  }),
  props: ['panel'],
  name: 'HelloWorld',
  components: {ObjectsTablePanel, ConnectionsListPanel, TypesListPanel },
  methods: {
    changeLocale(locale) {
      i18n.locale = locale;
    }
  },
  watch: {
    $route (to, from) {
      this.$store.commit('changeUri', to.fullPath)
    },
    getSimpleUri: function (newUri, oldUri) {
      this.$router.push({path: '/' + newUri})
    }
  },
  computed: {
    getSimpleUri () {
      return this.$store.getters.getSimpleUri
    }
  },
  created: function () {
    this.$store.commit('changeUri', this.$router.currentRoute.fullPath)
  }
}

</script>
