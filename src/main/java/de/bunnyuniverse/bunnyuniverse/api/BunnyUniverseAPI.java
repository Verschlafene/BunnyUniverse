package de.bunnyuniverse.bunnyuniverse.api;

import de.bunnyuniverse.bunnyuniverse.main.BunnyUniverse;
import de.bunnyuniverse.bunnyuniverse.modules.ranks.RankManager;
import de.bunnyuniverse.bunnyuniverse.modules.scoreboard.ScoreboardPlayer;
import de.bunnyuniverse.bunnyuniverse.modules.scoreboard.ScoreboardTitleUtils;
import de.bunnyuniverse.bunnyuniverse.utils.Placeholders;
import de.bunnyuniverse.bunnyuniverse.utils.Teams;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class BunnyUniverseAPI {public static void registerCustomPlaceholders(CustomPlaceholders placeholders) {
    Placeholders.placeholders.add(placeholders);
}
    public static void setScoreboard(Player player) {
        ScoreboardPlayer.setScoreboard(player);
    }
    public static void removeScoreboard(Player player) {
        ScoreboardPlayer.removeScoreboard(player);
    }

    public static void setScoreboardTitle(Player player, String title, boolean usePlaceholders) {
        if (!ScoreboardTitleUtils.setTitle(player, player.getScoreboard(), title, usePlaceholders, null)) {
            BunnyUniverse.plugin.getLogger().severe(BunnyUniverse.prefix + "Failed to set scoreboard title! The scoreboard is not registered yet - please set the scoreboard first!");
        }
    }
    public static void setScoreboardScore(Player player, String score, int index, boolean usePlaceholders) {
        if (!ScoreboardTitleUtils.setScore(player, player.getScoreboard(), score, index, usePlaceholders, null)) {
            BunnyUniverse.plugin.getLogger().severe(BunnyUniverse.prefix + "Failed to set scoreboard score! The scoreboard is not registered yet - please set the scoreboard first!");
        }
    }
    public static void setScoreboardScores(Player player, ArrayList<String> scores, boolean usePlaceholders) {
        if (!ScoreboardTitleUtils.setScores(player, player.getScoreboard(), scores, usePlaceholders, null)) {
            BunnyUniverse.plugin.getLogger().severe(BunnyUniverse.prefix + "Failed to set scoreboard scores! The scoreboard is not registered yet - please set the scoreboard first!");
        }
    }
    public static boolean setPrefix(Player player, String prefix) {
        Teams teams = Teams.get(player);
        if (teams == null) return false;
        teams.setPrefix(prefix);
        return true;
    }
    public static boolean setSuffix(Player player, String suffix) {
        Teams teams = Teams.get(player);
        if (teams == null) return false;
        teams.setSuffix(suffix);
        return true;
    }
    public static boolean setNameColorChar(Player player, String colorChar) {
        Teams teams = Teams.get(player);
        if (teams == null) return false;
        teams.setNameColor(colorChar);
        return true;
    }
    public String getPrefix(Player player) {
        Teams teams = Teams.get(player);
        if (teams == null) return null;
        return teams.getPrefix();
    }
    public String getSuffix(Player player) {
        Teams teams = Teams.get(player);
        if (teams == null) return null;
        return teams.getSuffix();
    }
    public ChatColor getNameColor(Player player) {
        Teams teams = Teams.get(player);
        if (teams == null) return null;
        return teams.getNameColor();
    }

    public static void updateTablistRanks(Player player) {
        RankManager.updateTablistRanks(player);
    }
}
