package com.gopang.account;

import com.gopang.constant.Role;
import com.gopang.entity.Account;
import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.ArrayList;
import java.util.List;

@Getter
public class UserAccount extends User {

    private Account account;
    public UserAccount(Account account){
        super(account.getNickname(), account.getPassword(), authorities(account.getRole()));
        this.account = account;
    }
    private static List<SimpleGrantedAuthority> authorities(Role role) {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));

        if (role == Role.ADMIN) {  //account 에 role 'ADMIN'이면
            authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        }

        return authorities;
    }
}