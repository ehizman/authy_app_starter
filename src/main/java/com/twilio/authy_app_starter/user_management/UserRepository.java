package com.twilio.authy_app_starter.user_management;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserRepository extends MongoRepository<AppUser, String > {
    Optional<AppUser> findAppUserByUsername(String username);
}
