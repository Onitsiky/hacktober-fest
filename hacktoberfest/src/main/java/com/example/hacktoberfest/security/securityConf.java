package com.example.hacktoberfest.security;

import com.example.hacktoberfest.filters.JwtRequestFilter;
import com.example.hacktoberfest.service.UserService;
import com.example.hacktoberfest.utils.JWTUtils;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@EnableWebSecurity
@Configuration
@AllArgsConstructor
public class securityConf extends WebSecurityConfigurerAdapter {
  @Bean
  public BCryptPasswordEncoder getPasswordEncoder(){return  new BCryptPasswordEncoder();}
  private final UserService userDetailsService;

  @Autowired
  private JwtRequestFilter jwtRequestFilter;

  @Override
  @Bean
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }

  @Bean
  public DaoAuthenticationProvider authenticationProvider(){
    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
    authProvider.setUserDetailsService(userDetailsService);
    authProvider.setPasswordEncoder(getPasswordEncoder());
    return authProvider;
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
      auth.authenticationProvider(authenticationProvider());
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
      http
          .authorizeRequests()
          .antMatchers(HttpMethod.GET, "/ping").authenticated()
          .antMatchers(HttpMethod.POST, "/authorize").permitAll()
          .antMatchers(HttpMethod.PUT, "/authorize").permitAll()
          .and()
          .formLogin()
          .and()
          .logout().permitAll()
          .and()
          .csrf().disable()
          .httpBasic()
          .and()
          .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
      http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
  }
}
