package com.huifengzhao.utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * @author huifengzhao
 * @ClassName RequestUtil
 * @date 2018/8/28
 */
public final class RequestUtil {
    public RequestUtil() {
    }

    public static Map<String, String> convertUrlMap(String url) throws UnsupportedEncodingException {
        if (StringUtil.isEmpty(url)) {
            return null;
        } else {
            Map<String, String> mapRequest = new HashMap<>(0);
            url = url.trim().toLowerCase();
            String[] arrSplit = url.split("[?]");
            if (arrSplit.length <= 1) {
                return mapRequest;
            } else {
                String allParam = arrSplit[1];
                if (StringUtil.isEmpty(allParam)) {
                    return mapRequest;
                } else {
                    arrSplit = allParam.split("[&]");
                    String[] var4 = arrSplit;
                    int var5 = arrSplit.length;

                    for (int var6 = 0; var6 < var5; ++var6) {
                        String strSplit = var4[var6];
                        String[] arrSplitEqual = strSplit.split("[=]");
                        if (arrSplitEqual.length > 1) {
                            mapRequest.put(arrSplitEqual[0], URLDecoder.decode(arrSplitEqual[1], "UTF-8"));
                        } else if (!"".equals(arrSplitEqual[0])) {
                            mapRequest.put(arrSplitEqual[0], "");
                        }
                    }

                    return mapRequest;
                }
            }
        }
    }

    public static Cookie getCookieByName(HttpServletRequest request, String name) {
        Map<String, Cookie> cookieMap = readCookieMap(request);
        return cookieMap.getOrDefault(name, null);
    }

    private static Map<String, Cookie> readCookieMap(HttpServletRequest request) {
        Map<String, Cookie> cookieMap = new HashMap<>(0);
        Cookie[] cookies = request.getCookies();
        if (null != cookies) {
            for (Cookie cookie : cookies) {
                cookieMap.put(cookie.getName(), cookie);
            }
        }

        return cookieMap;
    }

    public static Map<String, String> getHeaderMapValues(HttpServletRequest request) {
        Enumeration<String> enumeration = request.getHeaderNames();
        Map<String, String> headerMapValues = new HashMap<>(0);
        if (enumeration != null) {
            while (enumeration.hasMoreElements()) {
                String name = enumeration.nextElement();
                headerMapValues.put(name, request.getHeader(name));
            }
        }

        return headerMapValues;
    }
}
