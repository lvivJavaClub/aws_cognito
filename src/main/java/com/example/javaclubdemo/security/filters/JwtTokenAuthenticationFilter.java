package com.example.javaclubdemo.security.filters;

import com.example.javaclubdemo.model.entity.Permission;
import com.example.javaclubdemo.model.entity.User;
import com.example.javaclubdemo.repository.RoleRepository;
import com.example.javaclubdemo.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@Component
@RequiredArgsConstructor
public class JwtTokenAuthenticationFilter extends OncePerRequestFilter {


    private static final String AUTHORIZATION_HEADER = "Authorization";

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        tryBasicAuthorization(httpServletRequest);

        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

    private void authenticate(HttpServletRequest httpServletRequest, String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        User user;
        if (userOptional.isPresent()) {
            user = userOptional.get();
            if(!user.getConfirmed()) {
                user.setConfirmed(true);
                userRepository.save(user);
            }
        } else {
            User newUser = User.builder()
                    .email(email)
                    .role(roleRepository.findByName("admin"))
                    .confirmed(true)
                    .build();
            user = userRepository.save(newUser);
        }
        List<SimpleGrantedAuthority> authoritiesList = new ArrayList<>();
        for (Permission permission : user.getRole().getPermissions()) {
            authoritiesList.add(new SimpleGrantedAuthority(permission.getName()));
        }
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user,
                null, authoritiesList);
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
        SecurityContextHolder.getContext().setAuthentication(authentication);

    }

    private void tryBasicAuthorization(HttpServletRequest httpServletRequest) throws JsonProcessingException {
        if (httpServletRequest.getHeader(AUTHORIZATION_HEADER) != null) {
            String base64Credentials = httpServletRequest.getHeader(AUTHORIZATION_HEADER);
            String[] chunks = base64Credentials.split("\\.");
            String payload = new String(Base64.getUrlDecoder().decode(chunks[1]));
            ObjectMapper mapper = new ObjectMapper();
            Map<String, String> map = mapper.readValue(payload, Map.class);
            authenticate(httpServletRequest, map.get("email"));
        }
    }
}
