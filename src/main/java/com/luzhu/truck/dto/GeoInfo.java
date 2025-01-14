package com.luzhu.truck.dto;

import com.luzhu.truck.util.ApiRequestUtil;
import com.luzhu.truck.util.IpUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Data;

@Data
public class GeoInfo {

    // 時區
    private String timeZone;
    // 國家 ISO code
    private String isoCode;
    // IP
    private String ip;

    public static GeoInfo create(HttpServletRequest request) {
        GeoInfo geoInfo = new GeoInfo();
        geoInfo.setTimeZone(ApiRequestUtil.getTimeZone(request));
        geoInfo.setIsoCode(ApiRequestUtil.getHeaderLocation(request));
        geoInfo.setIp(IpUtil.getRemoteIp(request));
        return geoInfo;
    }

}
