package com.bdserver.impactassist.security;

import com.bdserver.impactassist.model.RoleDAO;
import com.bdserver.impactassist.model.UserDAO;
import com.bdserver.impactassist.model.UserPrincipal;
import com.bdserver.impactassist.repo.UserRepo;
import com.bdserver.impactassist.repo.UserRoleRepo;
import com.bdserver.impactassist.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class JwtFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserRepo userRepo;
    private final UserRoleRepo userRoleRepo;

    public JwtFilter(JwtService jwtService, UserRepo userRepo, UserRoleRepo userRoleRepo) {
        this.jwtService = jwtService;
        this.userRepo = userRepo;
        this.userRoleRepo = userRoleRepo;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        String header = request.getHeader("Authorization");
        if (header == null) {
            filterChain.doFilter(request, response);
            return;
        }

        if (!header.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = header.substring(7);
        if (!jwtService.validateAccessToken(token)) {
            filterChain.doFilter(request, response);
            return;
        }

        Integer userId = jwtService.extractUserId(token);
        UserDAO user = userRepo.findUserById(userId);
        List<RoleDAO> roles = userRoleRepo.getUserRolesByUserId(user.getId());
        Set<GrantedAuthority> authorities = roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toSet());
        UserPrincipal userPrincipal = new UserPrincipal(user, authorities);

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userPrincipal, null, userPrincipal.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request, response);
    }
}
