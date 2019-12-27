package com.bramgussekloo.projects.Security;

import com.bramgussekloo.projects.Properties.GetPropertyValues;
import com.bramgussekloo.projects.database.DatabaseConnection;
import com.bramgussekloo.projects.dataclasses.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    public static User HashUserPassword(User user) {
        User newUser = new User();
        newUser.setId(user.getId());
        newUser.setAuthority(user.getAuthority());
        newUser.setUser_name(user.getUser_name());
        newUser.setEnabled(user.getEnabled());
        String newPassword = encoder().encode(user.getPassword());
        System.out.println(newPassword);
        newUser.setPassword(newPassword);
        return newUser;
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
    private DataSource dataSource;

    @Autowired
    protected void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication().dataSource(dataSource)
                .usersByUsernameQuery("SELECT user_name, password, enabled"
                        + " FROM users WHERE user_name=?;")
                .authoritiesByUsernameQuery("SELECT user_name, authority "
                        + "FROM users WHERE user_name=?")
                .passwordEncoder(new BCryptPasswordEncoder());
    }


//        auth.inMemoryAuthentication()
//                .withUser("admin").password(encoder().encode("adminPass")).roles("ADMIN")
//                .and()
//                .withUser("user").password(encoder().encode("userPass")).roles("USER");
    @Configuration
    @Order(1)
    public static class ApiWebSecurityConfigurationAdapter extends WebSecurityConfigurerAdapter {


        @Autowired
        private AuthenticationEntryPoint authEntryPoint;

        protected void configure(HttpSecurity http) throws Exception {
            http
                    .csrf().disable()
                    .antMatcher("/api/admin/**")
                    .authorizeRequests()
                    .anyRequest().hasRole("ADMIN")
                    .and()
                    .httpBasic().authenticationEntryPoint(authEntryPoint);
        }
    }

    @Order(2)
    @Configuration
    public static class FormLoginWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.csrf().disable().authorizeRequests().anyRequest().permitAll();
        }
    }

}
