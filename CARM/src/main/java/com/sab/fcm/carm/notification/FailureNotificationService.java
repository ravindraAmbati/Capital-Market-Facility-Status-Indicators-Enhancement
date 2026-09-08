package com.sab.fcm.carm.notification;

public interface FailureNotificationService {

    void notifyFailure(String subject, Exception exception);
}
