package de.Herbystar.AVL;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import de.Herbystar.AVL.Events.PlayerChatEventHandler;
import de.Herbystar.AVL.Events.PlayerCommandPreprocessEventHandler;
import de.Herbystar.AVL.Events.PlayerDeathEventHandler;
import de.Herbystar.AVL.Events.PlayerJoinEventHandler;
import de.Herbystar.AVL.Events.PlayerLoginEventHandler;
import de.Herbystar.AVL.Events.PlayerQuitEventHandler;
import de.Herbystar.AVL.Events.PlayerRegisterChannelEventHandler;
import de.Herbystar.AVL.Events.PluginMessageReceivedHandler;
import de.Herbystar.AVL.Events.ServerCommandEventHandler;



public class Main extends JavaPlugin {
	
	public static Main instance;
	public boolean Update;
	public String forgeChannel;
	public String prefix = "§6[§cAdvancedLogger§6] ";
	private File f1 = new File("plugins/AdvancedLogger", "MySQL.yml");
	private YamlConfiguration config = YamlConfiguration.loadConfiguration(f1);
	public static HashMap<UUID, Boolean> cmdspy = new HashMap<UUID, Boolean>();
	public static HashMap<UUID, List<ForgeMod>> forgeModsByPlayer = new HashMap<UUID, List<ForgeMod>>();
	public boolean mysql = config.getBoolean("AVL.MySQL.Enabled");
	MySQL sql;
	
	public void onEnable() {
		instance = this;
		this.loadConfig();
		this.registerEvents();
		this.registerChannel();
		this.registerCommands();
		this.saveServerData();
		ConfigGenerator cg = new ConfigGenerator(this);
		cg.CreateConfigs();
		
		
		Bukkit.getConsoleSender().sendMessage("");
		Bukkit.getConsoleSender().sendMessage("§6==========>[§cAVL Terms§6]<==========");		
		Bukkit.getConsoleSender().sendMessage("§6-> You are not permitted to claim this plugin as your own!");
		Bukkit.getConsoleSender().sendMessage("§6-> You are not permitted to decompiling the plugin's sourcecode!");
		Bukkit.getConsoleSender().sendMessage("§6-> You are not permitted to modify the code or the plugin and call it your own!");
		Bukkit.getConsoleSender().sendMessage("§6-> You are not permitted to redistributing this plugin as your own!");
		Bukkit.getConsoleSender().sendMessage("§6=======>[§aTerms Accepted!§6]<=======");
		Bukkit.getConsoleSender().sendMessage("");
		//StartMetrics();
		StartMySQL();
		Bukkit.getConsoleSender().sendMessage(prefix + "§bVersion: " + getDescription().getVersion() + " §aby §c" + getDescription().getAuthors() + "§a enabled!");

	}
	
	public void onDisable() {
		Bukkit.getConsoleSender().sendMessage(prefix + "§bVersion: " + getDescription().getVersion() + " §aby §c" + getDescription().getAuthors() + "§c disabled!");
	}
	
	private void registerChannel() {
		forgeChannel = "";
		if(Main.instance.getConfig().getBoolean("AVL.Mods_Log") == false) {
			return;
		}
		if(Bukkit.getVersion().contains("1.13") | Bukkit.getVersion().contains("1.14") | Bukkit.getVersion().contains("1.15") | 
				Bukkit.getVersion().contains("1.16") | Bukkit.getVersion().contains("1.17") | Bukkit.getVersion().contains("1.18") | 
				Bukkit.getVersion().contains("1.19")) {
			forgeChannel = "fml:handshake";
		} else {
			forgeChannel = "FML|HS";
		}
		Bukkit.getMessenger().registerOutgoingPluginChannel(this, forgeChannel);
		Bukkit.getMessenger().registerIncomingPluginChannel(this, forgeChannel, new PluginMessageReceivedHandler());
	}
	
