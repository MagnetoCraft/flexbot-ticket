package fr.tioslexe.flexbotticket;

import com.j4fluxer.entities.channel.Category;
import com.j4fluxer.entities.channel.TextChannel;
import com.j4fluxer.entities.guild.Guild;
import com.j4fluxer.fluxer.Fluxer;
import com.j4fluxer.fluxer.FluxerBuilder;

// Temporary utility class for making API calls that JFA isn't able to perform yet.
public class TmpUtility {
    private static Fluxer bot = FluxerBuilder.create(System.getenv("TOKEN")).build();

    public static String createCategory(String guildId, String categoryName){
        Guild guild = bot.getGuildById(guildId);
        Category category = guild.createCategory(categoryName).complete();
//        category.upsertPermissionOverride(guildId, 0, null, EnumSet.of(Permission.VIEW_CHANNEL));
        return category.getId();
    }

    public static TextChannel createChannel(String guildId, String categoryId, String channelName){
        Guild guild = bot.getGuildById(guildId);
        Category category = guild.getCategoryById(categoryId);
        TextChannel channel = category.createTextChannel(channelName).complete();
        // guildId is also the @everyone role id
//        channel.upsertPermissionOverride(guildId, 0, EnumSet.of(Permission.VIEW_CHANNEL), null);
//        System.out.println(channel.getPermissionOverwrites());
        return channel;
    }

    public static void sendMessage(TextChannel channel, String message){
        channel.sendMessage(message).queue();
    }
}
