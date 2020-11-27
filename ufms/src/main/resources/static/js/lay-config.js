/**
 * date:2019/08/16
 * author:Mr.Chung
 * description:此处放layui自定义扩展
 * version:2.0.4
 */
window.rootPath = (function (src) {
    src = document.scripts[document.scripts.length - 1].src;
    return src.substring(0, src.lastIndexOf("/") + 1);
})();

layui.config({
    base: rootPath + "lay-module/",
    version: true
}).extend({
    miniAdmin: "layuimini/miniAdmin", // layuimini后台扩展
    miniMenu: "layuimini/miniMenu", // layuimini菜单扩展
    miniTab: "layuimini/miniTab", // layuimini tab扩展
    miniTheme: "layuimini/miniTheme", // layuimini 主题扩展
    miniTongji: "layuimini/miniTongji", // layuimini 统计扩展
    step: 'step-lay/step', // 分步表单扩展
    treetable: 'treetable-lay/treetable', //table树形扩展
    tableSelect: 'tableSelect/tableSelect', // table选择扩展
    iconPickerFa: 'iconPicker/iconPickerFa', // fa图标选择扩展
    echarts: 'echarts/echarts', // echarts图表扩展
    echartsTheme: 'echarts/echartsTheme', // echarts图表主题扩展
    wangEditor: 'wangEditor/wangEditor', // wangEditor富文本扩展
    layarea: 'layarea/layarea', //  省市县区三级联动下拉选择器
}).define(['jquery', 'layer'], function () {
    let $ = layui.jquery,
        layer = layui.layer;

    //加载中对话框index，用于close
    let loadIndex;
    $.ajaxSetup({
        beforeSend: () => {
            loadIndex = layer.load();
        },
        dataFilter: function (data, type) {
            let response = JSON.parse(data);
            console.log("dataFilter", response, type)
            // if (type === "json") {
            if (response.success) {
                return data
            } else {
                //排除menuInfo.json
                if (response.code !== undefined) {
                    //登录过期，则跳转到登录页面
                    if (response.code === 2001) {
                        sessionStorage.removeItem("user")
                        layer.alert("登录过期，将跳转到登录页面", function () {
                            parent.location.href = '/login.html';
                        })
                    } else {
                        layer.msg(response.msg)
                        // return data
                    }
                } else {
                    return data
                }
            }
            // }
            // return data
        },
        complete: () => {
            layer.close(loadIndex)
            // console.log("complete", xhr, status)
        }
    });
});