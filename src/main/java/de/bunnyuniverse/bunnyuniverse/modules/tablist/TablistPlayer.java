package de.bunnyuniverse.bunnyuniverse.modules.tablist;

import de.bunnyuniverse.bunnyuniverse.main.BunnyUniverse;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TablistPlayer {
    static BunnyUniverse plugin = BunnyUniverse.plugin;
    public static HashMap<Player, String> players = new HashMap<>();

    public static void addPlayer(Player player, @Nullable TablistManager tabmanager) {
        if (tabmanager == null) {
            TablistManager tablistManager = getMatchingTablist(player);
            if (tablistManager == null) tablistManager = TablistManager.get(plugin.getConfig().getString("tablist.text-default"));
            removePlayer(player, false);
            addPlayer(player, tablistManager);
        }else {
            tabmanager.addPlayer(player);
            players.put(player, tabmanager.getName());
        }
    }
    public static void removePlayer(Player player, boolean sendBlankTablist) {
        if(players.containsKey(player)) TablistManager.get(players.get(player)).removePlayer(player, sendBlankTablist);
    }

    public static TablistManager getMatchingTablist(Player player) {
		/* Config syntax:
		conditions:
		  - world:world AND permission:some.permission
		  - world:world AND permission:some.other.permission
		  - world:world AND gamemode:creative
		  - world:world_nether
		*/
        for (Map.Entry<String, TablistManager> e : TablistManager.tablists.entrySet()) {
            TablistManager tablistManager = e.getValue();
            if(tablistManager == null) {
                plugin.getLogger().severe(BunnyUniverse.prefix + "Could not set scoreboard " + tablistManager +"! File does not exists.");
                return null;
            }
            for (String condition : tablistManager.conditions) {
                ArrayList<String> andConditions = new ArrayList<>();
                if (condition.contains(" AND ")) {
                    for (String string : condition.split(" AND ")) {
                        andConditions.add(string);
                    }
                } else {
                    andConditions.add(condition);
                }
                boolean match = true;
                for (String string : andConditions) {
                    if(string.startsWith("world:")) {
                        String value = string.split("world:")[1];
                        if(!(player.getLocation().getWorld().getName().equalsIgnoreCase(value))) match = false;
                    }
                    if (string.startsWith("permission:")) {
                        String value = string.split("permission:")[1];
                        if(!(player.hasPermission(value))) match = false;
                    }
                    if (string.startsWith("gamemode:")) {
                        String value = string.split("gamemode:")[1];
                        if (!(player.getGameMode().name().equalsIgnoreCase(value))) match = false;
                    }
                }
                if (match == true) return tablistManager;
            }
        }
        return TablistManager.get(plugin.getConfig().getString("tablist.text-default"));
    }
}
