package pl.euler.bgs.restapi.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Configuration
    @Order(Ordered.HIGHEST_PRECEDENCE)
    protected static class AuthenticationSecurity extends GlobalAuthenticationConfigurerAdapter {

    @Autowired
    SecurityProperties securityProperties;

        @Override
        public void init(AuthenticationManagerBuilder auth) throws Exception {
            auth.inMemoryAuthentication()
                    .withUser(securityProperties.username)
                    .password(securityProperties.password)
                    .roles("ADMIN");
        }
    }

    @Configuration
    protected static class ApplicationSecurity extends WebSecurityConfigurerAdapter {

        @Value("${security.basic.realm}")
        String realm;

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
            entryPoint.setRealmName(realm);
            return entryPoint;
        }

    }

}
