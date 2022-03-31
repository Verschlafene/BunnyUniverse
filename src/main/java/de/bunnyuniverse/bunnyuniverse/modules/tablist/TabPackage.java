package de.bunnyuniverse.bunnyuniverse.modules.tablist;

import de.bunnyuniverse.bunnyuniverse.main.BunnyUniverse;
import de.bunnyuniverse.bunnyuniverse.utils.Version;
import de.bunnyuniverse.bunnyuniverse.versions.*;
import org.bukkit.entity.Player;

public class TabPackage {
    static BunnyUniverse plugin = BunnyUniverse.plugin;

    public static void send(Player player, String header, String footer) {
        if (BunnyUniverse.getBukkitVersion().compareTo(new Version("1.17")) >= 0) {
            player.setPlayerListHeaderFooter(header, footer);
        } else if (BunnyUniverse.getBukkitVersion().compareTo(new Version("1.16")) >= 0) {
            Version116.sendTab(player, header, footer);
        } else if (BunnyUniverse.getBukkitVersion().compareTo(new Version("1.15")) >= 0) {
            Version115.sendTab(player, header, footer);
        } else if (BunnyUniverse.getBukkitVersion().compareTo(new Version("1.14")) >= 0) {
            Version114.sendTab(player, header, footer);
        } else if (BunnyUniverse.getBukkitVersion().compareTo(new Version("1.13")) >= 0) {
            Version113.sendTab(player, header, footer);
        } else if (BunnyUniverse.getBukkitVersion().compareTo(new Version("1.12")) >= 0) {
            Version112.sendTab(player, header, footer);
        } else if (BunnyUniverse.getBukkitVersion().compareTo(new Version("1.11")) >= 0) {
            Version111.sendTab(player, header, footer);
        } else if (BunnyUniverse.getBukkitVersion().compareTo(new Version("1.10")) >= 0) {
            Version110.sendTab(player, header, footer);
        } else if (BunnyUniverse.getBukkitVersion().compareTo(new Version("1.9")) >= 0) {
            Version109.sendTab(player, header, footer);
        } else if (BunnyUniverse.getBukkitVersion().compareTo(new Version("1.8")) >= 0) {
            Version108.sendTab(player, header, footer);
        } else {
            plugin.getLogger().severe(BunnyUniverse.prefix + "You are using a unsupported Minecraft version!");
        }
    }
}
