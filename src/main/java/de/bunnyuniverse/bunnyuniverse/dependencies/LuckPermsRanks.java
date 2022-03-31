package de.bunnyuniverse.bunnyuniverse.dependencies;

import de.bunnyuniverse.bunnyuniverse.main.BunnyUniverse;
import de.bunnyuniverse.bunnyuniverse.main.ExternalPlugins;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.user.User;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;

public class LuckPermsRanks {
    static BunnyUniverse plugin = BunnyUniverse.plugin;

    public static boolean registerLuckPermsAPIRank(Player player) {
        if (ExternalPlugins.luckPerms == null) {
            plugin.getLogger().severe(BunnyUniverse.prefix + "LuckPuck-API enabled but LuckPerms is not installed!");
            return false;
        }
        LuckPerms api = ExternalPlugins.luckPerms;
        User user = api.getUserManager().getUser(player.getUniqueId());
        Group group = api.getGroupManager().getGroup(user.getPrimaryGroup());
        int weight = 0;
        try {
            weight = 999 - group.getWeight().getAsInt();
        } catch (Exception e) {
            plugin.getLogger().severe(BunnyUniverse.prefix + "The group " + group.getName() + " has no weight! Please set the weight with /lp group <group> setweight <weight>");
            return false;
        }

        String suffix, prefix, displayName, nameColor;

        prefix = user.getCachedData().getMetaData().getPrefix();
        suffix = user.getCachedData().getMetaData().getSuffix();
        displayName = group.getDisplayName();

        if (prefix == null) {
            prefix = "";
        } else {
            if (plugin.getConfig().getBoolean("ranks.luckperms-api.prefix-suffix-space")) prefix = prefix + " ";
        }
        if (suffix == null) {
            suffix = "";
        } else {
            if (plugin.getConfig().getBoolean("ranks.luckperms-api.prefix-suffix-space")) suffix = " " + suffix;
        }

        if (displayName == null) {
            plugin.getLogger().severe(BunnyUniverse.prefix + "The group " + group.getName() + " has no display name! Give the group the permission 'displayname.<displayname>', for example 'displayname.&4Owner'");
            return false;
        }

        YamlConfiguration configNoDefaultSettings = YamlConfiguration.loadConfiguration(new File(BunnyUniverse.pluginFolder + "/config.yml"));

        String chat = configNoDefaultSettings.getString("ranks.luckperms-api.chat-prefix." + group.getName());
        if (chat == null) chat = configNoDefaultSettings.getString("ranks.luckperms-api.chat-layout");
        if (chat == null) {
            plugin.getLogger().severe(BunnyUniverse.prefix + "The group " + group.getName() + " has an invalid chat configuration! Please check the setting 'chat-layout' in the 'luckperms-api' section in your config.yml.");
            chat = "(invalid config) %name% > ";
        }
        plugin.getLogger().info(BunnyUniverse.prefix + "Chat: " + chat);
        chat = chat.replace("%prefix%", prefix).replace("%name%", player.getName()).replace("%displayname%", displayName);
        nameColor = ChatColor.getLastColors(ChatColor.translateAlternateColorCodes('&', prefix));
        if (BunnyUniverse.debug) {
            if (nameColor == null) plugin.getLogger().warning(BunnyUniverse.prefix + "Could not get the last color of the prefix by " + player.getName() + "! Make sure to put a color code at the end of your prefix, otherwise the player name will always be white.");
            plugin.getLogger().info(BunnyUniverse.prefix + "The player " + player.getName() + " could not be added to a team! Please check your rank configuration");
        }
        return false;
    }
    public static boolean isPlayerInGroup(Player player, String g) {
        LuckPerms api = ExternalPlugins.luckPerms;
        User user = api.getUserManager().getUser(player.getUniqueId());
        String group = user.getPrimaryGroup();
        if (BunnyUniverse.debug) plugin.getLogger().info(BunnyUniverse.prefix + "Checking group of " + player.getName() + ": " + group);
        if (g.equals(group)) {
            return true;
        } else {
            if (BunnyUniverse.debug) plugin.getLogger().info(player.getName() + " has no valid group!");
        }
        return false;
    }
}
