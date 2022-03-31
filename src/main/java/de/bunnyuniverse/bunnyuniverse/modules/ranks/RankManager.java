package de.bunnyuniverse.bunnyuniverse.modules.ranks;

import de.bunnyuniverse.bunnyuniverse.api.TeamSetEvent;
import de.bunnyuniverse.bunnyuniverse.dependencies.LuckPermsRanks;
import de.bunnyuniverse.bunnyuniverse.main.BunnyUniverse;
import de.bunnyuniverse.bunnyuniverse.main.ExternalPlugins;
import de.bunnyuniverse.bunnyuniverse.utils.Teams;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;

public class RankManager {
    static BunnyUniverse plugin = BunnyUniverse.plugin;
    public static ArrayList<Player> updateDelay = new ArrayList<>();

    public static boolean register(Player player) {
        if (plugin.getConfig().getBoolean("ranks.luckperms-api.enabled")) {
            return LuckPermsRanks.registerLuckPermsAPIRank(player);
        } else if (plugin.getConfig().getString("ranks.permissionsystem").equalsIgnoreCase("api")) {
            TeamSetEvent teamSetEvent = new TeamSetEvent(player);
            Bukkit.getPluginManager().callEvent(teamSetEvent);
            if (!teamSetEvent.isCancelled()) Teams.addPlayer(player, teamSetEvent.getPrefix(), teamSetEvent.getSuffix(), teamSetEvent.getNameColorChar(), teamSetEvent.getChatPrefix(), teamSetEvent.getPlaceholderName(), teamSetEvent.getWeight());
            return true;
        } else {
            int weight = 0;
            for (String line : plugin.getConfig().getConfigurationSection("ranks.list").getValues(false).keySet()) {
                if (!line.contains(".")) {
                    String permission = plugin.getConfig().getString("ranks.list." + line + ".permission");
                    if (ExternalPlugins.luckPerms != null && plugin.getConfig().getString("ranks.permissionsystem").equalsIgnoreCase("luckperms")) {
                        String prefix = plugin.getConfig().getString("ranks.list." + line + ".prefix");
                        String suffix = plugin.getConfig().getString("ranks.list." + line + ".suffix");
                        String chatPrefix = plugin.getConfig().getString("ranks.list." + line + ".chatPrefix");
                        String placeholderName = plugin.getConfig().getString("ranks.list." + line + ".placeholder-name");
                        String nameColor = ChatColor.getLastColors(ChatColor.translateAlternateColorCodes('&', prefix));
                        if (LuckPermsRanks.isPlayerInGroup(player, permission)) {
                            if (BunnyUniverse.debug)
                                plugin.getLogger().info(BunnyUniverse.prefix + "(LuckPerms) The player " + player.getName() + " now has a new rank: Prefix: " + prefix + "; Suffix: " + suffix + "; Group: " + permission);
                            Teams.addPlayer(player, prefix, suffix, nameColor, chatPrefix, placeholderName, weight);
                            Teams teams = Teams.get(player);
                            teams.setPlaceholderName(teams.getNameColor() + teams.getPlaceholderName());
                            return true;
                        }
                    } else {
                        if (player.hasPermission(permission)) {
                            String prefix = plugin.getConfig().getString("ranks.list." + line + ".prefix");
                            String suffix = plugin.getConfig().getString("ranks.list." + line + ".suffix");
                            String chatPrefix = plugin.getConfig().getString("ranks.list." + line + ".chatPrefix");
                            String placeholderName = plugin.getConfig().getString("ranks.list." + line + ".placeholder-name");
                            String nameColor = ChatColor.getLastColors(ChatColor.translateAlternateColorCodes('&', prefix));
                            if (BunnyUniverse.debug) plugin.getLogger().info(BunnyUniverse.prefix + "The player " + player.getName() + " now has a new rank: Prefix: " + prefix + "; Suffix: " + suffix + "; Group: " + permission);
                            Teams.addPlayer(player, prefix, suffix, nameColor, chatPrefix, placeholderName, weight);
                            Teams teams = Teams.get(player);
                            teams.setPlaceholderName(teams.getNameColor() + teams.getPlaceholderName());
                            return true;
                        }
                    }
                    weight++;
                }
            }
            if (Teams.get(player) == null && !plugin.getConfig().getString("ranks.permissionsystem").equalsIgnoreCase("api")) {
                Teams.addPlayer(player, "", "", "f", "noRank", null, -5555);
                plugin.getLogger().warning(BunnyUniverse.prefix + "The player " + player.getName() + " has no Rank! Make sure he has the correct permission.");
            }
        }
        return false;
    }
    public static void setTablistRanks(Player player) {
        delay(player, 20 * 15);
        for (Player all : Bukkit.getOnlinePlayers()) {
            if (all != player) {
                Teams teams = Teams.get(all);
                if (teams != null) {
                    Team team = player.getScoreboard().getTeam(teams.getTeamname());
                    if (team == null) team = player.getScoreboard().registerNewTeam(teams.getTeamname());
                    String prefix = teams.getPrefix();
                    String suffix = teams.getSuffix();
                    ChatColor nameColor = teams.getNameColor();
                    setPrefixSuffix(player, team, prefix, suffix);
                    if (BunnyUniverse.aboveMC_1_13 && nameColor != null) team.setColor(nameColor);
                    team.addEntry(all.getName());
                } else {
                    plugin.getLogger().warning(BunnyUniverse.prefix + "Did not set tablist rank " + all.getName() + " for player " + player.getName() + "!");
                }
            }
        }
        Teams teams = Teams.get(player);
        if (teams != null) {
            ChatColor nameColor = teams.getNameColor();
            String prefix = teams.getPrefix();
            String suffix = teams.getSuffix();
            if (plugin.getConfig().getBoolean("ranks.useUnlimitedLongRanks")) player.setPlayerListName(prefix + player.getDisplayName());
            for (Player all : Bukkit.getOnlinePlayers()) {
                Team team = all.getScoreboard().getTeam(teams.getTeamname());
                if (team == null) team = all.getScoreboard().registerNewTeam(teams.getTeamname());
                setPrefixSuffix(player, team, prefix, suffix);
                if (nameColor != null && BunnyUniverse.aboveMC_1_13) team.setColor(nameColor);
                team.addEntry(player.getName());
            }
        } else {
            plugin.getLogger().severe(BunnyUniverse.prefix + "Did not set rank for already online players (" + player.getName() + ")!");
            if (BunnyUniverse.debug) plugin.getLogger().info(BunnyUniverse.prefix + "Tablist ranks set for player " + player.getName());
        }
    }

