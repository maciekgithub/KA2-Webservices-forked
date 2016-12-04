package pl.euler.bgs.restapi.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;

@Configuration
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfiguration {

    @Configuration
    @Order(Ordered.HIGHEST_PRECEDENCE)
    protected static class AuthenticationSecurity extends GlobalAuthenticationConfigurerAdapter {

        private final SecurityProperties securityProperties;

        @Autowired
        AuthenticationSecurity(SecurityProperties securityProperties) {
            this.securityProperties = securityProperties;
        }

        @Override
        public void init(AuthenticationManagerBuilder auth) throws Exception {
            auth.inMemoryAuthentication()
                    .withUser(this.securityProperties.getUser().getName())
                    .password(this.securityProperties.getUser().getPassword())
                    .roles(this.securityProperties.getUser().getRole().toArray(new String[0]));
        }
    }

    @Configuration
    @Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
    protected static class ApplicationSecurity extends WebSecurityConfigurerAdapter {

        private SecurityProperties security;

        @Autowired
        protected ApplicationSecurity(SecurityProperties security) {
            this.security = security;
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.csrf().disable();

            AuthenticationEntryPoint entryPoint = entryPoint();
            http
                .exceptionHandling()
                .authenticationEntryPoint(entryPoint);
            http
                .antMatcher("/management/**")
                .httpBasic()
                .authenticationEntryPoint(entryPoint);
            http.authorizeRequests()
                .antMatchers(HttpMethod.GET, "/management/maintenance", "/management/info", "/management/health").permitAll()
                .and()
                .authorizeRequests().antMatchers("/management/**").authenticated();
        }

        private AuthenticationEntryPoint entryPoint() {
            BasicAuthenticationEntryPoint entryPoint = new BasicAuthenticationEntryPoint();
            entryPoint.setRealmName(this.security.getBasic().getRealm());
            return entryPoint;
        }

    }

}
