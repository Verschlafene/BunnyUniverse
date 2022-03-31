package de.bunnyuniverse.bunnyuniverse.modules.scoreboard;

import de.bunnyuniverse.bunnyuniverse.main.BunnyUniverse;
import de.bunnyuniverse.bunnyuniverse.main.Config;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.io.File;
import java.io.FilenameFilter;
import java.util.*;

public class ScoreboardManager {
    public static HashMap<String, ScoreboardManager> scoreboards = new HashMap<>();

    String name;
    List<String> conditions;
    HashMap<Integer, ArrayList<String>> scores = new HashMap<>();
    ArrayList<String> title = new ArrayList<>();
    ArrayList<BukkitTask> scheduler = new ArrayList<>();
    ArrayList<Player> players = new ArrayList<>();
    String currentTitle;
    HashMap<Integer, String> currentScores = new HashMap<>();

    private ScoreboardManager(String name) {
        this.name = name;
        File file = new File(BunnyUniverse.pluginFolder + "/scoreboards/" + name + ".yml");
        if (!file.exists()) {
            BunnyUniverse.plugin.getLogger().severe(BunnyUniverse.prefix + "Could not load scoreboard named " + name + " because the config file does not exist!");
            return;
        }
        YamlConfiguration config = Config.loadConfiguration(file);
        if (config == null) {
            unregister(this);
            return;
        }
        conditions = config.getStringList("conditions");
        importScores(config);
        importTitle(config);
    }
    public static ScoreboardManager get(String name) {
        if (!scoreboards.containsKey(name)) scoreboards.put(name, new ScoreboardManager(name));
        return scoreboards.get(name);
    }

    private void importScores(YamlConfiguration config) {
        for (String string : config.getConfigurationSection("").getValues(false).keySet()) {
            try {
                int id = Integer.parseInt(string);
                if (config.getStringList(id + ".scores") != null && !config.getStringList(id + ".scores").isEmpty()) {
                    scores.put(id, new ArrayList<String>());
                    scores.get(id).addAll(config.getStringList(id + ".scores"));
                }
                startScoreAnimation(id, config.getInt(id + ".speed"));
            } catch (Exception ignored) {
            }
        }
        if (scores.size() > 14) BunnyUniverse.plugin.getLogger().warning(BunnyUniverse.prefix + "You have more than 14 scores in your scoreboard! Some scores cannot be displayed. This is a limitation of Minecraft!");
    }

    private void importTitle(YamlConfiguration config) {
        title.addAll(config.getStringList("title.titles"));
        startTitleAnimation(config.getInt("title.speed"));
    }

    private void startTitleAnimation(int speed) {
        if (title.size() == 0) {
            BunnyUniverse.plugin.getLogger().severe(BunnyUniverse.prefix + "Could not load scoreboard title for scoreboard " + name + "!");
            BunnyUniverse.plugin.getLogger().severe(BunnyUniverse.prefix + "Disabling plugin...");
            BunnyUniverse.plugin.getServer().getPluginManager().disablePlugin(BunnyUniverse.plugin);
            return;
        }
        currentTitle = title.get(0);
        if (speed >= 9999 || speed < 0) {
            if (BunnyUniverse.debug) {
                BunnyUniverse.plugin.getLogger().info(BunnyUniverse.prefix + "Scoreboard-Title (" + name + "): no animation needed (Speed higher than 9999 or negative!");
                return;
            }
        } else {
            if (BunnyUniverse.debug) {
                BunnyUniverse.plugin.getLogger().info(BunnyUniverse.prefix + "Scoreboard-Title (" + name + "): animation started!");
            }
        }
        if (title.size() == 0) {
            BunnyUniverse.plugin.getLogger().severe(BunnyUniverse.prefix + "You have an error in your scoreboard config! Even a simple space can cause this. (" + name + ".yml - Title)");
            return;
        }
        scheduler.add(
                Bukkit.getScheduler().runTaskTimerAsynchronously(BunnyUniverse.plugin, new Runnable() {
                    int count = 0;
                    @Override
                    public void run() {
                        if (players.size() != 0) {
                            String string = title.get(count);
                            currentTitle = string;
                            for (Player player : players) {
                                ScoreboardTitleUtils.setTitle(player, player.getScoreboard(), string, true, get(name));
                            }
                            if (count >= title.size() - 1) {
                                count = 0;
                            } else {
                                count++;
                            }
                        }
                    }
                }, 20, speed)
        );
    }

