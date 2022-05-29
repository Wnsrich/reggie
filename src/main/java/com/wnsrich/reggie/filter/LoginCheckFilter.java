package com.wnsrich.reggie.filter;

import com.alibaba.fastjson.JSON;
import com.wnsrich.reggie.common.BaseContext;
import com.wnsrich.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 全局过滤器
 */
@WebFilter(filterName = "loginCheckFilter", urlPatterns = "/*")
@Slf4j
public class LoginCheckFilter implements Filter {
    // 路径匹配器, 支持通配符
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        // 强转
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        // 获取本次请求的url
        String requestUrl = request.getRequestURI();

//        log.info("获取的url=>>"+requestUrl);
        // 定义不需要处理的请求路径
        String[] urls = new String[]{
                "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/front/**",
                "/common/**",
                "/user/login",
                "/user/sendMsg"
        };

        // 判断是否合法
        boolean check = checkUrl(urls, requestUrl);
//        log.info("check"+check);
        // 判断是否已经登录的url合法性，选择放行
        if (check || request.getSession().getAttribute("employee")!=null) {
            BaseContext.setThreadId((Long) request.getSession().getAttribute("employee"));
            filterChain.doFilter(request,response);
//            log.info("放行");
            return ;
        }

        if (check || request.getSession().getAttribute("user")!=null) {
            BaseContext.setThreadId((Long) request.getSession().getAttribute("user"));
            filterChain.doFilter(request,response);
//            log.info("放行");
            return ;
        }

        // 如果没有登录则返回错误信息
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
        return;



    }
    private boolean checkUrl(String[] urls,String requestUrl){
        for (String url : urls) {
            // 利用工具类来匹配url
            if (PATH_MATCHER.match(url,requestUrl)) return true;
        }
        return false;
    }
}
