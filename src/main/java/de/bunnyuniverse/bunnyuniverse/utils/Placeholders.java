package de.bunnyuniverse.bunnyuniverse.utils;

import de.bunnyuniverse.bunnyuniverse.api.CustomPlaceholders;
import de.bunnyuniverse.bunnyuniverse.dependencies.VaultAPI;
import de.bunnyuniverse.bunnyuniverse.main.BunnyUniverse;
import de.bunnyuniverse.bunnyuniverse.main.ExternalPlugins;
import de.bunnyuniverse.bunnyuniverse.modules.ranks.RankManager;
import de.bunnyuniverse.bunnyuniverse.versions.*;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Placeholders {
    static BunnyUniverse plugin = BunnyUniverse.plugin;
    public static String hexColorBegin = "", hexColorEnd = "";

    public static ArrayList<CustomPlaceholders> placeholders = new ArrayList<>();

    public static  String replace(Player player, String string) {
        for (CustomPlaceholders placeholders : placeholders) string = placeholders.replace(player, string);
        if (ExternalPlugins.hasPapi && !plugin.getConfig().getBoolean("prefer-plugin-placeholders")) string = PlaceholderAPI.setPlaceholders(player, string);
        if (string.contains("%tps%")) {
            string = string.replace("%tps%", "%server_tps%");
            plugin.getLogger().warning(BunnyUniverse.prefix + "You are using deprecated placeholders! Please change %tps% to %server_tps%");
        }
        if (string.contains("%money%")) {
            string = string.replace("%money%", "%player_money%");
            plugin.getLogger().warning(BunnyUniverse.prefix + "You are using deprecated placeholders! Please change %money% to %player_money%");
        }
        if(string.contains("%rank%")) {
            string = string.replace("%rank%", "%player_rank%");
            plugin.getLogger().warning(BunnyUniverse.prefix + "You are using deprecated placeholders! Please change %rank% to %player_rank%");
        }
        if(string.contains("%name%")) {
            string = string.replace("%name%", "%player_name%");
            plugin.getLogger().warning(BunnyUniverse.prefix + "You are using deprecated placeholders! Please change %name% to %player_name%");
        }
        if(string.contains("%loc_x%")) {
            string = string.replace("%loc_x%", "%player_loc_x%");
            plugin.getLogger().warning(BunnyUniverse.prefix + "You are using deprecated placeholders! Please change %loc_x% to %player_loc_x%");
        }
        if(string.contains("%loc_y%")) {
            string = string.replace("%loc_y%", "%player_loc_y%");
            plugin.getLogger().warning(BunnyUniverse.prefix + "You are using deprecated placeholders! Please change %loc_y% to %player_loc_y%");
        }
        if(string.contains("%loc_z%")) {
            string = string.replace("%loc_z%", "%player_loc_z%");
            plugin.getLogger().warning(BunnyUniverse.prefix + "You are using deprecated placeholders! Please change %loc_z% to %player_loc_z%");
        }
        if(string.contains("%world%")) {
            string = string.replace("%world%", "%player_world%");
            plugin.getLogger().warning(BunnyUniverse.prefix + "You are using deprecated placeholders! Please change %world% to %player_world%");
        }
        if(string.contains("%playeronline%")) {
            string = string.replace("%playeronline%", "%server_online_players%");
            plugin.getLogger().warning(BunnyUniverse.prefix + "You are using deprecated placeholders! Please change %playeronline% to %server_online_players%");
        }
        if(string.contains("%playermax%")) {
            string = string.replace("%playermax%", "%server_max_players%");
            plugin.getLogger().warning(BunnyUniverse.prefix + "You are using deprecated placeholders! Please change %playermax% to %server_max_players%");
        }
        if (string.contains("%server_tps%")) string = string.replace("%server_tps%", "" + TPS.getTPS());
        if (string.contains("%server_online_players%")) string = string.replace("%server_online_players%", "" + Bukkit.getOnlinePlayers().size());
        if (string.contains("%server_max_players%")) string = string.replace("%server_max_players%", "" + Bukkit.getMaxPlayers());
        if (string.contains("%player_name%")) string = string.replace("%player_name%", player.getName());
        if (string.contains("%player_display_name%")) string = string.replace("%player_display_name%", player.getDisplayName());
        if (string.contains("%player_loc_x%")) string = string.replace("%player_loc_x%", "" + player.getLocation().getBlockX());
        if (string.contains("%player_loc_y%")) string = string.replace("%player_loc_y%", "" + player.getLocation().getBlockY());
        if (string.contains("%player_loc_z%")) string = string.replace("%player_loc_z%", "" + player.getLocation().getBlockZ());
        if (string.contains("%player_food%")) string = string.replace("%player_food%", "" + player.getFoodLevel());
        if (string.contains("%player_saturation%")) string = string.replace("%player_saturation%", "" + player.getSaturation());
        if (string.contains("%player_health%")) string = string.replace("%player_health%", "" + player.getHealth());
        if (string.contains("%player_world%")) {
            String world = player.getWorld().getName();
            if (plugin.getConfig().isString("placeholder.world-names." + world)) world = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("placeholder.world-names." + world));
            string = string.replace("%player_world%", world);
        }
        if (string.contains("%player_rank%")) {
            Teams teams = Teams.get(player);
            if(teams == null) {
                RankManager.register(player);
                teams = Teams.get(player);
            }
            if(teams != null) {
                if(teams.getPlaceholderName() == null) {
                    string = string.replace("%player_rank%", teams.getPrefix());
                }else {
                    string = string.replace("%player_rank%", teams.getPlaceholderName());
                }
            }
        }
        if (string.contains("%player_money%")) {
            if (ExternalPlugins.hasVault) {
                int decimals = plugin.getConfig().getInt("placeholder.money-decimals");
                if (decimals != 0) {
                    string = string.replace("%player_money%", "" + MathUtils.round(VaultAPI.econ.getBalance(player), decimals));
                } else {
                    string = string.replace("%player_money%", "" + ((int) VaultAPI.econ.getBalance(player)));
                }
            } else {
                plugin.getLogger().severe(BunnyUniverse.prefix + "Could not get the players money because vault isn't installed or set up!");
                string = string.replace("%player_money%", "Error: see console");
            }
        }
        if (string.contains("%player_ping")) {
            int ping = 0;
            if (BunnyUniverse.getBukkitVersion().compareTo(new Version("1.16")) >= 0) {
                ping = Version116.getPing(player);
            } else if (BunnyUniverse.getBukkitVersion().compareTo(new Version("1.15")) >= 0) {
                ping = Version115.getPing(player);
            } else if (BunnyUniverse.getBukkitVersion().compareTo(new Version("1.14")) >= 0) {
                ping = Version114.getPing(player);
            } else if (BunnyUniverse.getBukkitVersion().compareTo(new Version("1.13")) >= 0) {
                ping = Version113.getPing(player);
            } else if (BunnyUniverse.getBukkitVersion().compareTo(new Version("1.12")) >= 0) {
                ping = Version112.getPing(player);
            } else if (BunnyUniverse.getBukkitVersion().compareTo(new Version("1.11")) >= 0) {
                ping = Version111.getPing(player);
            } else if (BunnyUniverse.getBukkitVersion().compareTo(new Version("1.10")) >= 0) {
                ping = Version110.getPing(player);
            } else if (BunnyUniverse.getBukkitVersion().compareTo(new Version("1.9")) >= 0) {
                ping = Version109.getPing(player);
            } else if (BunnyUniverse.getBukkitVersion().compareTo(new Version("1.8")) >= 0) {
                ping = Version108.getPing(player);
            } else {
                plugin.getLogger().severe(BunnyUniverse.prefix + "You are using a unsupported Minecraft version!");
            }

            if (ping > 999) {
                string = string.replace("%player_ping%", ChatColor.RED+"999+");
            } else {
                string = string.replace("%player_ping%", ping+"");
            }
        }
        if (string.contains("%time%")) {
            String format = plugin.getConfig().getString("placeholder.time-format");
            if (format != null) {
                try {
                    string = string.replace("%time%", new SimpleDateFormat(format).format(new Date()));
                } catch (Exception e) {
                    plugin.getLogger().severe(BunnyUniverse.prefix + "Invalid time format! Please check your placeholder settings in the config!");
                }
            } else {
                string = string.replace("%time%", new SimpleDateFormat("HH:mm").format(new Date()));
            }
        }
        if (string.contains("%date%")) {
            String format = plugin.getConfig().getString("placeholder.date-format");
            if (format != null) {
                try {
                    string = string.replace("%date%", new SimpleDateFormat(format).format(new Date()));
                } catch (Exception e) {
                    plugin.getLogger().severe(BunnyUniverse.prefix + "Invalid date format! Please check your placeholder settings in the config!");
                }
            } else {
                string = string.replace("%date%", new SimpleDateFormat("dd.MM.yyyy").format(new Date()));
            }
        }
        if (string.contains("%mem_total%")) string = string.replace("%mem_total%", "" + getReadableSize((int) getTotalMemory()));
        if (string.contains("%mem_free%")) string = string.replace("%mem_free%", "" + getReadableSize((int) getFreeMemory()));
        if (string.contains("%mem_used%")) string = string.replace("%mem_used%", "" + getReadableSize((int) getUsedMemory()));
        if (string.contains("%mem_max%")) string = string.replace("%mem_max%", "" + getReadableSize((int) getMaxMemory()));
        string = ChatColor.translateAlternateColorCodes('&', string);
        string = translateHexColors(string);
        if (ExternalPlugins.hasPapi && plugin.getConfig().getBoolean("prefer-plugin-placeholders")) string = PlaceholderAPI.setPlaceholders(player, string);
        return string;
    }

    public static long getTotalMemory() {
        return Runtime.getRuntime().totalMemory() / 1048576L;
    }
    public static long getFreeMemory() {
        return Runtime.getRuntime().freeMemory() / 1048576L;
    }
    public static long getMaxMemory() {
        return Runtime.getRuntime().maxMemory() / 1048576L;
    }
    public static long getUsedMemory() {
        return (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1048576L;
    }
    public  static String getReadableSize(int size) {
        String string;
        size *= 1024;
        double m = size / 1024.0;
        double g = size / 1048576.0;
        double t = size / 1073741824.0;
        if (t > 1) {
            DecimalFormat dec = new DecimalFormat("0.00");
            string = dec.format(t).concat("TB");
        } else if (g > 1) {
            DecimalFormat dec = new DecimalFormat("0.00");
            string = dec.format(g).concat("GB");
        } else if (m > 1) {
            DecimalFormat dec = new DecimalFormat("0");
            string = dec.format(m).concat("MB");
        } else {
            DecimalFormat dec = new DecimalFormat("0.00");
            string = dec.format(size).concat("KB");
        }
        return string;
    }
    public final static char COLOR_CHAR = ChatColor.COLOR_CHAR;
    public static String translateHexColors(String message) {
        try {
            final Pattern hexPattern = Pattern.compile(hexColorBegin + "([A-Fa-f0-9]{6})" + hexColorEnd);
            Matcher matcher = hexPattern.matcher(message);
            StringBuffer buffer = new StringBuffer(message.length() + 4 * 8);
            while (matcher.find()) {
                String group = matcher.group(1);
                matcher.appendReplacement(buffer, COLOR_CHAR + "X"
                        + COLOR_CHAR + group.charAt(0) + COLOR_CHAR + group.charAt(1)
                        + COLOR_CHAR + group.charAt(2) + COLOR_CHAR + group.charAt(3)
                        + COLOR_CHAR + group.charAt(4) + COLOR_CHAR + group.charAt(5)
                );
            }
            return matcher.appendTail(buffer).toString();
        } catch (Exception e) {
            plugin.getLogger().severe(BunnyUniverse.prefix + "You have an invalid Hex-Color-Code! Please check syntax... Text: " + message);
            return "InvalidHexColor";
        }
    }
}
