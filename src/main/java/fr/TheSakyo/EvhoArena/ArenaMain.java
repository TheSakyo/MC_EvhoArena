/* PARTIE IMPORTATIONS + PACKAGE DE LA CLASS */

package fr.TheSakyo.EvhoArena;

import java.time.Duration;
import java.util.*;

import fr.TheSakyo.EvhoArena.enums.GOver;
import fr.TheSakyo.EvhoArena.enums.GState;
import fr.TheSakyo.EvhoArena.registering.RegisterCommands;
import fr.TheSakyo.EvhoArena.registering.RegisterEvents;
import fr.TheSakyo.EvhoArena.utils.MethodCustom;
import fr.TheSakyo.EvhoArena.utils.ScoreBoard;
import fr.TheSakyo.EvhoUtility.config.ConfigFile;
import fr.TheSakyo.EvhoUtility.utils.custom.CustomMethod;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import net.minecraft.ChatFormatting;
import org.bukkit.Bukkit;

import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;


import fr.TheSakyo.EvhoUtility.UtilityMain;
import fr.TheSakyo.EvhoArena.config.ConfigFileManager;
import org.bukkit.scheduler.BukkitRunnable;

/* PARTIE IMPORTATIONS + PACKAGE DE LA CLASS */

public class ArenaMain extends JavaPlugin {
	
	//Variable pour récupérer la méthode "PluginManager" (sert à gérer le plugin)
	public static PluginManager pm = Bukkit.getServer().getPluginManager();

	//Variable pour récupérer le plugin
	public static Plugin plugin;

    // Variable avec l'instance globale //
	public static ArenaMain instance;
	// Variable avec l'instance globale //
	
	//API EvhoUtility//
	public UtilityMain utilityapi;
	//API EvhoUtility//
	
	//Fichier de Configuration du Plugin //
	public ConfigFile config;
	public ConfigFile killconfig;
	//Fichier de Configuration du Plugin //
	
	
	// Variable de la class "GState" //
	public GState state;
	// Variable de la class "GState" //


	// Variable de la class "GOver" //
	public GOver over;
	// Variable de la class "GOver" //
	
	
	// Variable de la class "ArenaManager" et "MethodCustom" //
	public ArenaManager manager = new ArenaManager(this);
	public MethodCustom MC = new MethodCustom(this);
	// Variable de la class "ArenaManager" et "MethodCustom" //
	
	// Variable pour Détecter la Console //
	public ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
	// Variable pour Détecter la Console //

	
	// Variable pour le Nom du Plugin //
	public String prefix = ChatFormatting.WHITE + "[" + ChatFormatting.GREEN + "Evho" + ChatFormatting.YELLOW + "Arena" + ChatFormatting.WHITE + "]" + " ";
	// Variable pour le Nom du Plugin //
		
	
	// Listes des joueurs //
	List<Player> players = new ArrayList<Player>();
	// Listes des joueurs //


	// Listes des tueurs //
	List<Player> killers = new ArrayList<Player>();
	// Listes des tueurs //

	
	
	// Listes des joueur(s) tué(s) (pour scoreboard) //
	public Map<String, Integer> playerKills = new HashMap<String, Integer>();
	public Map<UUID, Integer> Kills = new HashMap<UUID, Integer>();
	// Listes des joueur(s) tué(s)  (pour scoreboard) //

	   
	/* Vérifie si le plugin "EvhoUtility" éxiste dans le serveur */
	private Plugin getUtilityPlugin() { return Bukkit.getServer().getPluginManager().getPlugin("EvhoUtility"); }
	/* Vérifie si le plugin "EvhoUtility" éxiste dans le serveur */

	//Instance de la class "Main" //
	public static ArenaMain getInstance() { return instance; }
	//Instance de la class "Main" //

	/*************************************************************/
	/* PARTIE AVEC ACTIVATION/DÉSACTIVATION/CHARGEMENT DU PLUGIN */
	/************************************************************/

	/* Initialisation (Pour bien démarrer le Plugin) */
	private void init() {

		// ⬇️ Retourne l'Instance du Plugin et le Plugin vers cette 'Class' Principal "EvhoMenuMain" ⬇️ //
		plugin = (Plugin)this;
		instance = this;
		// ⬆️ Retourne l'Instance du Plugin et le Plugin vers cette 'Class' Principal "EvhoMenuMain" ⬆️ //


		ConfigFileManager.LoadConfig(); //Recharge les fichiers de configurations du Plugin utiles pour le serveur


		// ⬇️ Vérifie si le serveur fonctionne sous bungee, si c'est le cas, on enregistre les canaux 'bungee' ⬇️ //
		if(UtilityMain.getInstance().hasBungee() == true) {

			this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
		}
		//⬆️ Vérifie si le serveur fonctionne sous bungee, si c'est le cas, on enregistre les canaux 'bungee' ⬆️ //


		manager.setState(GState.WAITING); //Définit le jeu en atente
		manager.setOver(GOver.FALSE); //Définit le finalisation de la partie sur FAUX


		//Si des joueurs sont en ligne pendant la rechargement du serveur, il est automatiquement, téléporter au Serveur hub
		for(Player players : Bukkit.getServer().getOnlinePlayers()) { manager.ConnectPlayerToHubServer(players); }

		updateScoreboard(); //Recharge le Scoreboard

	}
	/* Initialisation (Pour bien démarrer le Plugin) */


