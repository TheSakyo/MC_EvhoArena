package fr.TheSakyo.EvhoArena.registering;

import fr.TheSakyo.EvhoArena.ArenaMain;
import fr.TheSakyo.EvhoArena.commands.*;
import org.bukkit.command.CommandExecutor;

public class RegisterCommands {

    public static void init(ArenaMain main) {

		   main.getCommand("arenareload").setExecutor((CommandExecutor) new ReloadCommand(main)); //Commande pour recharger le plugin
		   main.getCommand("arenarl").setExecutor((CommandExecutor)new ReloadCommand(main)); //Autre commande 'alias' pour recharger le plugin


		   main.getCommand("enablegame").setExecutor((CommandExecutor)new EnableGameCommand(main)); //Commande pour activer le mini-jeux
		   main.getCommand("setlobby").setExecutor((CommandExecutor)new SetLobbyCommand(main)); //Commande pour la création du point de spawn lobby
		   main.getCommand("setzonegame").setExecutor((CommandExecutor)new SetZoneGameCommand(main)); //Commande pour la création de la zone de jeu de l'arêne
		   /*main.getCommand("setgame").setExecutor((CommandExecutor)new SetGameCommand(main)); //Commande pour la création du point de spawn de l'arêne*/

		   main.getCommand("listlobby").setExecutor((CommandExecutor)new ListLobbyCommand(main)); //Commande pour afficher la liste des points de spawn lobby
		   main.getCommand("listgame").setExecutor((CommandExecutor)new ListGameCommand(main)); //Commande pour afficher la liste des points de spawn lobby

		   main.getCommand("disablegame").setExecutor((CommandExecutor)new DisableGameCommand(main)); //Commande pour désactiver le mini-jeux
		   main.getCommand("unsetlobby").setExecutor((CommandExecutor)new UnSetLobbyCommand(main)); //Commande pour l'annulation du point de spawn lobby
		   main.getCommand("unsetzonegame").setExecutor((CommandExecutor)new UnSetZoneGameCommand(main)); //Commande pour l'annulation de la zone de jeu de l'arêne
		   /*main.getCommand("unsetgame").setExecutor((CommandExecutor)new UnSetGameCommand(main)); //Commande pour l'annulation du point de spawn de l'arêne*/

    }
}
