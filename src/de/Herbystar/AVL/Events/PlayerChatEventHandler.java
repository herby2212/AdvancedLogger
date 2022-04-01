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
import org.bukkit.event.player.PlayerChatEvent;

import de.Herbystar.AVL.Main;

@SuppressWarnings("deprecation")
public class PlayerChatEventHandler implements Listener {
	
	@EventHandler
	public void onPlayerChatEvent(PlayerChatEvent e) {
		if(Main.instance.getConfig().getBoolean("AVL.Chat_Log") == false) {
			return;
		}
		Player p = e.getPlayer();		
		if(p.hasPermission("Avl.bypass.chat")) {
			return;
		}
		Date d = Calendar.getInstance().getTime();
		DateFormat tm = new SimpleDateFormat("dd-MM-yyyy");
		String date = tm.format(d);
		
		Date d1 = Calendar.getInstance().getTime();
		DateFormat tm1 = new SimpleDateFormat("HH:mm:ss");
		String time = tm1.format(d1);
		
		File f = new File("plugins/AdvancedLogger/Chat", date.replace(":", "_") + ".yml");
		YamlConfiguration config = YamlConfiguration.loadConfiguration(f);
		List<String> chat = config.getStringList("Chat");
		chat.add(time.toString() + " - " + p.getName() + " : " + e.getMessage());
		config.set("Chat", chat);
		Main.instance.saveConfig(config, f);
		
		File fp = new File("plugins/AdvancedLogger/Players", p.getAddress().getHostString().toString() + ".yml");
		YamlConfiguration configp = YamlConfiguration.loadConfiguration(fp);
		
		List<String> chatLog = configp.getStringList("Chat");
		if(chatLog.isEmpty()) {
			chatLog = new ArrayList<String>();
			chatLog.add(date + " | " + time + " : " + e.getMessage());
		} else {
			chatLog.add(date + " | " + time + " : " + e.getMessage());
		}
		configp.set("Chat", chatLog);		
		Main.instance.saveConfig(configp, fp);
	}
	
}
