function formatTime(timeStamp) {
    let date = new Date(timeStamp);
    let year = date.getFullYear();
    let month = date.getMonth() + 1;
    let day = date.getDate();
    let hour = date.getHours();
    let minute = date.getMinutes();
    let second = date.getSeconds();
    return [year, month, day].map(formatNumber).join('/') + ' ' + [hour, minute, second].map(formatNumber).join(':')
}


/**
 * 毫秒转换友好的显示格式
 * 输出格式：21小时前
 * @param  timeStamp [description]
 * @return {string}      [description]
 */
function timeAgo(timeStamp) {
    let inputDate = new Date(timeStamp);
    let inputTime = inputDate.getTime();
    let currentDate = new Date();
    let currentTime = currentDate.getTime();
    currentTime = parseInt((currentTime - inputTime) / 1000);

    //存储转换值
    let s = '';
    if (currentTime < 60) {//一分钟内
        return '刚刚';
    } else if ((currentTime < 60 * 60) && (currentTime >= 60)) {
        //超过十分钟少于1小时
        s = Math.floor(currentTime / 60);
        return s + "分钟前";
    } else if ((currentTime < 60 * 60 * 24) && (currentTime >= 60 * 60)) {
        //超过1小时少于24小时
        s = Math.floor(currentTime / 60 / 60);
        return s + "小时前";
    } else if ((currentTime < 60 * 60 * 24 * 3) && (currentTime >= 60 * 60 * 24)) {
        //超过1天少于3天内
        s = Math.floor(currentTime / 60 / 60 / 24);
        return s + "天前";
    } else {
        //超过3天
        if (inputDate.getFullYear() !== currentDate.getFullYear()) {
            s += inputDate.getFullYear() + "年"
        }
        return s + (inputDate.getMonth() + 1) + "月" + inputDate.getDate() + "日 " + [inputDate.getHours(), inputDate.getMinutes(), inputDate.getSeconds()].map(formatNumber).join(':');
    }
}

/**
 * 格式化秒
 * @param  value int 总秒数
 * @return result string 格式化后的字符串
 */
function formatSeconds(value) {
    let theTime = parseInt(value);// 需要转换的时间秒
    let theTime1 = 0;// 分
    let theTime2 = 0;// 小时
    let theTime3 = 0;// 天
    if (theTime > 60) {
        theTime1 = parseInt(String(theTime / 60));
        theTime = parseInt(String(theTime % 60));
        if (theTime1 > 60) {
            theTime2 = parseInt(String(theTime1 / 60));
            theTime1 = parseInt(String(theTime1 % 60));
            if (theTime2 > 24) {
                //大于24小时
                theTime3 = parseInt(String(theTime2 / 24));
                theTime2 = parseInt(String(theTime2 % 24));
            }
        }
    }
    let result = '';
    if (theTime > 0) {
        result = "" + parseInt(String(theTime)) + "秒";
    }
    if (theTime1 > 0) {
        result = "" + parseInt(String(theTime1)) + "分" + result;
    }
    if (theTime2 > 0) {
        result = "" + parseInt(String(theTime2)) + "小时" + result;
    }
    if (theTime3 > 0) {
        result = "" + parseInt(String(theTime3)) + "天" + result;
    }
    return result;
}

function formatNumber(n) {
    n = n.toString();
    return n[1] ? n : '0' + n
}

/**
 * 数据分页
 * @param jsonList 列表
 * @param start 开始的位置
 * @param count 个数
 */
function limit(jsonList, start, count) {
    return jsonList.filter((ele, index) => {
        return index >= start && index <= start + count - 1
    })
}

/**
 * 数据精确筛选
 *  @param jsonList 列表
 * @param propertyName 属性名
 * @param value 想要的的值
 */
function query(jsonList, propertyName, value) {
    return jsonList.filter((ele) => {
        return ele[propertyName] === value
    })
}

/**
 * 数据精确筛选
 * @param jsonList 列表
 * @param propertyName 属性名
 * @param value 不要的值
 */
function remove(jsonList, propertyName, value) {
    return jsonList.filter((ele) => {
        return ele[propertyName] !== value
    })
}

