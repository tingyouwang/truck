package com.luzhu.truck.filter;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.luzhu.truck.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

//public class JwtFilter extends GenericFilterBean {
public class JwtFilter extends OncePerRequestFilter {

//    @Override
//    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
//        HttpServletRequest req = (HttpServletRequest) servletRequest;
//        String jwtToken = req.getHeader("authorization");
//        String token = jwtToken.replace("Bearer ", "");
//        DecodedJWT decodedJWT = JwtUtil.verify(token);
//       boolean expire = decodedJWT.getExpiresAtAsInstant().isBefore(Instant.ofEpochSecond(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC)));
//
//
//        List<GrantedAuthority> authorities = new ArrayList<>();
//        UsernamePasswordAuthenticationToken token1 = new UsernamePasswordAuthenticationToken("", "", authorities);
//        SecurityContextHolder.getContext().setAuthentication(token1);
//
//        filterChain.doFilter(servletRequest, servletResponse);
//    }

    // 定義公開路徑
    private static final List<String> PUBLIC_PATHS = List.of("/user/register", "/user/login", "/authentication/login");

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String requestPath = request.getRequestURI();

        // 跳過公開路徑
        if (PUBLIC_PATHS.contains(requestPath)) {
            filterChain.doFilter(request, response);
            return;
        }

        // 獲取 Authorization 標頭
        String jwtToken = request.getHeader("Authorization");
        if (jwtToken == null || !jwtToken.startsWith("Bearer ")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Missing or invalid Authorization header");
            return;
        }

        try {
            // 移除 "Bearer " 前綴
            String token = jwtToken.replace("Bearer ", "");

            // 驗證 JWT
            DecodedJWT decodedJWT = JwtUtil.verify(token);

            // 檢查是否過期
            boolean expired = decodedJWT.getExpiresAtAsInstant().isBefore(Instant.now());
            if (expired) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Token has expired");
                return;
            }

            // 設置用戶身份到安全上下文
            // todo 未來可在這加入role 管理
            List<GrantedAuthority> authorities = new ArrayList<>(); // 根據需要填充角色
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(decodedJWT.getSubject(), null, authorities);
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);

            // 繼續執行過濾鏈
            filterChain.doFilter(request, response);

        } catch (Exception e) {
            // 處理 JWT 驗證異常
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Invalid token: " + e.getMessage());
        }
    }
}
