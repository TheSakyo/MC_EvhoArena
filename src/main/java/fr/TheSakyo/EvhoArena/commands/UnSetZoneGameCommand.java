/* PARTIE IMPORTATIONS + PACKAGE DE LA CLASS */

package fr.TheSakyo.EvhoArena.commands;

import fr.TheSakyo.EvhoUtility.config.ConfigFile;
import fr.TheSakyo.EvhoUtility.managers.ZoneManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.TheSakyo.EvhoArena.ArenaMain;
import org.bukkit.ChatColor;

/* PARTIE IMPORTATIONS + PACKAGE DE LA CLASS */

public class UnSetZoneGameCommand implements CommandExecutor {

	/* Récupère la class "Main" */
	private ArenaMain main;
	public UnSetZoneGameCommand(ArenaMain pluginMain) { this.main = pluginMain; }
	/* Récupère la class "Main" */


	/********************************************/
	/* PARTIE COMMANDE POUR POINT DE SPAWN JEUX */
    /********************************************/

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String alias, String[] args) {

		if(sender instanceof Player p) {

			if(!p.hasPermission("evhoarena.zonegame")) {

				p.sendMessage(main.prefix + ChatColor.RED + "Vous n'avez pas les permissions requises !");
				return false;
			}
		}

		if(args.length == 1) {

			String name = args[0].replaceAll("[^a-zA-Z0-9]", ""); // Récupère le nom de la zone

			if(ZoneManager.hasRegion(name)) {

				ZoneManager.delete(name); // Supprime la zone de jeu
				main.manager.removeGame(name); // Ajoute la zone de jeu dans la liste des arênes de jeux
				ConfigFile.removeKey(main.config, "zone." + name); // Enregistre la zone dans le fichier config
				ConfigFile.saveConfig(main.config); // Sauvegarde le fichier config

				sender.sendMessage(main.prefix + ChatColor.GREEN + "Vous avez supprimer la zone de jeu '" + name + "' !");
				return true;

			} else {

				sender.sendMessage(main.prefix + ChatColor.RED + "La zone de jeu '" + name + "' est introuvable !");
				return false;
			}

		} else { sender.sendMessage(main.prefix + ChatColor.RED + "Essayez /unsetzonegame <zoneName>"); }

		return false;
	}

	/*********************************************/
	/* PARTIE COMMANDE POUR POINT DE SPAWN JEUX  */
    /*********************************************/

}
