package com.example.hacktoberfest.service;

import com.example.hacktoberfest.model.Users;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class UsersDetails implements UserDetails {
  private Users myUser;

  public UsersDetails(Users myUser) {
    this.myUser = myUser;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {return null;}

  @Override
  public String getPassword() {
    return myUser.getPassword();
  }

  @Override
  public String getUsername() {
    return myUser.getUsername();
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
}
