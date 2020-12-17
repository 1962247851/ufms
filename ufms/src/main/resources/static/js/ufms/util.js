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
        if (s === 1) {
            return "昨天";
        } else if (s === 2) {
            return "前天";
        } else {
            return "大前天";
        }
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
    return (window.API.FILE.DOWNLOAD + "?type=" + type + "&uuid=" + uuid + "&filename=" + filename).replace(/\+/g, '%2B')
}

function getCustomUserInfoPath(properties) {
    if (properties === null) {
        return ''
    }
    let path = ''
    if (properties.userUuid !== null && properties.userName !== null) {
        path += "&uid=" + properties.userUuid
        path += "&uname=" + properties.userName
    }
    if (properties.userAvatar !== null) {
        path += "&uavatar=" + properties.userAvatar
    }
    return path;
}

/**
 * 获取产品反馈首页地址
 * @param productId 产品id
 * @returns {string}
 */
function getFullProductFeedbackPath(productId) {
    return '/page/product/feedback.html?pid=' + productId + getCustomUserInfoPath(getCustomUserInfo());
}

/**
 * 获取新增产品反馈地址
 * @returns {string}
 */
function getFullNewProductFeedbackPath(productId) {
    return '/page/product/new-feedback.html?pid=' + productId + getCustomUserInfoPath(getCustomUserInfo());
}

/**
 * 获取用户在某个产品下的所有反馈地址
 * @param productId productId
 * @param selectedUserUuid 选中的用户id
 * @returns {string}
 */
function getFullUserFeedbackPath(productId, selectedUserUuid) {
    return "/page/product/feedback-user.html?pid=" + productId + "&suid=" + selectedUserUuid + getCustomUserInfoPath(getCustomUserInfo())
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
 * @param productId 产品id
 * @param originalId 主贴id
 * @returns {string}
 */
function getFullProductFeedbackRepliesPath(productId, originalId) {
    return "/page/product/replies.html?pid=" + productId + "&oid=" + originalId + getCustomUserInfoPath(getCustomUserInfo())
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
            "<a style='color: #009688' target='_blank' href=" + getFullProductFeedbackRepliesPath(properties.productId, properties.originalId) + ">" +
            "共" + item.replies.totalElements + "条回复></a>" +
            "</div>")
    }
    return lis + '</div>'
}

/**
 * 生成附图
 * @param uuid {String}
 * @param pictures {JSON}
 * @returns {string}
 */
function generateFlowItemPictures(uuid, pictures) {
    // console.log("pictures", pictures)
    let str = '<div>';
    str += '<div class="image-set mt-15">';
    for (let index in pictures) {
        /**
         *       <a data-magnify="gallery"
         data-caption="Paraglider flying over Aurlandfjord, Norway by framedbythomas"
         href="https://farm1.staticflickr.com/512/32967783396_a6b4babd92_z.jpg">
         <img src="https://farm1.staticflickr.com/512/32967783396_a6b4babd92_s.jpg" alt="">
         </a>
         */
        let fileName = pictures[index];
        let fileFullPath = getFileFullPath(uuid, fileName, 'feedback');
        str += "<a data-magnify='gallery' href='" + fileFullPath + "' data-caption='" + fileName + "' data-group='" + uuid + "'>"
        str += '<img lay-src="' + fileFullPath + '" alt="' + fileName + '">'
        str += "</a>"
    }
    return str + "</div></div>";
}

/**
 * 生成反馈flowItem
 * @param item
 * @param properties{Object} [productId,]
 * @returns {string}
 */
