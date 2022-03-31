package de.bunnyuniverse.bunnyuniverse.versions;

import net.minecraft.server.v1_10_R1.ChatMessage;
import net.minecraft.server.v1_10_R1.IChatBaseComponent;
import net.minecraft.server.v1_10_R1.PacketPlayOutPlayerListHeaderFooter;
import org.bukkit.craftbukkit.v1_10_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;

public class Version110 {
    public static Integer getPing(Player player) {
        return ((CraftPlayer) player).getHandle().ping;
    }
    public static void sendTab(Player player, String head, String foot){
        IChatBaseComponent header = new ChatMessage(head);
        IChatBaseComponent footer = new ChatMessage(foot);
        PacketPlayOutPlayerListHeaderFooter packetPlayOutPlayerListHeaderFooter = new PacketPlayOutPlayerListHeaderFooter();
        try {
            Field headerField = packetPlayOutPlayerListHeaderFooter.getClass().getDeclaredField("a");
            headerField.setAccessible(true);
            headerField.set(packetPlayOutPlayerListHeaderFooter, header);
            headerField.setAccessible(!headerField.isAccessible());
            Field footerField = packetPlayOutPlayerListHeaderFooter.getClass().getDeclaredField("b");
            footerField.setAccessible(true);
            footerField.set(packetPlayOutPlayerListHeaderFooter, footer);
            footerField.setAccessible(!footerField.isAccessible());
        } catch (Exception e) {
            e.printStackTrace();
        }
        CraftPlayer craftPlayer = (CraftPlayer) player;
        craftPlayer.getHandle().playerConnection.sendPacket(packetPlayOutPlayerListHeaderFooter);
    }
}
