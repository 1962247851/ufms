import Vue from 'vue';
import router from './router'
import axios from "axios";

//意思是携带cookie信息,保持session的一致性
axios.defaults.withCredentials = true;
axios.defaults.crossDomain = true

//请求返回拦截，把数据返回到页面之前做些什么...
axios.interceptors.response.use((response) => {
    //特殊错误处理
    let errorCode = response.data.errorCode
    let errorMsg = response.data.errorMsg
    if (errorCode === 2001 || errorCode === 2002 || errorCode === 2004 || errorCode === 2005 || errorCode === 2006 || errorCode === 2009) {
        sessionStorage.removeItem("state")
        alert(errorMsg)
        router.replace({path: "/admin/login"})
        //其余错误状态处理
    } else if (errorCode !== 200) {
        // alert(errorMsg)
        console.log(errorMsg)
        //请求成功
    } else {
        //将我们请求到的信息返回页面中请求的逻辑
    }
    return response
}, function (error) {
    console.log(error)
    return Promise.reject(error)
});

window.axios = axios;
Vue.prototype.axios = axios;