package de.bunnyuniverse.bunnyuniverse.utils;

import de.bunnyuniverse.bunnyuniverse.main.BunnyUniverse;
import de.bunnyuniverse.bunnyuniverse.main.Config;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;

public class SelfCheck {
    static BunnyUniverse plugin = BunnyUniverse.plugin;

    public static boolean checkConfig() {
        String prefix = "SelfCheck config.yml -> ";
        try {
            File file = new File(BunnyUniverse.pluginFolder + "/config.yml");
            if (!file.exists()) {
                plugin.getLogger().severe(BunnyUniverse.prefix + "Skipped SelfCheck -> config.yml does not exist!");
                return false;
            }
            if (new Version("1.18").compareTo(BunnyUniverse.version) == 0) {
                plugin.getLogger().warning(BunnyUniverse.prefix + "Skipped SelfCheck -> Only works on 1.18+!");
                return true;
            }
            boolean disablePlugin = false;
            boolean needUpdate = false;
            plugin.getLogger().info(BunnyUniverse.prefix + prefix + "Loading...");
            FileConfiguration config = plugin.getConfig();
            YamlConfiguration configNoDefault = YamlConfiguration.loadConfiguration(file);

            File configSelfCheck = new File(BunnyUniverse.pluginFolder + "/.config_selfcheck.yml");
            try {
                InputStream inputStream = plugin.getClass().getClassLoader().getResourceAsStream("config.yml");
                OutputStream outputStream = new FileOutputStream(configSelfCheck);
                int length;
                byte[] bytes = new byte[1024];
                while ((length = inputStream.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, length);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            YamlConfiguration configDefault = Config.loadConfiguration(configSelfCheck);

            String[] checkBoolean = {
                    "scoreboard",
                    "tablist.text",
                    "tablist.ranks",
                    "chat.ranks",
                    "chat.allowHexColors",
                    "ranks.useUnlimitedLongRanks",
                    "ranks.luckperms-api.enable",
                    "ranks.luckperms-api.prefix-suffix-space",
                    "placeholder.prefer-plugin-placeholders",
                    "update.notification",
                    "update.autoupdater",
                    "debug"
            };

            String[] checkString = {
                    "scoreboard-default",
                    "tablist.text-default",
                    "chat.colorperm",
                    "ranks.permissionsystem",
                    "ranks.luckperms-api.chat-layout",

                    "placeholder.time-format",
                    "placeholder.date-format",
                    "placeholder.hexColorSyntax"
            };

            String[] checkInt = {
                    "ranks.update-interval",
                    "placeholder.money-decimals",
            };

            for (String string : checkBoolean) {
                if (!configNoDefault.contains(string)) {
                    plugin.getLogger().warning(BunnyUniverse.prefix + prefix + "The setting " + string + " does not exist! Adding...");
                    config.set(string, configDefault.getBoolean(string));
                    needUpdate = true;
                }
                if(!config.isBoolean(string)) {
                    plugin.getLogger().severe(BunnyUniverse.prefix + prefix + "The setting " + string + " is invalid! Please check your config.yml");
                    disablePlugin = true;
                }
            }
            for(String string : checkString) {
                if(!configNoDefault.contains(string)) {
                    plugin.getLogger().warning(BunnyUniverse.prefix + prefix + "The setting " + string + " does not exists! Adding...");
                    config.set(string, configDefault.getString(string));
                    needUpdate = true;
                }
                if(!config.isString(string)) {
                    plugin.getLogger().severe(BunnyUniverse.prefix + prefix + "The setting " + string + " is invalid! Please check your config.yml");
                    disablePlugin = true;
                }
            }
            for(String string : checkInt) {
                if(!configNoDefault.contains(string)) {
                    plugin.getLogger().warning( BunnyUniverse.prefix + prefix + "The setting " + string + " does not exists! Adding...");
                    config.set(string, configDefault.getInt(string));
                    needUpdate = true;
                }
                if(!config.isInt(string)) {
                    plugin.getLogger().severe(BunnyUniverse.prefix + prefix + "The setting " + string + " is invalid! Please check your config.yml");
                    disablePlugin = true;
                }
            }
            configSelfCheck.delete();
            if (needUpdate) plugin.saveConfig();
            plugin.getLogger().info(BunnyUniverse.prefix + prefix + "Finished!");
            if (disablePlugin) return true;
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    public static boolean checkTablist(String name, YamlConfiguration config) {
        if (!(config.contains("header") || config.contains("footer")
                || config.isList("header") || config.isList("footer"))) {
            plugin.getLogger().severe(BunnyUniverse.prefix + "Error in Tablist " + name + "! Please check for errors.");
            return false;
        }
        return true;
    }
}
