package org.nwolfhub;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.GetMe;
import com.pengrad.telegrambot.response.GetMeResponse;
import org.jsoup.Jsoup;
import org.nwolfhub.easycli.Defaults;
import org.nwolfhub.easycli.EasyCLI;
import org.nwolfhub.easycli.model.Level;
import org.nwolfhub.util.WebCacher;
import org.nwolfhub.utils.Configurator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        /*File configFile = new File("bot.cfg");
        if(!configFile.exists()) {
            try {
                configFile.createNewFile();
                try(FileOutputStream out = new FileOutputStream(configFile)) {
                    out.write("token=".getBytes());
                }
                System.out.println("Config file was created at " + configFile.getAbsolutePath() + ". Please put your token there");
                System.exit(2);
            } catch (IOException e) {
                System.out.println("Exception occured while creating config: " + e.getMessage() + ", " + e.getCause());
                System.exit(1);
            }
        }
        Configurator configurator = new Configurator(configFile);
        TelegramBot bot = new TelegramBot(configurator.getSingleValue("token"));
        GetMeResponse me = bot.execute(new GetMe());
        System.out.println("Logged in as @" + me.user().username());*/
        new WebCacher().reCache();
    }
}