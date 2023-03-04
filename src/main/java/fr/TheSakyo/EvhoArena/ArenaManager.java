/* PARTIE IMPORTATIONS + PACKAGE DE LA CLASS */

package fr.TheSakyo.EvhoArena;

import java.time.Duration;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import com.google.common.collect.Iterables;
import fr.TheSakyo.EvhoArena.enums.GOver;
import fr.TheSakyo.EvhoArena.enums.GState;
import fr.TheSakyo.EvhoUtility.UtilityMain;
import fr.TheSakyo.EvhoUtility.config.ConfigFile;
import fr.TheSakyo.EvhoUtility.config.WorldHandler;
import fr.TheSakyo.EvhoUtility.utils.custom.CustomMethod;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

/* PARTIE IMPORTATIONS + PACKAGE DE LA CLASS */

public class ArenaManager {
	
	/* Récupère la class "Main" */
	private ArenaMain main;
	public ArenaManager(ArenaMain pluginMain) { this.main = pluginMain; }
	/* Récupère la class "Main" */
<<<<<<< HEAD
	
=======


	// On vérifie si le jeux est désactivé //
	private boolean isDisabled = false;
	// On vérifie si le jeux est désactivé //


	/*************************************************/
	/* ON VÉRIFIE SI LE MINI-JEUX EST ACTIVÉ ON NON */
	/************************************************/
	public void isDisabled(boolean isDisabled) { this.isDisabled = isDisabled; }
	public boolean isDisabled() { return this.isDisabled; }

	/*************************************************/
	/* ON VÉRIFIE SI LE MINI-JEUX EST ACTIVÉ ON NON */
	/************************************************/

>>>>>>> 928b412 (Initial commit)
	
	/****************************/
	/* PARTIE STATES ET JOUEURS */
	/****************************/
<<<<<<< HEAD
	
	public void setState(GState state) { main.state = state; }
	
	
=======

	public void setState(GState state) { main.state = state; }


>>>>>>> 928b412 (Initial commit)
	public boolean isState(GState state) { return main.state == state; }



	public void setOver(GOver over) { main.over = over; }


	public boolean isOver(GOver over) { return main.over == over; }
	
	
	
	public List<Player> getPlayers() { return main.players; }
	
	public List<Location> getSpawns() { return main.spawns; }


	//Récupère les "Locations" spawns de façon random
	public Location RandomSpawn() {

		//Variable pour random
		Random r = new Random();

		return main.spawns.get(r.nextInt(main.spawns.size()));
	}
	//Récupère les "Locations" spawns de façon random


	// Élimine un joueur //
	public void eliminate(Player p, boolean killed) {

		Player winner = p.getKiller();
		
		if(isState(GState.WAITING) || isState(GState.STARTING)) {
			
			if(main.players.size() == 1) { if(main.players.contains(p)) main.players.remove(p); }

			if(main.players.size() == 0) TimerAfterPvP();

		} else {

			if(main.players.contains(p)) main.players.remove(p);
			p.setGameMode(GameMode.SPECTATOR);
			if(killed) p.sendMessage(main.prefix + ChatColor.RED.toString() + ChatColor.BOLD.toString() + "Vous avez perdu !");


			if(main.players.size() == 1) {

				winner = main.players.get(0);

				if(main.players.contains(p)) main.players.remove(p);

				main.manager.setOver(GOver.TRUE);
			}

			if(main.manager.isOver(GOver.TRUE)) CheckWin(winner);

			if(main.players.size() == 0) TimerAfterPvP();
		}
		
	}
	// Élimine un joueur //
	
	
	
