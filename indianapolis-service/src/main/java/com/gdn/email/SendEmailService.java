package com.gdn.email;

import com.gdn.entity.*;

import javax.mail.MessagingException;

public interface SendEmailService {
    void sendEmail(Pickup pickup) throws MessagingException;
}
