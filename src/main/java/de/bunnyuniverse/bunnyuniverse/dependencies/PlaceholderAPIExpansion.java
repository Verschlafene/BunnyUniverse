package de.bunnyuniverse.bunnyuniverse.dependencies;

import de.bunnyuniverse.bunnyuniverse.main.BunnyUniverse;
import de.bunnyuniverse.bunnyuniverse.utils.TPS;
import de.bunnyuniverse.bunnyuniverse.utils.Teams;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PlaceholderAPIExpansion extends PlaceholderExpansion {

    @Override
    public @NotNull String getIdentifier() {
        return "BunnyUniverse";
    }

    @Override
    public @NotNull String getAuthor() {
        return "NoSleepNikki";
    }

    @Override
    public @NotNull String getVersion() {
        return BunnyUniverse.plugin.getDescription().getVersion();
    }

    @Override
    public String onPlaceholderRequest(Player player, String placeholder) {
        if (placeholder.equalsIgnoreCase("tps")) return "" + TPS.getTPS();
        if (player == null) return "Player not found";
        Teams teams = Teams.get(player);
        if (teams == null) return "No team";
        if (placeholder.equalsIgnoreCase("prefix")) return teams.getPrefix();
        if (placeholder.equalsIgnoreCase("suffix")) return teams.getSuffix();
        if (placeholder.equalsIgnoreCase("chat_prefix")) return teams.getChatPrefix();
        if (placeholder.equalsIgnoreCase("display_name")) return teams.getPlaceholderName();
        return null;
    }
}
