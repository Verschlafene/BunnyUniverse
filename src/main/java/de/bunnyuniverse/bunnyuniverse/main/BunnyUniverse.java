package de.bunnyuniverse.bunnyuniverse.main;

import de.bunnyuniverse.bunnyuniverse.commands.BunnyUniverseCommand;
import de.bunnyuniverse.bunnyuniverse.commands.SetSpawnCommand;
import de.bunnyuniverse.bunnyuniverse.commands.SpawnCommand;
import de.bunnyuniverse.bunnyuniverse.listeners.*;
import de.bunnyuniverse.bunnyuniverse.modules.ranks.RankManager;
import de.bunnyuniverse.bunnyuniverse.modules.scoreboard.ScoreboardManager;
import de.bunnyuniverse.bunnyuniverse.modules.scoreboard.ScoreboardPlayer;
import de.bunnyuniverse.bunnyuniverse.modules.tablist.TablistManager;
import de.bunnyuniverse.bunnyuniverse.modules.tablist.TablistPlayer;
import de.bunnyuniverse.bunnyuniverse.utils.TPS;
import de.bunnyuniverse.bunnyuniverse.utils.Teams;
import de.bunnyuniverse.bunnyuniverse.utils.Updater;
import de.bunnyuniverse.bunnyuniverse.utils.Version;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class BunnyUniverse extends JavaPlugin {
    public static BunnyUniverse plugin;

    public static String pluginFolder = "plugins/BunnyUniverse";
    public static String prefix = ChatColor.GRAY + "[" + ChatColor.BLUE + "Bunny" + ChatColor.LIGHT_PURPLE + "Universe" + ChatColor.GRAY + "] ";

    public static Version version;
    public static boolean aboveMC_1_13 = false;
    public static boolean debug = false;

    @Override
    public void onEnable() {
        plugin = this;
        plugin.getLogger().info(prefix + "Loading BunnyUniverse");

        listenerRegistration();
        commandRegistration();

        version = getBukkitVersion();
        if (version.compareTo(new Version("1.13")) >= 0) {
            aboveMC_1_13 = true;

            if (!Config.loadConfig()) {
                plugin.getLogger().severe(prefix + "There were errors when loading the configuration! Disabling plugin...");
                sendPluginLoadFailed();
                Bukkit.getPluginManager().disablePlugin(plugin);
                return;
            }

            ExternalPlugins.initializePlugins();

            Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new TPS(), 100L, 1L);
            TPS.start();

            Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
                if (Updater.checkVersion()) {
                    plugin.getLogger().info(prefix + "-> A new version (" + Updater.getVersion() + ") is available! Current version: " + plugin.getDescription().getVersion());
                    plugin.getLogger().info(prefix + " -> Please update me!");
                }
            });

            if (plugin.getConfig().getBoolean("scoreboard")) ScoreboardManager.registerAlLScoreboards();
            if (plugin.getConfig().getBoolean("tablist.active")) TablistManager.registerAllTablists();

            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                plugin.getLogger().info(prefix + "Registering players...");
                for (Player all : Bukkit.getOnlinePlayers()) {
                    if (plugin.getConfig().getBoolean("chat.ranks") || plugin.getConfig().getBoolean("tablist.ranks")) {
                        Teams teams = Teams.get(all);
                        if (teams == null) RankManager.register(all);
                        RankManager.startTablistRanksUpdateScheduler();
                    }
                    if (plugin.getConfig().getBoolean("tablist.ranks")) RankManager.setTablistRanks(all);
                    if (plugin.getConfig().getBoolean("scoreboard")) ScoreboardPlayer.setScoreboard(all);
                    if (plugin.getConfig().getBoolean("tablist.active")) TablistPlayer.addPlayer(all, null);
                }
                plugin.getLogger().info(prefix + "All players have been registed!");
            }, 30);
            plugin.getLogger().info(prefix + " ");
            plugin.getLogger().info(prefix + "---- BunnyUniverse loaded ----");
            plugin.getLogger().info(prefix + "------------------------------");
        }
    }

    @Override
    public void onDisable() {
        if (plugin.getConfig().getBoolean("update.autoupdater")) {
            if (Updater.checkVersion()) Updater.downloadFile();
        }
        for (Player all : Bukkit.getOnlinePlayers()) {
            ScoreboardPlayer.removeScoreboard(all);
            Teams.removePlayer(all);
        }
        ScoreboardManager.unregisterAllScoreboards();

        if (plugin.getConfig().getBoolean("tablist.active")) TablistManager.unregisterAllTablists();
    }

    private void listenerRegistration() {
        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new ChatListener(), this);
        pluginManager.registerEvents(new GameModeListener(), this);
        pluginManager.registerEvents(new WorldListener(), this);
        pluginManager.registerEvents(new JoinListener(), this);
        pluginManager.registerEvents(new QuitListener(), this);
        pluginManager.registerEvents(new MotdListener(), this);
    }
    private void commandRegistration() {
        getCommand("setspawn").setExecutor(new SetSpawnCommand());
        getCommand("spawn").setExecutor(new SpawnCommand());
        getCommand("bunnyuniverse").setExecutor(new BunnyUniverseCommand());
    }

    private static boolean reloadDelay = false;
    public static void reloadConfigs(CommandSender s) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            if (reloadDelay) {
                s.sendMessage(prefix + ChatColor.RED + "Please wait 2 seconds before you reload again!");
                return;
            }
            reloadDelay = true;
            Bukkit.getScheduler().runTaskLater(plugin, () -> reloadDelay = false, 40);
            s.sendMessage(prefix + "Reloading config...");
            Config.loadConfig();
            if (BunnyUniverse.plugin.getConfig().getBoolean("scoreboard")) {
                s.sendMessage(prefix + "Reloading scoreboards...");
                ScoreboardManager.unregisterAllScoreboards();
                ScoreboardManager.registerAlLScoreboards();
                ScoreboardPlayer.players.clear();
                Bukkit.getScheduler().runTaskLater(plugin, () -> {
                    for (Player all : Bukkit.getOnlinePlayers()) ScoreboardPlayer.setScoreboard(all);
                }, 5);
            }
            if (plugin.getConfig().getBoolean("tablist.ranks")) {
                for (Player all : Bukkit.getOnlinePlayers()) {
                    Teams.removePlayer(all);
                    RankManager.register(all);
                    RankManager.setTablistRanks(all);
                }
            }
            if (plugin.getConfig().getBoolean("tablist.text")) {
                s.sendMessage(prefix + "Reloading tablists...");
                TablistManager.unregisterAllTablists();
                TablistManager.registerAllTablists();
                for (Player all : Bukkit.getOnlinePlayers()) TablistPlayer.addPlayer(all, null);
            }
            s.sendMessage(prefix + "Plugin reloaded!");
        });
    }

    public static Version getBukkitVersion() {
        if (version != null) return version;
        try {
            String s = Bukkit.getBukkitVersion();
            String version = s.substring(0, s.lastIndexOf("-R")).replace("_", ".");
            plugin.getLogger().info(prefix + "Detected Server Version (Original): " + s);
            plugin.getLogger().info(prefix + "Detected Server Version (Extracted): " + version);
            return new Version(version);
        } catch (Exception e) {
            e.printStackTrace();
            plugin.getLogger().severe(prefix + "Could not extract MC Version! Defaulting to 1.13.");
            return new Version("1.13");
        }
    }
    public static void sendPluginLoadFailed() {
        plugin.getLogger().severe(prefix + "----------------------------------------------------");
        plugin.getLogger().severe(prefix + "---- Errors occured while loading BunnyUniverse ----");
        plugin.getLogger().severe(prefix + "----------------------------------------------------");
    }
}
