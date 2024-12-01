package com.shaliya.springmultitenant.springmultitenant.service.impl;


import com.shaliya.springmultitenant.springmultitenant.auth.ApplicationUser;
import com.shaliya.springmultitenant.springmultitenant.entity.User;
import com.shaliya.springmultitenant.springmultitenant.entity.UserHasUserRole;
import com.shaliya.springmultitenant.springmultitenant.repo.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.shaliya.springmultitenant.springmultitenant.security.ApplicationUserRole.*;

@Service
public class ApplicationUserServiceImpl implements UserDetailsService {
    private final UserRepo userRepo;
    private final UserHasUserRoleRepo userHasUserRoleRepo;

    public ApplicationUserServiceImpl(UserRepo userRepo, UserHasUserRoleRepo userHasUserRoleRepo) {
        this.userRepo = userRepo;
        this.userHasUserRoleRepo = userHasUserRoleRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        User byUsername = userRepo.findByEmail(username);
        if (byUsername == null) {
            throw new UsernameNotFoundException(
                    String.format("username %s not found", username));
        }
        List<UserHasUserRole> userRole = userHasUserRoleRepo.
                findByUserId(byUsername.getUserId());
        Set<SimpleGrantedAuthority> grantedAuthorities = new HashSet<>();

        for (UserHasUserRole user :userRole){
            if (user.getUserRole().getRoleName().equals("USER")){
                grantedAuthorities.addAll(USER.getGrantedAuthorities());
            }
            if (user.getUserRole().getRoleName().equals("HOST")){
                grantedAuthorities.addAll(HOST.getGrantedAuthorities());
            }
            if (user.getUserRole().getRoleName().equals("ADMIN")){
                grantedAuthorities.addAll(ADMIN.getGrantedAuthorities());
            }


            if (user.getUserRole().getRoleName().equals("SELLER")){
                grantedAuthorities.addAll(SELLER.getGrantedAuthorities());
            }


        }
        ApplicationUser user = null;

                user = new ApplicationUser(
                    byUsername.getPassword(),
                    byUsername.getEmail(),
                    grantedAuthorities,
                    byUsername.isAccountNonExpired(),
                    byUsername.isAccountNonLocked(),
                    true,
                    true

            );


        return user;
    }
}
