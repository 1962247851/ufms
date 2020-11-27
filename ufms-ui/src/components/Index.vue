<template>
  <div class="d-flex justify-center align-end fill-height pb-5"
       style="background-image: url('index.jpg'); background-position: center; background-size: cover;">
    <v-btn v-for="(item,index) in buttons" :key="index" :to="item.path"
           :color="index===1?'primary':''" class="text-body-1 ps-5 pe-5 ma-5">
      {{ item.title }}
    </v-btn>
  </div>
</template>

<script>
export default {
  name: "Index",
  data: () => ({
    buttons: [
      {title: "注册", path: "/index/register"},
      {title: "登录", path: "/index/login"},
    ]
  })
}
</script>

<style scoped>

</style>