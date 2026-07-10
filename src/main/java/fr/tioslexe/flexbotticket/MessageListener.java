package fr.tioslexe.flexbotticket;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class MessageListener extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {

        // We don't want to respond to other bot accounts, including ourself
        if(event.getAuthor().isBot()) return;

        Message message = event.getMessage();
        String content = event.getMessage().getContentRaw();

        if(content.startsWith("!ping")){
            message.reply("pong").queue();
        }
    }
}
