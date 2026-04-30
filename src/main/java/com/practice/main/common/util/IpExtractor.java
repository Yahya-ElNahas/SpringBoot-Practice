package com.practice.main.common.util;

import jakarta.servlet.http.HttpServletRequest;

public abstract class IpExtractor {

    public static String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if(ip != null && !ip.isBlank() && !ip.equalsIgnoreCase("unknown")) {
            return ip.split(",")[0].trim();
        }

        ip = request.getHeader("X-Real-IP");
        if(ip != null && !ip.isBlank() && !ip.equalsIgnoreCase("unknown")) {
            return ip;
        }

        return request.getRemoteAddr();
    }
}
