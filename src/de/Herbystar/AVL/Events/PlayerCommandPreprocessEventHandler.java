package de.Herbystar.AVL.Events;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import de.Herbystar.AVL.Main;

public class PlayerCommandPreprocessEventHandler implements Listener {
	
	
//	private File f2 = new File("plugins/AdvancedLogger", "Blacklist.yml");
	private File f3 = new File("plugins/AdvancedLogger", "CommandsManagement.yml");
	
	@EventHandler
	public void onPlayerCommandPreprocessEvent(PlayerCommandPreprocessEvent e) {
		YamlConfiguration configCommandManagement = YamlConfiguration.loadConfiguration(f3);
		List<String> commandsForChatLog = configCommandManagement.getStringList("AVL.Commands.ForChatLog.List");
		
		if(Main.instance.getConfig().getBoolean("AVL.Command_Log") == false && (commandsForChatLog.size() == 0 || commandsForChatLog == null)) {
			return;
		}
		Player p = e.getPlayer();
		if(p.hasPermission("Avl.bypass.commands")) {
			return;
		}
		for(String s : configCommandManagement.getStringList("AVL.Commands.Blacklist")) {
			if(e.getMessage().contains(s)) {
				return;
			}
		}
		
		Date d = Calendar.getInstance().getTime();
		DateFormat tm = new SimpleDateFormat("dd-MM-yyyy");
		String date = tm.format(d);
		
		Date d1 = Calendar.getInstance().getTime();
		DateFormat tm1 = new SimpleDateFormat("HH:mm:ss");
		String time = tm1.format(d1);
		
		if(commandsForChatLog.size() != 0 && commandsForChatLog != null) {
			for(String s : commandsForChatLog) {
				if(e.getMessage().contains(s)) {
					Main.saveConfigFile("plugins/AdvancedLogger/Chat", "Chat", p.getName(), e.getMessage());
					
					if(configCommandManagement.getBoolean("AVL.Commands.ForChatLog.Exclusive") == true) {
						return;
					}
				}
			}
		}
		
		Main.saveConfigFile("plugins/AdvancedLogger/Commands", "Commands", p.getName(), e.getMessage());
		
		for(Player all : Bukkit.getOnlinePlayers()) {
			UUID allUUID = all.getUniqueId();
			if(Main.cmdspy.containsKey(allUUID) && Main.cmdspy.get(allUUID) == true) {
				all.sendMessage(time.toString() + " - " + p.getName() + " : " + e.getMessage());
			}
		}
		
		File fp = new File("plugins/AdvancedLogger/Players", p.getAddress().getHostString().toString() + ".yml");
		YamlConfiguration configp = YamlConfiguration.loadConfiguration(fp);
		
		List<String> commandsLog = configp.getStringList("Commands");
		if(commandsLog.isEmpty()) {
			commandsLog = new ArrayList<String>();
			commandsLog.add(date + " | " + time + " : " + e.getMessage());
		} else {
			commandsLog.add(date + " | " + time + " : " + e.getMessage());
		}
		configp.set("Commands", commandsLog);		
		Main.instance.saveConfig(configp, fp);
	}

}
