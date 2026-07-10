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

        if(content.startsWith("/help")){
            message.reply("Here is the list of the bot commands :\n- `/ticket open` is used to open a ticket.\n- `/ticket config` is only available for admin users and allows them to configure the ticket system.").queue();
        }
    }
}
