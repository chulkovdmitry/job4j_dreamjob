package ru.job4j.dream.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CORSFilter implements Filter {
    @Override
    public void doFilter(ServletRequest sreq, ServletResponse sres, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) sreq;
        ((HttpServletResponse) sres).addHeader("Access-Control-Allow-Origin", "*");
        ((HttpServletResponse) sres).addHeader(
                "Access-Control-Allow-Methods",
                "GET, OPTIONS, HEAD, PUT, POST"
        );
        HttpServletResponse res = (HttpServletResponse) sres;
        if (request.getMethod().equals("OPTIONS")) {
            res.setStatus(HttpServletResponse.SC_ACCEPTED);
            return;
        }
        chain.doFilter(request, sres);
    }
}
