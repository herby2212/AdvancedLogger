package de.Herbystar.AVL.Events;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.bukkit.command.BlockCommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerCommandEvent;

import de.Herbystar.AVL.Main;

public class ServerCommandEventHandler implements Listener {
	
	Main plugin;
	public ServerCommandEventHandler(Main main) {
		plugin = main;
	}
	
	@EventHandler
	public void onServerCommandEvent(ServerCommandEvent e) {
		if(Main.instance.getConfig().getBoolean("AVL.Console_Commands_Log") == false) {
			return;
		}
		if(e.getSender() instanceof BlockCommandSender) {
			if(Main.instance.getConfig().getBoolean("AVL.CommandBlock_Logging") == false) {
				return;
			}
		}
		Date d = Calendar.getInstance().getTime();
		DateFormat tm = new SimpleDateFormat("dd-MM-yyyy");
		String date = tm.format(d);
		
		Date d1 = Calendar.getInstance().getTime();
		DateFormat tm1 = new SimpleDateFormat("HH:mm:ss");
		String time = tm1.format(d1);
		
		File f = new File("plugins/AdvancedLogger/Console_Commands", date.replace(":", "_") + ".yml");
		YamlConfiguration config = YamlConfiguration.loadConfiguration(f);
		List<String> commands = config.getStringList("Console_Commands");
		commands.add(time.toString() + " : " + e.getCommand().toString());
		config.set("Console_Commands", commands);
		Main.instance.saveConfig(config, f);
	}
	
	

}
