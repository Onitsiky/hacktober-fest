package com.example.hacktoberfest.repository;

import com.example.hacktoberfest.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersRepository extends JpaRepository<Users, Long> {
  Users findByUsername(String username);
}
