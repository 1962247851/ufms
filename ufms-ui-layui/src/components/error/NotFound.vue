<template>
    <div class="d-flex flex-column justify-center align-center" style="height: 100%">
        <p class="font-weight-bold display-2">404</p><br/>
        <p class="display-2">{{notFoundType}}不见了哦</p><br/>
        <p>抱歉，您访问的资源不存在。</p>
        <div>
            <v-btn color="primary" large class="ma-5" @click="$router.back()">返回上一级/首页</v-btn>
            <v-btn class="ma-5" large @click="$router.replace({path:'/index/home'})">直接返回首页</v-btn>
        </div>
    </div>
</template>

<script>
    export default {
        name: "NotFound",
        data() {
            return {
                notFoundType: "页面"
            }
        },
        created: {},
        mounted() {
            let type = this.$route.params.type
            if (type !== undefined) {
                this.notFoundType = type
            }
        }
    }
</script>

<style scoped>

</style>