import API from './ufms/api.js'
import CONSTANT from './ufms/constant.js'
import UTIL from './ufms/util.js'

export default {
    API: API,
    CONSTANT: CONSTANT,
    UTIL: UTIL,
}

if (window.UTIL.getSessionStorageUser() === null) {
    if (
        window.location.pathname !== "/"
        && window.location.pathname !== "/login.html"
        && window.location.pathname !== "/page/product/feedback.html"
        && window.location.pathname !== "/page/product/new-feedback.html"
        && window.location.pathname !== "/page/404.html"
        && window.location.pathname !== "/page/product/feedback-user.html"
        && window.location.pathname !== "/page/product/replies.html"
    ) {
        //参数中有#需要先编码，不然正则表达式识别不出来
        console.log(window.location);
        window.location.replace("/login.html?redirect=" + escape(window.location.origin + "/#" + window.location.pathname))
    }
} else {
    if (window.location.pathname === "/login.html") {
        window.location.replace('/')
    }
}

