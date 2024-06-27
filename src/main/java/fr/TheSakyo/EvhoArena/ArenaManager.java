/* PARTIE IMPORTATIONS + PACKAGE DE LA CLASS */

package fr.TheSakyo.EvhoArena;

import java.time.Duration;
import java.util.*;

import com.google.common.collect.Iterables;
import fr.TheSakyo.EvhoArena.enums.GOver;
import fr.TheSakyo.EvhoArena.enums.GState;
import fr.TheSakyo.EvhoUtility.UtilityMain;
import fr.TheSakyo.EvhoUtility.config.ConfigFile;
import fr.TheSakyo.EvhoUtility.utils.custom.CustomMethod;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import net.minecraft.ChatFormatting;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

/* PARTIE IMPORTATIONS + PACKAGE DE LA CLASS */

public class ArenaManager {

	/* Récupère la class "Main" */
	private ArenaMain main;
	public ArenaManager(ArenaMain pluginMain) { this.main = pluginMain; }
	/* Récupère la class "Main" */


	// On vérifie si le jeux est désactivé //
	private boolean isDisabled = false;
	// On vérifie si le jeux est désactivé //

	// On vérifie si le jeux est désactivé //
	private List<String> games = new ArrayList<String>();
	// On vérifie si le jeux est désactivé //


	// On récupère un spawn lobby aléatoire avec le nom de sa zone associé //
	Map<String, Location> spawnLobby = new HashMap<String, Location>();
	// On récupère un spawn lobby aléatoire avec le nom de sa zone associé //


	/*************************************************/
	/* ON VÉRIFIE SI LE MINI-JEUX EST ACTIVÉ ON NON */
	/************************************************/
	public void isDisabled(boolean isDisabled) {

		this.isDisabled = isDisabled;

					/* ---------------------------------------------- */

		Bukkit.getServer().getOnlinePlayers().forEach(player -> {

			player.setExp(0);
			player.setLevel(0);
		});
	}
	public boolean isDisabled() { return this.isDisabled; }

	/*************************************************/
	/* ON VÉRIFIE SI LE MINI-JEUX EST ACTIVÉ ON NON */
	/************************************************/


	/**********************************************************************/
	/* PARTIE RÉCUPÉRATION/AJOUT/SUPPRESSION DES ZONES DE JEUX EXISTANTES */
	/**********************************************************************/

	public List<String> getGames() { return this.games; }

	public void addGame(String zoneNameOfGame) {

		if(this.games.contains(zoneNameOfGame)) { return; }
		this.games.add(zoneNameOfGame);
	}

	public void removeGame(String zoneNameOfGame) {

		if(!this.games.contains(zoneNameOfGame)) { return; }
		this.games.remove(zoneNameOfGame);
	}

	/**********************************************************************/
	/* PARTIE RÉCUPÉRATION/AJOUT/SUPPRESSION DES ZONES DE JEUX EXISTANTES */
	/**********************************************************************/


	/****************************/
	/* PARTIE STATES ET JOUEURS */
	/****************************/

	public void setState(GState state) { main.state = state; }
	public boolean isState(GState state) { return main.state == state; }



	public void setOver(GOver over) { main.over = over; }

	public boolean isOver(GOver over) { return main.over == over; }



	public List<Player> getPlayers() { return main.players; }


