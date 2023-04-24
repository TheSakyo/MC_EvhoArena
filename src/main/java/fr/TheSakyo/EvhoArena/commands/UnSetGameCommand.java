/* PARTIE IMPORTATIONS + PACKAGE DE LA CLASS */

package fr.TheSakyo.EvhoArena.commands;

import fr.TheSakyo.EvhoUtility.config.ConfigFile;
import net.minecraft.ChatFormatting;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.TheSakyo.EvhoArena.ArenaMain;

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
				
				p.sendMessage(main.prefix + ChatFormatting.RED + "Vous n'avez pas les permissions requises !");
				return false;
			}
		}
				
		if(args.length == 2) {

			String name = args[0].replaceAll("[^a-zA-Z0-9]", ""); // Récupère le nom de la zone
			
			if(args[1].equalsIgnoreCase("blue")) {
				
				try {

					ConfigFile.removeKey(main.config, "game." + name + "blue");
					ConfigFile.saveConfig(main.config);

					sender.sendMessage(main.prefix + ChatFormatting.GREEN + "Point de spawn de la zone de jeu '" + name + "' supprimé pour les " + ChatFormatting.AQUA + "bleus" + ChatFormatting.GREEN + " !");
					return true;

				} catch(IllegalArgumentException | NullPointerException e) {

					sender.sendMessage(main.prefix + ChatFormatting.RED + "Point de spawn de la zone de jeu '" + name + "' non éxistant pour les " + ChatFormatting.AQUA + "bleus" + ChatFormatting.RED + " !");
				}

			} else if(args[1].equalsIgnoreCase("red")) {
				
				try {

					ConfigFile.removeKey(main.config, "game." + name + "red");
					ConfigFile.saveConfig(main.config);

					sender.sendMessage(main.prefix + ChatFormatting.GREEN + "Point de spawn de la zone de jeu '" + name + "' supprimé pour les " + ChatFormatting.RED + "rouges" + ChatFormatting.GREEN + " !");
					return true;

				} catch(Exception e) {

					sender.sendMessage(main.prefix + ChatFormatting.RED + "Point de spawn de la zone de jeu '" + name + "' non éxistant pour les " + ChatFormatting.DARK_RED + "rouges" + ChatFormatting.RED + " !");
				}

			} else { Bukkit.getServer().dispatchCommand(sender, "unsetgame"); return false; }

		} else { sender.sendMessage(main.prefix + ChatFormatting.RED + "Essayez /unsetgame <zoneName>  <blue> ou <red>"); }

		return false;
	}
	
	/*********************************************/
	/* PARTIE COMMANDE POUR POINT DE SPAWN JEUX */
    /*********************************************/

}
