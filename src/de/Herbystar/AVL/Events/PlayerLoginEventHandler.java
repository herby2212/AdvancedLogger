package de.Herbystar.AVL.Events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;

import de.Herbystar.AVL.Main;

public class PlayerLoginEventHandler implements Listener {
	
	Main plugin;
	public PlayerLoginEventHandler(Main main) {
		plugin = main;
	}
	
	@EventHandler
	public void PlayerLoginEvent(PlayerLoginEvent e) {
		if(Main.forgeModsByPlayer.containsKey(e.getPlayer().getUniqueId()) && Main.instance.getConfig().getBoolean("AVL.BlockPlayersWithMods.Enabled") == true) {
			e.disallow(Result.KICK_OTHER, Main.instance.getConfig().getString("AVL.BlockPlayersWithMods.KickMessage"));
		}
	}

}
