package com.example.hacktoberfest.controller;

import com.example.hacktoberfest.model.AuthenticationResponse;
import com.example.hacktoberfest.model.Users;
import com.example.hacktoberfest.service.UserService;
import com.example.hacktoberfest.utils.JWTUtils;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class UsersController {
  @Autowired
  private JWTUtils jwtUtils;
  @Autowired
  private AuthenticationManager authenticationManager;
  private final UserService service;

  @GetMapping("/ping")
  public String pong (){
    return "Pong";
  }

  @PostMapping("/authorize")
  public ResponseEntity<?> createAuthenticationRequest(
      @RequestBody Users users
  ) throws Exception{
    try{
      authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
          users.getUsername(), users.getPassword()
      ));
    }
    catch (BadCredentialsException e){
      throw new Exception("Incorrect username or password", e);
    }
    final UserDetails usersDetails = service.loadUserByUsername(users.getUsername());
    final String jwt = jwtUtils.generateToken(usersDetails);
    return ResponseEntity.ok(new AuthenticationResponse(jwt));
  }

  @PutMapping("/authorize")
  public String saveUser (@RequestBody Users users){
    return service.createUser(users);
  }
}
