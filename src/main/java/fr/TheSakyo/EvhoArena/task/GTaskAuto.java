/* PARTIE IMPORTATIONS + PACKAGE DE LA CLASS */

package fr.TheSakyo.EvhoArena.task;

import com.google.common.collect.Iterables;
import fr.TheSakyo.EvhoArena.utils.ScoreBoard;
import fr.TheSakyo.EvhoUtility.managers.ZoneManager;
import fr.TheSakyo.EvhoUtility.utils.custom.CustomMethod;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import fr.TheSakyo.EvhoArena.ArenaMain;
import fr.TheSakyo.EvhoArena.enums.GState;

import java.util.List;
import java.util.Random;



/* PARTIE IMPORTATIONS + PACKAGE DE LA CLASS */

public class GTaskAuto extends BukkitRunnable {
	
	/* Récupère la class "Main" */
	private ArenaMain main;
	public GTaskAuto(ArenaMain pluginMain) { this.main = pluginMain; }
	/* Récupère la class "Main" */

	private int timer = 15;

	/***************************************/
	/* TEMPS PENDANT LE LANCEMENT DU jeu  */
	/***************************************/

	@Override
	public void run() {

		if(main.manager.isDisabled()) return;
		else {

			//Actualisation du "timer" au niveau de la barre d'XP des joueurs
			//Également, si il reste un seul joueur, le lancement est annulé
			for(Player pls : main.manager.getPlayers()) {

				//Annule le mini-jeu si il reste aucun joueur(s)
				if(main.manager.getPlayers().size() == 0) { cancel(); return; }
				//Annule le mini-jeu si il reste aucun joueur(s)


				//Annule le mini-jeu si il reste qu'un seul joueur
				if(main.manager.getPlayers().size() == 1) {

					Bukkit.getServer().broadcast(CustomMethod.StringToComponent(main.prefix + ChatColor.RED + "Le mini-jeu compte un seul joueur !"));
					main.manager.eliminate(main.manager.getPlayers().get(0), false);

					cancel();
					return;
				}
				//Annule le mini-jeu si il reste qu'un seul joueur

				pls.setFoodLevel(20);
				pls.setExp(0);
				pls.setLevel(timer);

									/* ---------------------------------------- */

				if(timer <= 5) pls.playSound(pls.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1f, 1f);
			}

			if(timer == 10 || timer == 5 || timer == 4 || timer == 3 || timer == 2) {

				Bukkit.getServer().broadcast(CustomMethod.StringToComponent(main.prefix + ChatColor.GOLD + "Lancement du jeu dans " + ChatColor.GREEN + timer + ChatColor.GOLD + " secondes !"));

			} else if(timer == 1) {

				Bukkit.getServer().broadcast(CustomMethod.StringToComponent(main.prefix + ChatColor.GOLD + "Lancement du jeu dans " + ChatColor.GREEN + timer + ChatColor.GOLD + " seconde !"));

			} else if(timer == 0) {

				Bukkit.getServer().broadcast(CustomMethod.StringToComponent(main.prefix + ChatColor.GOLD + "Lancement du jeu, PVP dans" + ChatColor.GREEN + " 10 " + ChatColor.GOLD + "secondes !"));
				main.manager.setState(GState.PVP);

				// Pour tous les joueurs en jeu, On vérifie si les spawns de jeu sont pas null et téléporte les joueurs à l'arêne //
				for(int i = 0; i < main.manager.getPlayers().size(); i++) {

					Location spawn = null;

					Player p = main.manager.getPlayers().get(i);

								/* -------------------------------------------------------  */

					/*//Vérifie la config des Spawns
					main.manager.LoadSpawnConfig(p, true, true);

					//Définit le spawn rouge ou bleu pour les 2 premiers joueurs de la listes
					if(p == main.manager.getPlayers().get(0) || p == main.manager.getPlayers().get(1)) spawn = main.manager.getSpawns().get(i);

					//Définit le spawn rouge ou bleu au hasard pour le restes des joueurs de la listes
					else if(i >= 2) { spawn = main.manager.RandomSpawn(); }*/

								/* -------------------------------------------------------  */

					Random r = new Random(); //Variable pour random

					Location firstPos = ZoneManager.getFirstLocationPos("game"); // Récupère la première position de la zone de jeu
					Location secondPos = ZoneManager.getSecondLocationPos("game"); // Récupère la deuxième position de la zone de jeu

					Location location = CustomMethod.getRandomLocation(firstPos, secondPos); // On récupère une localisation aléatoire entre deux coordonée précisée

					if(location != null) {

						boolean hasNoAir = false; // Vérifiera si la liste des localisations contiendra pas de l'air

						// On boucle on vérifie tant que il y'a de l'air dans la liste des localisations récupéré, alors on redéfinit la liste avec une nouvelle localisation aléatoire
						while(!hasNoAir) {

							location = CustomMethod.getRandomLocation(firstPos, secondPos); // On récupère une localisation aléatoire entre deux coordonée précisée
							List<Location> aroundLoc = CustomMethod.getNearbyLocations(location, 2, false); // On récupère une liste de localisations autour d'une autre localisation précisée

							// Vérifie si les localisations contienne pas d'air
							hasNoAir = aroundLoc.stream().allMatch(loc -> loc.getBlock().getType() != Material.AIR) && location.getBlock().getType() != Material.AIR;
						}
					}

					spawn = location; // Définit le spawn où le joueur sera téléporté

										/* -------------------------------------------- */

					//Si le spawn récupéré s'avéré à être null (erreur de configuration), la partie s'annule
					if(spawn == null) {

						Bukkit.getServer().broadcast(CustomMethod.StringToComponent(main.prefix + ChatColor.RED + "La partie a été annulé suite à une erreur de configuration ! Contactez un administrateur !"));
						main.manager.getPlayers().clear();
						cancel();
					}

					p.setGameMode(GameMode.ADVENTURE);
					p.getInventory().clear();
					p.setFoodLevel(20);
					p.setHealth(p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getDefaultValue());
					p.teleport(spawn);


					//Affiche le grade du joueur en réinitialisant le Scoreboard
					new ScoreBoard(main).getScoreBoard(p, true, false, true);

					p.damage(0); // Effectue des dommages au Joueur à zéro (initialise les 20 coeurs au dessus de la tête)


					// Si le joueur est le dernier de la liste, on peut téléporter tous les joueurs étant en mode Spéctateur //
					// En vérifiant également si les spawns de jeu sont pas null //
					if(p == Iterables.getLast(main.manager.getPlayers())) {

						for(Player pls : Bukkit.getServer().getOnlinePlayers()) {

							if(pls.getGameMode() == GameMode.SPECTATOR) {

								//Définit le spawn rouge ou bleu au hasard pour les joueurs Spéctateur
								spawn = main.manager.RandomSpawn();

								//Si l'un des spawns jeu s'avéré à être null (erreur de configuration), la partie s'annule
								if(spawn == null) {

									Bukkit.getServer().broadcast(CustomMethod.StringToComponent(main.prefix + ChatColor.RED + "La partie a été annulé suite à une erreur de configuration ! Contactez un administrateur !"));

									main.manager.getPlayers().clear();

									cancel();
								}

								pls.teleport(spawn);
							}
						}
					}
					// Si le joueur est le dernier de la liste, on peut téléporter tous les joueurs étant en mode Spéctateur //
					// En vérifiant également si les spawns de jeu sont pas null //
				}
				// Pour tous les joueurs en jeu, On vérifie si les spawns de jeu sont pas null et téléporte les joueurs à l'arêne //

				// Commence la partie //
				new GCycle(main).runTaskTimer(main, 0, 20L);
				// Commence la partie //


				cancel();
			}

			timer--;
		}
	}

	/***************************************/
	/* TEMPS PENDANT LE LANCEMENT DU jeu  */
	/***************************************/

}
