<template>
  <div class="layui-header">
    <ul class="layui-nav">
      <a href="/" class="layui-logo">UFMS</a>
      <li class="layui-nav-item">
        <a href="">控制台<span class="layui-badge">9</span></a>
      </li>
      <li class="layui-nav-item">
        <a href="">个人中心<span class="layui-badge-dot"></span></a>
      </li>
      <li class="layui-nav-item">
        <a href=""><img src="//t.cn/RCzsdCq" class="layui-nav-img">我</a>
        <dl class="layui-nav-child">
          <dd v-for="item in items" :key="item.title">
            <a href="javascript:;">
              <span>
                <i :class="'layui-icon '+ item.icon" style="color: black"/>
              </span>
              {{ item.title }}</a>
          </dd>
        </dl>
      </li>
    </ul>
  </div>
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
      {icon: 'layui-icon-console', title: '控制台', path: "/dashboard/home"},
      {icon: 'layui-icon-add-circle', title: '新建产品', path: "/dashboard/new-product"},
      {icon: 'layui-icon-app', title: '我的产品', path: "/dashboard/products"},
      {icon: 'layui-icon-logout', title: '退出登录'},
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
  },
  created() {
    console.log(window.layui)
    //注意：导航 依赖 element 模块，否则无法进行功能性操作
    window.layui.use('element', function () {
      let element = window.layui.element;
      console.log(element)
      //…
    });
  }
}
</script>

<style scoped>

</style>