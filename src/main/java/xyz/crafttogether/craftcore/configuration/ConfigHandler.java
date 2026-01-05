package xyz.crafttogether.craftcore.configuration;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.crafttogether.craftcore.CraftCore;

import java.io.File;
import java.nio.file.Files;

public class ConfigHandler {
    private static final Logger logger = LoggerFactory.getLogger(ConfigHandler.class);
    private static File file;
    private static Config config;

    public static void loadConfig() {
        verifyConfigVersion();  // Verify the config before loading it
        file = new File(CraftCore.getPlugin().getDataFolder() + "/config.yml");
        reloadConfig();
    }

    public static void reloadConfig() {
        FileConfiguration fc = YamlConfiguration.loadConfiguration(file);
        config = new Config(
                fc.getInt("configVersion"),
                fc.getString("discordToken"),
                fc.getLong("verifyExpireDelay"),
                fc.getLong("verifyCheckDelay"),
                fc.getLong("discordGuildId"),
                fc.getLong("discordChannelId"),
                fc.getString("discordWebhook"),
                fc.getBoolean("ircEnabled"),
                fc.getString("ircUsername"),
                fc.getString("ircHostname"),
                fc.getInt("ircPort"),
                fc.getBoolean("ircUseTls"),
                fc.getInt("ircTimeout"),
                fc.getString("ircChannel"),
                fc.getInt("ircReconnectAttempts"),
                fc.getInt("ircReconnectDelay"),
                fc.getString("ircCommandPrefix"),
                fc.getString("minecraftPrefix"),
                fc.getString("ircPrefix")
        );
    }

    private static void verifyConfigVersion() {
        if (CraftCore.getRequiredConfigVersion() == config.getConfigVersion()) return;
        logger.warn("Config version outdated, generating new config");
        // Move the config before regenerating the config, adding .bak (backup) extension
        boolean moved = file.renameTo(new File(file.getAbsolutePath() + ".bak"));
        if (moved) {
            logger.info("Created backup of config");
            CraftCore.getPlugin().getConfig().options().copyDefaults();
            CraftCore.getPlugin().saveDefaultConfig();
        } else {
            logger.error("Failed to create backup of config, aborting...");
        }
        logger.info("Unloaded CraftCore, please update config.yml and restart the server");
        CraftCore.unload();
    }

    public static Config getConfig() {
        return config;
    }
}
