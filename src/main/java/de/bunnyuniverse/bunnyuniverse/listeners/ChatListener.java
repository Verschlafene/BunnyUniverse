package de.bunnyuniverse.bunnyuniverse.listeners;

import de.bunnyuniverse.bunnyuniverse.main.BunnyUniverse;
import de.bunnyuniverse.bunnyuniverse.utils.Teams;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener implements Listener {
    static BunnyUniverse plugin = BunnyUniverse.plugin;
    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        if (plugin.getConfig().getBoolean("chat.ranks")) {
            Teams teams = Teams.get(player);
            if (teams != null) {
                if (!teams.getChatPrefix().equals("noRank")) {
                    String message = teams.getChat(event.getMessage());
                    message = message.replace("%", "%%");
                    event.setFormat(message);
                } else {
                    plugin.getLogger().warning(BunnyUniverse.prefix + "The player " + player.getName() + "has no rank!");
                }
            } else {
                plugin.getLogger().warning(BunnyUniverse.prefix + "The player " + player.getName() + " has no team! Please rejoin and try it again. If the problem persist, please check your Plugin configuration!");
            }
        }
    }
}
