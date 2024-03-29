package org.nwolfhub.telegram;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.InlineQuery;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.model.request.InlineQueryResult;
import com.pengrad.telegrambot.model.request.InlineQueryResultArticle;
import com.pengrad.telegrambot.model.request.InputTextMessageContent;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.AnswerInlineQuery;
import com.pengrad.telegrambot.request.GetMe;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.BaseResponse;
import com.pengrad.telegrambot.response.SendResponse;
import jakarta.annotation.PostConstruct;
import org.nwolfhub.database.model.PreparedMessage;
import org.nwolfhub.database.repositories.FieldRepository;
import org.nwolfhub.database.repositories.MessagesRepository;
import org.nwolfhub.database.repositories.SectionRepository;
import org.nwolfhub.database.repositories.UnitRepository;
import org.nwolfhub.util.WebCacher;
import org.nwolfhub.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
public class UpdateHandler {
    @Value("${bot.token}")
    private String botToken;
    @Value("${bot.admins}")
    private String adminsRaw;
    private List<Long> admins;
    private TelegramBot bot;
    private final FieldRepository fieldRepository;
    private final SectionRepository sectionRepository;
    private final UnitRepository unitRepository;
    private final MessagesRepository messagesRepository;

    private final WebCacher cacher;

    public UpdateHandler(FieldRepository fieldRepository, SectionRepository sectionRepository, UnitRepository unitRepository, MessagesRepository messagesRepository, WebCacher cacher) {
        this.fieldRepository = fieldRepository;
        this.sectionRepository = sectionRepository;
        this.unitRepository = unitRepository;
        this.messagesRepository = messagesRepository;
        this.cacher = cacher;
    }

    @PostConstruct
    private void initBot() {
        bot = new TelegramBot(botToken);
        admins = Arrays.stream(adminsRaw.replace(" ", "").split(",")).map(Long::valueOf).toList();
        bot.setUpdatesListener(new UpdatesListener() {
            @Override
            public int process(List<Update> list) {
                for(Update update:list) {
                    new Thread(() -> processUpdate(update)).start();
                }
                return CONFIRMED_UPDATES_ALL;
            }
        });
        System.out.println("Logged in as @" + bot.execute(new GetMe()).user().username());
    }

    private void processUpdate(Update update) {
        if(update.message()!=null) {
            handleNormalMessage(update.message());
        } else if(update.inlineQuery()!=null) {
            handleInline(update.inlineQuery());
        }
    }


    private void handleInline(InlineQuery query) {
        User from = query.from();
        String queryText = query.query();
        if(queryText.equals("")) {
            List<PreparedMessage> preparedMessages = messagesRepository.getPreparedMessagesByGlobalOrOwner(true, from.id());
            replyWithPrepared(query.id(), preparedMessages);
        }
    }

    private void replyWithPrepared(String id, List<PreparedMessage> messages) {
        List<InlineQueryResultArticle> articles = new ArrayList<>();
        for(PreparedMessage message:messages) {
            articles.add(
                    new InlineQueryResultArticle(
                            String.valueOf(message.id),
                            "Saved message",
                            message.getText()
                    ).inputMessageContent(new InputTextMessageContent(message.getText()).parseMode(ParseMode.MarkdownV2))
            );
        }
        response = bot.execute(new AnswerInlineQuery(id, articles.toArray(new InlineQueryResult[0])).cacheTime(0));
    }



    private void handleNormalMessage(Message message) {
        User from = message.from();
        String text = message.text();
        if(text.equals("/start")) {
             bot.execute(new SendMessage(from.id(), "Welcome to BotTalk helper bot!\n" +
                    "\n" +
                    "This is NOT OFFICIAL bot developed by @s0m31_tg to help people format messages in @bottalk easier!\n" +
                    "There's not much you can do in private: just try me in inline mode.\n" +
                    "And if you feel shy to chat with others right now, just write /help "));
            if(admins.contains(from.id())) {
                bot.execute(new SendMessage(from.id(), "You have admin access. Here's what that power gives you here:\n\n" +
                        "1) Manage global templates using /global\n" +
                        "2) Clear cache and crawl tg once again using /cc"));
            }
            if (from.id() == 334297800L) {
                bot.execute(new SendMessage(from.id(), "hey dot"));
            } else if(from.id() == 24421134L) {
                bot.execute(new SendMessage(from.id(), "hey rico"));
            }
        } else if(text.equals("/cc")) {
            if(admins.contains(from.id())) {
                Long start = new Date().getTime();
                System.out.println(from.id() + " requested caching at " + start);
                bot.execute(new SendMessage(from.id(), "Started caching"));
                try {
                    cacher.reCache();
                } catch (Exception e) {
                    bot.execute(new SendMessage(from.id(), "Exception occurred: " + e.getMessage()));
                }
                Long finish = new Date().getTime();
                bot.execute(new SendMessage(from.id(), "Finished caching in " + (finish-start) + "ms"));
            }
        } else if (text.equals("/help")) {
            bot.execute(new SendMessage(from, "Its empty here..."));
        }
    }
}
