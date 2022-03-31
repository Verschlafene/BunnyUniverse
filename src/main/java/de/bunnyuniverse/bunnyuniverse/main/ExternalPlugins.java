package de.bunnyuniverse.bunnyuniverse.main;

import de.bunnyuniverse.bunnyuniverse.dependencies.BStatsMetrics;
import de.bunnyuniverse.bunnyuniverse.dependencies.LuckPermsListener;
import de.bunnyuniverse.bunnyuniverse.dependencies.PlaceholderAPIExpansion;
import de.bunnyuniverse.bunnyuniverse.dependencies.VaultAPI;
import net.luckperms.api.LuckPerms;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;

public class ExternalPlugins {
    static BunnyUniverse plugin = BunnyUniverse.plugin;
    static Boolean debug = BunnyUniverse.debug;

    public static LuckPerms luckPerms = null;
    public static boolean hasVault = false;
    public static boolean hasPapi = false;
    public static boolean hasLuckPerms = false;

    public static void initializePlugins() {
        if (Bukkit.getPluginManager().isPluginEnabled("Vault")) {
            if (debug) plugin.getLogger().info(BunnyUniverse.prefix + "Loading vault...");
            if (VaultAPI.setupEconomy()) {
                hasVault = true;
                if (debug) plugin.getLogger().info(BunnyUniverse.prefix + "Successfully loaded Vault-Economy!");
            }
        }
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            hasPapi = true;
            new PlaceholderAPIExpansion().register();
        }
        if (Bukkit.getPluginManager().isPluginEnabled("LuckPerms")) {
            hasLuckPerms = true;
            RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
            if (provider != null) luckPerms = provider.getProvider();
            new LuckPermsListener(plugin, luckPerms);
        }
        try {
            int pluginId = 6722;
            BStatsMetrics metrics = new BStatsMetrics(plugin, pluginId);
            metrics.addCustomChart(new BStatsMetrics.SimplePie("update_auto_update", () -> plugin.getConfig().getBoolean("update.autoupdater") ? "Enabled" : "Disabled"));
            metrics.addCustomChart(new BStatsMetrics.SimplePie("update_notifications", () -> plugin.getConfig().getBoolean("update.notification") ? "Enabled" : "Disabled"));
            metrics.addCustomChart(new BStatsMetrics.SimplePie("setting_use_scoreboard", () -> plugin.getConfig().getBoolean("scoreboard") ? "Enabled" : "Disabled"));
            metrics.addCustomChart(new BStatsMetrics.SimplePie("setting_use_tablist_text", () -> plugin.getConfig().getBoolean("tablist.text") ? "Enabled" : "Disabled"));
            metrics.addCustomChart(new BStatsMetrics.SimplePie("setting_use_tablist_ranks", () -> plugin.getConfig().getBoolean("tablist.ranks") ? "Enabled" : "Disabled"));
            metrics.addCustomChart(new BStatsMetrics.SimplePie("setting_use_chat", () -> plugin.getConfig().getBoolean("chat.ranks") ? "Enabled" : "Disabled"));
            metrics.addCustomChart(new BStatsMetrics.SimplePie("setting_permsystem", () -> plugin.getConfig().getString("ranks.permissionsystem").toLowerCase()));
            if (BunnyUniverse.debug) plugin.getLogger().info(BunnyUniverse.prefix + "Analytics sent to bStats!");
        } catch (Exception e) {
            plugin.getLogger().warning(BunnyUniverse.prefix + "Could not send analytics to bStats!");
        }
    }
}