	// Récupère des spawns de façon 'random' pour les équipes //
	public Map<String, Location> randomSpawn() {

		Map<String, Location> locationMap = new HashMap<String, Location>();

							/* -------------------------------------------------- */
							/* -------------------------------------------------- */

		if(this.spawnLobby != null && !this.spawnLobby.isEmpty()) { return this.spawnLobby; }
		else {

			ConfigurationSection keysZone = ConfigFile.getConfigurationSection(main.config, "lobby");
			if(keysZone != null) {

				for(String gameZone : keysZone.getKeys(false)) {

					String worldName = ConfigFile.getString(main.config, "lobby." + gameZone + ".World");
					Location resultLocation = new Location(Bukkit.getServer().getWorld(worldName), 0, 0, 0);

										/* ----------------------------------------- */

					String xKey = "lobby." + gameZone + ".X";
					String yKey = "lobby." + gameZone + ".Y";
					String zKey = "lobby." + gameZone + ".Z";
					String yawKey = "lobby." + gameZone + ".Yaw";
					String pitchKey = "lobby." + gameZone + ".Pitch";

										/* ----------------------------------------- */

					if(ConfigFile.contains(main.config, xKey)) resultLocation.setX(ConfigFile.getDouble(main.config, xKey));
					if(ConfigFile.contains(main.config, yKey)) resultLocation.setY(ConfigFile.getDouble(main.config, yKey));
					if(ConfigFile.contains(main.config, zKey)) resultLocation.setZ(ConfigFile.getDouble(main.config, zKey));
					if(ConfigFile.contains(main.config, xKey)) resultLocation.setYaw(Float.parseFloat(ConfigFile.getString(main.config, yawKey)));
					if(ConfigFile.contains(main.config, xKey)) resultLocation.setPitch(Float.parseFloat(ConfigFile.getString(main.config, pitchKey)));

										/* ----------------------------------------- */

					locationMap.putIfAbsent(gameZone, resultLocation);
				}
			}

						/* -------------------------------------------------------------------- */

			Object[] keys = locationMap.keySet().toArray();
			String randomKey = String.valueOf(keys[new Random().nextInt(keys.length)]);
			this.spawnLobby.putIfAbsent(randomKey, locationMap.get(randomKey));

						/* -------------------------------------------------------------------- */

			return this.spawnLobby;
		}
	}
	// Récupère des spawns de façon 'random' pour les équipes //


	// Élimine un joueur //
	public void eliminate(Player p, boolean killed) {

		Player winner = p.getKiller();

		if(isState(GState.WAITING) || isState(GState.STARTING)) {

			if(main.players.size() == 1) {

				if(main.players.contains(p)) main.players.remove(p);
			}

			if(main.players.isEmpty()) TimerAfterPvP();

		} else {

			if(main.players.contains(p)) main.players.remove(p);
			p.setGameMode(GameMode.SPECTATOR);
			if(killed) p.sendMessage(main.prefix + ChatFormatting.RED.toString() + ChatFormatting.BOLD.toString() + "Vous avez perdu !");


			if(main.players.size() == 1) {

				winner = main.players.get(0);

				if(main.players.contains(p)) main.players.remove(p);

				main.manager.setOver(GOver.TRUE);
			}

			if(main.manager.isOver(GOver.TRUE)) CheckWin(winner);
			if(main.players.isEmpty()) TimerAfterPvP();
		}

	}
	// Élimine un joueur //



	// Vérifie si un joueur a gagné //
	public void CheckWin(Player p) {

		if(main.players.isEmpty()) {

			setState(GState.FINISH);
			return;

		} else if(main.players.size() == 1) {

			if(main.manager.isOver(GOver.FALSE)) return;

			setState(GState.FINISH);


			for(Player pls : Bukkit.getServer().getOnlinePlayers()) {

				if(pls == p && main.players.get(0) == pls) {

					Component title = CustomMethod.StringToComponent(ChatFormatting.GREEN.toString() + ChatFormatting.BOLD.toString() + "Vous avez gagné !");
					Component subtitle = CustomMethod.StringToComponent("");
					Title.Times times = Title.Times.times(Duration.ofSeconds(2), Duration.ofSeconds(2), Duration.ofSeconds(2));

					p.showTitle(Title.title(title, subtitle, times));

					p.playSound(p.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 1.5f, 1.5f);
					p.sendMessage(main.prefix + ChatFormatting.GREEN + "Vous avez tué(s) " + ChatFormatting.GOLD + main.Kills.get(p.getUniqueId()) + ChatFormatting.GREEN + " joueur(s) !");

					//Ajoute les kills au fichier de configuration et le sauvegarde //
					ConfigFile.set(main.killconfig, p.getUniqueId().toString(), String.valueOf(main.playerKills.get(p.getUniqueId().toString())));
					ConfigFile.saveConfig(main.killconfig);
					//Ajoute les kills au fichier de configuration et le sauvegarde //

				} else {

					Component title = CustomMethod.StringToComponent(ChatFormatting.GOLD +  "GG à " + ChatFormatting.YELLOW + p.getName());
					Component subtitle = CustomMethod.StringToComponent(ChatFormatting.GOLD + "Il gagne la partie !");
					Title.Times times = Title.Times.times(Duration.ofSeconds(2), Duration.ofSeconds(2), Duration.ofSeconds(2));

					pls.showTitle(Title.title(title, subtitle, times));
				}

			}

			TimerAfterPvP();
		}
	}
	// Vérifie si un joueur a gagné //

