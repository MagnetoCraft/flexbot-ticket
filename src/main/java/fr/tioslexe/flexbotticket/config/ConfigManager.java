package fr.tioslexe.flexbotticket.config;

import fr.tioslexe.flexbotticket.TmpUtility;
import net.dv8tion.jda.api.entities.Guild;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

public class ConfigManager {
    // thread-safe
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private static final HashMap<Guild, Config> configs = new HashMap<>();

    public static Config getConfig(Guild guild) throws IOException {
        if(!configs.containsKey(guild)){
            loadConfig(guild);
        }
        return configs.get(guild);
    }

    private static void loadConfig(Guild guild) throws IOException {
        Path path = getPath(guild);
        if(Files.exists(path)) {
            String jsonConfig = Files.readString(path);
            Config config = objectMapper.readValue(jsonConfig, Config.class);
            configs.put(guild, config);
        }else{
            saveDefaultConfig(guild);
        }
    }

    private static void saveDefaultConfig(Guild guild) throws IOException {
        // createCategory doesn't work yet with JFA
//        Category category = guild.createCategory("Tickets").complete();
//        String categoryId = category.getId();
        String categoryId = TmpUtility.createCategory(guild.getId(), "Tickets");

        Config config = new Config(categoryId);
        saveConfig(guild, config);
        configs.put(guild, config);
    }

    private static void saveConfig(Guild guild, Config config) throws IOException {
        String jsonConfig = objectMapper.writeValueAsString(config);
        Path path = getPath(guild);
        Files.createDirectories(path.getParent());
        Files.writeString(path, jsonConfig);
    }

    private static Path getPath(Guild guild){
        String guildId = guild.getId();
        return Paths.get("configs/" + guildId + ".json");
    }
}
