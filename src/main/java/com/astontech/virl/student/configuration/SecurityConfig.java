package com.astontech.virl.student.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.ldap.authentication.ad.ActiveDirectoryLdapAuthenticationProvider;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    // region FIELDS / PROPERTIES

    @Value("${spring.security.authentication.method}")
    private String authenticationMethod;

    @Value("${spring.security.ldap.domain}")
    private String ldapDomain;

    @Value("${spring.security.ldap.url}")
    private String ldapUrl;

    CustomSuccessHandler customSuccessHandler;

    public SecurityConfig(CustomSuccessHandler customSuccessHandler) {
        this.customSuccessHandler = customSuccessHandler;
    }

    // endregion

    // region BASE CONFIGURATION

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception  {

        if (authenticationMethod.equals("IN_MEMORY")) {

            auth.inMemoryAuthentication()
                    .withUser("test_admin").password("123").roles("MENTOR")
                    .and().withUser("test_mentee").password("123").roles("MENTEE");

        } else if (authenticationMethod.equals("LDAP")) {

            auth.authenticationProvider(activeDirectoryLdapAuthenticationProvider());

        }

    }

    @Bean
    public ActiveDirectoryLdapAuthenticationProvider activeDirectoryLdapAuthenticationProvider() {
        ActiveDirectoryLdapAuthenticationProvider authenticationProvider =
                new ActiveDirectoryLdapAuthenticationProvider(ldapDomain, ldapUrl);

        authenticationProvider.setConvertSubErrorCodesToExceptions(true);
        authenticationProvider.setUseAuthenticationRequestCredentials(true);

        return authenticationProvider;
    }

    // endregion

    // region ACCESS CONTROL

    protected void configure(HttpSecurity http) throws Exception {

        if (authenticationMethod.equals("NONE")) {

            http
                    .authorizeRequests()
                    .antMatchers("/").permitAll();

        } else if (authenticationMethod.equals("IN_MEMORY")) {

            //todo: will implement later...

        } else if (authenticationMethod.equals("LDAP")) {

            http
                    .authorizeRequests()
                    .anyRequest().authenticated()
                    .and()
                    .formLogin()
                    .loginPage("/login")
                    .successHandler(customSuccessHandler)
                    .permitAll()
                    .and()
                    .logout().logoutSuccessUrl("/")
                    .and().csrf().disable();
        }
    }

    // endregion

}
