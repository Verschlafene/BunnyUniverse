package de.bunnyuniverse.bunnyuniverse.versions;

import net.minecraft.server.v1_16_R3.IChatBaseComponent;
import net.minecraft.server.v1_16_R3.PacketPlayOutPlayerListHeaderFooter;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class Version116 {
    public static Integer getPing(Player player) {
        return ((CraftPlayer) player).getHandle().ping;
    }
    public static void sendTab(Player player, String msg1, String msg2) {
        PacketPlayOutPlayerListHeaderFooter packetPlayOutPlayerListHeaderFooter = new PacketPlayOutPlayerListHeaderFooter();
        packetPlayOutPlayerListHeaderFooter.header = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + msg1 + "\"}");
        packetPlayOutPlayerListHeaderFooter.footer = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + msg2 + "\"}");
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packetPlayOutPlayerListHeaderFooter);
    }
}
