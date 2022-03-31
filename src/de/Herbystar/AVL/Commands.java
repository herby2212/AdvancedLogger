package de.Herbystar.AVL;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Commands implements CommandExecutor {
	
	Main plugin;
	public Commands(Main main) {
		plugin = main;
	}

	@SuppressWarnings("deprecation")
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player p = (Player) sender;
		
		if(cmd.getName().equalsIgnoreCase("avl")) {
			if(args.length == 0) {
				if(p.hasPermission("Avl.commands.*")) {
					p.sendMessage("§6[]===============[§c§lAVL§6]===============[]");
					p.sendMessage("§c /avl cmdspy §7-> §eenable/disable live ingame command log");
					p.sendMessage("§c /avl modlist <player> §7-> §eshow all mods this player has installed");
					p.sendMessage("§6[]===============[§c§lAVL§6]===============[]");
					return true;
				}
			}
			if(args.length == 1) {
				if(args[0].equalsIgnoreCase("cmdspy")) {
					if(p.hasPermission("Avl.commands.cmdspy")) {
						UUID pUUID = p.getUniqueId();
						if(Main.cmdspy.containsKey(pUUID) == false) {
							Main.cmdspy.put(pUUID, true);
						} else if(Main.cmdspy.get(pUUID) == false) {
							Main.cmdspy.put(pUUID, true);
						} else {
							Main.cmdspy.put(pUUID, false);
						}
						return true;
					}
				}
				if(args[0].equalsIgnoreCase("modlist")) {
					if(p.hasPermission("Avl.commands.modlist")) {
						p.sendMessage(Main.instance.prefix + "§cError: No user defined!");
						p.sendMessage(Main.instance.prefix + "§c/avl modlist <player>");
						return true;
					}
				}
			}
			if(args.length == 2) {
				if(args[0].equalsIgnoreCase("modlist")) {
					if(p.hasPermission("Avl.commands.modlist")) {
						UUID pUUID;
						if(Bukkit.getPlayer(args[1]) != null) {
							pUUID = Bukkit.getPlayer(args[1]).getUniqueId();
						} else {
							pUUID = Bukkit.getOfflinePlayer(args[1]).getUniqueId();
						}
						if(Main.forgeModsByPlayer.get(pUUID) == null) {
							p.sendMessage(Main.instance.prefix + "§eThe player has currently no mods installed or they could not be detected.");
						} else {
							p.sendMessage(Main.instance.prefix + "§eThe player has installed following mods:");
							for(ForgeMod fm : Main.forgeModsByPlayer.get(pUUID)) {
								p.sendMessage("§a" + fm.getModName() + " §e» §c" + fm.getModVersion());
							}
						}
						return true;
					}
				}
			}
		}		
		return false;
	}
}
