/*
 * Copyright (c) 2019. by 8x8. Inc.
 *  _____      _____
 * |  _  |    |  _  |
 *  \ V /__  __\ V /   ___ ___  _ __ ___
 *  / _ \\ \/ // _ \  / __/ _ \| '_ ` _ \
 * | |_| |>  <| |_| || (_| (_) | | | | | |
 * \_____/_/\_\_____(_)___\___/|_| |_| |_|
 *
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of 8x8 Inc. ("Confidential Information").  You
 * shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with 8x8 Inc.
 */
package com.dianagrigore.rem.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("pos.rabbit")
public class RabbitMqProperties {
    private String emailExchange;
    private String emailPaymentKey;
    private String emailContactUsKey;
    private String smsExchange;
    private String smsPaymentKey;

    public String getEmailExchange() {
        return emailExchange;
    }

    public RabbitMqProperties setEmailExchange(String emailExchange) {
        this.emailExchange = emailExchange;
        return this;
    }

    public String getEmailPaymentKey() {
        return emailPaymentKey;
    }

    public String getEmailContactUsKey() {
        return emailContactUsKey;
    }

    public RabbitMqProperties setEmailContactUsKey(String emailContactUsKey) {
        this.emailContactUsKey = emailContactUsKey;
        return this;
    }

    public RabbitMqProperties setEmailPaymentKey(String emailPaymentKey) {
        this.emailPaymentKey = emailPaymentKey;
        return this;
    }

    public String getSmsExchange() {
        return smsExchange;
    }

    public RabbitMqProperties setSmsExchange(String smsExchange) {
        this.smsExchange = smsExchange;
        return this;
    }

    public String getSmsPaymentKey() {
        return smsPaymentKey;
    }

    public RabbitMqProperties setSmsPaymentKey(String smsPaymentKey) {
        this.smsPaymentKey = smsPaymentKey;
        return this;
    }
}
