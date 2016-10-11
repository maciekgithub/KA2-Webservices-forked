package pl.euler.bgs.restapi.web.maintenance;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.endpoint.mvc.MvcEndpoint;
import org.springframework.boot.actuate.endpoint.mvc.MvcEndpoints;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class MaintenanceFilter implements Filter {
    private final MvcEndpoints mvcEndpoints;
    private final MaintenanceService maintenanceService;

    @Autowired
    public MaintenanceFilter(MvcEndpoints mvcEndpoints, MaintenanceService maintenanceService) {
        this.mvcEndpoints = mvcEndpoints;
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
        String requestUrl = req.getRequestURL().toString();

        Set<String> paths = mvcEndpoints.getEndpoints().stream() //access to actuator endpoints
                .map(MvcEndpoint::getPath)
                .collect(Collectors.toSet());
        paths.add("/maintenance"); // access to maintenance endpoint

        CharSequence[] pathsArray = paths.stream().toArray(String[]::new);
        return StringUtils.endsWithAny(requestUrl, pathsArray);
    }

    @Override
    public void destroy() {

    }
}
