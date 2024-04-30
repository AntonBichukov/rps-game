package com.bichukov.rpsgame.tcp;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.integration.ip.tcp.connection.TcpConnectionCloseEvent;
import org.springframework.integration.ip.tcp.connection.TcpConnectionOpenEvent;
import org.springframework.integration.ip.tcp.connection.TcpNetConnection;

@RequiredArgsConstructor
public class ConnectionListener {

    private final Broadcaster broadcaster;

    @EventListener(TcpConnectionOpenEvent.class)
    public void login(TcpConnectionOpenEvent event) {
        broadcaster.enterNameMessage(event.getConnectionId());
    }

    @EventListener(TcpConnectionOpenEvent.class)
    public void connection(TcpConnectionOpenEvent event) {
        System.out.println(event);
        ConnectionsHolder.getInstance().addSession(event.getConnectionId(), (TcpNetConnection) event.getSource());
    }

    @EventListener(TcpConnectionCloseEvent.class)
    public void connection(TcpConnectionCloseEvent event) {
        System.out.println(event);
        ConnectionsHolder.getInstance().deleteSession(event.getConnectionId());
    }
}
