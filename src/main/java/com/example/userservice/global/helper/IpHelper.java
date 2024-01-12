package com.example.userservice.global.helper;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;


@Slf4j
public class IpHelper {


    public static String getClientIpAddr(HttpServletRequest request) {

        String clientIpAddr = request.getHeader("X-FORWARDED-FOR");

        if (clientIpAddr != null && !clientIpAddr.isEmpty()) {
            String[] ipAddresses = clientIpAddr.split(",");
            clientIpAddr = ipAddresses[0].trim();
        }

        Enumeration<String> headerNames = request.getHeaderNames();

        if (headerNames != null) {
            while (headerNames.hasMoreElements()) {
                String name = headerNames.nextElement();
                String value = request.getHeader(name);
                log.info("Header ::: " + name + " : " + value);
            }
        }

        return clientIpAddr;
    }

}
