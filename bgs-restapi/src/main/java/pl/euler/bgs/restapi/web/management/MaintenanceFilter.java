package pl.euler.bgs.restapi.web.management;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;
import pl.euler.bgs.restapi.core.management.MaintenanceService;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class MaintenanceFilter implements Filter {
    private final MaintenanceService maintenanceService;

    @Autowired
    public MaintenanceFilter(MaintenanceService maintenanceService) {
        this.maintenanceService = maintenanceService;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (!maintenanceService.isMaintenanceModeEnabled() || isActuatorOrMaintenanceEndpoint(request)) {
            chain.doFilter(request, response);
        } else {
            HttpServletResponse resp = (HttpServletResponse) response;
            resp.sendError(HttpServletResponse.SC_SERVICE_UNAVAILABLE, "Maintenance mode!");
        }
    }

    private boolean isActuatorOrMaintenanceEndpoint(ServletRequest request) {
        HttpServletRequest req = (HttpServletRequest) request;
        return new AntPathRequestMatcher("/management/**").matches(req);
    }

    @Override
    public void destroy() {

    }
}
