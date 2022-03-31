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
import org.bukkit.event.player.PlayerJoinEvent;

import de.Herbystar.AVL.Main;

public class PlayerJoinEventHandler implements Listener {
	
	@EventHandler
	public void PlayerJoinEvent(PlayerJoinEvent e) {
		if(Main.instance.forgeChannel.equals("fml:handshake")) {
			PlayerRegisterChannelEventHandler.sendPluginMessage(e.getPlayer(), Main.instance, Main.instance.forgeChannel, new byte[] { 1, 0, 0, 0 });
//			Bukkit.getConsoleSender().sendMessage("Forge Join Message sent!");
		}
		if(Main.instance.getConfig().getBoolean("AVL.Player_Name_Log") == false && Main.instance.getConfig().getBoolean("AVL.Player_UUID_Log") == false && Main.instance.getConfig().getBoolean("AVL.Join_Log") == false) {
			return;
		}
		Player p = e.getPlayer();
		if(p.hasPermission("Avl.bypass.account") && p.hasPermission("Avl.bypass.join")) {
			return;
		}
		if(!p.hasPermission("Avl.bypass.account")) {
			File fp = new File("plugins/AdvancedLogger/Players", p.getAddress().getHostString().toString() + ".yml");
			YamlConfiguration configp = YamlConfiguration.loadConfiguration(fp);
			
			if(Main.instance.getConfig().getBoolean("AVL.Player_UUID_Log") == true) {
				List<String> uuids = configp.getStringList("UUIDs");
				if(uuids.isEmpty()) {
					uuids = new ArrayList<String>();
					if(!uuids.contains(p.getUniqueId().toString())) {
						uuids.add(p.getUniqueId().toString());
					}			
				} else {
					if(!uuids.contains(p.getUniqueId().toString())) {
						uuids.add(p.getUniqueId().toString());
					}
				}
				configp.set("UUIDs", uuids);
			}
			if(Main.instance.getConfig().getBoolean("AVL.Player_Name_Log") == true) {
				List<String> names = configp.getStringList("PlayerNames");
				if(names.isEmpty()) {
					names = new ArrayList<String>();
					if(!names.contains(p.getName())) {
						names.add(p.getName());
					}
				} else {
					if(!names.contains(p.getName())) {
						names.add(p.getName());
					}
				}
				configp.set("PlayerNames", names);
			}
			Main.instance.saveConfig(configp, fp);
		}
		if(!p.hasPermission("Avl.bypass.join") &&  Main.instance.getConfig().getBoolean("AVL.Join_Log") == true) {
			
			Date d = Calendar.getInstance().getTime();
			DateFormat tm = new SimpleDateFormat("dd-MM-yyyy");
			String date = tm.format(d);
			
			Date d1 = Calendar.getInstance().getTime();
			DateFormat tm1 = new SimpleDateFormat("HH:mm:ss");
			String time = tm1.format(d1);
			
			File f = new File("plugins/AdvancedLogger/Joins", date.replace(":", "_") + ".yml");
			YamlConfiguration config = YamlConfiguration.loadConfiguration(f);
			
			Integer totalJoins = config.getInt("TotalJoins");
			totalJoins = totalJoins + 1;
			config.set("TotalJoins", totalJoins);
			List<String> joins = config.getStringList("Joins");
			joins.add(time.toString() + " - " + p.getAddress().getHostString().toString() + " - " + p.getName());
			config.set("Joins", joins);
			Main.instance.saveConfig(config, f);
		}
	}

}
