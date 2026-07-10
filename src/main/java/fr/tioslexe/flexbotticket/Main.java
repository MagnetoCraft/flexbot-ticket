package fr.tioslexe.flexbotticket;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;

public class Main {

    private static String TOKEN = System.getenv("TOKEN");

    public static JDA jda;

    public static void main(String[] args) {
        jda = JDABuilder.createDefault(TOKEN)
                // Enables access to message.getContentRaw()
                .enableIntents(GatewayIntent.MESSAGE_CONTENT)
                .addEventListeners(new MessageListener())
                .build();

//        jda.updateCommands().addCommands(Commands.slash("test", "This is a test.")).queue();

//        jda.awaitReady();
    }
}
