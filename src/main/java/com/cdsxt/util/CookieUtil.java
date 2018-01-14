package com.cdsxt.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

public class CookieUtil {


    public static String getCookieValue(HttpServletRequest req, String cookieName) {

        Cookie[] cookieArr = req.getCookies();
        //cookie的值
        String cookieVal = null;
        for (Cookie cookie : cookieArr) {
            if (cookieName.equals(cookie.getName())) {
                cookieVal = cookie.getValue();
                return cookieVal;
            }
        }
        //找不到对应值返回null
        return null;
    }
}
