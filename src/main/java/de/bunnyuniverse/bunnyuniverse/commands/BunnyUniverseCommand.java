package de.bunnyuniverse.bunnyuniverse.commands;

import de.bunnyuniverse.bunnyuniverse.main.BunnyUniverse;
import de.bunnyuniverse.bunnyuniverse.modules.scoreboard.ScoreboardPlayer;
import de.bunnyuniverse.bunnyuniverse.utils.Updater;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class BunnyUniverseCommand implements CommandExecutor, TabCompleter {
    static BunnyUniverse plugin = BunnyUniverse.plugin;
    String designLine = BunnyUniverse.prefix + ChatColor.STRIKETHROUGH + "X------------------------X";

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1 && args[0].equalsIgnoreCase("info")) {
            sender.sendMessage(designLine);
            sender.sendMessage(BunnyUniverse.prefix + "Your version: v" + plugin.getDescription().getVersion());
            sender.sendMessage(BunnyUniverse.prefix + "Newest version: v " + Updater.getVersion());
            sender.sendMessage(BunnyUniverse.prefix + "Author: Nikki");
            sender.sendMessage(designLine);
        } else if ((sender instanceof Player) && (args.length == 1 || args.length == 2) && args[0].equalsIgnoreCase("toggle")) {
            Player player = (Player) sender;
            if (player.hasPermission("bunnyuniverse.toggle.scoreboard")) {
                if (ScoreboardPlayer.players.containsKey(player)) {
                    ScoreboardPlayer.removeScoreboard(player);
                    sender.sendMessage(BunnyUniverse.prefix + "Disabled scoreboard!");
                    return true;
                } else {
                    if (plugin.getConfig().getBoolean("scoreboard")) {
                        ScoreboardPlayer.setScoreboard(player);
                        sender.sendMessage(BunnyUniverse.prefix + "Enabled scoreboard!");
                        return true;
                    } else {
                        sender.sendMessage(BunnyUniverse.prefix + "The scoreboard was disabled on this server!");
                    }
                }
            } else {
                sender.sendMessage(BunnyUniverse.prefix + "You need the permission 'bunnyuniverse.toggle.scoreboard' to do that.");
            }
        } else if (args.length == 1 && (args[0].equalsIgnoreCase("reload") || args[0].equalsIgnoreCase("rl"))) {
            if(!(sender instanceof Player) || (sender instanceof Player && ((Player) sender).hasPermission("bunnyuniverse.reload"))) {
                BunnyUniverse.reloadConfigs(sender);
                return true;
            } else {
                sender.sendMessage(BunnyUniverse.prefix + "You need the permission 'bunnyuniverse.reload' to do that.");
            }
        } else if (args.length >= 1 && args[0].equalsIgnoreCase("update")) {
            if (args.length == 2 && args[1].equalsIgnoreCase("confirm")) {
                if (!(sender instanceof Player) || (sender instanceof Player && ((Player) sender).hasPermission("bunnyuniverse.update"))) {
                    sender.sendMessage(BunnyUniverse.prefix + "Downloading the newest version...");
                    if (Updater.downloadFile()) {
                        sender.sendMessage(BunnyUniverse.prefix + "Download finished! Restarting server...");
                        Bukkit.spigot().restart();
                        return true;
                    } else {
                        sender.sendMessage(BunnyUniverse.prefix + "Download failed! Please try again later. More information are available in the console.");
                    }
                } else {
                    sender.sendMessage(BunnyUniverse.prefix + "You need the permission 'bunnyuniverse.update' to do that.");
                }
            } else {
                sender.sendMessage(BunnyUniverse.prefix + "Warning: If you update the plugin, the server will be automatically restarted (if you have a restart script) after the download is finished. "
                        + "Please type '/bunnyuniverse update confirm' to update the plugin.");
            }

        } else if (args.length == 1 && args[0].equalsIgnoreCase("debug")) {
            if(!(sender instanceof Player) || (sender instanceof Player && ((Player) sender).hasPermission("powerboard.debug"))) {
                if (BunnyUniverse.debug) {
                    BunnyUniverse.debug = false;
                    sender.sendMessage(BunnyUniverse.prefix + "Disabled debug.");
                } else {
                    BunnyUniverse.debug = true;
                    sender.sendMessage(BunnyUniverse.prefix + "Enabled debug.");
                }
                return true;
            } else {
                sender.sendMessage(BunnyUniverse.prefix + "You need the permission 'powerboard.debug' to do that.");
            }
        } else {
            sendInfoPage(sender);
        }
        return true;
    }
    public void sendInfoPage(CommandSender sender) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            sender.sendMessage(designLine);
            sender.sendMessage(BunnyUniverse.prefix + "/bu info - Shows all information about the plugin.");
            if (player.hasPermission("bunnyuniverse.toggle.*"))
                sender.sendMessage(BunnyUniverse.prefix + "/bu toggle - Toggles the scoreboard for you.");
            if (player.hasPermission("bunnyuniverse.reload"))
                sender.sendMessage(BunnyUniverse.prefix + "/bu reload - Reloads the configurations.");
            if (player.hasPermission("bunnyuniverse.update"))
                sender.sendMessage(BunnyUniverse.prefix + "/bu update - Downloads the newest version.");
            if (player.hasPermission("bunnyuniverse.debug"))
                sender.sendMessage(BunnyUniverse.prefix + "/bu debug - Toggles the debug.");
                sender.sendMessage(designLine);
        } else {
            sender.sendMessage(designLine);
            sender.sendMessage(BunnyUniverse.prefix + "/bu info - Shows all information about the plugin.");
            sender.sendMessage(BunnyUniverse.prefix + "/bu reload - Reloads the configurations.");
            sender.sendMessage(BunnyUniverse.prefix + "/bu update - Downloads the newest version.");
            sender.sendMessage(BunnyUniverse.prefix + "/bu debug - Toggles the debug.");
            sender.sendMessage(designLine);
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> list = new ArrayList<String>();
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length == 1) {
                list.add("info");
                if (player.hasPermission("bunnyuniverse.toggle.*"))
                    list.add("toggle");
                if (player.hasPermission("bunnyuniverse.reload"))
                    list.add("reload");
                if (player.hasPermission("bunnyuniverse.update"))
                    list.add("update");
                if (player.hasPermission("bunnyuniverse.debug"))
                    list.add("debug");
            }
        } else {
            if (args.length == 1) {
                list.add("info");
                list.add("reload");
                list.add("update");
                list.add("debug");
            }
        }
        return list;
    }
}
