/* PARTIE IMPORTATIONS + PACKAGE DE LA CLASS */

package fr.TheSakyo.EvhoArena.events;

import fr.TheSakyo.EvhoArena.utils.ScoreBoard;
import fr.TheSakyo.EvhoArena.config.ConfigFileManager;
import fr.TheSakyo.EvhoUtility.config.ConfigFile;
import fr.TheSakyo.EvhoUtility.managers.ScoreboardManager;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockFormEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import fr.TheSakyo.EvhoArena.ArenaMain;
import fr.TheSakyo.EvhoArena.enums.GState;
import fr.TheSakyo.EvhoArena.task.GWaiting;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

/* PARTIE IMPORTATIONS + PACKAGE DE LA CLASS */

public class GPlayerListener implements Listener {

	/* Récupère la class "Main" */
	private ArenaMain main;
	public GPlayerListener(ArenaMain pluginMain) { this.main = pluginMain; }
	/* Récupère la class "Main" */



	// "Location" du lobby //
	public static Location loclobby;
	// "Location" du lobby //



	/*********************/
	/* PARTIE CONNECTION */
	/*********************/


	// Évènement quand un joueur rejoint le serveur //
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
	
		Player p = e.getPlayer();
		
		if(main.playerKills.get(p.getUniqueId().toString()) == null) { main.playerKills.put(p.getUniqueId().toString(), 0); }

		if(main.Kills.get(p.getUniqueId()) == null) main.Kills.put(p.getUniqueId(), 0);

		//Si le joueur rejoint le serveur pendant que la partie est en train de se terminer
		//Il est donc téléporter au serveur "Hub"
		if(main.manager.isState(GState.FINISH)) {	
			
			main.manager.ConnectPlayerToHubServer(p);
			return;
		}

		//Essaye de vérifier si le spawn lobby est définit pour continuer le code
		try {
			
			World World = Bukkit.getServer().getWorld(ConfigFile.getString(main.config, "lobby.World"));
			
			Double X = ConfigFile.getDouble(main.config, "lobby.X");
			Double Y = ConfigFile.getDouble(main.config, "lobby.Y");
			Double Z = ConfigFile.getDouble(main.config, "lobby.Z");
			Float Yaw = Float.valueOf(ConfigFile.getString(main.config, "lobby.Yaw"));
			Float Pitch = Float.valueOf(ConfigFile.getString(main.config, "lobby.Pitch"));

			//Essaye de définit une "Location" du spawn lobby (vérifie si les spawn définit sont bien des coordonnées)
			try {
				
				loclobby = new Location(World, X, Y, Z, Yaw, Pitch);
				
				p.teleport(loclobby);
				p.getInventory().clear();
				p.setFoodLevel(20);
				p.setHealth(p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getDefaultValue());
				
				p.sendMessage(main.prefix + ChatColor.AQUA + "/hub ou /lobby pour retourner au hub !");
				
				p.sendMessage(main.prefix + ChatColor.AQUA + "PvPArena chacun pour soi ! Créez vous des alliances et/ou trahisez vous !");
				
				if(!main.manager.isState(GState.WAITING)) {
					
					p.setGameMode(GameMode.SPECTATOR);
					
					if(main.manager.isState(GState.STARTING)) { p.sendMessage(main.prefix + ChatColor.GRAY.toString() + ChatColor.ITALIC.toString() + "Le jeu est en cours de démarrage ! ");  }
						
					else { p.sendMessage(main.prefix + ChatColor.GRAY.toString() + ChatColor.ITALIC.toString() + "Le jeu a déjà démarré ! "); }
					
					return;
				}
				
				if(!main.manager.getPlayers().contains(p)) main.manager.getPlayers().add(p);
				
				p.setGameMode(GameMode.ADVENTURE);
				p.setLevel(0);
				p.setExp(0);
				GInventoryListener.setItemLobby(p);
				
				if(!main.manager.getPlayers().isEmpty() && main.manager.getPlayers().size() <= 1) { new GWaiting(main).runTaskTimer(main, 0, 20L); }

			//Un message d'erreur est envoyé si le spawn lobby est pas définit
			} catch(NullPointerException ex) { 
				
				String error = ChatColor.RED + "Point de spawn lobby non éxistant ! \n Si vous n'êtes pas administrateur, veuillez demander à un administrateur de le définir !";
				
				p.sendMessage(main.prefix + error);
			
			}

  		//Un message d'erreur est envoyé si le spawn lobby est pas définit et qu'il y'a un erreur dans la configuration (Coordonnées du spawn null)"
		} catch(IllegalArgumentException ex) {

			String error = ChatColor.RED + "Point de spawn lobby non éxistant, Erreur de Configuration ! \n Si vous n'êtes pas administrateur, veuillez demander à un administrateur de le définir !";

			p.sendMessage(main.prefix + error);
		}