function generateFlowItem(item, properties) {
    let productId = properties.productId
    let user = item.user;
    //主贴或者回复详情页面
    if (item.original === undefined || item.original === null || properties.isReplies || properties.isUserDetail) {
        //设置originalId
        properties.originalId = item.id
        let str = ''
        str += '<li class="bottom-toolbar-controller">' +
            '<div class="layui-card" style="margin: 10px;">' +
            '<div>' +
            '<div class="layui-card-header">' +
            '<div>'
        //用户头像
        if (user.avatar === undefined || user.avatar === null) {
            str += '<div class="avatar-container">' +
                '<span class="avatar-content">' + user.username[user.username.length - 1] + '</span>' +
                '</div>'
        } else {
            str += '<img data-magnify="gallery" class="layui-nav-img" style="margin: 5px;cursor: zoom-in;"  lay-src="' + user.avatar + '" data-src="' + user.avatar + '" alt="' + user.username + '" data-group="' + user.username + '"/>'
        }
        //用户在某个产品下的所有反馈
        str += '<a target="_blank" style="color: '
        if (item.isAdmin) {
            str += '#FFB800';
        } else {
            str += '#009688';
        }
        str += ';margin-left: 5px" href="' + getFullUserFeedbackPath(productId, user.uuid) + '">' + user.username + '</a>' +
            '<span style="margin-left: 5px; font-size: small" >' + timeAgo(item.createdDate) + '</span>' +
            '<span style="float: right">';
        if (item.isLocked) {
            str += '<span class="label label-lock">已锁定</span>'
        }
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
            str += " 回复 " + '<a target="_blank" style="color: '
            if (item.parent.isAdmin) {
                str += '#FFB800';
            } else {
                str += '#009688';
            }
            str += '" href="' + getFullUserFeedbackPath(productId, item.parent.user.uuid) + '">' + item.parent.user.username + '</a>：'
        }
        //回复详情——文字
        str += '<span class="feedback-content">' + item.content + '</span>'
        //回复详情——配图
        if (item.pictures !== null) {
            str += generateFlowItemPictures(item.uuid, JSON.parse(item.pictures))
        }
        str += '<div style="text-align: right;">' +
            '<div class="bottom-toolbar-hover-hide-item">'
        //主贴并且已登录并且才显示置顶等操作
        if (item.original === null) {
            console.log(item)
            let user = window.UTIL.getSessionStorageUser();
            if (user !== null && item.product.user.id === user.id) {
                str += '<i class="iconButton fa fa-arrow-circle-o-up layui-icon-top" type="top" pid="' + productId + '" fid="' + item.id + '"/>' +
                    '<i class="iconButton fa fa-lock fa-eye-slash" type="hide" pid="' + productId + '" fid="' + item.id + '"/>' +
                    '<i class="iconButton fa fa-lock layui-icon-top" type="lock" pid="' + productId + '" fid="' + item.id + '"/>' +
                    '<i class="iconButton fa fa-star-o" type="recommend" pid="' + productId + '" fid="' + item.id + '"/>'
            }
        }
        if (!properties.isReplies) {
            let originalIdTemp = properties.originalId;
            if (item.original !== null) {
                originalIdTemp = item.original.id
            }
            //反馈详情
            str += '<a target="_blank" href="' + getFullProductFeedbackRepliesPath(productId, originalIdTemp) + '"><i class="iconButton fa fa-file-text-o" type="detail" pid="' + productId + '" fid="' + item.id + '"/></a>';
        }
        //点赞次数
        str += '</div><i class="iconButton fa fa-thumbs-o-up" type="like" pid="' + productId + '" fid="' + item.id + '">';
        str += "<span style='font-size: 14px;margin-left: 3px;'>" + (item.likeCount === 0 ? '' : item.likeCount) + "</span>";
        str += '</i>';
        //回复按钮
        if ((!properties.isReplies || (item.original !== undefined && item.original !== null)) && !properties.isUserDetail) {
            //要判断是不是主贴，主贴不显示回复按钮
            let target = "_blank"
            let feedbackId = properties.originalId
            if (item.original !== undefined && item.original !== null) {
                target = "_self"
                feedbackId = item.original.id
            }
            str += '<a target="' + target + '" href="' + getFullProductFeedbackRepliesPath(productId, feedbackId) + '#reply" style="color: #333;"><i class="iconButton fa fa-reply" type="reply" pid="' + productId + '" fid="' + item.id + '" style="margin-left: 6px"/></a>';
        }
        str += '</div>' +
            '</div>';
        //回复
        if (item.replies !== undefined && item.replies !== null && !item.replies.empty) {
            str += '<ul>' + generateFlowReplyItem(item, properties) + '</ul>'
        }
        return str + '</div></li>';
    } else {
        let str = '<li><a target="_blank" style="color: ';
        if (item.isAdmin) {
            str += '#FFB800';
        } else {
            str += '#009688';
        }
        str += '" href="' + getFullUserFeedbackPath(productId, user.uuid) + '">' + user.username + '</a>';
        if (item.parent !== null) {
            str += " 回复 " + '<a target="_blank" style="color: '
            if (item.parent.isAdmin) {
                str += '#FFB800';
            } else {
                str += '#009688';
            }
            str += '" href="' + getFullUserFeedbackPath(productId, item.parent.user.uuid) + '">' + item.parent.user.username + '</a>'
        }
        str += '：<span class="feedback-content">' + item.content + '</span>' +
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
    return getUrlParamByUrl(name, window.location.search)
}

