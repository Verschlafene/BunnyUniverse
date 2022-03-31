package de.bunnyuniverse.bunnyuniverse.modules.scoreboard;

import de.bunnyuniverse.bunnyuniverse.main.BunnyUniverse;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ScoreboardPlayer {
    static BunnyUniverse plugin = BunnyUniverse.plugin;

    public static HashMap<Player, String> players = new HashMap<>();

    public static void setScoreboard(Player player) {
        Scoreboard scoreboard = player.getScoreboard();
        removeScoreboard(player);
        Objective objective = scoreboard.getObjective(DisplaySlot.SIDEBAR);
        if (objective == null) {
            if (BunnyUniverse.aboveMC_1_13) {
                objective = scoreboard.registerNewObjective("aaa", "bbb", "BUPlugin");
            } else {
                objective = scoreboard.registerNewObjective("aaa", "bbb");
            }
        }
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        ScoreboardManager scoreboardManager = getMatchingScoreboard(player);
        if (scoreboardManager == null) return;
        scoreboardManager.addPlayer(player);
        ScoreboardTitleUtils.setTitle(player, scoreboard, scoreboardManager.getCurrentTitle(), true, scoreboardManager);
        ScoreboardTitleUtils.setScores(player, scoreboard, scoreboardManager.getCurrentScores(), true, scoreboardManager);
        player.setScoreboard(scoreboard);
        if (BunnyUniverse.debug) plugin.getLogger().info(BunnyUniverse.prefix + "Scoreboard set for player " + player.getName());
    }
    public static void updateScoreboard(Player player) {
        if (!players.containsKey(player)) return;
        ScoreboardManager newScoreboard = getMatchingScoreboard(player);
        if (newScoreboard == null) return;
        if (BunnyUniverse.debug) plugin.getLogger().info(BunnyUniverse.prefix + "Scoreboard for player " + player.getName() + " set to " + newScoreboard.getName());
        if (!players.get(player).equals(newScoreboard.getName())) {
            removeScoreboard(player);
            setScoreboard(player);
        }
    }
    public static ScoreboardManager getMatchingScoreboard(Player player) {
        for (Map.Entry<String, ScoreboardManager> entry : ScoreboardManager.scoreboards.entrySet()) {
            ScoreboardManager scoreboardManager = entry.getValue();
            if (scoreboardManager == null) {
                plugin.getLogger().severe(BunnyUniverse.prefix + "There was an error loading a scoreboard! Please check your configurations.");
                return null;
            }
            if (scoreboardManager.conditions == null) {
                plugin.getLogger().severe(BunnyUniverse.prefix + "Could not get Scoreboard " + scoreboardManager.getName() + "! Probably a configuration problem...");
                return null;
            }
            for (String condition : scoreboardManager.conditions) {
                ArrayList<String> andConditions = new ArrayList<>();
                if (condition.contains(" AND ")) {
                    for (String string : condition.split(" AND ")) andConditions.add(string);
                } else {
                    andConditions.add(condition);
                }
                boolean match = true;
                for (String string : andConditions) {
                    if (string.startsWith("world:")) {
                        String value = string.split("world:")[1];
                        if (!(player.getLocation().getWorld().getName().equalsIgnoreCase(value))) match = false;
                    }
                    if (string.startsWith("permissions:")) {
                        String value = string.split("permissions:")[1];
                        if (!(player.hasPermission(value))) match = false;
                    }
                }
                if (match == true) return scoreboardManager;
            }
        }
        return ScoreboardManager.get(plugin.getConfig().getString("scoreboard-default"));
    }
    public static void removeScoreboard(Player player) {
        if (!players.containsKey(player)) return;
        ScoreboardManager.get(players.get(player)).removePlayer(player);
        players.remove(player);
        for (Team team : player.getScoreboard().getTeams()) {
            if (team.getName().startsWith("score-")) team.unregister();
        }
        Objective objective = player.getScoreboard().getObjective(DisplaySlot.SIDEBAR);
        if (objective != null) objective.unregister();
        if (BunnyUniverse.debug) plugin.getLogger().info(BunnyUniverse.prefix + "Removed scoreboard of " + player.getName() + "!");
    }
}
