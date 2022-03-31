package de.Herbystar.AVL.Events;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import de.Herbystar.AVL.Main;

public class PlayerQuitEventHandler implements Listener {
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		
		if(Main.instance.getConfig().getBoolean("AVL.Quit_Log") == false) {
			return;
		}
		
		if(p.hasPermission("Avl.bypass.quit")) {
			return;
		}
		
		Date d = Calendar.getInstance().getTime();
		DateFormat tm = new SimpleDateFormat("dd-MM-yyyy");
		String date = tm.format(d);
		
		Date d1 = Calendar.getInstance().getTime();
		DateFormat tm1 = new SimpleDateFormat("HH:mm:ss");
		String time = tm1.format(d1);
		
		File f = new File("plugins/AdvancedLogger/Quits", date.replace(":", "_") + ".yml");
		YamlConfiguration config = YamlConfiguration.loadConfiguration(f);
		Integer totalQuits = config.getInt("TotalQuits");
		totalQuits = totalQuits + 1;
		config.set("TotalQuits", totalQuits);
		List<String> quits = config.getStringList("Quits");
		quits.add(time.toString() + " - " + p.getAddress().getHostString().toString() + " - " + p.getName());
		config.set("Quits", quits);
		Main.instance.saveConfig(config, f);
		
	}
}
