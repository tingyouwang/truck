package com.luzhu.truck.util;

import com.luzhu.truck.exception.WebRuntimeException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class IpUtil {
    public static String getRemoteIp(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip != null) {
            return ip.replace(" ", "").split(",")[0];
        } else {
            switch (request.getRemoteAddr()) {
                case "localhost":
                case "127.0.0.1":
                case "0:0:0:0:0:0:0:1":
                    return request.getRemoteAddr();
                default:
                    log.error("[IP]取得失敗");
                    throw WebRuntimeException.systemError();
            }
        }
    }
}
