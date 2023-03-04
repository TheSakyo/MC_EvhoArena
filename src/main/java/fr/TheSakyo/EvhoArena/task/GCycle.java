/* PARTIE IMPORTATIONS + PACKAGE DE LA CLASS */

package fr.TheSakyo.EvhoArena.task;

import fr.TheSakyo.EvhoArena.enums.GOver;
import fr.TheSakyo.EvhoArena.utils.Kits;
import fr.TheSakyo.EvhoUtility.managers.ZoneManager;
import fr.TheSakyo.EvhoUtility.utils.custom.CustomMethod;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import fr.TheSakyo.EvhoArena.ArenaMain;
import fr.TheSakyo.EvhoArena.enums.GState;

import java.util.Random;

/* PARTIE IMPORTATIONS + PACKAGE DE LA CLASS */

public class GCycle extends BukkitRunnable {

	
	/* Récupère la class "Main" */
	private ArenaMain main;
	public GCycle(ArenaMain pluginMain) { this.main = pluginMain; }
	/* Récupère la class "Main" */

	
	private int timerPvP = 10;
	
	private int timer = 300;


	/**************************/
	/* TEMPS PENDANT LE jeu  */
	/**************************/

	@Override
	public void run() {

		// Pour tous les joueurs en jeu, On vérifie toujours si les spawns de jeu sont pas null //
		// + Actualisation du "timer" au niveau de la barre d'XP des joueurs //
		for(int i = 0; i < main.manager.getPlayers().size(); i++) {

			//Annule le mini-jeu si il reste aucun joueur(s)
			if(main.manager.getPlayers().size() == 0) { cancel(); return; }
			//Annule le mini-jeu si il reste aucun joueur(s)


			//Annule le mini-jeu si il reste qu'un seul joueur
			if(main.manager.getPlayers().size() == 1 && !main.manager.isState(GState.FINISH) && !main.manager.isOver(GOver.TRUE)) {

				Bukkit.getServer().broadcast(CustomMethod.StringToComponent(main.prefix + ChatColor.RED + "Le mini-jeu compte un seul joueur !"));
				main.manager.eliminate(main.manager.getPlayers().get(0), false);

				cancel();
				return;
			}
			//Annule le mini-jeu si il reste qu'un seul joueur

			Player pls = main.manager.getPlayers().get(i); // Récupère le joueur en question

			//Actualisation du "timer" au niveau de la barre d'XP du joueur
			if(timerPvP != 0) pls.setLevel(timerPvP);
			if(timerPvP != 0 && timerPvP <= 3) pls.playSound(pls.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1f, 1f);
		}
		// Pour tous les joueurs en jeu, On vérifie toujours si les spawns de jeu sont pas null //
		// + Actualisation du "timer" au niveau de la barre d'XP des joueurs //


		if(timerPvP == 5 || timerPvP == 4 || timerPvP == 3 || timerPvP == 2) {

			Bukkit.getServer().broadcast(CustomMethod.StringToComponent(main.prefix + ChatColor.GOLD + "PVP dans " + ChatColor.GREEN + timerPvP + ChatColor.GOLD + " secondes !"));
		
		} else if(timerPvP == 1) {
			
			Bukkit.getServer().broadcast(CustomMethod.StringToComponent(main.prefix + ChatColor.GOLD + "PVP dans " + ChatColor.GREEN + timerPvP + ChatColor.GOLD + " seconde !"));
			
		} else if(timerPvP == 0) {

			if(main.manager.isState(GState.PVP)) {

				Bukkit.getServer().broadcast(CustomMethod.StringToComponent(main.prefix + ChatColor.GOLD + "PVP Activé !"));

				final Kits kit = Kits.randomKits(); // Récupère un kit aléatoirement

				// Donne le Stuff aux joueurs //
				for(Player pls : main.manager.getPlayers()) {

					GetItems(kit, pls);
					pls.setLevel(0);
					pls.setGameMode(GameMode.SURVIVAL);

					pls.playSound(pls.getLocation(), Sound.BLOCK_ANVIL_PLACE, 1f, 1f);
				}
				// Donne le Stuff aux joueurs //

				//Définit la partie en mode jeu
				main.manager.setState(GState.PLAYING);
			}
			
			if(timer == 30 || timer == 10 || timer == 5 || timer == 4 || timer == 3 || timer == 2) {
				
				Bukkit.getServer().broadcast(CustomMethod.StringToComponent(main.prefix + ChatColor.GOLD + "Fin du jeu dans " + ChatColor.GREEN + timer + ChatColor.GOLD + " secondes !"));
			
			} else if(timer == 1) {
				
				Bukkit.getServer().broadcast(CustomMethod.StringToComponent(main.prefix + ChatColor.GOLD + "Fin du jeu dans " + ChatColor.GREEN + timer + ChatColor.GOLD + " seconde !"));
				
			} else if(timer == 0) {
				
				Bukkit.getServer().broadcast(CustomMethod.StringToComponent(main.prefix + ChatColor.GOLD + "Fin du jeu ! Temps écoulé !"));
				main.manager.setState(GState.FINISH);				

				//Fin du jeu, si le temps est écoulé (temps trop long) //
				for(int i = 0; i < main.manager.getPlayers().size(); i++) {
					
					Player p = main.manager.getPlayers().get(i);
					
					p.setGameMode(GameMode.ADVENTURE);
					p.getInventory().clear();
					p.setFoodLevel(20);
					p.setHealth(p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getDefaultValue());
						
				}
				//Fin du jeu, si le temps est écoulé (temps trop long) //

				//Affiche aux joueurs que le temps est finit
				if(main.manager.getPlayers().size() <= 2) { main.manager.DelayLong(); }
				
			 	cancel();
			}

			if(main.manager.getPlayers().size() == 1) { cancel(); }

			timer--;
		}
		
		if(timerPvP != 0) timerPvP--;
	}

	/**************************/
	/* TEMPS PENDANT LE jeu  */
	/**************************/


	/*********************************/
	/* PARTIE ÉQUIPEMENTS DU JOUEUR  */
	/********************************/
	private void GetItems(Kits kit, Player p) {

		ItemStack[] equipments = kit.getEquipments().toArray(new ItemStack[]{});

		p.getInventory().setHeldItemSlot(0); //Définit le "slot" occupant la main du joueur

		p.getInventory().setHelmet(equipments[0]);
		p.getInventory().setChestplate(equipments[1]);
		p.getInventory().setLeggings(equipments[2]);
		p.getInventory().setBoots(equipments[3]);

		p.getInventory().setItemInMainHand(equipments[4]);
		p.getInventory().setItemInOffHand(equipments[5]);
		p.getInventory().setItem(2, equipments[6]);
		p.getInventory().setItem(7, equipments[7]);
		p.getInventory().setItem(5, equipments[8]);
		p.getInventory().setItem(4, equipments[9]);

		try {

			p.getInventory().setItem(1, equipments[10]);
			p.getInventory().setItem(8, equipments[11]);

		} catch(ArrayIndexOutOfBoundsException ignored) {}
	}
	/*********************************/
	/* PARTIE ÉQUIPEMENTS DU JOUEUR  */
	/********************************/

}