/**
 * 数据模糊筛选
 * @param jsonList 列表
 * @param propertyName 属性名
 * @param value 包含的值
 */
function contain(jsonList, propertyName, value) {
    return jsonList.filter((ele) => {
        return ele[propertyName].indexOf(value) !== -1
    })
}

/**
 * 时间戳转化为年 月 日 时 分 秒
 * number: 传入时间戳
 * format：返回格式，支持自定义，但参数必须与formateArr里保持一致
 */
function formatTimeTwo(number, format) {

    let formateArr = ['Y', 'M', 'D', 'h', 'm', 's'];
    let returnArr = [];

    let date = new Date(number * 1000);
    returnArr.push(date.getFullYear());
    returnArr.push(formatNumber(date.getMonth() + 1));
    returnArr.push(formatNumber(date.getDate()));

    returnArr.push(formatNumber(date.getHours()));
    returnArr.push(formatNumber(date.getMinutes()));
    returnArr.push(formatNumber(date.getSeconds()));

    for (let i in returnArr) {
        format = format.replace(formateArr[i], returnArr[i]);
    }
    return format;
}

/**
 * 已知两个时间戳
 * 计算两个时间差
 */
function diffTime(startDate, endDate) {
    //时间差的毫秒数
    let diff = endDate - startDate;
    //计算出相差天数
    let days = Math.floor(diff / (24 * 3600 * 1000));
    //计算天数后剩余的毫秒数
    let leave1 = diff % (24 * 3600 * 1000);
    //计算出小时数
    let hours = Math.floor(leave1 / (3600 * 1000));
    //计算小时数后剩余的毫秒数
    let leave2 = leave1 % (3600 * 1000);
    //计算相差分钟数
    let minutes = Math.floor(leave2 / (60 * 1000));
    //计算分钟数后剩余的毫秒数
    let leave3 = leave2 % (60 * 1000);
    //计算相差秒数
    let seconds = Math.round(leave3 / 1000);
    return {days: days, hours: hours, minutes: minutes, seconds: seconds};
}

function uuid() {
    let s = [];
    let hexDigits = "0123456789abcdef";
    for (let i = 0; i < 36; i++) {
        s[i] = hexDigits.substr(Math.floor(Math.random() * 0x10), 1);
    }
    s[12] = "4";  // bits 12-15 of the time_hi_and_version field to 0010
    s[16] = hexDigits.substr((s[16] & 0x3) | 0x8, 1);  // bits 6-7 of the clock_seq_hi_and_reserved to 01
    return s.join("");
}

function getFileFullPath(uuid, filename, type) {
    return window.API.FILE.DOWNLOAD + "?type=" + type + "&uuid=" + uuid + "&filename=" + filename
}

function getCustomUserInfoPath(properties) {
    let path = ''
    if (properties.userUuid !== null && properties.userName !== null && properties.userAvatar !== null) {
        path += "&uid=" + properties.userUuid
        path += "&uname=" + properties.userName
        path += "&uavatar=" + properties.userAvatar
    }
    return path;
}

/**
 * 获取产品反馈首页地址
 * @param properties 产品id、用户id、用户名、用户头像地址等
 * @returns {string}
 */
function getFullProductFeedbackPath(properties) {
    return '/page/product/feedback.html?pid=' + properties.productId + getCustomUserInfoPath(properties);
}


/**
 * 获取用户在某个产品下的所有反馈地址
 * @param selectedUserUuid 选中的用户id
 * @param properties productId、userUuid、userName、userAvatar
 * @returns {string}
 */
function getFullUserFeedbackPath(selectedUserUuid, properties) {
    return "/page/product/feedback-user.html?pid=" + properties.productId + "&suid=" + selectedUserUuid + getCustomUserInfoPath(properties)
}

function setCurrentProduct(product) {
    sessionStorage.setItem("currentProduct", JSON.stringify(product))
}

function getCurrentProduct() {
    let product = sessionStorage.getItem("currentProduct");
    if (product !== null) {
        return JSON.parse(product)
    }
    return null
}

