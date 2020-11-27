<template>
  <v-app-bar
      app
      color="primary"
      dark
      clipped-left
  >
    <div class="d-flex align-center">
      <v-toolbar-title>UFMS</v-toolbar-title>
    </div>

    <v-spacer></v-spacer>

    <v-menu offset-y v-model="menuOpen" transition="slide-y-transition">
      <template v-slot:activator="{ on, attrs }" v-ripple>
        <v-btn v-bind="attrs"
               text
               height="45"
               v-on="on"
        >
          <div class="d-flex align-center">
            <div class="me-2 text-subtitle-1">{{ user.name }}</div>
            <!--用户头像-->
            <v-avatar size="40">
              <v-img
                  :src="user.avatar"
              />
            </v-avatar>
            <v-icon>mdi-chevron-down</v-icon>
          </div>
        </v-btn>
      </template>
      <v-list>
        <v-list-item
            v-for="(item, index) in items"
            :key="index"
            @click="onMenuItemClick(item)"
        >
          <v-icon class="me-2">{{ item.icon }}</v-icon>
          <v-list-item-title>{{ item.title }}</v-list-item-title>
        </v-list-item>
      </v-list>
    </v-menu>
  </v-app-bar>
</template>

<script>
export default {
  name: "Header",
  data: () => ({
    menuOpen: false,
    user: {
      name: "用户名",
      avatar: "https://dss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=2534506313,1688529724&fm=26&gp=0.jpg"
    },
    items: [
      {icon: 'mdi-view-dashboard-outline', title: '控制台', path: "/dashboard/home"},
      {icon: 'mdi-plus', title: '新建产品', path: "/dashboard/new-product"},
      {icon: 'mdi-view-grid-outline', title: '我的产品', path: "/dashboard/products"},
      {icon: 'mdi-logout-variant', title: '退出登录'},
    ],
  }),
  methods: {
    onMenuItemClick(item) {
      if (item.title === this.items[this.items.length - 1].title) {
        alert("退出登录")
        this.$router.push("/index")
      } else {
        if (this.$router.currentRoute.path !== item.path) {
          this.$router.push(item.path)
        }
      }
    }
  }
}
</script>

<style scoped>

</style>