	private void registerEvents() {
		Bukkit.getServer().getPluginManager().registerEvents(new PlayerJoinEventHandler(), this);
		Bukkit.getServer().getPluginManager().registerEvents(new PlayerQuitEventHandler(), this);
		Bukkit.getServer().getPluginManager().registerEvents(new PlayerLoginEventHandler(this), this);
		Bukkit.getServer().getPluginManager().registerEvents(new PlayerChatEventHandler(), this);
		Bukkit.getServer().getPluginManager().registerEvents(new PlayerCommandPreprocessEventHandler(), this);
		Bukkit.getServer().getPluginManager().registerEvents(new ServerCommandEventHandler(this), this);
		Bukkit.getServer().getPluginManager().registerEvents(new PlayerDeathEventHandler(), this);
		if(Main.instance.getConfig().getBoolean("AVL.Mods_Log") == true) {
			Bukkit.getServer().getPluginManager().registerEvents(new PlayerRegisterChannelEventHandler(), this);
		}
	}
	
	/*
	private void StartMetrics() {
		if(this.getConfig().getBoolean("AVL.Metrics") == true) {
			try {
				Metrics m = new Metrics(this);
				m.start();
				Bukkit.getConsoleSender().sendMessage(this.prefix + "§aStarted §eMetrics §asuccessful!");
			} catch (IOException e) {
				e.printStackTrace();
				Bukkit.getConsoleSender().sendMessage(this.prefix + "§cFailed to start the §eMetrics§c!");
			}
		} else {
			Bukkit.getConsoleSender().sendMessage(prefix + "§eMetrics §cdisabled!");
		}
	}
	*/
	public MySQL getMySQL() {
		return this.sql;
	}
	
	private void StartMySQL() {
		if(mysql == true) {
			try {
				this.sql = new MySQL();
				Bukkit.getConsoleSender().sendMessage("§6[§cAdvancedLogger§6]  §aStarted §eMySQL-Service §asuccessful!");
			} catch (Exception e) {
				Bukkit.getConsoleSender().sendMessage("§6[§cAdvancedLogger§6] §cFailed to start §eMySQL-Service§c (" + e.getMessage() + ")§c!");
			}
		} else {
			Bukkit.getConsoleSender().sendMessage("§6[§cAdvancedLogger§6] §eMySQL-Service §cdisabled!");
		}
	}
	
	private void registerCommands() {
		getCommand("avl").setExecutor(new Commands(this));
	}

	
	public void saveConfig(YamlConfiguration config, File file) {
		try {
			config.save(file);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
	private void loadConfig() {
		this.getConfig().options().copyDefaults(true);
		saveConfig();		
	}
	
	public static void saveConfigFile(String savePath, String saveKnot, String playerName, String message) {
		Date d = Calendar.getInstance().getTime();
		DateFormat tm = new SimpleDateFormat("dd-MM-yyyy");
		String date = tm.format(d);
		
		Date d1 = Calendar.getInstance().getTime();
		DateFormat tm1 = new SimpleDateFormat("HH:mm:ss");
		String time = tm1.format(d1);
		
		File f = new File(savePath, date.replace(":", "_") + ".yml");
		YamlConfiguration config = YamlConfiguration.loadConfiguration(f);
		List<String> content = config.getStringList(saveKnot);
		content.add(time.toString() + " - " + playerName + " : " + message);
		config.set(saveKnot, content);
		Main.instance.saveConfig(config, f);
	}
	
	@SuppressWarnings("deprecation")
	private void saveServerData() {
		File f = new File("plugins/AdvancedLogger", "ServerData.yml");
		YamlConfiguration config = YamlConfiguration.loadConfiguration(f);
		if(Main.instance.getConfig().getBoolean("AVL.ServerData") == true) {
			//String name = Bukkit.getServerName();
			String name = Bukkit.getServer().getName();			
			String version = Bukkit.getServer().getBukkitVersion();
			String motd = Bukkit.getServer().getMotd();
			int port = Bukkit.getServer().getPort();
			config.set("Name", name);
			if(!Bukkit.getVersion().contains("1.14") && !Bukkit.getVersion().contains("1.15") && 
					!Bukkit.getVersion().contains("1.16") && !Bukkit.getVersion().contains("1.17") && !Bukkit.getVersion().contains("1.18")  && !Bukkit.getVersion().contains("1.19")) {
				String id = Bukkit.getServerId();
				config.set("ID", id);
			}
			config.set("IP", this.getServer().getIp());
			config.set("BukkitVersion", version);
			config.set("MOTD", motd);
			config.set("Port", Integer.toString(port));
		}
		this.saveConfig(config, f);
	}

}
