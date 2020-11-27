<template>
  <div>NavigationDrawer</div>
</template>

<script>
export default {
  name: "NavigationDrawer",
  data: () => ({
    loadingProducts: true,
    currentProduct: {},
    products: [
      {
        name: "产品1",
        logo: "https://dss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=2534506313,1688529724&fm=26&gp=0.jpg"
      },
      {
        name: "产品2222222",
        logo: "https://dss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=2534506313,1688529724&fm=26&gp=0.jpg"
      }
    ],
    items: [
      {
        title: '首页', icon: 'mdi-home', path: "/dashboard/home"
      },
      {title: "新建产品", icon: "mdi-plus", path: "/dashboard/new-product"},
      {
        title: "管理", icon: "mdi-home", path: "/dashboard/manage", items: [
          {title: "反馈列表", icon: "mdi-home", path: "/dashboard/feedbacks"}
        ]
      },
      {
        title: "设置", icon: "mdi-home", path: "/dashboard/settings", items: [
          {title: "产品设置", icon: "mdi-home", path: "/dashboard/product"}
        ]
      },
      {title: "统计", icon: "mdi-home", path: "/dashboard/analytics"},
    ],
  }),
  created() {
    //TODO 加载产品
    console.log("加载产品", this.products);
    this.selectProduct(this.products[0]);
    this.loadingProducts = false;
  },
  methods: {
    selectProduct(product) {
      this.currentProduct = product;
    },
    onProductItemClick(item) {
      this.selectProduct(item);
    }
  }
}
</script>

<style scoped>

</style>