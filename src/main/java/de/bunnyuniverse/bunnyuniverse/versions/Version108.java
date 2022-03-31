package de.bunnyuniverse.bunnyuniverse.versions;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

public class Version108 {
    public static Integer getPing(Player player) {
        return ((CraftPlayer) player).getHandle().ping;
    }

    public static Class<?> getNmsClass(String mnsClassName) throws ClassNotFoundException {
        return Class.forName("net.minecraft.server." + Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3] + "." + mnsClassName);
    }

    public static String getServerVersion() {
        return Bukkit.getServer().getClass().getPackage().getName().substring(23);
    }

    public static void sendTab(Player player, String msg1, String msg2) {
        try {
            if ((getServerVersion().equalsIgnoreCase("v1_9_R1")) || (getServerVersion().equalsIgnoreCase("v1_9_R2"))) {
                Object header = getNmsClass("ChatComponentText").getConstructor(new Class[] { String.class }).newInstance(new Object[] { msg1 });
                Object footer = getNmsClass("ChatComponentText").getConstructor(new Class[] { String.class }).newInstance(new Object[] { msg2 });
                Object packetPlayOutPlayerListHeaderFooter = getNmsClass("PacketPlayOutPlayerListHeaderFooter").getConstructor(new Class[] { getNmsClass("IChatBaseComponent") }).newInstance(new Object[] { header });
                Field field = packetPlayOutPlayerListHeaderFooter.getClass().getDeclaredField("b");
                field.setAccessible(true);
                field.set(packetPlayOutPlayerListHeaderFooter, footer);
                Object getHandle = player.getClass().getMethod("getHandle", new Class[0]).invoke(player, new Object[0]);
                Object playerConnection = getHandle.getClass().getField("playerConnection").get(getHandle);
                playerConnection.getClass().getMethod("sendPacket", new Class[] { getNmsClass("Packet") }).invoke(playerConnection, new Object[] { packetPlayOutPlayerListHeaderFooter });
            } else if ((getServerVersion().equalsIgnoreCase("v1_8_R2")) || (getServerVersion().equalsIgnoreCase("v1_8_R3"))) {
                Object header = getNmsClass("IChatBaseComponent$ChatSerializer").getMethod("a", new Class[] { String.class }).invoke(null, new Object[] { "{'text': '" + msg1 + "'}" });
                Object footer = getNmsClass("IChatBaseComponent$ChatSerializer").getMethod("a", new Class[] { String.class }).invoke(null, new Object[] { "{'text': '" + msg2 + "'}" });
                Object packetPlayOutPlayerListHeaderFooter = getNmsClass("PacketPlayOutPlayerListHeaderFooter").getConstructor(new Class[] { getNmsClass("IChatBaseComponent") }).newInstance(new Object[] { header });
                Field field = packetPlayOutPlayerListHeaderFooter.getClass().getDeclaredField("b");
                field.setAccessible(true);
                field.set(packetPlayOutPlayerListHeaderFooter, footer);
                Object getHandle = player.getClass().getMethod("getHandle", new Class[0]).invoke(player, new Object[0]);
                Object playerConnection = getHandle.getClass().getField("playerConnection").get(getHandle);
                playerConnection.getClass().getMethod("sendPacket", new Class[] { getNmsClass("Packet") }).invoke(playerConnection, new Object[] { packetPlayOutPlayerListHeaderFooter });
            } else {
                Object header = getNmsClass("ChatSerializer").getMethod("a", new Class[] { String.class }).invoke(null, new Object[] { "{'text': '" + msg1 + "'}" });
                Object footer = getNmsClass("ChatSerializer").getMethod("a", new Class[] { String.class }).invoke(null, new Object[] { "{'text': '" + msg2 + "'}" });
                Object packetPlayOutPlayerListHeaderFooter = getNmsClass("PacketPlayOutPlayerListHeaderFooter").getConstructor(new Class[] { getNmsClass("IChatBaseComponent") }).newInstance(new Object[] { header });
                Field field = packetPlayOutPlayerListHeaderFooter.getClass().getDeclaredField("b");
                field.setAccessible(true);
                field.set(packetPlayOutPlayerListHeaderFooter, footer);
                Object getHandle = player.getClass().getMethod("getHandle", new Class[0]).invoke(player, new Object[0]);
                Object playerConnection = getHandle.getClass().getField("playerConnection").get(getHandle);
                playerConnection.getClass().getMethod("sendPacket", new Class[] { getNmsClass("Packet") }).invoke(playerConnection, new Object[] { packetPlayOutPlayerListHeaderFooter });
            }
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException | ClassNotFoundException | InstantiationException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }
}
