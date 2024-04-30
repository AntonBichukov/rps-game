package com.bichukov.rpsgame.messaging;

import org.springframework.messaging.MessageHeaders;

public interface MessageProcessor {
    void processMessage(byte[] payload, MessageHeaders messageHeaders);
}
