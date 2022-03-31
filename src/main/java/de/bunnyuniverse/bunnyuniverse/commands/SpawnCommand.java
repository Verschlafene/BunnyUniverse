package de.bunnyuniverse.bunnyuniverse.commands;

import de.bunnyuniverse.bunnyuniverse.main.BunnyUniverse;
import de.bunnyuniverse.bunnyuniverse.main.Config;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class SpawnCommand implements CommandExecutor {
    static BunnyUniverse plugin = BunnyUniverse.plugin;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            YamlConfiguration config = Config.getSpawnConfig();
            if (player.hasPermission(config.getString("commandPermission")) || player.isOp()) {
                if (!config.getBoolean("enabled")) return false;
                World world = Bukkit.getWorld(config.getString("world"));
                if (world != null) {
                    player.teleport(new Location(world,
                            config.getInt("x"),
                            config.getInt("y"),
                            config.getInt("z"),
                            (float) config.getDouble("yaw"),
                            (float) config.getDouble("pitch")));
                    player.sendMessage(BunnyUniverse.prefix + config.getString("successMessage"));
                }
            } else {
                if (config.getBoolean("showNotEnoughPermissionsMessage")) player.sendMessage(BunnyUniverse.prefix + config.getString("notEnoughPermissions"));
            }
        } else {
            sender.sendMessage(BunnyUniverse.prefix + "This command can only be used by players!");
        }
        return false;
    }
}
