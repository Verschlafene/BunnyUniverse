package de.bunnyuniverse.bunnyuniverse.listeners;

import de.bunnyuniverse.bunnyuniverse.main.BunnyUniverse;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MotdListener implements Listener {
    static BunnyUniverse plugin = BunnyUniverse.plugin;

    @EventHandler
    public void onServerListPing(ServerListPingEvent event) {
        if (plugin.getConfig().getBoolean("motd.active")) {
            List<String> firstLines = plugin.getConfig().getStringList("motd.firstLines");
            List<String> secondLines = plugin.getConfig().getStringList("motd.secondLines");
            event.setMotd(format(firstLines.get(new Random().nextInt(firstLines.size())) + "\n" + secondLines.get(new Random().nextInt(secondLines.size()))));
            event.setMaxPlayers(plugin.getConfig().getInt("motd.maxPlayers"));
        }
    }
    private String format(String message) {
        final Pattern pattern = Pattern.compile("#[a-fA-F0-9]{6}");
        Matcher matcher = pattern.matcher(message);
        while (matcher.find()) {
            String color = message.substring(matcher.start(), matcher.end());
            message = message.replace(color, ChatColor.of(color) + "");
            matcher = pattern.matcher(message);
        }
        return ChatColor.translateAlternateColorCodes('&', message);
    }
}
