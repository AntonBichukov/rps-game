package com.bichukov.rpsgame.tcp;

import org.springframework.integration.ip.tcp.connection.TcpNetConnection;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class ConnectionsHolder {
    private static ConcurrentMap<String, TcpNetConnection> SESSIONS = new ConcurrentHashMap<>();
    private static ConnectionsHolder INSTANCE;

    private ConnectionsHolder() {
    }

    public static synchronized ConnectionsHolder getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ConnectionsHolder();
        }
        return INSTANCE;
    }

    public void addSession(String id, TcpNetConnection session) {
        SESSIONS.put(id, session);
    }

    public TcpNetConnection getSession(String id) {
        return SESSIONS.get(id);
    }

    public void deleteSession(String id) {
        SESSIONS.remove(id);
    }

    public void killSession(String id) {
        TcpNetConnection session = getSession(id);
        try {
            session.shutdownInput();
            session.shutdownOutput();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
