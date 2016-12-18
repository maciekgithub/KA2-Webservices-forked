package pl.euler.bgs.restapi.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SecurityProperties {

    @Value("${security.user.name}")
    public String username;

    @Value("${security.user.password}")
    public String password;

}
