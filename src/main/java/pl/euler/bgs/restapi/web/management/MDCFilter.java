package pl.euler.bgs.restapi.web.management;

import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Filter which add the IP client address for logback logs.
 */
@Component
public class MDCFilter implements Filter {

    private static final String CLIENT_IP_PARAM = "client-ip";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {
            MDC.put(CLIENT_IP_PARAM, getIpAddress((HttpServletRequest) request));
            chain.doFilter(request, response);
        } finally {
            MDC.remove(CLIENT_IP_PARAM);
        }
    }

    private String getIpAddress(HttpServletRequest request) {
        String ipAddress = request.getHeader("X-FORWARDED-FOR");
        if (ipAddress == null) {
            ipAddress = request.getRemoteAddr();
        }
        return ipAddress;
    }

    @Override
    public void destroy() {

    }
}
