package com.twilio.authy_app_starter.user_management;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<AppUser, Long> {
    @Query("select ap from AppUser ap where username = ?1")
    Optional<AppUser> findAppUserByUsername(String username);
}
