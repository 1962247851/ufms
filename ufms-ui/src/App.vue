<template>
  <v-app id="app">
    <router-view name="header"/>
    <v-content>
      <router-view/>
    </v-content>
    <router-view name="footer"/>
  </v-app>
</template>

<script>

export default {
  name: 'App',

  components: {},

};
</script>