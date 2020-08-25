package com.bramgussekloo.projects.config;

import com.bramgussekloo.projects.utils.GetPropertyValues;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private DataSource dataSource;

    public static String HashUserPassword(String password) {
        return encoder().encode(password);
    }

    @Bean
    public static javax.sql.DataSource createDataSource() {
        String propFileName = "Database_config.properties";
        String[] values = GetPropertyValues.getDatabasePropValues(propFileName);
        org.postgresql.ds.PGPoolingDataSource ds = new org.postgresql.ds.PGPoolingDataSource();
        ds.setDataSourceName("A Data Source");
        ds.setServerName(values[3]);
        ds.setDatabaseName(values[4]);
        ds.setUser(values[1]);
        ds.setPassword(values[2]);
        ds.setPortNumber(5432);
        return ds;
    }

    @Bean
    public static PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    protected void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication().dataSource(dataSource)
                .usersByUsernameQuery("SELECT user_name, password, enabled"
                        + " FROM users WHERE user_name=?;")
                .authoritiesByUsernameQuery("SELECT user_name, authority "
                        + "FROM users WHERE user_name=?")
                .passwordEncoder(new BCryptPasswordEncoder());
    }

    @Configuration
    @Order(1)
    public static class ApiWebSecurityConfigurationAdapter extends WebSecurityConfigurerAdapter {
        @Autowired
        private AuthenticationEntryPoint authEntryPoint;

        protected void configure(HttpSecurity http) throws Exception {
            http
                    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                    .csrf().disable()
                    .antMatcher("/api/admin/**")
                    .authorizeRequests().anyRequest()
                    .hasAnyRole("USER", "ADMIN")
                    .and()
                    .httpBasic().authenticationEntryPoint(authEntryPoint);
        }
    }

    @Configuration
    @Order(2)
    public static class UserStatementsSecurity extends WebSecurityConfigurerAdapter {
        @Autowired
        private AuthenticationEntryPoint authEntryPoint;

        protected void configure(HttpSecurity http) throws Exception {
            http
                    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                    .csrf().disable().antMatcher("/api/users")
                    .authorizeRequests().anyRequest()
                    .hasRole("ADMIN")
                    .and().httpBasic()
                    .authenticationEntryPoint(authEntryPoint);
        }
    }
    @Configuration
    @Order(3)
    public static class UserStatementsSecurit extends WebSecurityConfigurerAdapter {
        @Autowired
        private AuthenticationEntryPoint authEntryPoint;

        protected void configure(HttpSecurity http) throws Exception {
            http
                    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                    .csrf().disable().antMatcher("/api/users/**")
                    .authorizeRequests().anyRequest()
                    .hasRole("ADMIN")
                    .and().httpBasic()
                    .authenticationEntryPoint(authEntryPoint);
        }
    }

    @Order(4)
    @Configuration
    public static class FormLoginWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.cors().and().anonymous();
        }
    }

}
