package de.Herbystar.AVL.Events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

import de.Herbystar.AVL.Main;

public class PlayerLoginEventHandler implements Listener {
	
	Main plugin;
	public PlayerLoginEventHandler(Main main) {
		plugin = main;
	}
	
	@EventHandler
	public void PlayerLoginEvent(PlayerLoginEvent e) {		
	}

}
