/* PARTIE IMPORTATIONS + PACKAGE DE LA CLASS */

package fr.TheSakyo.EvhoArena.commands;

import fr.TheSakyo.EvhoUtility.config.ConfigFile;
import fr.TheSakyo.EvhoUtility.managers.ZoneManager;
import net.minecraft.ChatFormatting;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.TheSakyo.EvhoArena.ArenaMain;

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

				if(args.length == 2) {

                    Location location = p.getLocation(); // Récupère la localisation du Joueur
                    String name = args[0].replaceAll("[^a-zA-Z0-9]", ""); // Récupère le nom de la zone

					if(args[1].equalsIgnoreCase("pos1")) {

                        if(ZoneManager.isExist(name) && ZoneManager.hasRegion(name)) {

                            p.sendMessage(main.prefix + ChatFormatting.RED + "La zone de jeu '" + name + "' éxiste déjà, vous pouvez toujours la supprimer pour la commande fonctionne de nouveau " + ChatFormatting.GOLD + "/unsetzonegame " + name + ChatFormatting.RED + "' !");
                            return false;

                        } else {

                            if(location == null) {

                                p.sendMessage(main.prefix + ChatFormatting.RED + "Une erreur est survenue à la définition de la première position de la zone de jeu '" + name + "' !");
                                return false;
                            }

                            ZoneManager.create(name); // Créer la zone de jeu
                            ZoneManager.addGroupForZone(name, "default"); // Ajoute le grade par défaut à la zone

                            main.manager.addGame(name); // Ajoute la zone de jeu dans la liste des arênes de jeux
                            ConfigFile.set(main.config, "zone." + name, true); // Enregistre la zone dans le fichier config
				            ConfigFile.saveConfig(main.config); // Sauvegarde le fichier config

                            p.sendMessage(main.prefix + ChatFormatting.GREEN + "La zone de jeu a été créer !");

                                                                        /* ---------------------------------------------- */

                            ZoneManager.setWorld(name, location.getWorld().getName());
                            ZoneManager.setFirstPos(name, location.getX(), location.getY(), location.getZ());

                            p.sendMessage(main.prefix + ChatFormatting.GREEN + "Vous avez définit la première position de la zone de jeu '" + name + "', vous pouvez faire de même '" +
                                         ChatFormatting.GRAY.toString() + ChatFormatting.ITALIC.toString() +  "<pos2>" + ChatFormatting.GREEN + "' pour la deuxième position de la région !");

                            return true;

                        }

					} else if(args[1].equalsIgnoreCase("pos2")) {

                        if(ZoneManager.isExist(name) && ZoneManager.hasRegion(name)) {

                            p.sendMessage(main.prefix + ChatFormatting.RED + "La zone de jeu '" + name + "' éxiste déjà, vous pouvez toujours la supprimer pour la commande fonctionne de nouveau " + ChatFormatting.GOLD + "/unsetzonegame" + name +  ChatFormatting.RED + "' !");

                        } else {

                            Location firstPos = ZoneManager.getFirstLocationPos(name); // Récupère la première position de la zone de jeu
                            World world = ZoneManager.getWorld(name); // Récupère le monde où est censé se trouver la zone de jeu

                            if(location == null) {

                                p.sendMessage(main.prefix + ChatFormatting.RED + "Une erreur est survenue à la définition de la première position de la zone de jeu '" + name + "' !");
                                return false;
                            }

                            if(firstPos == null) {

                                p.sendMessage(main.prefix + ChatFormatting.RED + "La première position de la zone de jeu '" + name + "' n'est pas définit, veuillez d'abord la définir !");
                                return false;
                            }

                            if(firstPos.getWorld() != world && location.getWorld() != world) {

                                p.sendMessage(main.prefix + ChatFormatting.RED + "La première position de la zone de jeu '" + name + "' n'est pas dans le monde où vous êtes, veuillez aller dans le monde '" + world.getName() + "' !");
                                return false;
                            }

                            ZoneManager.setWorld(name, firstPos.getWorld().getName()); // Redéfinit le monde de la zone
                            ZoneManager.setSecondPos(name, location.getX(), location.getY(), location.getZ()); // Définit la deuxième position de la zone de jeu

                            p.sendMessage(main.prefix + ChatFormatting.GREEN + "Vous avez définit la deuxième position de la zone de jeu '" + name + "', la région à donc été créer !");
                            return true;
                        }

					} else { Bukkit.getServer().dispatchCommand(sender, "setzonegame"); return true; }

				} else { p.sendMessage(main.prefix + ChatFormatting.RED + "Essayez /setzonegame <zoneName> <pos1> ou <pos2>"); }

			} else { p.sendMessage(main.prefix + ChatFormatting.RED + "Vous n'avez pas les permissions requises !"); }

		} else { sender.sendMessage(main.prefix + "Vous devez être en jeu pour définir une zone de jeu !"); }
		return false;
	}

	/*********************************************/
	/* PARTIE COMMANDE POUR POINT DE SPAWN JEUX */
    /*********************************************/
}
