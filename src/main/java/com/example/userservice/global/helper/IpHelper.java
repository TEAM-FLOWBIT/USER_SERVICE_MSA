package com.example.userservice.global.helper;

import javax.servlet.http.HttpServletRequest;

public class IpHelper {


    public static String getClientIpAddr(HttpServletRequest request) {
        String ip =null;

        String clientIpAddr = request.getHeader("X-FORWARDED-FOR");

        if (clientIpAddr == null || clientIpAddr.isEmpty()) {
            // If the header is not present, fall back to the default way of obtaining the client's IP address
            ip = request.getRemoteAddr();
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        return ip;
    }

}
