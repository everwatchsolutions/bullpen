/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.acesinc.ats.web.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 *
 * @author andrewserff
 */
@Configuration
@ComponentScan
@PropertySource({"classpath:${spring.profiles.active}/application.properties"})
@EnableWebSecurity(debug = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private static final Logger log = LoggerFactory.getLogger(SecurityConfig.class);

//    @Value("${security.Access-Control-Allow-Origin}")
//    private String accessControlAllowOrigin;
    @Autowired
    private UserDetailsService loginService;
    @Value("${secutiry.embed.whitelist}")
    private String embedWhitelist;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth)
            throws Exception {
        auth.userDetailsService(loginService).passwordEncoder(encoder());
    }

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
//        http.requiresChannel()
//            .anyRequest()
//            .requiresSecure();

        // TODO: this should be dynamic and not hard-coded.  The url, that is.
//        if (accessControlAllowOrigin == null) {
//            accessControlAllowOrigin = "http://localhost:8080";
//        }
//        http.headers().addHeaderWriter(new StaticHeadersWriter("Access-Control-Allow-Origin", accessControlAllowOrigin));
//        http.headers().addHeaderWriter(new StaticHeadersWriter("Access-Control-Allow-Credentials", "true"));
        
        //to allow our pages to be embedded into other sites, we have to set a whitelist of domains that are allowed. 
//        String[] sites = embedWhitelist.split(",");
//        log.debug("Allowing XFrameOption from : " + Arrays.toString(sites));
//        http.headers().addHeaderWriter(new XFrameOptionsHeaderWriter(new WhiteListedAllowFromStrategy(Arrays.asList(sites))));
        
        //this fixes a bug that causes Firefox to not render the content of our openings when embeded in an iFrame.
        //This is because by default, the X-Frame-Options are set to DENY.  So unless we want to 
        //specify a list of all our clients, we have to disable this.  This could cause other problems later if we need headers...
        //TODO I can't tell if this disabled CSRF requests or not...need to test more!
        http.headers().disable();
        
        http.csrf().ignoringAntMatchers("/notification/**");
        http.csrf().ignoringAntMatchers("/embed/**/apply");
        http.csrf().ignoringAntMatchers("/view/**/apply");
        
        http.authorizeRequests()
                .antMatchers("/").permitAll()
                .antMatchers("/index").permitAll()
                .antMatchers("/static/**").permitAll()
                .antMatchers("/assets/**").permitAll()
                .antMatchers("/register").permitAll()
                .antMatchers("/password/reset").permitAll()
                .antMatchers("/password/reset/complete").permitAll()
                .antMatchers("/email/verify").permitAll()
                .antMatchers("/view/**").permitAll()
                .antMatchers("/embed/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .defaultSuccessUrl("/dashboard", false) //Force user to always go to the home page.  
//                .successHandler(successHandler())
                .permitAll()
                .and()
                .httpBasic()
                .and()
                .logout()
                .permitAll();
        
//        http.addFilterBefore(new MultipartFilter(), ChannelProcessingFilter.class);

    }

//    @Bean
//    public SavedRequestAwareAuthenticationSuccessHandler successHandler() {
//        SavedRequestAwareAuthenticationSuccessHandler successHandler = new SavedRequestAwareAuthenticationSuccessHandler();
//        successHandler.setTargetUrlParameter("/dashboard");
//        return successHandler;
//    }
}
