package de.Herbystar.AVL.Events;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRegisterChannelEvent;
import org.bukkit.plugin.Plugin;

import de.Herbystar.AVL.Main;

public class PlayerRegisterChannelEventHandler implements Listener {
	
	@EventHandler
	public void onPlayerRegisterChannelEvent(PlayerRegisterChannelEvent e) {
		if(e.getChannel().equals(Main.instance.forgeChannel) && Bukkit.getMessenger().isOutgoingChannelRegistered(Main.instance, Main.instance.forgeChannel)) {
			Player p = e.getPlayer();
//			Bukkit.getConsoleSender().sendMessage("Forge recognized!");
			/**
			 * https://wiki.vg/Minecraft_Forge_Handshake
			 */
			if(Main.instance.forgeChannel.equals("fml:handshake")) {
//				sendPluginMessage(p, Main.instance, Main.instance.forgeChannel, new byte[] { 1, 0, 0, 0});
			} else {
				sendPluginMessage(p, Main.instance, Main.instance.forgeChannel, new byte[] { -2, 0 });
				sendPluginMessage(p, Main.instance, Main.instance.forgeChannel, new byte[] { 0, 2, 0, 0, 0, 0 });
				sendPluginMessage(p, Main.instance, Main.instance.forgeChannel, new byte[] { 2, 0, 0, 0, 0 });
			}
//			Bukkit.getConsoleSender().sendMessage("Forge request send!");
		} else if(e.getChannel().equals("fabric:registry/sync")) {
			//
		}
	}
	
	public static void sendPluginMessage(Player player, Plugin plugin, String channel, byte[] bytes) {
		player.sendPluginMessage(plugin, channel, bytes);
	}
	
}