	/* Activation du plugin */
	public void onEnable() {
		
	   //Vérifie si trouve bien le plugin "EvhoUtility" dans le serveur
	   if(getUtilityPlugin().isEnabled()) {

		   this.init(); //Fait Appel à une méthode pour bien intialiser le Plugin

		   RegisterEvents.init(this, pm); // Initialise les évènements
		   RegisterCommands.init(this); // Initialise les commandes

		   console.sendMessage(prefix + ChatFormatting.GREEN + "ArenaGame Enabled"); //Message disant que le plugin est activé
		   manager.randomSpawn();

	   } else {
			   
		   //  ⬇️ Demande le plugin "EvhoUtility" pour le fonctionnement du plugin ⬇️ //
		   console.sendMessage("");
		   console.sendMessage(prefix + ChatFormatting.RED + "Veuillez nous excuser, ce plugin requiert un plugin spécifique !");
		   console.sendMessage(prefix + ChatFormatting.RED + "Le plugin est le suivant : " + ChatFormatting.YELLOW + "EvhoUtility");
		   console.sendMessage("");
		   // ⬆️ Demande le plugin "EvhoUtility" pour le fonctionnement du plugin ⬆️ //

		   pm.disablePlugin(plugin); //Désactive le plugin
	   }
	}
	/* Activation du plugin */


	/* Désactivation du plugin */
	public void onDisable() {

		instance = null; //Retourne l'instance du plugin en "NULL"

		this.disable(); //Petite méthode pour la désactivation du Plugin

		console.sendMessage(prefix + ChatFormatting.DARK_RED + "ArenaGame Disabled"); //Message disant que le plugin est désactivé
	}
	/* Désactivation du plugin */


	/* Petite méthode pour la désactivation du Plugin */
	private void disable() {

		// ⬇️ Pour tous les joueurs en ligne, on leurs affichent un message flottant et on les déconnectes du serveur ⬇️ //
		for(Player players : Bukkit.getServer().getOnlinePlayers()) {

			Component title = CustomMethod.StringToComponent(ChatFormatting.GOLD + "Le Mini-Jeu a été arrété...");
			Component subtitle = CustomMethod.StringToComponent(ChatFormatting.RED + "Retour au Hub...");
			Title.Times times = Title.Times.times(Duration.ofSeconds(2), Duration.ofSeconds(2), Duration.ofSeconds(2));

			players.showTitle(Title.title(title, subtitle, times));
			manager.ConnectPlayerToHubServer(players);
		}
		// ⬆️ Pour tous les joueurs en ligne, on leurs affichent un message flottant et on les déconnectes du serveur ⬆️ //

 		Kills.clear(); //Remet à 0 le compteur de "kill" local des joueurs
	}
	/* Petite méthode pour la désactivation du Plugin */


	/*******************************************************/
	/****** ⬇️ PARTIE AVEC RECHARGEMENT DU PLUGIN ⬇️ ******/
	/******************************************************/

	public void reloadPlugin() {

		this.disable(); //Petite méthode pour la désactivation du Plugin
		this.init(); //Fait Appel à une méthode pour bien intialiser le Plugin
	}

	/*******************************************************/
	/****** ⬆️ PARTIE AVEC RECHARGEMENT DU PLUGIN ⬆️ ******/
	/******************************************************/



	/*************************************************************/
	/* PARTIE AVEC ACTIVATION/DÉSACTIVATION/CHARGEMENT DU PLUGIN */
	/************************************************************/


	/* Boucle qui actualise le scoreboad */
	public void updateScoreboard() {

		new BukkitRunnable() {
			public void run () {

               for(Player p : Bukkit.getServer().getOnlinePlayers()) {

				   if(manager.isState(GState.WAITING)) new ScoreBoard(getInstance()).update(p);
				   else if(manager.isState(GState.PVP)) new ScoreBoard(getInstance()).update(p);

			   }
            }
		}.runTaskTimer(this, 0, 20);
	}
	/* Boucle qui actualise le scoreboad */
}

