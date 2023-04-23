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

public class UnSetLobbyCommand implements CommandExecutor {

	/* Récupère la class "Main" */
	private ArenaMain main;
	public UnSetLobbyCommand(ArenaMain pluginMain) { this.main = pluginMain; }
	/* Récupère la class "Main" */


	/*********************************************/
	/* PARTIE COMMANDE POUR POINT DE SPAWN LOBBY */
    /*********************************************/

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String alias, String[] args) {

		if(sender instanceof Player p) {

			if(!p.hasPermission("evhoarena.lobby")) {

				p.sendMessage(main.prefix + ChatColor.RED + "Vous n'avez pas les permissions requises !");
				return false;
			}
		}

		if(args.length == 1) {

			String name = args[0].replaceAll("[^a-zA-Z0-9]", ""); // Récupère le nom de la zone

			try {

				ConfigFile.removeKey(main.config, "lobby." + name);
				ConfigFile.saveConfig(main.config);

				sender.sendMessage(main.prefix + ChatColor.GREEN + "Point de spawn lobby supprimé pour la zone de jeu '" + name + "' !");

			} catch(Exception e) { sender.sendMessage(main.prefix + ChatColor.RED + "Point de spawn lobby pour la zone de jeu '" + name + "' non éxistant !");  }
			return true;

		} else if(args.length != 0) { sender.sendMessage(main.prefix + ChatColor.RED + "Essayez /unsetlobby <zoneName>"); }

		return false;
	}

	/*********************************************/
	/* PARTIE COMMANDE POUR POINT DE SPAWN LOBBY */
    /*********************************************/

}
