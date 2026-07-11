package fr.tioslexe.flexbotticket.save;

import fr.tioslexe.flexbotticket.TmpUtility;
import net.dv8tion.jda.api.entities.Guild;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Hashtable;
import java.util.Map;

public class SaveManager {
    // thread-safe
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final Map<Guild, Save> saves = new Hashtable<>();

    public static Save getSave(Guild guild) throws IOException {
        loadSave(guild);
        return saves.get(guild);
    }

    // This method is synchronized to avoid the save being loaded multiple times
    private static synchronized void loadSave(Guild guild) throws IOException {
        if(!saves.containsKey(guild)){
            Path path = getPath(guild);
            if(Files.exists(path)) {
                String jsonSave = Files.readString(path);
                Save save = objectMapper.readValue(jsonSave, Save.class);
                saves.put(guild, save);
            }else{
                saveDefaultSave(guild);
            }
        }
    }

    private static void saveDefaultSave(Guild guild) throws IOException {
        // createCategory doesn't work yet with JFA
//        Category category = guild.createCategory("Tickets").complete();
//        String categoryId = category.getId();
        String categoryId = TmpUtility.createCategory(guild.getId(), "Tickets");

        Save save = new Save(categoryId, 0);
        save(guild, save);
        saves.put(guild, save);
    }

    // synchronized to avoid the save file being on an old version
    public static synchronized void save(Guild guild, Save save) throws IOException {
        String jsonSave = objectMapper.writeValueAsString(save);
        Path path = getPath(guild);
        Files.createDirectories(path.getParent());
        Files.writeString(path, jsonSave);
    }

    private static Path getPath(Guild guild){
        String guildId = guild.getId();
        return Paths.get("saves/" + guildId + ".json");
    }
}
