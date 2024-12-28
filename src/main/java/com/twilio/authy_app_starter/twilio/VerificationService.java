package com.twilio.authy_app_starter.twilio;

import com.twilio.authy_app_starter.user_management.AppUser;
import com.twilio.authy_app_starter.user_management.UserService;
import com.twilio.rest.verify.v2.service.entity.Challenge;
import com.twilio.rest.verify.v2.service.entity.Factor;
import com.twilio.rest.verify.v2.service.entity.NewFactor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;

@Service
public class VerificationService {
    private final Logger log = LoggerFactory.getLogger(VerificationService.class);
    private final UserService userService;
    private final String VERIFY_SERVICE_SID;


    public VerificationService(UserService userService, TwilioConfig twilioConfig) {
        this.userService = userService;
        this.VERIFY_SERVICE_SID = Objects.requireNonNull(twilioConfig.getSERVICE_SID());
    }

    public Map<String, Object> createFactor (String username) throws NullPointerException{
        try {
            log.info("Username --> {}", username);
            AppUser user = Objects.requireNonNull(userService.findUserByUsername(username));
            String userId = user.getUserId();
            log.info("UserId --> {}", userId);
            NewFactor factor = NewFactor.creator(VERIFY_SERVICE_SID, userId, username, NewFactor.FactorTypes.TOTP).create();
            log.info("New Factor created --> {}", factor);
            user.set_2faFactorSid(factor.getSid());
            userService.save(user);
            return factor.getBinding();
        } catch (NullPointerException exception){
            log.error("User does not exist");
            throw exception;
        }
    }

    public boolean verifyFactor(String username, String authCode) {
        try {
            AppUser user = Objects.requireNonNull(userService.findUserByUsername(username));
            Factor factor = Factor.updater(VERIFY_SERVICE_SID, user.getUserId(), user.get_2faFactorSid())
                    .setAuthPayload(authCode)
                    .update();
            log.info("Factor verified --> {}", factor);
            if (factor.getStatus().toString().equalsIgnoreCase("verified")) {
                user.setI2FAEnabled(true);
                userService.save(user);
                return true;
            } else {
                return false;
            }
        } catch (NullPointerException exception) {
            log.error("Application error. User does not exist");
            return false;
        }
    }

    public boolean verifyToken(String username, String authPayload) {
        try {
            AppUser user = Objects.requireNonNull(userService.findUserByUsername(username));
            Challenge challenge = Challenge.creator(VERIFY_SERVICE_SID, user.getUserId(), user.get_2faFactorSid())
                    .setAuthPayload(authPayload)
                    .create();
            return challenge.getStatus().toString().equalsIgnoreCase("approved");
        } catch (NullPointerException exception) {
            log.error("Application error. User does not exist");
            return false;
        }
    }
}
