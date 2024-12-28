package com.twilio.authy_app_starter.twilio;

import com.twilio.Twilio;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class TwilioClient {
    private final Logger log = LoggerFactory.getLogger(TwilioClient.class);
    private final TwilioConfig twilioConfig;

    public TwilioClient(TwilioConfig twilioConfig) {
        this.twilioConfig = twilioConfig;
    }

    @PostConstruct
    public void initializeTwilioClient() {
        try {
            String ACCOUNT_SID = Objects.requireNonNull(twilioConfig.getACCOUNT_SID());
            String AUTH_TOKEN = Objects.requireNonNull(twilioConfig.getAUTH_TOKEN());
            Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
            log.info("Twilio Client initialized successfully");
        } catch (NullPointerException exception) {
            log.error("Twilio Client not initialized. Twilio ACCOUNT SID or AUTH TOKEN is missing");
        }
    }
}