		ConfigFileManager.LoadKillList();

		// Créer un scoreboard pour le joueur //
		new ScoreBoard(main).getScoreBoard(p, false, true, true);
		// Créer un scoreboard pour le joueur //

	}
	// Évènement quand un joueur rejoint le serveur //


	// Évènement quand un joueur quitte le serveur //
	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		
		Player p = e.getPlayer();

		// Retire les information du scoreboard au joueur //
		Scoreboard board = ScoreboardManager.getScoreboard(p);

		for(String entries : board.getEntries()) board.resetScores(entries);
		for(Team team : board.getTeams()) team.unregister();

		// Retire les information du scoreboard au joueur //


		//Retire le joueur de la liste des joueurs en jeu
		if(main.manager.getPlayers().contains(p)) main.manager.getPlayers().remove(p);

		
		if(main.manager.isState(GState.WAITING) || main.manager.isState(GState.STARTING) || main.manager.isState(GState.FINISH)) { return; }
		else { main.manager.CheckWin(p); } //Vérifie qui à gagner s'il y'a un gagnant si le joueur quitte en pleine jeu

	}
	// Évènement quand un joueur rejoint le serveur //


	// Évènement quand le joueur jette un item //
	@EventHandler
	public void onDrop(PlayerDropItemEvent e) {

		Player p = e.getPlayer();

		if(fr.TheSakyo.EvhoUtility.utils.custom.CustomMethod.hasByPassPerm(p)) { e.setCancelled(false); }
		else { e.setCancelled(true); }
	}
	// Évènement quand le joueur jette un item //


	// Évènement quand le joueur rammasse un item //
	@EventHandler
	public void onPickUp(EntityPickupItemEvent e) {

		if(e.getEntity() instanceof Player p) {

			if(fr.TheSakyo.EvhoUtility.utils.custom.CustomMethod.hasByPassPerm(p)) { e.setCancelled(false);
			} else { e.setCancelled(true); }
		}
	}
	// Évènement quand le joueur rammasse un item //



	// Évènement quand le joueur casse un bloc //
	@EventHandler
	public void onBreak(BlockBreakEvent e) {

		Player p = e.getPlayer();

		if(fr.TheSakyo.EvhoUtility.utils.custom.CustomMethod.hasByPassPerm(p)) { e.setCancelled(false); }
		else { e.setCancelled(true); }
	}
	// Évènement quand le joueur casse un bloc //


	// Évènement quand le joueur pose un bloc //
	@EventHandler
	public void onPlace(BlockPlaceEvent e) {

		Player p = e.getPlayer();

		ItemStack it = e.getItemInHand();

		Block placedBlock = e.getBlockPlaced();
		Block blockAgainst = e.getBlockAgainst();

		if(fr.TheSakyo.EvhoUtility.utils.custom.CustomMethod.hasByPassPerm(p)) { e.setCancelled(false); }
		else e.setCancelled(true);
	}
	// Évènement quand le joueur pose un bloc //

						/* ---------------------------------------------------------------------- */

	// Évènement quand un bloc apparaît automatiquement suite à un autre évènement //
	@EventHandler
	public void onFromTo(BlockFromToEvent e) {

		e.setCancelled(true);

								/* ------------------------------------------- */

		// Aprés cinq secondes, on recharge les 'NPCs' & hologrammes pour le Joueur //
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(main, new Runnable() {

			@Override
			public void run() { e.getBlock().setType(Material.AIR); }

		}, 20*5);
		// Aprés cinq secondes, on recharge les 'NPCs' & hologrammes pour le Joueur //
	}
	// Évènement quand un bloc apparaît automatiquement suite à un autre évènement //

										/* ------------------------------------ */

	// Évènement quand un bloc se forme automatiquement //
	@EventHandler
	public void onFromTo(BlockFormEvent e) { e.setCancelled(true); }
	// Évènement quand un bloc se forme automatiquement //

	/*********************/
	/* PARTIE CONNECTION */
	/*********************/
	
}