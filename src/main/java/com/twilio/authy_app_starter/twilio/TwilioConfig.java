package com.twilio.authy_app_starter.twilio;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

@Configuration
@PropertySource(value = "classpath:twilio-config.properties")
public class TwilioConfig {
    private final Environment env;
    private final String ACCOUNT_SID;
    private final String AUTH_TOKEN;
    private final String SERVICE_SID;

    public TwilioConfig(Environment env) {
        this.env = env;
        this.ACCOUNT_SID = env.getProperty("ACCOUNT_SID");
        this.AUTH_TOKEN = env.getProperty("AUTH_TOKEN");
        this.SERVICE_SID = env.getProperty("SERVICE_SID");
    }

    public String getACCOUNT_SID() {
        return ACCOUNT_SID;
    }

    public String getAUTH_TOKEN() {
        return AUTH_TOKEN;
    }

    public String getSERVICE_SID() {
        return SERVICE_SID;
    }
}
