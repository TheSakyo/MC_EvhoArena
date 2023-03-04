/* PARTIE IMPORTATIONS + PACKAGE DE LA CLASS */

package fr.TheSakyo.EvhoArena.task;

import fr.TheSakyo.EvhoArena.enums.GState;
import fr.TheSakyo.EvhoUtility.utils.custom.CustomMethod;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import fr.TheSakyo.EvhoArena.ArenaMain;

import java.time.Duration;

/* PARTIE IMPORTATIONS + PACKAGE DE LA CLASS */

public class GWaiting extends BukkitRunnable {

	/* Récupère la class "Main" */
	private ArenaMain main;

	public GWaiting(ArenaMain pluginMain) { this.main = pluginMain; }
	/* Récupère la class "Main" */

	private int timer = 60;

	@Override
	public void run() {

		if(timer > 0) {

			for(Player pls : main.manager.getPlayers()) {

				pls.setFoodLevel(20);
				pls.setExp(0);

				if(timer > 20) {

					Component title = CustomMethod.StringToComponent(" ");
					Component subtitle = CustomMethod.StringToComponent(ChatColor.GREEN.toString() + ChatColor.BOLD.toString() + "Attente de joueur(s)...");
					Title.Times times = Title.Times.of(Duration.ofSeconds(2), Duration.ofSeconds(4), Duration.ofSeconds(2));

					pls.showTitle(Title.title(title, subtitle, times));
					pls.setLevel(0);

				} else {

					if(timer == 20) {

						Component title = CustomMethod.StringToComponent(ChatColor.GREEN.toString() + ChatColor.BOLD.toString() + "Attente de joueur(s)...");
						Component subtitle = CustomMethod.StringToComponent(ChatColor.GOLD + "Le jeu va bientôt commencé !");
						Title.Times times = Title.Times.of(Duration.ofSeconds(2), Duration.ofSeconds(2), Duration.ofSeconds(2));

						pls.showTitle(Title.title(title, subtitle, times));
					}

								/* ---------------------------------------- */

					if(timer <= 20) pls.setLevel(timer);
					if(timer != 1 && timer <= 3) pls.playSound(pls.getLocation(), Sound.BLOCK_NOTE_BLOCK_FLUTE, 1f, 1f);
					else if(timer == 1) {

						Component title = CustomMethod.StringToComponent(ChatColor.GOLD + "Lancement du Jeu...");
						Component subtitle = CustomMethod.StringToComponent(ChatColor.RED + "Attente de Joueur(s) désactivé !");
						Title.Times times = Title.Times.of(Duration.ofSeconds(2), Duration.ofSeconds(2), Duration.ofSeconds(2));

						pls.playSound(pls.getLocation(), Sound.BLOCK_BELL_USE, 1f, 1f);
						pls.showTitle(Title.title(title, subtitle, times));
					}
				}
			}

			if(main.manager.getPlayers().size() >= 2) { timer--; }
			else { timer--; timer++; }

		} else if(timer == 0) {

			if(main.manager.getPlayers().size() >= 2) {

				if(main.manager.isState(GState.WAITING)) {

					// Démarre la partie //
					new GTaskAuto(main).runTaskTimer(main, 0, 20L);
					main.manager.setState(GState.STARTING);
					// Démarre la partie //
				}
				cancel();
			}
		}
								/* --------------------------------------------------------- */

		if(main.manager.getPlayers().size() == 0) { cancel(); }
		else if(main.manager.getPlayers().size() <= 1) { timer = 60; }
	}
}
