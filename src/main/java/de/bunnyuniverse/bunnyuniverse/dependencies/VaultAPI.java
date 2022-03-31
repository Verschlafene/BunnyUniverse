package de.bunnyuniverse.bunnyuniverse.dependencies;

import de.bunnyuniverse.bunnyuniverse.main.BunnyUniverse;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.ServicesManager;

public class VaultAPI {
    static BunnyUniverse plugin = BunnyUniverse.plugin;
    public static Economy econ = null;

    public static boolean setupEconomy() {
        RegisteredServiceProvider<Economy> rsp = plugin.getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            plugin.getLogger().warning(BunnyUniverse.prefix + "Error hooking into Vault-Economy! <- Ignore if you don't have an economy plugin.");
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

    public static void setupChat() {
        ServicesManager servicesManager = plugin.getServer().getServicesManager();
        Permission permission = new VaultPermissionsImplementation();
        servicesManager.register(Permission.class, permission, plugin, ServicePriority.Highest);
        servicesManager.register(Chat.class, new VaultChatImplementation(permission), plugin, ServicePriority.Highest);
        plugin.getLogger().info(BunnyUniverse.prefix + "Registered Vault-Chat");
    }
}