	/****************************/
	/* PARTIE STATES ET JOUEURS */
	/****************************/



	// Si le temps d'attente du jeu est finit, et que personne gagnent, on l'affiche. //
	// On fait aprés appel à la méthode TimerAfterPvP() //
	public void DelayLong() {

		setState(GState.FINISH);

		Bukkit.getServer().broadcast(CustomMethod.StringToComponent(main.prefix + ChatFormatting.RED + " Aucun joueurs gagnent !"));
	}
	// Si le temps d'attente du jeu est finit, et que personne gagnent, on l'affiche. //
	// On fait aprés appel à la méthode TimerAfterPvP() //



	// Méthode qui fait appel à la méthode ConnectPlayerToServer() pour tous les joueurs de l'arêne //
	private void TimerAfterPvP() {

	    // Aprés trois seconde, on téléporte le joueur //
        new Timer().schedule(new TimerTask() {

			@Override
			public void run() {

				if(main.players.isEmpty() || main.players.size() == 1) {

					for(Player pls : Bukkit.getServer().getOnlinePlayers()) { ConnectPlayerToHubServer(pls);}
				}
			}

        }, 3 * 1000);
        // Aprés trois seconde, on téléporte le joueur //
        
        
        // Vérifie si le jeu n'est pas en attente ou n'est pas en chargement //
        if(!isState(GState.WAITING) || !isState(GState.STARTING)) {

        	if(main.players.size() <= 1) {

        	    // Aprés cinq secondes on recharge le plugin //
                new Timer().schedule(new TimerTask() {

        			@Override
        			public void run() {

        				//Désactive le Plugin, (pm = "PluginManager")
						ArenaMain.pm.disablePlugin(main);

						//Active le Plugin, (pm = "PluginManager")
						ArenaMain.pm.enablePlugin(main); }

                }, 5 * 1000);
				// Aprés cinq secondes on recharge le plugin //
        	}

        }
        // Vérifie si le jeu n'est pas en attente ou n'est pas en chargement //
	}

	// Méthode qui fait appel à la méthode ConnectPlayerToServer() pour tous les joueurs de l'arêne //




	// Petite Méthode (Bungee) pour connecter un joueur vers un autre serveur //
	public void ConnectPlayerToHubServer(Player p) {

		//Vérifie si le serveur fonctionne sous bungee
		if(UtilityMain.getInstance().hasBungee() == false) { return; }

		//Oblige le paramètre "p" de contenir un joueur (Info : Le système de message Bungee requiert obligatoirement un joueur)
		if(p == null) p = Iterables.getFirst(Bukkit.getServer().getOnlinePlayers(), null);

		//Utilisations de l'interface google "ByteArrayDataOutput" pour récupérer un message sous forme binaire
		ByteArrayDataOutput out = ByteStreams.newDataOutput();

		//Variable pour random
		Random r = new Random();

		out.writeUTF("ConnectOther");
		out.writeUTF(p.getName());
		out.writeUTF(UtilityMain.getInstance().hubs.get(r.nextInt(UtilityMain.getInstance().hubs.size())));

		p.getServer().sendPluginMessage(main, "BungeeCord", out.toByteArray());
	}
	// Petite Méthode (Bungee) pour connecter un joueur vers un autre serveur //

}
