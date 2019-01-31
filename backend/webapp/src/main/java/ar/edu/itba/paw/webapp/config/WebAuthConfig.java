package ar.edu.itba.paw.webapp.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableWebSecurity
@ComponentScan("ar.edu.itba.paw.webapp.auth")
public class WebAuthConfig extends WebSecurityConfigurerAdapter {

    @Value("classpath:rememberme.key")
    private Resource rememberMeKey;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private AuthenticationEntryPoint entryPoint;

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public HttpSessionRequestCache httpSessionRequestCache() {
        return new HttpSessionRequestCache();
    }

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http.userDetailsService(userDetailsService)
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().exceptionHandling()
                    .authenticationEntryPoint(entryPoint)
                .and().authorizeRequests()
                    .antMatchers("/login/**", "/register/**", "/forgotPassword/**", "/resetPassword/**").anonymous()
                    .antMatchers("/logout/**", "/sendMessage/**", "/Conversations/**",
                            "/Conversation/**", "/reserveClass/**", "/reservations/**", "/postComment/**", "/courseFiles/**",
                            "/downloadFile/**").authenticated()
                    .antMatchers("/registerAsProfessor/**").hasRole("USER")
                    .antMatchers("/createCourse/**", "/Profile/**", "/CreateTimeSlot/**",
                            "/editProfessorProfile/**", "/RemoveTimeSlot/**", "/deleteCourse/**", "/classRequests/**",
                            "/denyClassRequest/**", "/approveClassRequest/**", "/uploadFiles/**", "/deleteFile/**").hasRole("PROFESSOR")
                    .anyRequest().permitAll()
                .and().formLogin()
                    .successHandler(new SavedRequestAwareAuthenticationSuccessHandler())
                    .usernameParameter("username")
                    .passwordParameter("password")
                    .loginProcessingUrl("/login")
                .and().csrf().disable();
    }

    @Override
    public void configure(final WebSecurity web) throws Exception {
        web.ignoring()
                .antMatchers("/resources/css/**", "/resources/js/**", "/resources/images/**",
                        "/favicon.ico", "/403");
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder());
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    private String getRememberMeKey() {
        final StringWriter stringWriter = new StringWriter();
        try {
            Reader reader = new InputStreamReader(rememberMeKey.getInputStream());
            char[] data = new char[1024];
            int read;
            while ((read = reader.read(data)) != -1) {
                stringWriter.write(data,0, read);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return stringWriter.toString();
    }

}
