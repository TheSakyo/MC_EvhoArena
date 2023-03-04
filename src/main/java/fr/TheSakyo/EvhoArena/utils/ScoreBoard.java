/* PARTIE IMPORTATIONS + PACKAGE DE LA CLASS */

package fr.TheSakyo.EvhoArena.utils;

import fr.TheSakyo.EvhoArena.ArenaMain;
import fr.TheSakyo.EvhoUtility.managers.ScoreboardManager;
import fr.TheSakyo.EvhoUtility.utils.custom.CustomMethod;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

/* PARTIE IMPORTATIONS + PACKAGE DE LA CLASS */

public class ScoreBoard {

	/* Récupère la class "Main" */
	private ArenaMain main;
	public ScoreBoard(ArenaMain pluginMain) { this.main = pluginMain; }
	/* Récupère la class "Main" */


	// Titre du Scoreboard //
	String boardname = ArenaMain.instance.prefix.replace(ChatColor.WHITE + "[", "").replace(ChatColor.WHITE +  "]", "");
	// Titre du Scoreboard //


	// Récupère "l'Objective" du ScoreBoard du joueur //
	private Objective getSidebarObjectivePlayer(Player p) { return ScoreboardManager.getScoreboard(p).getObjective(DisplaySlot.SIDEBAR); }
	// Récupère "l'Objective" du ScoreBoard du joueur //

	// Récupère "l'Objective" du ScoreBoard du joueur //
	private Objective getHeartObjectivePlayer(Player p) { return ScoreboardManager.getScoreboard(p).getObjective(DisplaySlot.BELOW_NAME); }
	// Récupère "l'Objective" du ScoreBoard du joueur //



	// Petite Méthode pour afficher les cœurs du joueurs //
	private void getHealth(Objective heart, Player p) {
		heart.getScore(p.getName()).setScore((int) Math.round(p.getHealth()));
	}
	// Petite Méthode pour les cœurs du joueurs  //



	// Petite Méthode pour créer les lignes du scoreboard //
	private void updateScoreBoard(Player p, Objective sidebar, Scoreboard board) {

		// Variables différentes codes couleurs tchat pour le titre du scoreboard //
		String RB = ChatColor.RED.toString() + ChatColor.BOLD.toString();

		String WB = ChatColor.WHITE.toString() + ChatColor.BOLD.toString();
		// Variables différentes codes couleurs tchat pour le titre du scoreboard //


		sidebar.setDisplaySlot(DisplaySlot.SIDEBAR);
		sidebar.displayName(CustomMethod.StringToComponent(WB + "» » » " + ChatColor.RESET + boardname + WB + "« « «"));
		/* sidebar.setDisplayName(WB + "» » » " + ChatColor.RESET + boardname + RB + " " + WB + " « « «"); */

		Score line8 = sidebar.getScore(ChatColor.BLACK + " ");
		line8.setScore(8);


		Team line7 = board.registerNewTeam("Onlines");
		line7.addEntry(ChatColor.DARK_GREEN.toString());
		line7.prefix(CustomMethod.StringToComponent(ChatColor.GREEN + "Nombre(s) de joueur(s) " + ChatColor.WHITE + ": "));
		line7.suffix(CustomMethod.StringToComponent(ChatColor.YELLOW.toString() + main.manager.getPlayers().size()));
		sidebar.getScore(ChatColor.DARK_GREEN.toString()).setScore(7);

		Score line6 = sidebar.getScore(ChatColor.DARK_GRAY + " ");
		line6.setScore(6);

		Score line5 = sidebar.getScore(ChatColor.GRAY + " ");
		line5.setScore(5);

		Score line4 = sidebar.getScore(ChatColor.AQUA.toString() + ChatColor.UNDERLINE.toString()  + "Statistique Du Classement :");
		line4.setScore(4);

		Score line3 = sidebar.getScore(ChatColor.WHITE + " ");
		line3.setScore(3);

		Team line2 = board.registerNewTeam("Kills");
		line2.addEntry(ChatColor.DARK_PURPLE.toString());
		line2.prefix(CustomMethod.StringToComponent(ChatColor.GOLD + "Joueur(s) Tué(s) " + ChatColor.WHITE + ": "));
		line2.suffix(CustomMethod.StringToComponent(ChatColor.RED.toString() + main.playerKills.get(p.getUniqueId().toString())));
		sidebar.getScore(ChatColor.DARK_PURPLE.toString()).setScore(2);

		Score line1 = sidebar.getScore(" ");
		line1.setScore(1);

		Score score = sidebar.getScore(WB + "-------------------------");
		score.setScore(0);

	}
	// Petite Méthode pour créer les lignes du scoreboard //


	// Méthode globale pour ScoreBoard //
    public void getScoreBoard(Player p, boolean heart, boolean sideboard, boolean reset) {

		Scoreboard board = ScoreboardManager.getScoreboard(p);

		if(reset == true) ScoreboardManager.makeScoreboard(p, true);

		if(sideboard == true) {

			Objective obj_sidebar = board.registerNewObjective("gameboard", "dummy", CustomMethod.StringToComponent(" "));

			updateScoreBoard(p, obj_sidebar, board);
		}

		if(heart == true) {

			Objective obj_heart = board.registerNewObjective("showhealth", Criterias.HEALTH, CustomMethod.StringToComponent(ChatColor.DARK_RED + "❤"));
			obj_heart.setDisplaySlot(DisplaySlot.BELOW_NAME);
			obj_heart.setRenderType(RenderType.HEARTS);

			getHealth(obj_heart, p);
		}

		//Fait appel à une classe de l'api "EvhoUtility"
		//(pour enregistrer le scoreboard avec l'actualisation de l'affichage en haut de la tête du joueur)
		ScoreboardManager.makeScoreboard(p, false);


		//On actualise les coeurs du joueurs, si ('heart = true")
		if(heart == true) getHealth(getHeartObjectivePlayer(p), p);

	}
    // Méthode globale pour ScoreBoard //


	//Méthode pour actualiser le scoreboard //
	public void update(Player p) {

		Scoreboard board = ScoreboardManager.getScoreboard(p);

		if(board.getTeam("Onlines") != null) board.getTeam("Onlines").suffix(CustomMethod.StringToComponent(ChatColor.YELLOW.toString() + main.manager.getPlayers().size()));
		if(board.getTeam("Kills") != null) board.getTeam("Kills").suffix(CustomMethod.StringToComponent(ChatColor.RED.toString() + main.playerKills.get(p.getUniqueId().toString())));
	}
	//Méthode pour actualiser le scoreboard //
}