package com.example.springfilter.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.PatternMatchUtils;

import java.io.IOException;

@Slf4j
public class LoginFilter implements Filter {

    private static final String[] WHITE_LIST = { "/", "/user/signup", "/login", "/logout" };

    @Override
    public void doFilter(
            ServletRequest request,
            ServletResponse response,
            FilterChain chain
    ) throws IOException, ServletException {

        // 기능이 많은 `HttpServletRequest`로 다운캐스팅
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String requestURI = httpRequest.getRequestURI();

        HttpServletResponse httpResponse = (HttpServletResponse) response;

        log.info("로그인 필터 로직 실행");

        if (false == isWhiteList(requestURI)) {

            HttpSession session = httpRequest.getSession(false);

            if (session == null || session.getAttribute("sessionKey") == null) {
                throw new RuntimeException("로그인 해주세요.");
            }

            // 로그인 성공 로직
            log.info("로그인에 성공했습니다.");
        }

        // 1번째 경우 : WHITE LIST에 등록된 URL 요청이라면 chain.doFilter() 호출
        // 2번째 경우 : WHITE LIST가 아닌 경우 위 필터 로직을 통과 후에 chain.doFilter() 다음 Filter나 Servlet을 호출한다.
        chain.doFilter(httpRequest, httpResponse);
    }

    private boolean isWhiteList(String requestURI) {
        return PatternMatchUtils.simpleMatch(WHITE_LIST, requestURI);
    }

}
