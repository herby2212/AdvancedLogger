package de.Herbystar.AVL.Events;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import de.Herbystar.AVL.ForgeMod;
import de.Herbystar.AVL.Main;
import de.Herbystar.AVL.Utils;

public class PluginMessageReceivedHandler implements PluginMessageListener {
	
	private String originalModList;
	
	public void onPluginMessageReceived(String channel, Player player, byte[] message) {	
		channel = channel.replaceAll("[\\000-\\t\\013\\f\\016-\\037]", "");
	    
	    if(message[0] != 2) {
	    	return;
	    }
	    
	    byte[] bytes = new byte[message.length - 2];
	    for(int i = 2; i < message.length; i++) {
	    	bytes[(i - 2)] = message[i];
	    }
	    
	    message = bytes;
	    
	    try {
			this.originalModList = Utils.getReadableString(new String(message, "UTF-8").replace("\000", ""));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
//	    Bukkit.getConsoleSender().sendMessage(originalModList);
	    //Add forge mods
	    String[] mods;
	    List<ForgeMod> playerMods = new ArrayList<ForgeMod>();
	    //>= 1.13
	    if(Main.instance.forgeChannel.equals("fml:handshake")) {
	    	mods = this.originalModList.split(" {2}")[0].split(" ");
	    	for(int i = 0; i < mods.length; i+=2) {
	    		playerMods.add(new ForgeMod(mods[i], mods[i+1]));
	    	}
	    } else {
	    	mods = this.originalModList.split(" ");
	    	for(int i = 0; i < mods.length-1; i+=2) {
	    		playerMods.add(new ForgeMod(mods[i], mods[i+1]));
	    	}
	    }
		Main.forgeModsByPlayer.put(player.getUniqueId(), playerMods);
		this.logMods(player, playerMods);
	}
	
	private void logMods(Player player, List<ForgeMod> playerMods) {
		if(!player.hasPermission("Avl.bypass.account")) {
			File fp = new File("plugins/AdvancedLogger/Players", player.getAddress().getHostString().toString() + ".yml");
			YamlConfiguration configp = YamlConfiguration.loadConfiguration(fp);
			
			if(Main.instance.getConfig().getBoolean("AVL.Mods_Log") == true) {
				List<String> mods = configp.getStringList("Mods");
				if(mods.isEmpty()) {
					mods = new ArrayList<String>();
					for(ForgeMod fm : playerMods) {
						mods.add(fm.getModName() + " - " + fm.getModVersion());
					}
				} else {
					List<ForgeMod> loggedMods = new ArrayList<ForgeMod>();
					for(String s : mods) {
						String[] mod = s.split(" - ");
						loggedMods.add(new ForgeMod(mod[0], mod[1]));
					}
					for(ForgeMod fm : playerMods) {
						if(!loggedMods.contains(fm)) {
							mods.add(fm.getModName() + " - " + fm.getModVersion());
						}
					}
				}
				configp.set("Mods", mods);
			}
			Main.instance.saveConfig(configp, fp);
		}
	}
	
}
