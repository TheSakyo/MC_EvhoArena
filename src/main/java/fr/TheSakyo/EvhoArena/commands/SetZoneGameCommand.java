/* PARTIE IMPORTATIONS + PACKAGE DE LA CLASS */

package fr.TheSakyo.EvhoArena.commands;

import fr.TheSakyo.EvhoUtility.managers.ZoneManager;
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

public class SetZoneGameCommand implements CommandExecutor {

	/* Récupère la class "Main" */
	private ArenaMain main;
	public SetZoneGameCommand(ArenaMain pluginMain) { this.main = pluginMain; }
	/* Récupère la class "Main" */


	/********************************************/
	/* PARTIE COMMANDE POUR POINT DE SPAWN JEUX */
    /********************************************/

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String alias, String[] args) {

		if(sender instanceof Player p) {

			if(p.hasPermission("evhoarena.zonegame")) {

				if(args.length == 1) {

                    Location location = p.getLocation(); // Récupère la localisation du Joueur

					if(args[0].equalsIgnoreCase("pos1")) {

                        if(location == null) {

                            p.sendMessage(main.prefix + ChatColor.RED + "Une erreur est survenue à la définition de la première position de la zone de jeu !");
                            return false;
                        }

                        ZoneManager.setFirstPos("game", location.getX(), location.getY(), location.getZ());
                        ZoneManager.setWorld("game", p.getWorld().getName());
                        p.sendMessage(main.prefix + ChatColor.GREEN + "Vous avez définit la première position de la zone de jeu !");

						return true;

					} else if(args[0].equalsIgnoreCase("pos2")) {

                        Location firstPos = ZoneManager.getFirstLocationPos("game"); // Récupère la première position de la zone de jeu
                        World world = ZoneManager.getWorld("game"); // Récupère le monde où est censé se trouver la zone de jeu

                        if(location == null) {

                            p.sendMessage(main.prefix + ChatColor.RED + "Une erreur est survenue à la définition de la première position de la zone de jeu !");
                            return false;
                        }

                        if(firstPos == null) {

                            p.sendMessage(main.prefix + ChatColor.RED + "La première position de la zone de jeu n'est pas définit, veuillez d'abord la définir !");
                            return false;
                        }

                        if(firstPos.getWorld() != world) {

                            p.sendMessage(main.prefix + ChatColor.RED + "La première position de la zone de jeu n'est pas dans le monde où vous êtes, veuillez aller dans le monde '" + world.getName() + "' !");
                            return false;
                        }

                        ZoneManager.setSecondPos("game", location.getX(), location.getY(), location.getZ()); // Définit la deuxième position de la zone de jeu
                        ZoneManager.setWorld("game", firstPos.getWorld().getName());

                                                     /* ----------------------------------------- */

                        ZoneManager.create("game"); // Créer la zone de jeu
                        ZoneManager.addGroupForZone("game", "default"); // Ajoute le grade par défaut à la zone

                        p.sendMessage(main.prefix + ChatColor.GREEN + "Vous avez définit la deuxième position de la zone de jeu !");

						return true;

					} else { Bukkit.getServer().dispatchCommand(sender, "setzonegame"); return true; }

				} else if(args.length != 1) { p.sendMessage(main.prefix + ChatColor.RED + "Essayez /setzonegame <pos1> ou <pos2>"); }

			} else { p.sendMessage(main.prefix + ChatColor.RED + "Vous n'avez pas les permissions requises !"); }
			return true;

		} else { sender.sendMessage(main.prefix + "Vous devez être en jeu pour définit la zone de jeu !"); }
		return false;
	}

	/*********************************************/
	/* PARTIE COMMANDE POUR POINT DE SPAWN JEUX */
    /*********************************************/
}
