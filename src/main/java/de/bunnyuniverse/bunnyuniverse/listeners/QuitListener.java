package de.bunnyuniverse.bunnyuniverse.listeners;

import de.bunnyuniverse.bunnyuniverse.main.BunnyUniverse;
import de.bunnyuniverse.bunnyuniverse.modules.scoreboard.ScoreboardPlayer;
import de.bunnyuniverse.bunnyuniverse.modules.tablist.TablistPlayer;
import de.bunnyuniverse.bunnyuniverse.utils.Teams;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class QuitListener implements Listener {
    BunnyUniverse plugin = BunnyUniverse.plugin;

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if (plugin.getConfig().getBoolean("chat.leaveMessage")) {
            event.setQuitMessage(plugin.getConfig().getString("chat.leaveMessageFormat").replace("%player_name%", player.getName()));
        }
        ScoreboardPlayer.removeScoreboard(player);
        TablistPlayer.removePlayer(player, false);
        Teams.removePlayer(player);
    }
}
