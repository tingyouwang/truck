package com.luzhu.truck.util;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.util.StringUtils;

public class ApiRequestUtil {
    private ApiRequestUtil() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * 取得header time zone
     *
     * @param request
     * @return
     */
    public static String getTimeZone(HttpServletRequest request) {
        String timeZone = request.getHeader("Time-Zone");
        if (StringUtils.hasText(timeZone)) {
            return timeZone;
        } else {
            return "UTC";
        }
    }

    /**
     * 取得header country iso code
     *
     * @param request
     * @return
     */
    public static String getHeaderLocation(HttpServletRequest request) {
        // cloudflare
        String cloudflareHeader = request.getHeader("cf-ipcountry");
        // google
        String googleHeader = request.getHeader("X-Client-Geo-Location");
        if (StringUtils.hasLength(cloudflareHeader)) {
            return cloudflareHeader;
        } else if (StringUtils.hasLength(googleHeader)) {
            return googleHeader;
        } else {
            return "";
        }
    }
}
