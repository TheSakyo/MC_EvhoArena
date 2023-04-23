/* PARTIE IMPORTATIONS + PACKAGE DE LA CLASS */

package fr.TheSakyo.EvhoArena.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.TheSakyo.EvhoArena.ArenaMain;

/* PARTIE IMPORTATIONS + PACKAGE DE LA CLASS */

public class ReloadCommand implements CommandExecutor {
	
	/* Récupère la class "Main" */
	private ArenaMain main;
	public ReloadCommand(ArenaMain pluginMain) { this.main = pluginMain; }
	/* Récupère la class "Main" */
	
	
	
	/********************************************/
	/* PARTIE COMMANDE POUR RECHARGER LE PLUGIN */ 
    /********************************************/
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String alias, String[] args) {
		
		if(sender instanceof Player p) {

			if(p.hasPermission("evhoarena.reload")) {
				
				if(args.length == 0) {

					main.reloadPlugin(); //Recharge le plugin
					p.sendMessage(main.prefix + ChatColor.GREEN + "Le plugin a été rechargé !");
					return true;
				
				} else if(args.length != 0) {

					p.sendMessage(main.prefix + ChatColor.RED + "Essayez /arenareload ou /arenarl sans arguments");
				}
				
			} else { p.sendMessage(main.prefix + ChatColor.RED + "Vous n'avez pas les permissions requises !"); }

		} else {
	
			if(args.length == 0) {
				
				main.reloadPlugin(); //Recharge le plugin
				sender.sendMessage(main.prefix + ChatColor.GREEN + "Le plugin a été rechargé !");
				return true;
			
			} else if(args.length != 0) {

				sender.sendMessage(main.prefix + ChatColor.RED + "Essayez /arenareload ou /arenarl sans arguments");
			}
		}
		return false;
	}
	
	/********************************************/
	/* PARTIE COMMANDE POUR RECHARGER LE PLUGIN */ 
    /********************************************/
	
}
