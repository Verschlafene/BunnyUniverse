package de.bunnyuniverse.bunnyuniverse.commands;

import de.bunnyuniverse.bunnyuniverse.main.BunnyUniverse;
import de.bunnyuniverse.bunnyuniverse.main.Config;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class SetSpawnCommand implements CommandExecutor {
    static BunnyUniverse plugin = BunnyUniverse.plugin;
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            YamlConfiguration config = Config.getSpawnConfig();
            if (player.hasPermission(config.getString("setCommandPermission")) || player.isOp()) {
                if (!config.getBoolean("enabled")) return false;
                Location location = player.getLocation();
                Config.setSpawn(location.getWorld().getName(), location.getBlockX(), location.getBlockY(), location.getBlockZ(), location.getYaw(), location.getPitch());
                player.sendMessage(BunnyUniverse.prefix + config.getString("setSuccessMessage"));
            } else {
                if (config.getBoolean("showNotEnoughPermissionMessage")) player.sendMessage(BunnyUniverse.prefix + config.getString("notEnoughPermissions"));
            }
        } else {
            sender.sendMessage(BunnyUniverse.prefix + "This command can only be used by players!");
        }
        return false;
    }
}
