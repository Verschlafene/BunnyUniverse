package de.bunnyuniverse.bunnyuniverse.listeners;

import de.bunnyuniverse.bunnyuniverse.main.BunnyUniverse;
import de.bunnyuniverse.bunnyuniverse.modules.scoreboard.ScoreboardManager;
import de.bunnyuniverse.bunnyuniverse.modules.scoreboard.ScoreboardPlayer;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerGameModeChangeEvent;

public class GameModeListener implements Listener {
    @EventHandler
    public void onGameModeSwitch(PlayerGameModeChangeEvent event) {
        if (ScoreboardManager.scoreboards.size() > 1) Bukkit.getScheduler().runTaskLater(BunnyUniverse.plugin, () -> ScoreboardPlayer.updateScoreboard(event.getPlayer()), 5);
    }
}
