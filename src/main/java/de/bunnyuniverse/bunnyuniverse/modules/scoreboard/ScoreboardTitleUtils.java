package de.bunnyuniverse.bunnyuniverse.modules.scoreboard;

import de.bunnyuniverse.bunnyuniverse.main.BunnyUniverse;
import de.bunnyuniverse.bunnyuniverse.utils.Placeholders;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;

public class ScoreboardTitleUtils {
    public static boolean setTitle(Player player, Scoreboard scoreboard, String title, boolean usePlaceholders, ScoreboardManager scoreboardManager) {
        Objective objective = scoreboard.getObjective(DisplaySlot.SIDEBAR);
        if (objective == null) return false;
        if (usePlaceholders) title = Placeholders.replace(player, title);
        if (!BunnyUniverse.debug) {
            try {
                objective.setDisplayName(title);
            } catch (IllegalArgumentException e) {
                if (BunnyUniverse.aboveMC_1_13) {
                    objective.setDisplayName(ChatColor.RED + "Error: too long - check console!");
                    BunnyUniverse.plugin.getLogger().warning(BunnyUniverse.prefix + " ");
                    BunnyUniverse.plugin.getLogger().warning(BunnyUniverse.prefix + "-> The scoreboard-title is too long! The limit is 128 chars!");
                    if (scoreboardManager != null) BunnyUniverse.plugin.getLogger().warning(BunnyUniverse.prefix + "-> Scoreboard " + scoreboardManager.getName());
                    BunnyUniverse.plugin.getLogger().warning(BunnyUniverse.prefix + "-> Title: " + title);
                    BunnyUniverse.plugin.getLogger().warning(BunnyUniverse.prefix + "-> Player: " + player.getName());
                    BunnyUniverse.plugin.getLogger().warning(BunnyUniverse.prefix + " ");
                } else {
                    objective.setDisplayName(ChatColor.RED + "TOO LONG");
                    BunnyUniverse.plugin.getLogger().warning(BunnyUniverse.prefix + " ");
                    BunnyUniverse.plugin.getLogger().warning(BunnyUniverse.prefix + "-> The scoreboard-title is too long! The limit is 16 chars!");
                    if (scoreboardManager != null) BunnyUniverse.plugin.getLogger().warning(BunnyUniverse.prefix + "-> Scoreboard " + scoreboardManager.getName());
                    BunnyUniverse.plugin.getLogger().warning(BunnyUniverse.prefix + "-> Title: " + title);
                    BunnyUniverse.plugin.getLogger().warning(BunnyUniverse.prefix + "-> Player: " + player.getName());
                    BunnyUniverse.plugin.getLogger().warning(BunnyUniverse.prefix + " ");
                }
            }
        } else {
            objective.setDisplayName(title);
        }
        if (scoreboardManager != null) scoreboardManager.addPlayer(player);
        return true;
    }
    public static boolean setScores(Player player, Scoreboard scoreboard, ArrayList<String> scores, boolean usePlaceholders, ScoreboardManager scoreboardManager) {
        Objective objective = scoreboard.getObjective(DisplaySlot.SIDEBAR);
        if (objective == null) return false;
        for (int i = 0; i < scores.size(); i++) {
            int id = scores.size() - i - 1;
            setScore(player, scoreboard, scores.get(id), i, usePlaceholders, scoreboardManager);
        }
        return true;
    }
    public static boolean setScore(Player player, Scoreboard scoreboard, String score, int scoreId, boolean usePlaceholders, ScoreboardManager scoreboardManager) {
        Objective objective = scoreboard.getObjective(DisplaySlot.SIDEBAR);
        if (objective == null) return false;
        String colorCode = "§" + scoreId;
        if (scoreId > 9) {
            if (scoreId == 10) colorCode = "§a";
            if (scoreId == 11) colorCode = "§b";
            if (scoreId == 12) colorCode = "§c";
            if (scoreId == 13) colorCode = "§d";
            if (scoreId == 14) colorCode = "§e";
        }
        Team team = scoreboard.getTeam("score-" + scoreId);
        if (team == null) {
            team = scoreboard.registerNewTeam("score-" + scoreId);
            team.addEntry(colorCode);
            objective.getScore(colorCode).setScore(scoreId);
        }
        if (score.length() == 0) score = " ";
        if (!score.equals(" ") && usePlaceholders) score = Placeholders.replace(player, score);
        if (BunnyUniverse.aboveMC_1_13) {
            String[] string = getScorePrefixSuffix(score, 16, 30);
            if (string == null) {
                team.setPrefix(ChatColor.RED + "TOO LONG");
                BunnyUniverse.plugin.getLogger().warning(BunnyUniverse.prefix + " ");
                BunnyUniverse.plugin.getLogger().warning(BunnyUniverse.prefix + "-> The scoreboard-score is too long! The limit is 30 chars!");
                if (scoreboardManager != null) BunnyUniverse.plugin.getLogger().warning(BunnyUniverse.prefix + "-> Scoreboard " + scoreboardManager.getName());
                BunnyUniverse.plugin.getLogger().warning(BunnyUniverse.prefix + "-> Score: " + score);
                BunnyUniverse.plugin.getLogger().warning(BunnyUniverse.prefix + "-> Chars: " + score.length());
                BunnyUniverse.plugin.getLogger().warning(BunnyUniverse.prefix + "-> Player: " + player.getName());
                BunnyUniverse.plugin.getLogger().warning(BunnyUniverse.prefix + " ");
            } else {
                team.setPrefix(string[0]);
                team.setSuffix(string[1]);
            }
        } else {
            String[] string = getScorePrefixSuffix(score, 16, 30);
            if (string == null) {
                team.setPrefix(ChatColor.RED + "TOO LONG");
                BunnyUniverse.plugin.getLogger().warning(BunnyUniverse.prefix + " ");
                BunnyUniverse.plugin.getLogger().warning(BunnyUniverse.prefix + "-> The scoreboard-score is too long! The limit is 30 chars!");
                if (scoreboardManager != null)
                    BunnyUniverse.plugin.getLogger().warning(BunnyUniverse.prefix + "-> Scoreboard " + scoreboardManager.getName());
                BunnyUniverse.plugin.getLogger().warning(BunnyUniverse.prefix + "-> Score: " + score);
                BunnyUniverse.plugin.getLogger().warning(BunnyUniverse.prefix + "-> Chars: " + score.length());
                BunnyUniverse.plugin.getLogger().warning(BunnyUniverse.prefix + "-> Player: " + player.getName());
                BunnyUniverse.plugin.getLogger().warning(BunnyUniverse.prefix + " ");
            } else {
                team.setPrefix(string[0]);
                team.setSuffix(string[1]);
            }
        }
        if (scoreboardManager != null) scoreboardManager.addPlayer(player);
        return true;
    }
    public static String[] getScorePrefixSuffix(String score, int split, int maxChars) {
        String[] string = new String[2];
        if(score.length() > maxChars)
            return null;
        if(score.length() > split) {
            string[0] = score.substring(0, split);
            string[1] = ChatColor.getLastColors(string[0])+score.substring(split);
        } else {
            string[0] = score;
            string[1] = "";
        }
        return string;
    }
}
