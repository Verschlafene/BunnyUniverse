package de.bunnyuniverse.bunnyuniverse.listeners;

import de.bunnyuniverse.bunnyuniverse.main.BunnyUniverse;
import de.bunnyuniverse.bunnyuniverse.modules.scoreboard.ScoreboardManager;
import de.bunnyuniverse.bunnyuniverse.modules.scoreboard.ScoreboardPlayer;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;

public class WorldListener implements Listener {
    @EventHandler
    public void onWorldSwitch(PlayerChangedWorldEvent event) {
        Bukkit.getScheduler().runTaskLater(BunnyUniverse.plugin, () -> {
            if (ScoreboardManager.scoreboards.size() > 1) {
                ScoreboardPlayer.updateScoreboard(event.getPlayer());
            }
        }, 5);
    }
}
