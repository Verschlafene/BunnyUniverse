package de.bunnyuniverse.bunnyuniverse.modules.tablist;

import de.bunnyuniverse.bunnyuniverse.main.BunnyUniverse;
import de.bunnyuniverse.bunnyuniverse.utils.Placeholders;
import de.bunnyuniverse.bunnyuniverse.utils.SelfCheck;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TablistManager {
    private static BunnyUniverse plugin = BunnyUniverse.plugin;
    public static HashMap<String, TablistManager> tablists = new HashMap<>();
    String name;
    List<String> conditions;
    HashMap<Integer, ArrayList<String>> headers = new HashMap<>();
    HashMap<Integer, ArrayList<String>> footers = new HashMap<>();
    HashMap<Integer, String> currentHeader = new HashMap<>();
    HashMap<Integer, String> currentFooter = new HashMap<>();
    ArrayList<BukkitTask> scheduler = new ArrayList<>();
    ArrayList<Player> players = new ArrayList<>();

    private TablistManager(String name) {
        this.name = name;
        File file = new File(BunnyUniverse.pluginFolder + "/" + name + ".yml");
        if (!file.exists()) {
            plugin.getLogger().severe(BunnyUniverse.prefix + "Could not load tablist named " + name + ", because the config file does not exist");
            return;
        }
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        conditions = config.getStringList("conditions");
        register(config);
    }
    public static TablistManager get(String name) {
        if (!tablists.containsKey(name)) tablists.put(name, new TablistManager(name));
        return tablists.get(name);
    }
    private void register(YamlConfiguration config) {
        SelfCheck.checkTablist(name, config);
        for (String line : config.getConfigurationSection("header").getValues(false).keySet()) {
            try {
                int i = Integer.parseInt(line);
                if (!headers.containsKey(i)) headers.put(i, new ArrayList<String>());
                for (String header : config.getStringList("header." + i + ".lines")) {
                    headers.get(i).add(header);
                }
            } catch (Exception e) {
                plugin.getLogger().severe(BunnyUniverse.prefix + "Wrong header entry in tablist " + name + "! Line: " + line);
                return;
            }
        }
        for (String line : config.getConfigurationSection("footer").getValues(false).keySet()) {
            try {
                int i = Integer.parseInt(line);
                if (!footers.containsKey(i))
                    footers.put(i, new ArrayList<String>());
                for (String footer : config.getStringList("footer." + i + ".lines")) {
                    footers.get(i).add(footer);
                }
            } catch (Exception e) {
                plugin.getLogger().severe(BunnyUniverse.prefix + "Wrong footer entry in tablist " + name + "! Line: " + line);
            }
        }
        startAnimation(config);
    }
    private void startAnimation(YamlConfiguration config) {
        int interval = 20 * 60;
        for(int line : headers.keySet()) {
            int speed = config.getInt("header." + line + ".speed");
            if(speed < 1) speed = 1;
            scheduler.add(
                    Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, new Runnable() {
                        int step = 0;
                        @Override
                        public void run() {
                            String text = headers.get(line).get(step);
                            currentHeader.put(line, text);
                            if (step >= headers.get(line).size() - 1) {
                                step = 0;
                            } else {
                                step++;
                            }
                        }
                    }, 0, speed)
            );
            interval = Math.min(interval, speed);
        }
        for(int line : footers.keySet()) {
            int speed = config.getInt("footer." + line + ".speed");
            if(speed < 1) speed = 1;
            scheduler.add(
                    Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, new Runnable() {
                        int step = 0;
                        @Override
                        public void run() {
                            String text = footers.get(line).get(step);
                            currentFooter.put(line, text);
                            if (step >= footers.get(line).size() - 1) {
                                step = 0;
                            } else {
                                step++;
                            }
                        }
                    }, 0, speed)
            );
            interval = Math.min(interval, speed);
        }

        scheduler.add(
                Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, () -> {
                    for(Player player : players) {
                        sendPlayer(player);
                    }
                }, 20, interval)
        );
        if (BunnyUniverse.debug) plugin.getLogger().info(BunnyUniverse.prefix + "Tablist " +  name + " loaded");
    }

    private void sendPlayer(Player player) {
        String header = "", footer = "";
        for(Map.Entry<Integer, String> entry : currentHeader.entrySet()) header += entry.getValue() + "\n";
        for(Map.Entry<Integer, String> entry : currentFooter.entrySet()) footer += entry.getValue() + "\n";
        if(header.length() > 0) header = header.substring(0, header.length()-1);
        if(footer.length() > 0) footer = footer.substring(0, footer.length()-1);
        header = Placeholders.replace(player, header);
        footer = Placeholders.replace(player, footer);
        TabPackage.send(player, header, footer);
    }
    public String getName() {
        return this.name;
    }
    public void unregister() {
        for(BukkitTask task : scheduler) task.cancel();
        tablists.remove(name);
    }
    public static void unregisterAllTablists() {
        for(TablistManager tablistManager : tablists.values()) for(BukkitTask task : tablistManager.scheduler) task.cancel();
        tablists.clear();
    }
    public static void registerAllTablists() {
        TablistManager.get("tablist");
    }

    public void addPlayer(Player player) {
        if(!players.contains(player)) {
            players.add(player);
            sendPlayer(player);
        }
    }
    public void removePlayer(Player player, boolean sendBlankTablist) {
        if(players.contains(player)) {
            players.remove(player);
            if(sendBlankTablist) TabPackage.send(player, null, null);
        }
    }
}
