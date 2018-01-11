//cookie工具类-单体对象
var Cookie = {};
// expires不传入:则设置cookie：关闭浏览器就清除cookie，路径为/，当前域名所有路径都可以访问
Cookie.setCookie = function (name, value, expires) {
//	var exp = new Date();
//	exp.setTime(exp.getTime() + 1 * 24 * 60 * 60 * 1000);
//	exp = exp.toGMTString();
    var exp;
    if (expires) {
        exp = "expires=" + expires;
    }
    document.cookie = name + "=" + value + ";path=/;" + exp;
}
// 获取指定名的cookie值
Cookie.getCookie = function (name) {
    // (^|)cart=\"?([^\";]*)\"?(;|$)
    // cart=[{"pid":1,"image":"nan_yifu_01.jpg","shopPrice":188,"count":1},{"pid":1,"image":"nan_yifu_01.jpg","shopPrice":188,"count":1}]
    var arr, reg = new RegExp("(^|)" + name + "=\"?([^\";]*)\"?(;|$)")
    if (arr = document.cookie.match(reg)) {
        //取出值-去除前后的":val:可能是xxx或"xxx或xxx"
        //var val=arr[2].substring(1,arr[2].length-1);
        //var val=arr[2].substring(1,arr[2].length);
        var val = arr[2];
        return val;
    } else {
        return null;
    }
}
//删除cookie:设置cookie立即过期
Cookie.delCookie = function (name) {
    var exp = new Date();
    exp.setTime(exp.getTime() - 1);
    var cval = Cookie.getCookie(name);
    if (cval != null) {
        document.cookie = name + "=" + cval + ";path=/;expires=" + exp.toGMTString();
    }
}