	// Vérifie si un joueur a gagné //
	public void CheckWin(Player p) {
		
		if(main.players.size() == 0) { setState(GState.FINISH); return; }

		else if(main.players.size() == 1) {

			if(main.manager.isOver(GOver.FALSE)) return;
			
			setState(GState.FINISH);


			for(Player pls : Bukkit.getServer().getOnlinePlayers()) {

				if(pls == p && main.players.get(0) == pls) {

					Component title = CustomMethod.StringToComponent(ChatColor.GREEN.toString() + ChatColor.BOLD.toString() + "Vous avez gagné !");
					Component subtitle = CustomMethod.StringToComponent("");
					Title.Times times = Title.Times.of(Duration.ofSeconds(2), Duration.ofSeconds(2), Duration.ofSeconds(2));

					p.showTitle(Title.title(title, subtitle, times));

					p.playSound(p.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 1.5f, 1.5f);
					p.sendMessage(main.prefix + ChatColor.GREEN + "Vous avez tué(s) " + ChatColor.GOLD + main.Kills.get(p.getUniqueId()) + ChatColor.GREEN + " joueur(s) !");

					//Ajoute les kills au fichier de configuration et le sauvegarde //
					ConfigFile.set(main.killconfig, p.getUniqueId().toString(), String.valueOf(main.playerKills.get(p.getUniqueId().toString())));
					ConfigFile.saveConfig(main.killconfig);
					//Ajoute les kills au fichier de configuration et le sauvegarde //

				} else {

					Component title = CustomMethod.StringToComponent(ChatColor.GOLD +  "GG à " + ChatColor.YELLOW + p.getName());
					Component subtitle = CustomMethod.StringToComponent(ChatColor.GOLD + "Il gagne la partie !");
					Title.Times times = Title.Times.of(Duration.ofSeconds(2), Duration.ofSeconds(2), Duration.ofSeconds(2));

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
		
		Bukkit.getServer().broadcast(CustomMethod.StringToComponent(main.prefix + ChatColor.RED + " Aucun joueurs gagnent !"));
	}
	// Si le temps d'attente du jeu est finit, et que personne gagnent, on l'affiche. //
	// On fait aprés appel à la méthode TimerAfterPvP() //
	
	
	
	// Méthode qui fait appel à la méthode ConnectPlayerToServer() pour tous les joueurs de l'arêne //
	private void TimerAfterPvP() {
		
	    // Aprés trois seconde, on téléporte le joueur //
        new Timer().schedule(new TimerTask() {
    	   
			@Override
			public void run() { 
				
				if(main.players.size() == 0 || main.players.size() == 1) {
					
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
	
	
	
	
	//Recharge les "Locations" des spawn dans le fichier de configuration
	public void LoadSpawnConfig(Player p, boolean red, boolean blue) {
		
		try {
			
			World WorldLobby = Bukkit.getServer().getWorld(ConfigFile.getString(main.config, "lobby.World"));
			
			Double XLobby = ConfigFile.getDouble(main.config, "lobby.World");
			
			Double YLobby = ConfigFile.getDouble(main.config, "lobby.Y");
			
			Double ZLobby = ConfigFile.getDouble(main.config, "lobby.Z");
			
			Float YawLobby = Float.valueOf(ConfigFile.getString(main.config, "lobby.Yaw"));
			
			Float PitchLobby  = Float.valueOf(ConfigFile.getString(main.config, "lobby.Pitch"));
			
			Location locLobby = new Location(WorldLobby, XLobby, YLobby, ZLobby, YawLobby, PitchLobby);
			
			
			if(locLobby != null) { if(WorldLobby == null) new WorldHandler(main.utilityapi, ConfigFile.getString(main.config, "lobby.World")); }
			
		} catch(IllegalArgumentException | NullPointerException e)  {

			String errorplayer = ChatColor.RED + "Point de spawn lobby non éxistant, Erreur de Configuration ! \n Si vous n'êtes pas administrateur, veuillez demander à un administrateur de le définir !";

			String error = ChatColor.RED + "Point de spawn lobby non éxistant, Erreur de Configuration !";

			if(p == null) { main.console.sendMessage(main.prefix + error); }
			else { p.sendMessage(main.prefix + errorplayer); }

			return;
		}

		if(blue == true) {

			try {

				World WorldBlue = Bukkit.getServer().getWorld(ConfigFile.getString(main.config, "game.blue.World"));

				Double XBlue = ConfigFile.getDouble(main.config, "game.blue.X");

				Double YBlue = ConfigFile.getDouble(main.config, "game.blue.Y");

				Double ZBlue = ConfigFile.getDouble(main.config, "game.blue.Z");

				Float YawBlue = Float.valueOf(ConfigFile.getString(main.config, "game.blue.Yaw"));

				Float PitchBlue  = Float.valueOf(ConfigFile.getString(main.config, "game.blue.Pitch"));

				Location locBlue = new Location(WorldBlue, XBlue, YBlue, ZBlue, YawBlue, PitchBlue);


				if(locBlue != null) {

					if(getSpawns().contains(locBlue)) getSpawns().remove(locBlue);
					getSpawns().add(locBlue);

					if(WorldBlue == null) new WorldHandler(main.utilityapi, ConfigFile.getString(main.config, "game.blue.World"));

				}

			} catch(IllegalArgumentException | NullPointerException e)  {

				String errorplayer = ChatColor.RED + "Point de spawn jeu pour les " + ChatColor.AQUA + "bleus" + ChatColor.RED + " non éxistant, Erreur de Configuration ! \n Si vous n'êtes pas administrateur, veuillez demander à un administrateur de le définir !";

				String error = ChatColor.RED + "Point de spawn jeu pour les " + ChatColor.AQUA + "bleus" + ChatColor.RED + " non éxistant, Erreur de Configuration !";

				if(p == null) { main.console.sendMessage(main.prefix + error); }
				else { p.sendMessage(main.prefix + errorplayer); }
			}
		}

		if(red == true) {

			try {

				World WorldRed = Bukkit.getServer().getWorld(ConfigFile.getString(main.config, "game.red.World"));

				Double XRed = ConfigFile.getDouble(main.config, "game.red.X");

				Double YRed = ConfigFile.getDouble(main.config, "game.red.Y");

				Double ZRed = ConfigFile.getDouble(main.config, "game.red.Z");

				Float YawRed = Float.valueOf(ConfigFile.getString(main.config, "game.red.Yaw"));

				Float PitchRed  = Float.valueOf(ConfigFile.getString(main.config, "game.red.Pitch"));

				Location locRed = new Location(WorldRed, XRed, YRed, ZRed, YawRed, PitchRed);


				if(locRed != null) {

					if(getSpawns().contains(locRed)) getSpawns().remove(locRed);
					getSpawns().add(locRed);

					if(WorldRed == null) new WorldHandler(main.utilityapi, ConfigFile.getString(main.config, "game.red.World"));
				}

			} catch(IllegalArgumentException | NullPointerException e) {

				String errorplayer = ChatColor.RED + "Point de spawn jeu pour les " + ChatColor.DARK_RED + "rouges" + ChatColor.RED + " non éxistant, Erreur de Configuration ! \n Si vous n'êtes pas administrateur, veuillez demander à un administrateur de le définir !";

				String error = ChatColor.RED + "Point de spawn jeu pour les " + ChatColor.DARK_RED + "rouges" + ChatColor.RED + " non éxistant, Erreur de Configuration !";

				if(p == null) { main.console.sendMessage(main.prefix + error); }
				else { p.sendMessage(main.prefix + errorplayer); }
			}
		}
	}
	//Recharge les "Locations" des spawn dans le fichier de configuration
	

}
