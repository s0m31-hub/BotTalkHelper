package org.nwolfhub.telegram;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.GetMe;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class UpdateHandler {
    @Value("${bot.token}")
    private String botToken;
    private TelegramBot bot;

    @PostConstruct
    private void initBot() {
        bot = new TelegramBot(botToken);
        System.out.println("Logged in as @" + bot.execute(new GetMe()).user().username());
    }
}
