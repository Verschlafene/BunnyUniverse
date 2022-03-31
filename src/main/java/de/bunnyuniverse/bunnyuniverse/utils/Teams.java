package de.bunnyuniverse.bunnyuniverse.utils;

import de.bunnyuniverse.bunnyuniverse.main.BunnyUniverse;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class Teams {
    public static HashMap<Player, Teams> TeamsList = new HashMap<>();
    private static int TeamCount = 0;

    Player player;
    String prefix;
    String suffix;
    String nameColor;
    String teamName;
    String chatPrefix;
    String placeholderName;

    public Teams(Player player, String prefix, String suffix, String nameColor, String chatPrefix, String placeholderName, int weight) {
        this.player = player;
        this.prefix = prefix;
        this.suffix = suffix;
        this.nameColor = nameColor;
        this.chatPrefix = chatPrefix;
        this.placeholderName = placeholderName;

        if (weight < 0 || weight > 999) {
            BunnyUniverse.plugin.getLogger().warning(BunnyUniverse.prefix + "-----------------------------------------------------------------------------------------------------------------------");
            BunnyUniverse.plugin.getLogger().warning(BunnyUniverse.prefix + "You cannot use negative or above 100 weights! Player: " + player.getName() + " this will cause issues with the sorting.");
            BunnyUniverse.plugin.getLogger().warning(BunnyUniverse.prefix + "-----------------------------------------------------------------------------------------------------------------------");
        }
        TeamCount++;
        this.teamName = String.format("%03d", weight) + "team-" + TeamCount;
    }
    public static Teams addPlayer(Player player, String prefix, String suffix, String nameColor, String chatPrefix, String placeholderName, int weight) {
        Teams teams = new Teams(player, prefix, suffix, nameColor, chatPrefix, placeholderName, weight);
        TeamsList.put(player, teams);
        return teams;
    }
    public static void removePlayer(Player player) {
        if(TeamsList.containsKey(player)) {
            if(player.getScoreboard().getTeam(Teams.get(player).teamName) != null) player.getScoreboard().getTeam(Teams.get(player).teamName).unregister();
            TeamsList.remove(player);
        }
    }
    public static Teams get(Player player) {
        if (TeamsList.containsKey(player)) return TeamsList.get(player);
        return null;
    }

    public String getChatPrefix() {
        return chatPrefix;
    }
    public String getPlaceholderName() {
        return placeholderName;
    }
    public String getPrefix() {
        if (this.player == null) {
            BunnyUniverse.plugin.getLogger().severe(BunnyUniverse.prefix + "An error occurred while reading data for the player " + player.getName() + "!");
            return null;
        }
        if (this.prefix == null) {
            BunnyUniverse.plugin.getLogger().severe(BunnyUniverse.prefix + "An error occurred while reading the prefix of the player " + player.getName() + "!");
            return null;
        }
        return Placeholders.replace(this.player, this.prefix);
    }
    public String getSuffix() {
        if (this.player == null) {
            BunnyUniverse.plugin.getLogger().severe(BunnyUniverse.prefix + "An error occurred while reading data for the player " + player.getName() + "!");
            return null;
        }
        if (this.suffix == null) {
            BunnyUniverse.plugin.getLogger().severe(BunnyUniverse.prefix + "An error occurred while reading the suffix of the player " + player.getName() + "!");
            return null;
        }
        return Placeholders.replace(this.player, this.suffix);
    }
    public ChatColor getNameColor() {
        if (this.player == null) {
            BunnyUniverse.plugin.getLogger().severe(BunnyUniverse.prefix + "An error occurred while reading data for the player " + player.getName() + "!");
            return ChatColor.WHITE;
        }
        if (this.nameColor == null) {
            BunnyUniverse.plugin.getLogger().severe(BunnyUniverse.prefix + "An error occurred while reading the tablist color of the player " + player.getName() + "!");
            return ChatColor.WHITE;
        }

        String nameColors = this.nameColor;
        nameColors = nameColors.replace("&", "").replace("§", "");
        try {
            return ChatColor.getByChar(nameColors);
        } catch (Exception e) {
            return ChatColor.WHITE;
        }
    }
    public String getChat(String message) {
        if (this.player == null) {
            BunnyUniverse.plugin.getLogger().severe(BunnyUniverse.prefix + "An error occurred while reading data for the player " + player.getName() + "!");
            return null;
        }
        if (this.chatPrefix == null) {
            BunnyUniverse.plugin.getLogger().severe(BunnyUniverse.prefix + "An error occurred while player " + player.getName() + " was sending a chat message!");
            return message;
        }
        if (!BunnyUniverse.plugin.getConfig().getString("chat.colorperm").equals("none") && player.hasPermission(BunnyUniverse.plugin.getConfig().getString("chat.colorperm"))) {
            message = ChatColor.translateAlternateColorCodes('&', message);
            if (BunnyUniverse.plugin.getConfig().getBoolean("chat.allowHexColors")) {
                String hex = Placeholders.translateHexColors(message);
                if (!hex.equalsIgnoreCase("InvalidHexColor")) message = hex;
            }
        }
        return Placeholders.replace(player, this.chatPrefix) + message;
    }

    public String getTeamname() {
        if (this.player == null) {
            BunnyUniverse.plugin.getLogger().severe(BunnyUniverse.prefix + "An error occurred while reading the data of the player " + player.getName() + "!");
            return null;
        }
        if (this.teamName == null) {
            BunnyUniverse.plugin.getLogger().severe(BunnyUniverse.prefix + "An error occurred while reading the team-name of the player " + player.getName() + "!");
            return null;
        }
        return this.teamName;
    }

    public void setPlaceholderName(String name) {
        this.placeholderName = name;
    }
    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }
    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }
    public void setNameColor(String color) {
        this.nameColor = color;
    }
}