function copyToClipboard(elementId, value) {
    let transfer = document.createElement('input');
    document.body.appendChild(transfer);
    //这里表示想要复制的内容
    transfer.value = value;
    transfer.select();
    if (document.execCommand('copy')) {
        transfer.blur();
        document.body.removeChild(transfer);
        document.getElementById(elementId).style.color = "green";
    }
}

/**
 * 获取产品某个反馈下的所有回复的PATH
 * @param properties
 * @returns {string}
 */
function getFullProductFeedbackRepliesPath(properties) {
    return "/page/product/replies.html?pid=" + properties.productId + "&oid=" + properties.originalId + getCustomUserInfoPath(properties)
}

/**
 * 生成回复flowItem
 * @param item
 * @param properties
 * @returns {string}
 */
function generateFlowReplyItem(item, properties) {
    let lis = '';
    lis += '<div class="layui-card-body"><div style="background-color: #f2f2f2;padding: 5px 10px">'
    layui.each(item.replies.content, (index, reply) => {
        // console.log(index, reply)
        lis += (generateFlowItem(reply, properties));
    })
    if (item.replies.totalElements > 3) {
        lis += ("<div>" +
            "<a style='color: #009688' target='_blank' href=" + getFullProductFeedbackRepliesPath(properties) + ">" +
            "共" + item.replies.totalElements + "条回复></a>" +
            "</div>")
    }
    return lis + '</div>'
}

/**
 * 生成反馈flowItem
 * @param item
 * @param properties{Object} [productId,]
 * @returns {string}
 */
function generateFlowItem(item, properties) {
    let productId = properties.productId
    //主贴或者回复详情页面
    if (item.original === undefined || item.original === null || properties.isReplies) {
        let str = ''
        str += '<li>' +
            '<div class="layui-card" style="margin: 10px;">' +
            '<div class="bottom-toolbar-controller">' +
            '<div class="layui-card-header">' +
            '<div class="layui-row">' +
            '<div class="layui-col-lg10 layui-col-md10 layui-col-sm9 layui-col-xs9 avatar">';
        //用户头像
        if (item.userAvatar === null) {
            str += '<div class="avatar-container">' +
                '<span class="avatar-content">' + item.userName[item.userName.length - 1] + '</span>' +
                '</div>'
        } else {
            str += '<img class="layui-nav-img" style="margin: 5px;"  lay-src="' + item.userAvatar + '" alt="' + item.userName + '"/>'
        }
        //用户在某个产品下的所有反馈
        str += '<a target="_blank" style="color: #009688;margin-left: 5px" href="' + getFullUserFeedbackPath(item.userUuid, properties) + '">' + item.userName + '</a>' +
            '<span style="margin-left: 5px; font-size: small" >' + timeAgo(item.createdDate) + '</span>' +
            '</div>' +
            '<div class="layui-col-lg2 layui-col-md2 layui-col-sm3 layui-col-xs3" style="text-align: right">';
        if (item.isRecommend) {
            str += '<span class="label label-good">好问题</span>'
        }
        if (item.isTopping) {
            str += '<span class="label">置顶</span>'
        }
        str += '</div>' +
            '</div>' +
            '</div>' +
            '<div class="layui-card-body">';
        if (item.parent !== null) {
            str += " 回复 " + '<a target="_blank" style="color: #009688" href="' + getFullUserFeedbackPath(item.parent.userUuid, properties) + '">' + item.parent.userName + '</a>：'
        }
        //回复详情——文字
        str += '<span>' + item.content + '</span>'
        //回复详情——配图
        str += '<div style="text-align: right;">' +
            '<div class="bottom-toolbar-hover-hide-item">' +
            '<i class="iconButton fa fa-arrow-circle-o-up layui-icon-top" type="top" pid="' + productId + '" fid="' + item.id + '"/>' +
            '<i class="iconButton fa fa-lock fa-eye-slash" type="hide" pid="' + productId + '" fid="' + item.id + '"/>' +
            '<i class="iconButton fa fa-lock layui-icon-top" type="lock" pid="' + productId + '" fid="' + item.id + '"/>' +
            '<i class="iconButton fa fa-star-o" type="recommend" pid="' + productId + '" fid="' + item.id + '"/>' +
            '</div>' +
            '<i class="iconButton fa fa-thumbs-o-up" type="like" pid="' + productId + '" fid="' + item.id + '">';
        if (item.likeCount !== 0) {
            //点赞次数
            str += "<span style='font-size: 15px;margin-left: 5px'>" + item.likeCount + "</span>";
        }
        str += '</i><i class="iconButton fa fa-reply" type="reply" pid="' + productId + '" fid="' + item.id + '"/>' +
            '</div>' +
            '</div>' +
            '</div>';
        //回复
        if (item.replies !== undefined && item.replies !== null && !item.replies.empty) {
            properties.originalId = item.id
            str += '<ul>' + generateFlowReplyItem(item, properties) + '</ul>'
        }
        return str + '</li>';
    } else {
        let str = '<li>' +
            '<a target="_blank" style="color: #009688" href="' + getFullUserFeedbackPath(item.userUuid, properties) + '">' + item.userName + '</a>';
        if (item.parent !== null) {
            str += " 回复 " + '<a target="_blank" style="color: #009688" href="' + getFullUserFeedbackPath(item.parent.userUuid, properties) + '">' + item.parent.userName + '</a>'
        }
        str += '：<span>' + item.content + '</span>' +
            '</li>'
        return str;
    }
}

