package de.bunnyuniverse.bunnyuniverse.main;

import de.bunnyuniverse.bunnyuniverse.utils.SelfCheck;
import org.apache.commons.lang.Validate;
import org.bukkit.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Config {
    static BunnyUniverse plugin = BunnyUniverse.plugin;
    public static boolean loadConfig() {
        plugin.getLogger().info(BunnyUniverse.prefix + " ");
        plugin.getLogger().info(BunnyUniverse.prefix + "Loading Configuartion...");

        File folder = new File(BunnyUniverse.pluginFolder);
        if (!folder.isDirectory()) folder.mkdirs();

        plugin.getConfig().options().copyDefaults(true);
        plugin.saveDefaultConfig();
        plugin.reloadConfig();

        BunnyUniverse.debug = plugin.getConfig().getBoolean("debug");

        if (!SelfCheck.checkConfig()) {
            plugin.getLogger().severe(BunnyUniverse.prefix + "Severe errors have been found in your config.yml... Please check configuration!");
            return false;
        }

        File sbFolder = new File(BunnyUniverse.pluginFolder + "/scoreboards/");
        if (!sbFolder.exists() || !sbFolder.isDirectory()) sbFolder.mkdirs();

        createDefaultScoreboard();
        createDefaultTabList();
        createDefaultSpawnConfig();

        plugin.getLogger().info(BunnyUniverse.prefix + "Configurations loaded!");
        plugin.getLogger().info(BunnyUniverse.prefix + " ");
        return true;
    }
    public static void createDefaultSpawnConfig() {
        File file = new File(BunnyUniverse.pluginFolder + "/spawn.yml");
        if (!file.exists()) {
            try {
                file.createNewFile();
                YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
                config.addDefault("enabled", true);
                config.addDefault("commandPermission", "bunnyuniverse.spawn");
                config.addDefault("setCommandPermission", "bunnyuniverse.setspawn");
                config.addDefault("successMessage", "You were teleported to the spawn!");
                config.addDefault("setSuccessMessage", "You successfully set the spawn at your location!");
                config.addDefault("notEnoughPermissions", "You don't have enough permissions to do this!");
                config.addDefault("showNotEnoughPermisssionsMessage", false);
                config.addDefault("world", "world");
                config.addDefault("x", 0);
                config.addDefault("y", 64);
                config.addDefault("z", 0);
                config.addDefault("yaw", 0.0F);
                config.addDefault("pitch", 0.0F);
                config.options().copyDefaults(true);
                config.save(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public static void setSpawn(String world, int blockX, int blockY, int blockZ, float yaw, float pitch) {
        try {
            File file = new File(BunnyUniverse.pluginFolder + "/spawn.yml");
            if (!file.exists()) return;
            YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
            config.set("world", world);
            config.set("x", blockX);
            config.set("y", blockY);
            config.set("z", blockZ);
            config.set("yaw", yaw);
            config.set("pitch", pitch);
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static YamlConfiguration getSpawnConfig() {
        File file = new File(BunnyUniverse.pluginFolder + "/spawn.yml");
        if (file.exists()) {
            return YamlConfiguration.loadConfiguration(file);
        }
        return null;
    }
    public static void createDefaultScoreboard() {
        File file = new File(BunnyUniverse.pluginFolder + "/scoreboards/scoreboard.yml");
        if (!file.exists()) {
            try {
                file.createNewFile();
                YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
                String header = "Customize your scoreboard here!\n"
                        + "You can add as many animation steps as you like.\n\n"
                        + "You can set up to 14 scores. Just add a new number like this: \"'7':\"\n\n"
                        + "If you have static scores (no animations or updates needed): Set the 'speed' value to '9999' or higher.\n"
                        + "The scheduler won't start and that will save performance.\n\n"
                        + "Note: Specify the speed in ticks, not seconds! 20 ticks = 1 second";
                config.options().header(header);

                ArrayList<String> title = new ArrayList<String>();
                title.add(ChatColor.DARK_RED + "ServerName");
                title.add(ChatColor.DARK_RED + "ServerName");
                title.add(ChatColor.RED + "ServerName");
                title.add(ChatColor.RED + "ServerName");
                title.add(ChatColor.GOLD + "ServerName");
                title.add(ChatColor.GOLD + "ServerName");
                title.add(ChatColor.YELLOW + "ServerName");
                title.add(ChatColor.YELLOW + "ServerName");
                title.add(ChatColor.GREEN + "ServerName");
                title.add(ChatColor.GREEN + "ServerName");
                title.add(ChatColor.DARK_GREEN + "ServerName");
                title.add(ChatColor.DARK_GREEN + "ServerName");
                title.add(ChatColor.DARK_AQUA + "ServerName");
                title.add(ChatColor.DARK_AQUA + "ServerName");
                title.add(ChatColor.BLUE + "ServerName");
                title.add(ChatColor.BLUE + "ServerName");
                title.add(ChatColor.DARK_PURPLE + "ServerName");
                title.add(ChatColor.DARK_PURPLE + "ServerName");
                title.add(ChatColor.LIGHT_PURPLE + "ServerName");
                title.add(ChatColor.LIGHT_PURPLE + "ServerName");
                title.add(" ");
                config.addDefault("title.speed", 6);
                config.addDefault("title.titles", title);

                ArrayList<String> score1 = new ArrayList<>();
                ArrayList<String> score2 = new ArrayList<>();
                ArrayList<String> score3 = new ArrayList<>();
                ArrayList<String> score4 = new ArrayList<>();
                ArrayList<String> score5 = new ArrayList<>();
                ArrayList<String> score6 = new ArrayList<>();
                ArrayList<String> score7 = new ArrayList<>();
                score1.add("Not animated example");
                config.addDefault("0.speed", 9999);
                config.addDefault("0.scores", score1);

                score2.add(" ");
                config.addDefault("1.speed", 9999);
                config.addDefault("1.scores", score2);

                score3.add("A");
                score3.add("An");
                score3.add("Ani");
                score3.add("Anim");
                score3.add("Anima");
                score3.add("Animat");
                score3.add("Animate");
                score3.add("Animated");
                score3.add("Animated - Example");
                score3.add("Animated \\");
                score3.add("Animated | Example");
                score3.add("Animated /");
                score3.add("Animated - Example");
                score3.add("Animated \\");
                score3.add("Animated | Example");
                score3.add("Animated /");
                score3.add("Animated - Example");
                score3.add("Animated");
                score3.add("Animate");
                score3.add("Animat");
                score3.add("Anima");
                score3.add("Anim");
                score3.add("Ani");
                score3.add("An");
                score3.add("A");
                config.addDefault("2.speed", 5);
                config.addDefault("2.scores", score3);

                score4.add(" ");
                config.addDefault("3.speed", 9999);
                config.addDefault("3.scores", score4);

                score5.add(ChatColor.GRAY + "Information:");
                config.addDefault("4.speed", 9999);
                config.addDefault("4.scores", score5);

                score6.add(ChatColor.GRAY + "> " + ChatColor.RED + "Your Rank:");
                score6.add(ChatColor.GRAY + "> " + ChatColor.YELLOW + "Your Name:");
                score6.add(ChatColor.GRAY + "> " + ChatColor.GREEN + "Your Saturation:");
                score6.add(ChatColor.GRAY + "> " + ChatColor.GREEN + "Your Food");
                score6.add(ChatColor.GRAY + "> " + ChatColor.AQUA + "Your Hearts:");
                score6.add(ChatColor.GRAY + "> " + ChatColor.BLUE + "Your World:");
                score6.add(ChatColor.GRAY + "> " + ChatColor.LIGHT_PURPLE + "Time:");
                score6.add(ChatColor.GRAY + "> " + ChatColor.DARK_PURPLE + "Date:");
                score6.add(ChatColor.GRAY + "> " + ChatColor.LIGHT_PURPLE + "Server TPS:");
                score6.add(ChatColor.GRAY + "> " + ChatColor.DARK_PURPLE + "Server RAM:");
                score6.add(ChatColor.GRAY + "> " + ChatColor.LIGHT_PURPLE + "Server Players:");
                config.addDefault("5.speed", 30);
                config.addDefault("5.scores", score6);

                score7.add(ChatColor.GRAY + ">> " + ChatColor.DARK_RED + "%player_rank%");
                score7.add(ChatColor.GRAY + ">> " + ChatColor.GOLD + "%player_name%");
                score7.add(ChatColor.GRAY + ">> " + ChatColor.DARK_GREEN + "%player_saturation%");
                score7.add(ChatColor.GRAY + ">> " + ChatColor.DARK_GREEN + "%player_food%");
                score7.add(ChatColor.GRAY + ">> " + ChatColor.DARK_AQUA + "%player_health%");
                score7.add(ChatColor.GRAY + ">> " + ChatColor.DARK_BLUE + "%player_world%");
                score7.add(ChatColor.GRAY + ">> " + ChatColor.DARK_PURPLE + "%time%");
                score7.add(ChatColor.GRAY + ">> " + ChatColor.LIGHT_PURPLE + "%date%");
                score7.add(ChatColor.GRAY + ">> " + ChatColor.DARK_PURPLE + "%server_tps%");
                score7.add(ChatColor.GRAY + ">> " + ChatColor.LIGHT_PURPLE + "%mem_used%" + ChatColor.GRAY + "/" + ChatColor.DARK_RED + "%mem_total%");
                score7.add(ChatColor.GRAY + ">> " + ChatColor.DARK_PURPLE + " %server_online_players%" + ChatColor.GRAY + "/" + ChatColor.DARK_PURPLE + "%server_max_players%");
                config.addDefault("6.speed", 30);
                config.addDefault("6.scores", score7);

                List<String> conditions = new ArrayList<>();
                conditions.add("Add conditions here");
                config.addDefault("conditions", conditions);

                config.options().copyDefaults(true);
                config.save(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public static void createDefaultTabList() {
        File file = new File(BunnyUniverse.pluginFolder + "/tablist.yml");
        if (!file.exists()) {
            try {
                file.createNewFile();
                YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
                String header = "Customize your tablist here!\n"
                        + "You can add as many animation steps as you like.\n\n"
                        + "You can set up to 14 scores. Just add a new number like this: \"'4':\"\n\n"
                        + "If you have static scores (no animations or updates needed): Set the 'speed' value to '9999' or higher.\n"
                        + "The scheduler won't start and that will save performance.\n\n"
                        + "Note: Specify the speed in ticks, not seconds! 20 ticks = 1 second";
                config.options().header(header);

                ArrayList<String> header1 = new ArrayList<>();
                ArrayList<String> header2 = new ArrayList<>();

                header1.add(ChatColor.LIGHT_PURPLE + "Location: " + ChatColor.DARK_PURPLE + "X: %player_loc_x% Y: %player_loc_y% Z: %player_loc_z%");
                config.set("header.1.speed", 5);
                config.set("header.1.lines", header1);
                header2.add(" ");
                config.set("header.2.speed", 9999);
                config.set("header.2.lines", header2);

                ArrayList<String> footer1 = new ArrayList<>();
                ArrayList<String> footer2 = new ArrayList<>();
                ArrayList<String> footer3 = new ArrayList<>();
                ArrayList<String> footer4 = new ArrayList<>();

                footer1.add(" ");
                config.set("footer.1.speed", 9999);
                config.set("footer.1.lines", footer1);

                footer2.add(ChatColor.GRAY + "Information:");
                config.set("footer.2.speed", 9999);
                config.set("footer.2.lines", footer2);

                footer3.add(ChatColor.GRAY + "> " + ChatColor.RED + "Your Rank" + ChatColor.GRAY + ": <");
                footer3.add(ChatColor.GRAY + "> " + ChatColor.YELLOW + "Your Name" + ChatColor.GRAY + ": <");
                footer3.add(ChatColor.GRAY + "> " + ChatColor.GREEN + "Your Saturation" + ChatColor.GRAY + ": <");
                footer3.add(ChatColor.GRAY + "> " + ChatColor.GREEN + "Your Food" + ChatColor.GRAY + ": <");
                footer3.add(ChatColor.GRAY + "> " + ChatColor.AQUA + "Your Hearts" + ChatColor.GRAY + ": <");
                footer3.add(ChatColor.GRAY + "> " + ChatColor.BLUE + "Your World" + ChatColor.GRAY + ": <");
                footer3.add(ChatColor.GRAY + "> " + ChatColor.RED + "Time" + ChatColor.GRAY + ": <");
                footer3.add(ChatColor.GRAY + "> " + ChatColor.RED + "Date" + ChatColor.GRAY + ": <");
                footer3.add(ChatColor.GRAY + "> " + ChatColor.RED + "Server TPS" + ChatColor.GRAY + ": <");
                footer3.add(ChatColor.GRAY + "> " + ChatColor.RED + "Server RAM" + ChatColor.GRAY + ": <");
                footer3.add(ChatColor.GRAY + "> " + ChatColor.LIGHT_PURPLE + "Online Players" + ChatColor.GRAY + ": <");
                config.set("footer.3.speed", 30);
                config.set("footer.3.lines", footer3);

                footer4.add(ChatColor.GRAY + ">> " + ChatColor.DARK_RED + "%player_rank%" + ChatColor.GRAY + " <<");
                footer4.add(ChatColor.GRAY + ">> " + ChatColor.GOLD + "%player_name%" + ChatColor.GRAY + " <<");
                footer4.add(ChatColor.GRAY + ">> " + ChatColor.DARK_GREEN + "%player_saturation%" + ChatColor.GRAY + " <<");
                footer4.add(ChatColor.GRAY + ">> " + ChatColor.DARK_GREEN + "%player_food%" + ChatColor.GRAY + " <<");
                footer4.add(ChatColor.GRAY + ">> " + ChatColor.DARK_AQUA + "%player_health%" + ChatColor.GRAY + " <<");
                footer4.add(ChatColor.GRAY + ">> " + ChatColor.DARK_BLUE + "%player_world%" + ChatColor.GRAY + " <<");
                footer4.add(ChatColor.GRAY + ">> " + ChatColor.DARK_RED + "%time%" + ChatColor.GRAY + " <<");
                footer4.add(ChatColor.GRAY + ">> " + ChatColor.DARK_RED + "%date%" + ChatColor.GRAY + " <<");
                footer4.add(ChatColor.GRAY + ">> " + ChatColor.DARK_RED + "%server_tps%" + ChatColor.GRAY + " <<");
                footer4.add(ChatColor.GRAY + ">> " + ChatColor.DARK_RED + "%mem_used%" + ChatColor.GRAY + "/" + ChatColor.DARK_RED + "%mem_total%" + ChatColor.GRAY + " <<");
                footer4.add(ChatColor.GRAY + ">> " + ChatColor.DARK_PURPLE + " %server_online_players%" + ChatColor.GRAY + "/" + ChatColor.DARK_PURPLE + "%server_max_players%" + ChatColor.GRAY + " <<");
                config.set("footer.4.speed", 30);
                config.set("footer.4.lines", footer4);

                config.options().copyDefaults(true);
                config.save(file);
            } catch (IOException e) {
                e.printStackTrace();
                plugin.getLogger().severe(BunnyUniverse.prefix + "Could not create the tablist.yml file!");
            }
        }
    }

    public static YamlConfiguration loadConfiguration(File file) {
        Validate.notNull(file, BunnyUniverse.prefix + "File cannot be null");
        YamlConfiguration config = new YamlConfiguration();
        try {
            config.load(file);
        } catch (FileNotFoundException e) {
            plugin.getLogger().severe(BunnyUniverse.prefix + "Failed to load configuration " + file.getAbsolutePath() + "! File does not exist.");
        } catch (IOException e) {
            return null;
        } catch (InvalidConfigurationException e) {
            plugin.getLogger().severe(BunnyUniverse.prefix + "Cannot read configuration " + file.getAbsolutePath() + "!");
            plugin.getLogger().severe(BunnyUniverse.prefix + "This was probably caused by a typo in your scoreboard config.");
            return null;
        }
        return config;
    }
}
