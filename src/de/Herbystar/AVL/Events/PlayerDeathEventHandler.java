package de.Herbystar.AVL.Events;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import de.Herbystar.AVL.Main;

public class PlayerDeathEventHandler implements Listener {
	
	@EventHandler
	public void onPlayerDeathEvent(PlayerDeathEvent e) {
		Player p = e.getEntity();
		
		if(Main.instance.getConfig().getBoolean("AVL.KillDeathLog") == false) {
			return;
		}

		try {			
			Date d = Calendar.getInstance().getTime();
			DateFormat tm = new SimpleDateFormat("dd-MM-yyyy");
			String date = tm.format(d);
			
			Date d1 = Calendar.getInstance().getTime();
			DateFormat tm1 = new SimpleDateFormat("HH:mm:ss");
			String time = tm1.format(d1);
			
			File f = new File("plugins/AdvancedLogger/KillDeath", date.replace(":", "_") + ".yml");
			YamlConfiguration config = YamlConfiguration.loadConfiguration(f);
			List<String> deaths = config.getStringList("Death-Kill");
			
			File fp = new File("plugins/AdvancedLogger/Players", p.getAddress().getHostString().toString() + ".yml");
			YamlConfiguration configp = YamlConfiguration.loadConfiguration(fp);
			List<String> death = configp.getStringList("Death");
					
			if(p.hasPermission("Avl.bypass.death")) {
				return;
			}
			
			if(p.getKiller() != null) {
				Player killer = p.getKiller();
				
				File fk = new File("plugins/AdvancedLogger/Players", killer.getAddress().getHostString().toString() + ".yml");
				YamlConfiguration configk = YamlConfiguration.loadConfiguration(fk);
				List<String> kill = configk.getStringList("Kill");
				
				if(!killer.hasPermission("Avl.bypass.kill")) {				
					if(death.isEmpty()) {
						death = new ArrayList<String>();
						death.add(date + " | " + time + " <- " + killer.getName());
					} else {
						death.add(date + " | " + time + " <- " + killer.getName());
					}
					
					if(kill.isEmpty()) {
						kill = new ArrayList<String>();
						kill.add(date + " | " + time + " -> " + p.getName());
					} else {
						kill.add(date + " | " + time + " -> " + p.getName());
					}
					
					configk.set("Kill", kill);
					Main.instance.saveConfig(configk, fk);
				
					deaths.add(time.toString() + " | " + p.getName() + " <- " + killer.getName());
				}
			} else {
				if(death.isEmpty()) {
					death = new ArrayList<String>();
					death.add(date + " | " + time);
				} else {
					death.add(date + " | " + time);
				}

				deaths.add(time.toString() + " | " + p.getName() + " <- Unknown");
			}	
			configp.set("Death", death);		
			Main.instance.saveConfig(configp, fp);		
			
			config.set("Death-Kill", deaths);
			Main.instance.saveConfig(config, f);
		} catch(NullPointerException ex) {
			return;
		}
	}

}
