package fr.tioslexe.flexbotticket.save;

import net.dv8tion.jda.api.entities.Guild;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

public class SaveManager {
    // thread-safe
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private static final HashMap<Guild, Save> saves = new HashMap<>();

    public static Save getSave(Guild guild) throws IOException {
        if(!saves.containsKey(guild)){
            loadSave(guild);
        }
        return saves.get(guild);
    }

    private static void loadSave(Guild guild) throws IOException {
        Path path = getPath(guild);
        if(Files.exists(path)) {
            String jsonSave = Files.readString(path);
            Save save = objectMapper.readValue(jsonSave, Save.class);
            saves.put(guild, save);
        }else{
            saveDefaultSave(guild);
        }
    }

    private static void saveDefaultSave(Guild guild) throws IOException {
        Save save = new Save(0);
        saveSave(guild, save);
        saves.put(guild, save);
    }

    public static void saveSave(Guild guild, Save save) throws IOException {
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