    private void startScoreAnimation(int id, int speed) {
        currentScores.put(id, scores.get(id).get(0));
        if (speed >= 9999 || speed < 0) {
            if (BunnyUniverse.debug) {
                BunnyUniverse.plugin.getLogger().info(BunnyUniverse.prefix + "Scoreboard-Title (" + name + "): no animation needed (Speed higher than 9999 or negative!");
                return;
            }
        } else {
            if (BunnyUniverse.debug) {
                BunnyUniverse.plugin.getLogger().info(BunnyUniverse.prefix + "Scoreboard-Title (" + name + "): animation started!");
            }
        }
        if (scores.size() == 0) {
            BunnyUniverse.plugin.getLogger().severe(BunnyUniverse.prefix + "You have an error in your scoreboard config! Even a simple space can cause this. (" + name + ".yml - Scores)");
            return;
        }
        for (Map.Entry<Integer, ArrayList<String>> entry : scores.entrySet()) {
            if (entry.getValue().size() == 0) {
                BunnyUniverse.plugin.getLogger().severe(BunnyUniverse.prefix + "You have an error in your scoreboard config! Even a simple space can cause this. (" + name + ".yml - Scores)");
                return;
            }
        }
        scheduler.add(
                Bukkit.getScheduler().runTaskTimerAsynchronously(BunnyUniverse.plugin, new Runnable() {
                    int count = 0;
                    @Override
                    public void run() {
                        if (players.size() != 0) {
                            String score = scores.get(id).get(count);
                            int i = scores.size() - id - 1;
                            currentScores.replace(id, score);
                            for (Player player : players) {
                                ScoreboardTitleUtils.setScore(player, player.getScoreboard(), score, i, true, get(name));
                            }
                            if (count >= scores.get(id).size() - 1) {
                                count = 0;
                            } else {
                                count++;
                            }
                        }
                    }
                }, 20, speed)
        );
    }

    public void addPlayer(Player player) {
        if (!players.contains(player)) players.add(player);
        ScoreboardPlayer.players.remove(player);
        ScoreboardPlayer.players.put(player, name);
    }
    public void removePlayer(Player player) {
        players.remove(player);
        ScoreboardPlayer.players.remove(player);
    }
    public String getCurrentTitle() {
        return currentTitle;
    }
    public ArrayList<String> getCurrentScores() {
        return new ArrayList<String>(currentScores.values());
    }
    public String getName() {
        return this.name;
    }
    public static void unregister(ScoreboardManager scoreboardManager) {
        for (BukkitTask task : scoreboardManager.scheduler) task.cancel();
        scoreboards.remove(scoreboardManager.getName());
    }
    public static void registerAlLScoreboards() {
        ArrayList<String> scoreboards = new ArrayList<>();
        File file = new File(BunnyUniverse.pluginFolder + "/scoreboards/");
        FilenameFilter filter = new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".yml");
            }
        };
        File[] files = file.listFiles(filter);
        for (int i = 0; i < files.length; i++) {
            String string = files[i].getName();
            scoreboards.add(string.substring(0, string.lastIndexOf(".yml")));
        }
        new ScoreboardPlayer();
        for (String scoreboard : scoreboards) ScoreboardManager.get(scoreboard);
    }
    public static void unregisterAllScoreboards() {
        for (Iterator<ScoreboardManager> iterator = scoreboards.values().iterator(); iterator.hasNext();) {
            ScoreboardManager scoreboardManager = iterator.next();
            for (BukkitTask task : scoreboardManager.scheduler) task.cancel();
            iterator.remove();
        }
    }
}
