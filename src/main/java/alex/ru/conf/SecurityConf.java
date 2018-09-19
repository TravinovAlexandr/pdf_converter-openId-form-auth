package alex.ru.conf;

import alex.ru.filters.OpenIdConnectFilter;
import alex.ru.conf.sub.auth.FormAuthProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.filter.OAuth2ClientContextFilter;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;

@Configuration @EnableWebSecurity @EnableOAuth2Sso
public class SecurityConf extends WebSecurityConfigurerAdapter {

    private OAuth2RestTemplate restTemplate;

    private FormAuthProvider formAuthProvider;

    private FilterRegistrationBean stopServerFilterRegistrationBean;

    @Autowired
    public SecurityConf(final OAuth2RestTemplate restTemplate,
                        final FormAuthProvider formAuthProvider,
                        final FilterRegistrationBean stopServerFilterRegistrationBean) {
        this.restTemplate = restTemplate;
        this.formAuthProvider = formAuthProvider;
        this.stopServerFilterRegistrationBean = stopServerFilterRegistrationBean;
    }

    @Override
    public void configure(final WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/resources/**");
    }

    @Bean
    public OpenIdConnectFilter openIdConnectFilter() {
        final OpenIdConnectFilter filter = new OpenIdConnectFilter("/google-login");
        filter.setRestTemplate(restTemplate);
        return filter;
    }

    @Override
    protected void configure(final HttpSecurity http) throws Exception {

            http.addFilterBefore(stopServerFilterRegistrationBean.getFilter(), AbstractPreAuthenticatedProcessingFilter.class)
                .addFilterAfter(new OAuth2ClientContextFilter(), AbstractPreAuthenticatedProcessingFilter.class)
                .addFilterAfter(openIdConnectFilter(), OAuth2ClientContextFilter.class)
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                .and()
                .httpBasic()
                .authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/google-login"))
                .and()
                .csrf().disable()
                .antMatcher("/**")
                .authorizeRequests()
                .antMatchers("/",
                        "/registration",
                        "/checkStatus",
                        "/logOut",
                        "/log",
                        "/formLogin",
                        "/account")
                .permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .logout().disable();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(8);
    }

    @Override @Bean(name = BeanIds.AUTHENTICATION_MANAGER)
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Autowired
    public void configure(final AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(formAuthProvider);
    }

}
