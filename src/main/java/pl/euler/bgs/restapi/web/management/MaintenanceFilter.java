package pl.euler.bgs.restapi.web.management;

import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import pl.euler.bgs.restapi.core.management.MaintenanceService;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class MaintenanceFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (!MaintenanceService.isMaintenanceModeEnabled() || isMaintenanceEndpoint(request)) {
            chain.doFilter(request, response);
        } else {
            HttpServletResponse resp = (HttpServletResponse) response;
            resp.sendError(HttpServletResponse.SC_SERVICE_UNAVAILABLE, "Maintenance mode!");
        }
    }

    private boolean isMaintenanceEndpoint(ServletRequest request) {
        HttpServletRequest req = (HttpServletRequest) request;
        return new AntPathRequestMatcher("/management/**").matches(req);
    }

    @Override
    public void destroy() { }
}
