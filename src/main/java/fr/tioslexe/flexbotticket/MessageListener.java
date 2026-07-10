package fr.tioslexe.flexbotticket;

import com.j4fluxer.entities.channel.TextChannel;
import fr.tioslexe.flexbotticket.config.ConfigManager;
import fr.tioslexe.flexbotticket.save.Save;
import fr.tioslexe.flexbotticket.save.SaveManager;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.internal.entities.GuildImpl;
import net.dv8tion.jda.internal.entities.channel.concrete.TextChannelImpl;

import java.io.IOException;

public class MessageListener extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {

        // We don't want to respond to other bot accounts, including ourself
        User user = event.getAuthor();
        if(user.isBot()) return;

        Message message = event.getMessage();
        String content = event.getMessage().getContentRaw();

        if(content.startsWith("/help")){
            message.reply("Here is the list of the bot commands :\n- `/ticket open [title]` is used to open a ticket.\n- `/ticket config` is only available for admin users and allows them to configure the ticket system. (coming soon)").queue();
        }else if(content.startsWith("/ticket open")){
            Guild guild = event.getGuild();
            try {
                Save save = SaveManager.getSave(guild);
                save.incrementTicketCount();
                Integer ticketCount = save.getTicketCount();

                String channelName = String.valueOf(ticketCount);
                if(content.length() > 13){
                    channelName += "-" + content.substring(13);
                }

                SaveManager.saveSave(guild, save);

                String openingMessage = "<@" + user.getId() + "> Please describe the reasoning for opening this ticket. A moderator will assist you shortly.";
                String categoryId = ConfigManager.getConfig(guild).categoryId();
//                createTextChannel doesn't work yet with JFA
//                Category category = Main.jda.getCategoryById(categoryId);
//                TextChannel channel = category.createTextChannel(String.valueOf(ticketCount)).complete();
//                channel.sendMessage(openingMessage);

                TextChannel channel = TmpUtility.createChannel(guild.getId(), categoryId, channelName);
                TmpUtility.sendMessage(channel, openingMessage);

                TextChannelImpl jdaChannel = new TextChannelImpl(Long.valueOf(channel.getId()), (GuildImpl) guild);
                jdaChannel.upsertPermissionOverride(guild.getSelfMember()).setAllowed(Permission.VIEW_CHANNEL).queue();
                jdaChannel.upsertPermissionOverride(event.getMember()).setAllowed(Permission.VIEW_CHANNEL).queue();
                jdaChannel.upsertPermissionOverride(guild.getRoleById(guild.getId())).setDenied(Permission.VIEW_CHANNEL).queue();
            } catch (IOException e) {
                System.err.println("The server crashed while loading a file.");
                throw new RuntimeException(e);
            }
        }
    }
}
