package ke.or.karp.middleware.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.sql.DataSource;
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, proxyTargetClass = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {


    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private DataSource dataSource;


    @Autowired
    private UserDetailsService customUserDetailsService;

 /*   @Value("${spring.queries.users-query}")
     private String usersQuery = "select email, password, active,cid, username from user where email=?";

     @Value("${spring.queries.roles-query}")
    private String rolesQuery = "select u.username, u.email, u.cid, r.role from user u inner join user_role ur on(u.user_id=ur.user_id) inner join role r on(ur.role_id=r.role_id) where u.email=?";
*/

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(customUserDetailsService)
                .passwordEncoder(bCryptPasswordEncoder);
    }
    PersistentTokenRepository persistentTokenRepository(){
        JdbcTokenRepositoryImpl tokenRepositoryImpl = new JdbcTokenRepositoryImpl();
        tokenRepositoryImpl.setDataSource(dataSource);
        return tokenRepositoryImpl;
    }
    private static final String[] AUTH_WHITELIST = {
            // -- swagger ui
            "/v2/api-docs",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/api.html",
            "/swagger-ui.html",
            "/webjars/**"
            // other public endpoints of your API may be appended to this array
    };


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.
                headers().frameOptions().sameOrigin();

        http.
                authorizeRequests()
                .antMatchers("/").permitAll()
                //.antMatchers("/setup").permitAll()
                //.antMatchers("/auth/activate").permitAll()
                .antMatchers("/auth/login").permitAll()
                .antMatchers("/auth/cresetpassword").permitAll()
                .antMatchers("/auth/activate/*").permitAll()
                .antMatchers("/auth/deactivate/*").permitAll()
               // .antMatchers("/auth/registration").permitAll()
                .antMatchers("/dashboard/view").permitAll()
                .antMatchers("/dashboard/views").permitAll()
                .antMatchers("/dashboard/upload_data").permitAll()
                .antMatchers("/reporting/home").permitAll()
                .antMatchers("/reporting/dataupload").permitAll()
                .antMatchers("/reporting/upload_data").permitAll()
                .antMatchers("/rest/v1/api/resetpassword").permitAll()
                .antMatchers("/downloads/templates").permitAll()
                .antMatchers("/downloads/download/*").permitAll()
                .antMatchers("/system/users").permitAll()
                .antMatchers("/system/adduser").permitAll()
                .antMatchers("/databasemanager/restoredb").permitAll()
                .antMatchers("/databasemanager/restoredbs").permitAll()
                .antMatchers("/databasemanager/facilities").permitAll()
                .antMatchers("/databasemanager/uploadFile").permitAll()
                .antMatchers("/databasemanager/masterdatabases").permitAll()
                .antMatchers("/databasemanager/restore/*").permitAll()
                .antMatchers("/databasemanager/drop/*").permitAll()
                .antMatchers("/databasemanager/download/*").permitAll()
                .antMatchers("/metabase/home").permitAll()
                .antMatchers("/auth/resetpassword/*").permitAll()
                .antMatchers("/system/users/*").permitAll()
                .antMatchers("/system/period").permitAll()
                .antMatchers("/etl/admin").permitAll()
                .antMatchers("/data/**").permitAll()
               // .antMatchers("/data/upload_data/*").permitAll()
               // .antMatchers("/data/upload_dataa").permitAll()
                .antMatchers("/middleware/data/upload_dataa").permitAll()
                .antMatchers("/etl/refresh").permitAll()
                .antMatchers("/auth/admin/**").hasAuthority("SUPERADMIN").anyRequest()
                .authenticated().and().csrf().disable().formLogin()
                .loginPage("/")
                .failureUrl("/?error=true")
                .defaultSuccessUrl("/reporting/home")
                .usernameParameter("email")
                .passwordParameter("password")
                .and().logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .deleteCookies("my-remember-me-cookie")
                .permitAll()
                .and()
                .rememberMe()
                //.key("my-secure-key")
                .rememberMeCookieName("my-remember-me-cookie")
                .tokenRepository(persistentTokenRepository())
                .tokenValiditySeconds(24 * 60 * 60)
                .and()
                .exceptionHandling()
        ;
    }
    @Override
    public void configure(WebSecurity web) throws Exception {
        web
                .ignoring()
                .antMatchers("/resources/**",
                        "/static/**",
                        "/themes/**",
                        "/themes/adminlite/**",
                        "/themes/assets/**",
                        "/themes/css/**",
                        "/themes/img/**",
                        "/themes/jquery/**",
                        "/themes/js/**",
                        "/themes/jtoaster/**");

    }
}
