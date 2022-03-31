package de.bunnyuniverse.bunnyuniverse.utils;

import de.bunnyuniverse.bunnyuniverse.main.BunnyUniverse;
import org.bukkit.Bukkit;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class Updater {
    private static final BunnyUniverse plugin = BunnyUniverse.plugin;
    private static final int pluginId = 101047;
    private static String version;

    public static String getVersion() {
        if (version == null) {
            InputStream inputStream;
            try {
                inputStream = new URL("https://api.spigotmc.org/legacy/update.php?resource=" + pluginId).openStream();
                Scanner scanner = new Scanner(inputStream);
                if (scanner.hasNext()) {
                    String d = scanner.next();
                    version = d;
                    return d;
                }
            } catch (IOException e) {
                plugin.getLogger().info(BunnyUniverse.prefix + "Updater -> cannot look for updates: " + e.getMessage());
                return "Could not check for updates! SpigotMC probably blocked your IP. Please wait a few minutes or hours";
            }
            Bukkit.getScheduler().runTaskLaterAsynchronously(BunnyUniverse.plugin, () -> version = null, 20 * 60 * 60);
        }
        return version;
    }

    public static boolean checkVersion() {
        return !getVersion().equals(plugin.getDescription().getVersion());
    }
    public static boolean downloadFile() {
        plugin.getLogger().info(BunnyUniverse.prefix + "Updater -> Downloading newest version...");
        File file = new File("plugis/" + BunnyUniverse.plugin.getDescription().getName() + ".jar");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        String url = "https://github.com/verschlafene/BunnyUniverse/releases/latest/download" + BunnyUniverse.plugin.getDescription().getName() + ".jar";
        try {
            HttpURLConnection connection = (HttpURLConnection) (new URL(url)).openConnection();
            connection.connect();
            FileOutputStream outputStream = new FileOutputStream(file);
            InputStream inputStream = connection.getInputStream();
            byte[] buffer = new byte[1024];
            int readBytes;
            while ((readBytes = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, readBytes);
            }
            outputStream.close();
            inputStream.close();
            connection.disconnect();
            plugin.getLogger().info(BunnyUniverse.prefix + "Updater -> Download finished! Please restart your server.");
            return true;
        } catch (IOException e) {
            plugin.getLogger().info(BunnyUniverse.prefix + "Updater -> Download failed! Please try again later.");
            e.printStackTrace();
            return false;
        }
    }
}