    public static boolean updateTablistRanks(Player player) {
        if (updateDelay.contains(player)) return false;
        delay(player, 20 * 15);
        RankManager.register(player);
        try {
            Teams teams = Teams.get(player);
            String prefix = teams.getPrefix();
            String suffix = teams.getSuffix();
            if (plugin.getConfig().getBoolean("ranks.useUnlimitedLongRanks")) player.setPlayerListName(prefix + player.getDisplayName() + suffix);
            if (teams != null) {
                ChatColor nameColor = teams.getNameColor();
                for (Player all : Bukkit.getOnlinePlayers()) {
                    Team team = all.getScoreboard().getTeam(teams.getTeamname());
                    if (team == null) team = all.getScoreboard().registerNewTeam(teams.getTeamname());
                    setPrefixSuffix(player, team, prefix, suffix);
                    if (nameColor != null && BunnyUniverse.aboveMC_1_13) team.setColor(nameColor);
                    team.addEntry(player.getName());
                }
            }
        } catch (Exception e) {
            plugin.getLogger().warning(BunnyUniverse.prefix + "There was an error whilst updating the rank of " + player.getName() + "!");
        }
        return true;
    }
    public static void startTablistRanksUpdateScheduler() {
        int interval = plugin.getConfig().getInt("ranks.update-interval");
        if (interval == -1) return;
        interval = interval * 20 * 60;
        Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, new Runnable() {
            @Override
            public void run() {
                for (Player all : Bukkit.getOnlinePlayers()) {
                    updateTablistRanks(all);
                }
            }
        }, interval, interval);
    }

    public static void setPrefixSuffix(Player player, Team team, String prefix, String suffix) {
        if (!plugin.getConfig().getBoolean("ranks.useUnlimitedLongRanks")) {
            if (!BunnyUniverse.debug) {
                try {
                    if (prefix.length() != 0) team.setPrefix(prefix);
                    if (suffix.length() != 0) team.setSuffix(suffix);
                    player.setPlayerListName(null);
                } catch (IllegalArgumentException e) {
                    if (BunnyUniverse.aboveMC_1_13) {
                        team.setPrefix(ChatColor.RED + "TOO LONG: check console");
                        plugin.getLogger().severe(BunnyUniverse.prefix + "Prefix/Suffix of " + player.getName() + " is too long! The length is limited by Minecraft to 64 chars. Prefix: " + prefix + "; Suffix: " + suffix);
                    } else {
                        plugin.getLogger().severe(BunnyUniverse.prefix + "Prefix/Suffix of " + player.getName() + " is too long! The length is limited by Minecraft to 16 chars."
                                + "If you update your server to 1.13+, the limit will be increased to 64 chars. Prefix: " + prefix + "; Suffix: " + suffix + ";");
                    }
                }
            } else {
                if (prefix.length() != 0) team.setPrefix(prefix);
                if (suffix.length() != 0) team.setSuffix(suffix);
                player.setPlayerListName(null);
            }
        } else {
            team.setPrefix("");
            team.setSuffix("");
        }
    }
    private static void delay(Player player, int i) {
        updateDelay.add(player);
        Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
            @Override
            public void run() {
                updateDelay.remove(player);
            }
        }, i);
    }
}
