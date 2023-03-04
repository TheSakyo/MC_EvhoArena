/* PARTIE IMPORTATIONS + PACKAGE DE LA CLASS */

package fr.TheSakyo.EvhoArena.commands;

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

			if(p.hasPermission("evhoarena.zonegame")) {

				if(args.length == 0) {

					if(ZoneManager.hasRegion("game")) {

						ZoneManager.delete("game"); // Supprime la zone de jeu

						p.sendMessage(main.prefix + ChatColor.GREEN + "Vous avez supprimer la zone de jeu !");
						return true;

					} else {

						p.sendMessage(main.prefix + ChatColor.RED + "La zone de jeu est introuvable !");
						return false;
					}


				} else { p.sendMessage(main.prefix + ChatColor.RED + "Essayez /unsetzonegame"); }

			} else { p.sendMessage(main.prefix + ChatColor.RED + "Vous n'avez pas les permissions requises !"); }
			return true;

		} else { sender.sendMessage(main.prefix + "Vous devez être en jeu pour définit le spawn jeu !"); }
		return false;
	}

	/*********************************************/
	/* PARTIE COMMANDE POUR POINT DE SPAWN JEUX  */
    /*********************************************/

}
