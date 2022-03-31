package de.bunnyuniverse.bunnyuniverse.dependencies;

import de.bunnyuniverse.bunnyuniverse.main.BunnyUniverse;
import de.bunnyuniverse.bunnyuniverse.modules.ranks.RankManager;
import de.bunnyuniverse.bunnyuniverse.utils.Teams;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.event.EventBus;
import net.luckperms.api.event.user.UserDataRecalculateEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class LuckPermsListener {
    public LuckPermsListener (BunnyUniverse plugin, LuckPerms api) {
        EventBus eventBus = api.getEventBus();
        eventBus.subscribe(UserDataRecalculateEvent.class, event -> Bukkit.getScheduler().runTaskLater(plugin, () -> {
            Player player = Bukkit.getPlayer(event.getUser().getUniqueId());
            if (player != null) {
                Teams teams = Teams.get(player);
                if (teams != null) {
                    if (plugin.getConfig().getBoolean("tablist.ranks")) {
                        if (RankManager.updateTablistRanks(player)) plugin.getLogger().info(BunnyUniverse.prefix + "(LuckPermsAPI) Updated rank of " + player.getName() + "!");
                    }
                }
            }
        }, 10));
    }
}
