/* PARTIE IMPORTATIONS + PACKAGE DE LA CLASS */

package fr.TheSakyo.EvhoArena.events;

import fr.TheSakyo.EvhoArena.ArenaMain;
import fr.TheSakyo.EvhoArena.enums.GState;
import fr.TheSakyo.EvhoUtility.config.ConfigFile;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;


/* PARTIE IMPORTATIONS + PACKAGE DE LA CLASS */

public class GDamageListener implements Listener {

	/* Récupère la class "Main" */
	private ArenaMain main;
	public GDamageListener(ArenaMain pluginMain) { this.main = pluginMain; }
	/* Récupère la class "Main" */



	// Évènement quand le joueur perd des dégats et meurt (PVP, dans le vide ou autres) //
	@EventHandler
	public void onDamage(EntityDamageEvent e) {

		if(!(e.getEntity() instanceof Player victim)) return;

		Player killer = victim.getKiller();

		// Annule l'évènement selon l'état du jeu //
		// Téléporte aux spawn précédent s'il tombe dans le vide //
		if(main.manager.isState(GState.WAITING) || main.manager.isState(GState.STARTING)) {

			if(e.getCause() == DamageCause.VOID) {

				e.setDamage(0);

				victim.sendMessage(main.prefix + ChatColor.RED + "Vous ne pouvez pas mourir dans le vide !");

				victim.teleport(GPlayerListener.loclobby.add(0.0, 0.2, 0.0));

				return;

			} else { e.setCancelled(true); }

			return;

		} else {

			//Définit le spawn rouge ou bleu au hasard pour les joueurs Spéctateur
			Location spawn = main.manager.RandomSpawn();

			if(main.manager.isState(GState.FINISH)) { e.setCancelled(true); return; }

			else if(main.manager.isState(GState.PVP)) {

				if(e.getCause() == DamageCause.VOID) {

					e.setDamage(0);

					victim.sendMessage(main.prefix + ChatColor.RED + "Vous ne pouvez pas mourir dans le vide !");

					victim.teleport(spawn.add(0.0, 0.2, 0.0));

					victim.setFallDistance(0);

					return;

				} else { e.setCancelled(true); }

				return;

			} else {

				if(e.getCause() == DamageCause.VOID) {

					e.setDamage(0);

					victim.sendMessage(main.prefix + ChatColor.RED + "Vous ne pouvez pas mourir dans le vide !");

					victim.teleport(spawn.add(0.0, 0.2, 0.0));

					victim.setFallDistance(0);

					return;

				} else { e.setCancelled(false); }
			}
		}
		// Annule l'évènement selon l'état du jeu //
		// Téléporte aux spawn précédent s'il tombe dans le vide //


		if(killer == null && victim.getHealth() <= e.getFinalDamage()) {

			//Définit les dommage(s) causé(s) à 0
			e.setDamage(0);

			//Élimine le joueur
			main.manager.eliminate(victim, true);

		} else if(killer != null && victim.getHealth() <= e.getFinalDamage()) {

			//Définit les dommage(s) causé(s) à 0
			e.setDamage(0);

			// Ajoute les kills des joueurs //
			main.playerKills.replace(killer.getUniqueId().toString(), main.playerKills.get(killer.getUniqueId().toString()) + 1); //Compteur de "kill" global
			main.Kills.replace(killer.getUniqueId(), main.Kills.get(killer.getUniqueId()) + 1); //Compteur de "kill" local
			// Ajoute les kills des joueurs //

			//Ajoute les kills au fichier de configuration et le sauvegarde //
			ConfigFile.set(main.killconfig, killer.getUniqueId().toString(), String.valueOf(main.playerKills.get(killer.getUniqueId().toString())));
			ConfigFile.saveConfig(main.killconfig);
			//Ajoute les kills au fichier de configuration et le sauvegarde //

			// Message de prévention disant au tueur qu'elle est le joueur tué
			killer.sendMessage(main.prefix + ChatColor.RED + "Vous avez tué " + ChatColor.YELLOW + victim.getName() + ChatColor.RED + " !");

			main.manager.eliminate(victim, true); // Élimine le joueur
		}
		return;
	}
	// Évènement quand le joueur perd des dégats et meurt (PVP, dans le vide ou autres) //

}