package fr.tioslexe.flexbotticket;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

public class Main {

    private static String TOKEN = System.getenv("TOKEN");

    public static void main(String[] args) {
        JDA jda = JDABuilder.createDefault(TOKEN).build();

//        jda.awaitReady();
    }
}