/**
 * 从url中获取所有query参数
 * @returns {{}}
 */
function getAllUrlParams() {
    let search = window.location.search;
    // 写入数据字典
    let tmparray = search.substr(1, search.length).split("&");
    let paramsArray = {};
    // let paramsArray = [];
    if (tmparray != null) {
        for (let i = 0; i < tmparray.length; i++) {
            let reg = /[=|^==]/; // 用=进行拆分，但不包括==
            let set1 = tmparray[i].replace(reg, '&');
            let tmpStr2 = set1.split('&');
            // let array = [];
            paramsArray[decodeURIComponent(tmpStr2[0])] = decodeURIComponent(tmpStr2[1]);
            // paramsArray.push(array);
        }
    }
    //将参数数组进行返回
    return paramsArray;
}

/**
 * 根据name获取url参数的value
 * @param name key
 * @returns {string|null} value
 */
function getUrlParam(name) {
    let reg = new RegExp("(^|&)" + decodeURIComponent(name) + "=([^&]*)(&|$)");
    let r = window.location.search.substr(1).match(reg);
    if (r != null) return decodeURIComponent(r[2]);
    return null;
}

/**
 * 获取自定义登录态参数
 * @returns {{userAvatar: *, userUuid: (*|null|number), userName: *}|null}
 */
function getCustomUserInfo() {
    let allUrlParams = getAllUrlParams()
    if (allUrlParams.uid !== undefined && allUrlParams.uid !== null &&
        allUrlParams.uname !== undefined && allUrlParams.uname !== null &&
        allUrlParams.uavatar !== undefined && allUrlParams.uavatar !== null) {
        return {
            userUuid: allUrlParams.uid,
            userName: allUrlParams.uname,
            userAvatar: allUrlParams.uavatar
        }
    } else {
        return null
    }
}

export const UTIL = {
    formatSeconds: formatSeconds,
    formatTime: formatTime,
    formatTimeTwo: formatTimeTwo,
    diffTime: diffTime,
    timeAgo: timeAgo,
    limit: limit,
    query: query,
    remove: remove,
    contain: contain,
    uuid: uuid,
    getFileFullPath: getFileFullPath,
    getCurrentProduct: getCurrentProduct,
    setCurrentProduct: setCurrentProduct,
    getFullUserFeedbackPath: getFullUserFeedbackPath,
    copyToClipboard: copyToClipboard,
    generateFlowItem: generateFlowItem,
    generateFlowReplyItem: generateFlowReplyItem,
    getFullProductFeedbackPath: getFullProductFeedbackPath,
    getFullProductFeedbackRepliesPath: getFullProductFeedbackRepliesPath,
    getCustomUserInfo: getCustomUserInfo,
    getUrlParam: getUrlParam,
    getAllUrlParams: getAllUrlParams,
};

export default {
    UTIL: UTIL
}

window.UTIL = UTIL