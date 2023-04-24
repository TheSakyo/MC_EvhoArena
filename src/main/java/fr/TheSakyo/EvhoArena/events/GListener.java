package fr.TheSakyo.EvhoArena.events;

import fr.TheSakyo.EvhoArena.ArenaMain;
import org.bukkit.GameRule;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldLoadEvent;

public class GListener implements Listener {

    /* Récupère la class "Main" */
	private ArenaMain main;

	public GListener(ArenaMain pluginMain) { this.main = pluginMain; }
	/* Récupère la class "Main" */

	/*****************************/
	/* PARTIE ÉVÈNEMENTS UTILES */
	/****************************/

	@EventHandler
	public void onWorldLoad(WorldLoadEvent e) { e.getWorld().setGameRule(GameRule.DO_MOB_SPAWNING, false); }

    /*****************************/
	/* PARTIE ÉVÈNEMENTS UTILES */
	/****************************/
}
