<template>
  <v-navigation-drawer app clipped permanent>

    <v-list nav>

      <!--产品logo-->
      <div class="d-flex justify-center pt-2">
        <v-avatar class="elevation-2">
          <v-skeleton-loader type="avatar" v-if="loadingProducts"/>
          <v-img v-else :src="currentProduct.logo"/>
        </v-avatar>
      </div>

      <!--所有产品-->
      <v-list-item class="mb-0">
        <v-list-item-content>
          <v-skeleton-loader v-if="loadingProducts" type="text"/>
          <v-list-item-title v-else v-text="currentProduct.name"/>
        </v-list-item-content>
        <v-list-item-action>
          <v-menu open-on-hover offset-y transition="slide-y-transition">
            <template v-slot:activator="{ on, attrs }" v-ripple>
              <v-btn v-bind="attrs" icon v-on="on">
                <v-skeleton-loader v-if="loadingProducts" class="pa-2" type="avatar"/>
                <v-icon v-else>mdi-chevron-down</v-icon>
              </v-btn>
            </template>
            <v-list>
              <v-list-item
                  v-for="(item, index) in products"
                  :key="index"
                  @click="onProductItemClick(item)"
              >
                <v-list-item-title>{{ item.name }}</v-list-item-title>
              </v-list-item>
            </v-list>
          </v-menu>
        </v-list-item-action>

      </v-list-item>

      <v-divider class="mb-2"/>

      <div v-for="(item,index) in items"
           :key="item.title" :class="index===0?'mb-1':index===items.length-1?'mt-1':'mt-1 mb-1'">

        <v-list-item
            :to="item.items===undefined?item.path:''"
            :color="item.items!==undefined?'':'primary'"
            :disabled="item.items!==undefined">
          <v-list-item-avatar>
            <v-icon v-text="item.icon"/>
          </v-list-item-avatar>

          <v-list-item-content>
            <v-list-item-title v-text="item.title"/>
          </v-list-item-content>

        </v-list-item>

        <div v-if="item.items!==undefined">

          <v-list nav subheader>

            <v-list-item class="ps-14" v-for="subItem in item.items" :key="subItem.title"
                         :to="subItem.path===undefined?item.path+'/'+subItem.id:subItem.path" color="primary">
              <v-list-item-content>
                <v-list-item-title>{{ subItem.title }}</v-list-item-title>
              </v-list-item-content>
            </v-list-item>

          </v-list>

        </div>

      </div>

      <!--            <v-list-group -->
      <!--                v-for="item in items"-->
      <!--                :key="item.title"-->
      <!--                v-model="item.active"-->
      <!--                :prepend-icon="item.icon"-->
      <!--                :append-icon="item.items===undefined?'':'mdi-chevron-down'"-->
      <!--            >-->
      <!--              <template v-slot:activator>-->
      <!--                <v-list-item-content>-->
      <!--                  <v-list-item-title v-text="item.title"/>-->
      <!--                </v-list-item-content>-->
      <!--              </template>-->

      <!--              <v-list nav v-if="item.items!==undefined">-->
      <!--                <v-list-item-->
      <!--                    replace-->
      <!--                    :to="subItem.path===undefined?{name:'/dashboard/products',params:{id:subItem.id}}:subItem.path"-->
      <!--                    v-for="subItem in item.items"-->
      <!--                    :key="subItem.title"-->
      <!--                    @click="onSubItemClick(subItem)">-->
      <!--                  <v-list-item-content>-->
      <!--                    <v-list-item-title v-text="subItem.title"/>-->
      <!--                  </v-list-item-content>-->
      <!--                  <v-icon v-text="subItem.icon"/>-->
      <!--                </v-list-item>-->
      <!--              </v-list>-->

      <!--            </v-list-group>-->

    </v-list>
  </v-navigation-drawer>
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