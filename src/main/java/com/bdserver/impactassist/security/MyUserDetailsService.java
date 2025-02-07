package com.bdserver.impactassist.security;

import com.bdserver.impactassist.model.RoleDAO;
import com.bdserver.impactassist.model.UserDAO;
import com.bdserver.impactassist.model.UserPrincipal;
import com.bdserver.impactassist.repo.UserRepo;
import com.bdserver.impactassist.repo.UserRoleRepo;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class MyUserDetailsService implements UserDetailsService {

    private final UserRepo userRepo;
    private final UserRoleRepo userRoleRepo;

    public MyUserDetailsService(UserRepo userRepo, UserRoleRepo userRoleRepo) {
        this.userRepo = userRepo;
        this.userRoleRepo = userRoleRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDAO user = userRepo.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException(username);
        }

        List<RoleDAO> roles = userRoleRepo.getUserRolesByUserId(user.getId());
        Set<GrantedAuthority> authorities = roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toSet());

        return new UserPrincipal(user, authorities);
    }
}
