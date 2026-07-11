package fr.tioslexe.flexbotticket;

import com.j4fluxer.entities.channel.TextChannel;
import fr.tioslexe.flexbotticket.save.Save;
import fr.tioslexe.flexbotticket.save.SaveManager;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.internal.entities.GuildImpl;
import net.dv8tion.jda.internal.entities.channel.concrete.TextChannelImpl;

import java.io.IOException;
import java.util.List;

public class MessageListener extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {

        // We don't want to respond to other bot accounts, including ourself
        User user = event.getAuthor();
        if(user.isBot()) return;

        Member member = event.getMember();
        Guild guild = event.getGuild();
        Message message = event.getMessage();
        String content = event.getMessage().getContentRaw();

        if(content.startsWith("/help")){
            message.reply("Here is the list of the bot commands :\n- `/ticket open [title]` is used to open a ticket with an optional title.\n"
                    + "- `/ticket setrole [role]` is only available for admin users and allows them to set the role (role name or role ID) that will handle tickets. If no roles are specified, only admins will have access to ticket channels."
            + "\n-# You can join our [support server](https://fluxer.gg/diZjBcsq) and check the source code on [GitHub](<https://github.com/MagnetoCraft/flexbot-ticket>)\n").queue();
        }else if(content.startsWith("/ticket open")){
            try {
                Save save = SaveManager.getSave(guild);

                Integer ticketCount = save.incrementTicketCount();
                SaveManager.save(guild, save);

                String channelName = String.valueOf(ticketCount);
                if(content.length() > 13){
                    channelName += "-" + content.substring(13);
                }

                String categoryId = SaveManager.getSave(guild).getCategoryId();
//                createTextChannel doesn't work yet with JFA
//                Category category = Main.jda.getCategoryById(categoryId);
//                TextChannel channel = category.createTextChannel(String.valueOf(ticketCount)).complete();
//                channel.sendMessage(openingMessage);

                TextChannel channel = TmpUtility.createChannel(guild.getId(), categoryId, channelName);

                TextChannelImpl jdaChannel = new TextChannelImpl(Long.valueOf(channel.getId()), (GuildImpl) guild);

                String openingMessage = "<@" + user.getId() + "> Please describe the reasoning for opening this ticket. ";
                String moderatorsId = SaveManager.getSave(guild).getRoleId();
                if(moderatorsId == null) {
                    openingMessage += "An admin";
                }else{
                    Role moderators = guild.getRoleById(moderatorsId);
                    jdaChannel.upsertPermissionOverride(moderators).setAllowed(Permission.VIEW_CHANNEL).queue();
                    openingMessage += "A " + moderators.getAsMention();
                }
                openingMessage += " will assist you shortly.";

                jdaChannel.upsertPermissionOverride(guild.getSelfMember()).setAllowed(Permission.VIEW_CHANNEL).queue();
                jdaChannel.upsertPermissionOverride(member).setAllowed(Permission.VIEW_CHANNEL).queue();
                // guild ID = @everyone role ID
                jdaChannel.upsertPermissionOverride(guild.getRoleById(guild.getId())).setDenied(Permission.VIEW_CHANNEL).queue();

                TmpUtility.sendMessage(channel, openingMessage);
            } catch (IOException e) {
                System.err.println("The server crashed while loading a file.");
                throw new RuntimeException(e);
            }
        }else if(content.startsWith("/ticket setrole")){
            if(member.hasPermission(Permission.ADMINISTRATOR)){
                String roleId = null;
                if(content.length() > 16){
                    roleId = content.substring(16);
                    if(!this.existRole(guild, roleId)){
                        if(!roleId.startsWith("@")){
                            roleId = "@" + roleId;
                        }
                        List<Role> roles = guild.getRolesByName(roleId, true);
                        if(roles.isEmpty()){
                            message.reply("No role was found!").queue();
                            return;
                        }else{
                            roleId = roles.get(0).getId();
                        }
                    }
                }
                try {
                    Save config = SaveManager.getSave(guild);
                    config.setRoleId(roleId);
                    SaveManager.save(guild, config);
                    if(roleId == null){
                        message.reply("No one except admins will have access to ticket channels.").queue();
                    }else{{
                        message.reply("You have successfully set the ticket role to <@&" + roleId + ">.").queue();
                    }}
                } catch (IOException e) {
                    System.err.println("The server crashed while loading a file.");
                    throw new RuntimeException(e);
                }
            }else{
                message.reply("You need to be an admin to use this command!").queue();
            }
        }
    }

    private boolean existRole(Guild guild, String roleId){
        boolean exist;
        try {
            exist = guild.getRoleById(roleId) != null;
        } catch (NumberFormatException e) {
            exist = false;
        }
        return exist;
    }
}
