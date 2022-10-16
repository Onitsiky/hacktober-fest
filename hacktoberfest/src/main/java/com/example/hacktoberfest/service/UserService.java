package com.example.hacktoberfest.service;

import com.example.hacktoberfest.model.Users;
import com.example.hacktoberfest.repository.UsersRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {

  private PasswordEncoder bCryptPasswordEncoder() {
    return new BCryptPasswordEncoder();
  };

  UsersRepository repository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Users actual = repository.findByUsername(username);
    if(actual == null){
      throw new UsernameNotFoundException("User not found");
    }
    return new UsersDetails(actual);
  }

  public String createUser (Users user){
    String encryptedPassword = bCryptPasswordEncoder().encode(user.getPassword());
    user.setPassword(encryptedPassword);
    repository.save(user);
    return "User created successfully !";
  }
}
