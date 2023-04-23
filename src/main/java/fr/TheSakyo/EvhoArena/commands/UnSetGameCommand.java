/* PARTIE IMPORTATIONS + PACKAGE DE LA CLASS */

package fr.TheSakyo.EvhoArena.commands;

import fr.TheSakyo.EvhoUtility.config.ConfigFile;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.TheSakyo.EvhoArena.ArenaMain;
import org.bukkit.ChatColor;

/* PARTIE IMPORTATIONS + PACKAGE DE LA CLASS */

public class UnSetGameCommand implements CommandExecutor {

	/* Récupère la class "Main" */
	private ArenaMain main;
	public UnSetGameCommand(ArenaMain pluginMain) { this.main = pluginMain; }
	/* Récupère la class "Main" */



	/*********************************************/
	/* PARTIE COMMANDE POUR POINT DE SPAWN JEUX */
    /*********************************************/
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String alias, String[] args) {
		
		if(sender instanceof Player p) {

			if(!p.hasPermission("evhoarena.game")) {
				
				p.sendMessage(main.prefix + ChatColor.RED + "Vous n'avez pas les permissions requises !");
				return false;
			}
		}
				
		if(args.length == 2) {

			String name = args[0].replaceAll("[^a-zA-Z0-9]", ""); // Récupère le nom de la zone
			
			if(args[1].equalsIgnoreCase("blue")) {
				
				try {

					ConfigFile.removeKey(main.config, "game." + name + "blue");
					ConfigFile.saveConfig(main.config);

					sender.sendMessage(main.prefix + ChatColor.GREEN + "Point de spawn de la zone de jeu '" + name + "' supprimé pour les " + ChatColor.AQUA + "bleus" + ChatColor.GREEN + " !");
					return true;

				} catch(IllegalArgumentException | NullPointerException e) {

					sender.sendMessage(main.prefix + ChatColor.RED + "Point de spawn de la zone de jeu '" + name + "' non éxistant pour les " + ChatColor.AQUA + "bleus" + ChatColor.RED + " !");
				}

			} else if(args[1].equalsIgnoreCase("red")) {
				
				try {

					ConfigFile.removeKey(main.config, "game." + name + "red");
					ConfigFile.saveConfig(main.config);

					sender.sendMessage(main.prefix + ChatColor.GREEN + "Point de spawn de la zone de jeu '" + name + "' supprimé pour les " + ChatColor.RED + "rouges" + ChatColor.GREEN + " !");
					return true;

				} catch(Exception e) {

					sender.sendMessage(main.prefix + ChatColor.RED + "Point de spawn de la zone de jeu '" + name + "' non éxistant pour les " + ChatColor.DARK_RED + "rouges" + ChatColor.RED + " !");
				}

			} else { Bukkit.getServer().dispatchCommand(sender, "unsetgame"); return false; }

		} else { sender.sendMessage(main.prefix + ChatColor.RED + "Essayez /unsetgame <zoneName>  <blue> ou <red>"); }

		return false;
	}
	
	/*********************************************/
	/* PARTIE COMMANDE POUR POINT DE SPAWN JEUX */
    /*********************************************/

}
