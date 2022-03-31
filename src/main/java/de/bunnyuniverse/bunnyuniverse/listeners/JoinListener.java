package de.bunnyuniverse.bunnyuniverse.listeners;

import de.bunnyuniverse.bunnyuniverse.main.BunnyUniverse;
import de.bunnyuniverse.bunnyuniverse.modules.ranks.RankManager;
import de.bunnyuniverse.bunnyuniverse.modules.scoreboard.ScoreboardPlayer;
import de.bunnyuniverse.bunnyuniverse.modules.tablist.TablistPlayer;
import de.bunnyuniverse.bunnyuniverse.utils.Teams;
import de.bunnyuniverse.bunnyuniverse.utils.Updater;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener implements Listener {
    BunnyUniverse plugin = BunnyUniverse.plugin;

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (plugin.getConfig().getBoolean("chat.joinMessage")) {
            event.setJoinMessage(plugin.getConfig().getString("chat.joinMessageFormat").replace("%player_name%", player.getName()));
        }
        if (player.hasPermission("bunnyuniverse.updatenotify") || player.isOp()) {
            Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
                if (Updater.checkVersion()) {
                    if (plugin.getConfig().getBoolean("update.notification")) {
                        player.sendMessage(BunnyUniverse.prefix + "A new update is available (" + Updater.getVersion() + ")!\n"
                                + "Your version: " + plugin.getDescription().getVersion());
                        if (plugin.getConfig().getBoolean("update.autoupdates")) {
                            player.sendMessage(BunnyUniverse.prefix + "The plugin will be updated automatically after a server restart.");
                        } else {
                            player.sendMessage(BunnyUniverse.prefix + "You can download the newest version here: https://www.spigotmc.org/resources/bunnyuniverse.101047/ or enable the auto updater in the config.yml!");
                        }
                    }
                }
            });
        }
        if (plugin.getConfig().getBoolean("tablist.ranks") || plugin.getConfig().getBoolean("scoreboard")) player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            if (plugin.getConfig().getBoolean("chat.ranks") || plugin.getConfig().getBoolean("tablist.ranks")) {
                if (Teams.get(player) == null) RankManager.register(player);
            }
            if (plugin.getConfig().getBoolean("scoreboard")) ScoreboardPlayer.setScoreboard(player);
            if (plugin.getConfig().getBoolean("tablist.ranks")) RankManager.setTablistRanks(player);
            if (plugin.getConfig().getBoolean("tablist.active")) TablistPlayer.addPlayer(player, null);
        }, 3);
    }
}
