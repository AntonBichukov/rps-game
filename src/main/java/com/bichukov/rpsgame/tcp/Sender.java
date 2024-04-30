package com.bichukov.rpsgame.tcp;

import org.springframework.integration.ip.IpHeaders;
import org.springframework.messaging.handler.annotation.Header;

public interface Sender {

    void send(String payload, @Header(IpHeaders.CONNECTION_ID) String connectionId);

}
