package com.bichukov.rpsgame.messaging;

import com.bichukov.rpsgame.service.GameService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.ip.IpHeaders;
import org.springframework.messaging.MessageHeaders;

@Slf4j
@RequiredArgsConstructor
public class DefaultMessageProcessor implements MessageProcessor {

    private final GameService gameService;

    @Override
    @ServiceActivator(inputChannel = "fromTcp.input")
    public void processMessage(byte[] payload, MessageHeaders messageHeaders) {
        String message = new String(payload);
        String connectionId = (String) messageHeaders.get(IpHeaders.CONNECTION_ID);
        log.info("Receive message = {} with connectionId = {}", message, connectionId);
        gameService.precessGameStep(connectionId, message);
    }


}
