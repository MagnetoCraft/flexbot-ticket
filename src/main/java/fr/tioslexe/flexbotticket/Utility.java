package fr.tioslexe.flexbotticket;

import net.dv8tion.jda.api.entities.Guild;

public class Utility {
    public static String createTicketCategory(Guild guild){
        return TmpUtility.createCategory(guild.getId(), "Tickets");
    }
}