/**
 * 根据name获取url中的value
 * @param name key
 * @param url url
 * @returns {string|null} value
 */
function getUrlParamByUrl(name, url) {
    let reg = new RegExp("(^|&)" + decodeURIComponent(name) + "=([^&]*)(&|$)");
    let r = url.substr(1).match(reg);
    if (r != null) return decodeURIComponent(r[2]);
    return null;
}

/**
 * 获取自定义登录态参数
 * 优先返回url中设置的user
 * 如果已登录，但是访问其他非自己创建的，则不是admin，会在controller中进一步判断，如果已登录并且是产品的创建者，则自动isAdmin=true
 * @returns {{userAvatar: (String)|null, userUuid: (String), userName: {String}}|null}
 */
function getCustomUserInfo() {
    let allUrlParams = getAllUrlParams()
    if (allUrlParams.uid !== undefined && allUrlParams.uid !== null &&
        allUrlParams.uname !== undefined && allUrlParams.uname !== null) {
        return {
            userUuid: allUrlParams.uid,
            userName: allUrlParams.uname,
            userAvatar: allUrlParams.uavatar === undefined ? null : allUrlParams.uavatar
        }
    } else {
        let localUser = localStorage.getItem("user");
        if (localUser !== null) {
            let parse = JSON.parse(localUser);
            return {
                userUuid: parse.uid,
                userName: parse.uname,
                userAvatar: parse.uavatar
            }
        }
        return null
        // let user = getSessionStorageUser();
        // if (user !== null) {
        //     return {
        //         userUuid: user.uuid,
        //         userName: user.username,
        //         userAvatar: user.avatar
        //     }
        // }
    }
    // return null
}

/**
 * 获取session中保存的user
 * @returns {null|Object}
 */
function getSessionStorageUser() {
    let userString = sessionStorage.getItem("user");
    if (userString !== null) {
        return JSON.parse(userString)
    }
    return null
}

/**
 * 获取当前用户对象，如果是sessionStorage，则设置sessionFlag=true
 * @return {Object|null}
 */
function getUser() {
    let sessionStorageUser = getSessionStorageUser();
    if (sessionStorageUser !== null) {
        return {
            userUuid: sessionStorageUser.uuid,
            userName: sessionStorageUser.username,
            userAvatar: sessionStorageUser.avatar,
            sessionFlag: true
        }
    } else {
        let customUser = getCustomUserInfo()
        if (customUser !== null) {
            customUser.sessionFlag = false
        }
        return customUser
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
    // generateFlowReplyItem: generateFlowReplyItem,
    getFullProductFeedbackPath: getFullProductFeedbackPath,
    // getFullProductFeedbackRepliesPath: getFullProductFeedbackRepliesPath,
    getUrlParam: getUrlParam,
    getAllUrlParams: getAllUrlParams,
    getFullNewProductFeedbackPath: getFullNewProductFeedbackPath,
    getSessionStorageUser: getSessionStorageUser,
    getUser: getUser,
    getUrlParamByUrl: getUrlParamByUrl,
    generateFlowItemPictures: generateFlowItemPictures,
    getFullProductFeedbackRepliesPath: getFullProductFeedbackRepliesPath
};

export default {
    UTIL: UTIL
}

window.UTIL = UTIL