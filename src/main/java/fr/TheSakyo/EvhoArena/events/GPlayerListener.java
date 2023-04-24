/* PARTIE IMPORTATIONS + PACKAGE DE LA CLASS */

package fr.TheSakyo.EvhoArena.events;

import fr.TheSakyo.EvhoArena.utils.ScoreBoard;
import fr.TheSakyo.EvhoArena.config.ConfigFileManager;
import fr.TheSakyo.EvhoUtility.managers.ScoreboardManager;
import net.minecraft.ChatFormatting;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.player.*;

import fr.TheSakyo.EvhoArena.ArenaMain;
import fr.TheSakyo.EvhoArena.enums.GState;
import fr.TheSakyo.EvhoArena.task.GWaiting;
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


	/***********************************/
	/* PARTIE CONNECTION/DÉCONNECTION */
	/**********************************/

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

		// ** ⬇️ Essaye de vérifier si le spawn lobby est définit pour téléporter le joueur où il faut ⬇️ ** //
		try {

			String locname = String.valueOf(main.manager.randomSpawn().keySet().toArray()[0]);
			loclobby = main.manager.randomSpawn().get(locname);

						/* ------------------------------------------------------ */
						/* ------------------------------------------------------ */

			p.teleport(loclobby);
			p.getInventory().clear();
			p.setFoodLevel(20);
			p.setHealth(p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getDefaultValue());

			p.sendMessage(main.prefix + ChatFormatting.AQUA + "/hub ou /lobby pour retourner au hub !");

			p.sendMessage(main.prefix + ChatFormatting.AQUA + "PvPArena chacun pour soi ! Créez vous des alliances et/ou trahisez vous !");

			if(!main.manager.isState(GState.WAITING)) {

				p.setGameMode(GameMode.SPECTATOR);

				if(main.manager.isState(GState.STARTING)) { p.sendMessage(main.prefix + ChatFormatting.GRAY.toString() + ChatFormatting.ITALIC.toString() + "Le jeu est en cours de démarrage ! ");  } else { p.sendMessage(main.prefix + ChatFormatting.GRAY.toString() + ChatFormatting.ITALIC.toString() + "Le jeu a déjà démarré ! "); }

				return;
			}

			if(!main.manager.getPlayers().contains(p)) main.manager.getPlayers().add(p);

			p.setGameMode(GameMode.ADVENTURE);
			p.setLevel(0);
			p.setExp(0);
			GInventoryListener.setItemLobby(p);

			if(!main.manager.getPlayers().isEmpty() && main.manager.getPlayers().size() <= 1) { new GWaiting(main).runTaskTimer(main, 0, 20L); }

		} catch(IllegalArgumentException ex) {

			String error = ChatFormatting.RED + "Point de spawn lobby non éxistant, Erreur de Configuration ! \n Si vous n'êtes pas administrateur, veuillez demander à un administrateur de le définir !";
			p.sendMessage(main.prefix + error);
		}
		// ** ⬆️ Essaye de vérifier si le spawn lobby est définit pour téléporter le joueur où il faut ⬆️ ** //

		ConfigFileManager.LoadKillList(); // Recharge la liste des joueurs tués

		// Créer un scoreboard pour le joueur //
		new ScoreBoard(main).getScoreBoard(p, false, true, true);
		// Créer un scoreboard pour le joueur //

		if(main.manager.isDisabled()) {

			p.sendMessage(main.prefix + ChatFormatting.GRAY.toString() + ChatFormatting.ITALIC.toString() + "Le jeu actuellement désactivé ! ");
		}

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


		if(main.manager.isState(GState.WAITING) || main.manager.isState(GState.STARTING) || main.manager.isState(GState.FINISH)) { return; } else { main.manager.CheckWin(p); } //Vérifie qui à gagner s'il y'a un gagnant si le joueur quitte en pleine jeu

	}
	// Évènement quand un joueur rejoint le serveur //

	/***********************************/
	/* PARTIE CONNECTION/DÉCONNECTION */
	/**********************************/


	/**************************************/
	/* PARTIE INTÉRACTION AVEC LE JOUEUR */
	/*************************************/

	// Évènement quand le joueur jette un item //
	@EventHandler
	public void onDrop(PlayerDropItemEvent e) {

		Player p = e.getPlayer();

		if(fr.TheSakyo.EvhoUtility.utils.custom.CustomMethod.hasByPassPerm(p)) { e.setCancelled(false); } else { e.setCancelled(true); }
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

		if(fr.TheSakyo.EvhoUtility.utils.custom.CustomMethod.hasByPassPerm(p)) { e.setCancelled(false); } else { e.setCancelled(true); }
	}
	// Évènement quand le joueur casse un bloc //

			/* --------------------------------------------------------------------------------------------------------------------------- */

	// Évènement quand le joueur pose un bloc //
	@EventHandler
	public void onPlace(BlockPlaceEvent e) {

		Player p = e.getPlayer();
		Block block = e.getBlock();
		Material type = e.getBlock().getType();

		if(fr.TheSakyo.EvhoUtility.utils.custom.CustomMethod.hasByPassPerm(p)) { e.setCancelled(false); }
		else { e.setCancelled(true); }
	}
	// Évènement quand le joueur pose un bloc //

			/* ---------------------------------------------------------------------- */

	// Évènement quand le joueur vide un seau (lave ou eau) //
	@EventHandler
	public void onPlaceLiquid(PlayerBucketEmptyEvent e) {

		Player p = e.getPlayer(); // Récupère le joueur qui a effectuer l'action
		Material bucket = e.getBucket(); // Vérifie le seau en question

		Block block = e.getBlock(); // Récupère le bloc dans le seau qui a été placer
		Block blockClicked = e.getBlockClicked(); // Récupère bloc qui a été cliuer

		// Si le joueur n'a pas la permission, on effectue quelque permissions une fois le liquide placer
		if(!fr.TheSakyo.EvhoUtility.utils.custom.CustomMethod.hasByPassPerm(p)) {

			/* ⬇️ Si le seau en question est un 'seau d'eau' ou un 'seau de lave' alors on annule l'évènement, on définit un bloc liquide en fonction du seau récupérer,
			   ensuite on met à jour son état de force pour empêcher de le faire couler, aprés 5 seconde on retire le bloc ⬇️ */
			if(bucket == Material.WATER_BUCKET || bucket == Material.LAVA_BUCKET) {

				e.setCancelled(true); // Annule l'évènement

									/* ----------------------------------------- */

				// On créer une donnée de bloc pour un liquide comme de l'eau ou de lave en fonction du seau qui a été récupérer
				BlockData blockData = Bukkit.getServer().createBlockData(bucket == Material.WATER_BUCKET ? Material.WATER : Material.LAVA);

				Block blockAbove = blockClicked.getRelative(e.getBlockFace()); // On récupère le bloc au-dessus du bloc qui a été cliquer
				blockAbove.setBlockData(blockData, false); // On définit une donnée de bloc au bloc au-dessus du bloc qui a été cliquer en précisant qu'on veut aucune propagation

									/* ----------------------------------------- */

				// Aprés trois secondes, on remplace le fameux bloc liquide par de l'air (retire le bloc) //
				Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(main, new Runnable() {

					@Override
					public void run() { blockAbove.setType(Material.AIR); }

				}, 20*3);
				// Aprés trois secondes, on remplace le fameux bloc liquide par de l'air (retire le bloc) //
			}
			/* ⬆️ Si le seau en question est un 'seau d'eau' ou un 'seau de lave' alors on annule l'évènement, on définit un bloc liquide en fonction du seau récupérer,
			   ensuite on met à jour son état de force pour empêcher de le faire couler, aprés 5 seconde on retire le bloc ⬆️ */
		}
	}
	// Évènement quand le joueur vide un seau (lave ou eau) //

			/* --------------------------------------------------------------------------------------------------------------------------- */

	// Évènement quand des blocs se forment par quelconques évènements //
	public void onForm(BlockFromToEvent e) {

		Block toBlock = e.getToBlock(); // Récupère le bloc qui est censé se former

		// On annule l'évènement si le type du bloc formé est de l'obsidienne ou de la pierre taillé
		if(toBlock.getType() == Material.OBSIDIAN || toBlock.getType() == Material.COBBLESTONE) { e.setCancelled(true); }
	}
	// Évènement quand des blocs se forment par quelconques évènements //

	/**************************************/
	/* PARTIE INTÉRACTION AVEC LE JOUEUR */
	/*************************************/
}
