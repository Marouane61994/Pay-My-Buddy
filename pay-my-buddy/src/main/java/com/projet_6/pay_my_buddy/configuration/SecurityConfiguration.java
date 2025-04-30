package com.projet_6.pay_my_buddy.configuration;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityConfiguration {

    /*
    @Bean
      public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
       http
             .authorizeHttpRequests(auth -> auth
                     .requestMatchers("/users/register", "/users/login", "/css/**", "/js/**").permitAll()
                     .anyRequest().authenticated()
            )
             .formLogin(form -> form
                 .loginPage("/users/login")
                 .loginProcessingUrl("/users/login")
                  .usernameParameter("email")
                  .passwordParameter("password")
                  .defaultSuccessUrl("/users/profile", true)
                 .failureUrl("/users/login?error=true")
                  .permitAll()
          )
           .logout(logout -> logout
                   .logoutUrl("/users/logout")
               .logoutSuccessUrl("/users/login?logout=true")
                 .invalidateHttpSession(true)
                 .permitAll()
        );
      return http.build();
     }
    */

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}




