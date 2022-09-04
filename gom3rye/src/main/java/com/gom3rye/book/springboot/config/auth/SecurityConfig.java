package com.gom3rye.book.springboot.config.auth;

import com.gom3rye.book.springboot.domain.user.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter; //WebSecurityConfigurerAdapter는 deprecated 되어, 상속을 받지 않고 모두 Bean으로 등록하여 사용하는 방식으로 변경 됨.
import org.springframework.security.web.SecurityFilterChain;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity // Spring Security 설정들을 활성화시켜 준다.
public class SecurityConfig {
    private final CustomOAuth2UserService customOAuth2UserService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .headers().frameOptions().disable() // h2-console 화면을 사용하기 위해 해당 옵션들을 disable 한다.
                .and()
                .authorizeHttpRequests(authorize -> authorize
                        .antMatchers("/", "/css/**", "/images/**", "/js/**", "/h2-console/**", "/profile").permitAll()
                        .antMatchers("/category/**","/categories","/write", "/api/**", "/post/**", "/posts").hasRole(Role.USER.name())
                        .anyRequest().authenticated()) // anyRequest : 설정된 갓들 이외 나머지 URL들을 나타냄
                // 여기서는 authenticated()를 추가해 나머지 URL들은 모두 인증죈 사용자들에게만 허용하게 한다. 인증된 사용자 = 로그인한 사용자
                .logout(logout -> logout
                        .logoutSuccessUrl("/")) // 로그아웃 기능에 대한 여러 설정의 진입점, 로그아웃 성공 시 / 주소로 이동한다.
                .oauth2Login(oauth2Login -> oauth2Login
                        .userInfoEndpoint() // OAuth 2 로그인 성공 이후 사용자 정보를 가져올 때의 설정들을 담당
                        .userService(customOAuth2UserService)); // 소셜 로그인 성공 시 후속 조치를 진행할 UserService 인터페이스의 구현체를 등록, 리소스 서버(즉, 소셜 서비스들)에서 사용자 정보를 가져온 상태에서 추가로 진행하고자 하는 기능을 명시할 수 있다.

        return http.build();
    }
}
