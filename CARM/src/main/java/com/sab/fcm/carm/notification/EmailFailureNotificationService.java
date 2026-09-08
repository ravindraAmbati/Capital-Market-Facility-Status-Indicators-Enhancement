package com.sab.fcm.carm.notification;

import com.sab.fcm.carm.config.CarmFcmProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class EmailFailureNotificationService
        implements FailureNotificationService {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(EmailFailureNotificationService.class);

    private final CarmFcmProperties properties;

    public EmailFailureNotificationService(
            CarmFcmProperties properties) {
        this.properties = properties;
    }

    @Override
    public void notifyFailure(
            String subject,
            Exception exception) {

        if (!properties.isNotificationEnabled()) {
            return;
        }

        /*
         * Email implementation intentionally not coupled to the cache.
         * Replace this body later with the existing enterprise/CARM
         * mail service or JavaMail implementation.
         */
        LOGGER.error(
                "FCM email notification provision triggered. " +
                "subject={}, from={}, to={}",
                subject,
                properties.getNotificationFrom(),
                properties.getNotificationTo(),
                exception);
    }
}
