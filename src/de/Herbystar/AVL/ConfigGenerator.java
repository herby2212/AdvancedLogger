package de.Herbystar.AVL;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.file.YamlConfiguration;

public class ConfigGenerator {
	
	Main plugin;
	public ConfigGenerator(Main main) {
		plugin = main;
	}
	
	public void CreateConfigs() {
		File f1 = new File("plugins/AdvancedLogger", "MySQL.yml");
		YamlConfiguration config1 = YamlConfiguration.loadConfiguration(f1);
		
		config1.addDefault("AVL.MySQL.Enabled", false);
		config1.addDefault("AVL.MySQL.host", "localhost");
		config1.addDefault("AVL.MySQL.port", 3306);
		config1.addDefault("AVL.MySQL.user", "user");
		config1.addDefault("AVL.MySQL.password", "password");
		config1.addDefault("AVL.MySQL.database", "database");	
		config1.options().copyDefaults(true);
		saveConfig(config1, f1);
		
//		File f2 = new File("plugins/AdvancedLogger", "Blacklist.yml");
//		YamlConfiguration config2 = YamlConfiguration.loadConfiguration(f2);
		List<String> commands = new ArrayList<String>();
		commands.add("/help");
//		config2.addDefault("AVL.Commands", commands);
//		config2.options().copyDefaults(true);
//		saveConfig(config2, f2);
		
		File f3 = new File("plugins/AdvancedLogger", "CommandsManagement.yml");
		YamlConfiguration config3 = YamlConfiguration.loadConfiguration(f3);
//		List<String> commands2 = new ArrayList<String>();
//		commands2.add("/help");
		config3.addDefault("AVL.Commands.Blacklist", commands);
		List<String> commandsForChat = new ArrayList<String>();
		commandsForChat.add("/exampleCommand");
		config3.addDefault("AVL.Commands.ForChatLog.Exclusive", false);
		config3.addDefault("AVL.Commands.ForChatLog.List", commandsForChat);
		config3.options().copyDefaults(true);
		saveConfig(config3, f3);
	}
	
	public void saveConfig(YamlConfiguration config, File file) {
		try {
			config.save(file);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

}
