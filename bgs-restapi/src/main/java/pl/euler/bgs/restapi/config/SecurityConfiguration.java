package pl.euler.bgs.restapi.config;

import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.security.web.access.expression.WebSecurityExpressionRoot;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.http.HttpServletRequest;

@Configuration
//    @ConditionalOnProperty(prefix = "security.basic", name = "enabled", havingValue = "false")
//    @Order(SecurityProperties.BASIC_AUTH_ORDER)
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

//
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http.requestMatcher(new RequestMatcher() {
//            @Override
//            public boolean matches(HttpServletRequest request) {
//                return false;
//            }
//        });
//    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
//        http.anonymous().disable();
//        http.anonymous();

//        hasIpAddress("127.0.0.1");
        http.authorizeRequests().antMatchers("/proxy/**").access("hasIpAddress('0:0:0:0:0:0:0:1') OR hasIpAddress('127.0.0.1')");
//        http.authorizeRequests().antMatchers("/css/**").permitAll().anyRequest()
//                .fullyAuthenticated().and().formLogin().loginPage("/login")
//                .failureUrl("/login?error").permitAll().and().logout().permitAll();
//
//        http.authorizeRequests().expressionHandler(new WebSecurityExpressionRoot().hasIpAddress(""))
//
//        http.authorizeRequests().expressionHandler()
    }

//    @Override
//    public void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.inMemoryAuthentication().withUser("user").password("user").roles("USER");
//    }

}
