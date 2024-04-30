package com.bichukov.rpsgame.config;

import com.bichukov.rpsgame.dao.GameDao;
import com.bichukov.rpsgame.dao.UserDao;
import com.bichukov.rpsgame.matching.CreatedGameRule;
import com.bichukov.rpsgame.matching.Rule;
import com.bichukov.rpsgame.matching.WaitingGameRule;
import com.bichukov.rpsgame.messaging.DefaultMessageProcessor;
import com.bichukov.rpsgame.messaging.MessageProcessor;
import com.bichukov.rpsgame.service.GameService;
import com.bichukov.rpsgame.service.MatchingService;
import com.bichukov.rpsgame.service.UserService;
import com.bichukov.rpsgame.tcp.Broadcaster;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GameConfig {

    @Bean
    public Rule matchingRule() {
        return new WaitingGameRule(new CreatedGameRule());
    }

    @Bean
    public MatchingService matchingService(Rule matchingRule) {
        return new MatchingService(matchingRule);
    }

    @Bean
    public GameDao gameDao() {
        return new GameDao();
    }

    @Bean
    public UserDao userDao() {
        return new UserDao();
    }

    @Bean
    public GameService gameService(Broadcaster broadcaster, UserService userService,
                                   GameDao gameDao, MatchingService matchingService) {
        return new GameService(broadcaster, userService, gameDao, matchingService);
    }

    @Bean
    public UserService userService(UserDao userDao) {
        return new UserService(userDao);
    }

    @Bean
    public MessageProcessor messageProcessor(GameService gameService) {
        return new DefaultMessageProcessor(gameService);
    }
}
