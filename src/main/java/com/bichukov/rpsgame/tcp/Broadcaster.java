package com.bichukov.rpsgame.tcp;

import com.bichukov.rpsgame.domain.Game;
import com.bichukov.rpsgame.domain.Result;
import com.bichukov.rpsgame.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

@Component
@DependsOn("gateway")
public class Broadcaster {

    private Sender sender;

    public Broadcaster(@Autowired Sender sender) {
        this.sender = sender;
    }

    public void send(String what, String where) {
        sender.send(what, where);
    }

    public void helloMessage(User user) {
        send(String.format(">>> Hi %s! Waiting your opponent...", user.getName()), user.getId()); //userId is connectionId
    }

    public void gameReadyMessage(Game game) {
        game.getUsers().forEach(u ->
                send(">>> Game is ready. Select your option: [1 - Stone, 2 - Scissors, 3 - Paper]", u.getId()));

    }

    public void wrongOptionMessage(User user) {
        send("Selected option is incorrect. Try again.", user.getId());
    }

    public void enterNameMessage(String connectionId) {
        send(">>> Please enter you name.", connectionId);
    }

    public void waitingMessage(User user) {
        send(">>> Waiting your opponent...", user.getId());
    }

    public void resultMessage(Game game) {
        game.getUsers().forEach(u -> send(getResultMessageText(u.getResult()), u.getId()));
    }

    private String getResultMessageText(Result result) {
        return switch (result) {
            case WIN -> ">>> Congrats. You win!";
            case LOSE -> ">>> You lose.";
            case TIE -> ">>> It's tie. Try again";
        };
    }
}
