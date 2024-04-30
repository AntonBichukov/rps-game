package com.bichukov.rpsgame.config;

import com.bichukov.rpsgame.tcp.Broadcaster;
import com.bichukov.rpsgame.tcp.ConnectionListener;
import com.bichukov.rpsgame.tcp.Sender;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.ip.dsl.Tcp;
import org.springframework.integration.ip.dsl.TcpNetServerConnectionFactorySpec;
import org.springframework.integration.ip.tcp.connection.AbstractServerConnectionFactory;


@Configuration
@EnableIntegration
@IntegrationComponentScan
public class TcpServerConfig {

    @Value("${tcp.server.port:22}")
    private int port;

    @Bean
    public TcpNetServerConnectionFactorySpec serverFactory() {
        return Tcp.netServer(port).soKeepAlive(true);
    }

    @Bean
    public IntegrationFlow tcpServer(AbstractServerConnectionFactory serverFactory) {

        return IntegrationFlow.from(Tcp.inboundAdapter(serverFactory)).channel("fromTcp.input").get();
    }

    /*
     * Gateway flow for controller.
     */
    @Bean
    public IntegrationFlow gateway() {
        return IntegrationFlow.from(Sender.class).channel("toTcp.input").get();
    }

    /*@Bean
    public IntegrationFlow fromTcp(MessageProcessor messageProcessor) {
        return f -> f.handle(messageProcessor::processMessage);
    }*/

    /*
     * Outbound channel adapter flow.
     */
    @Bean
    public IntegrationFlow toTcp(AbstractServerConnectionFactory serverFactory) {
        return f -> f.handle(Tcp.outboundAdapter(serverFactory));
    }

    @Bean
    public ConnectionListener listener(Broadcaster broadcaster) {
        return new ConnectionListener(broadcaster);
    }

}
