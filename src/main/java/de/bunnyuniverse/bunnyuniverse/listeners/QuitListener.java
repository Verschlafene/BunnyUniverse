package de.bunnyuniverse.bunnyuniverse.listeners;

import de.bunnyuniverse.bunnyuniverse.modules.scoreboard.ScoreboardPlayer;
import de.bunnyuniverse.bunnyuniverse.modules.tablist.TablistPlayer;
import de.bunnyuniverse.bunnyuniverse.utils.Teams;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class QuitListener implements Listener {
    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        ScoreboardPlayer.removeScoreboard(player);
        TablistPlayer.removePlayer(player, false);
        Teams.removePlayer(player);
    }
}
