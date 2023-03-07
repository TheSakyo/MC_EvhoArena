package fr.TheSakyo.EvhoArena.commands;

import fr.TheSakyo.EvhoArena.ArenaMain;
import fr.TheSakyo.EvhoUtility.utils.custom.CustomMethod;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.time.Duration;

public class DisableGameCommand implements CommandExecutor {


	/* Récupère la class "Main" */
	private ArenaMain main;
	public DisableGameCommand(ArenaMain pluginMain) { this.main = pluginMain; }
	/* Récupère la class "Main" */


    private Component title = CustomMethod.StringToComponent(" ");
    private Component subtitle = CustomMethod.StringToComponent(ChatColor.RED.toString() + ChatColor.BOLD.toString() + "Mini-Jeu Désactivé !");
    private Title.Times times = Title.Times.of(Duration.ofSeconds(2), Duration.ofSeconds(4), Duration.ofSeconds(2));



	/**********************************************************/
	/* PARTIE COMMANDE POUR DÉSACTIVER LE MINI-JEUX DU PLUGIN */
    /*********************************************************/

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String alias, String[] args) {

		if(sender instanceof Player p) {

			if(p.hasPermission("evhoarena.gamestate")) {

				if(args.length == 0) {

					main.manager.isDisabled(false); //Active le Mini-Jeux
					p.sendMessage(main.prefix + ChatColor.GREEN + "Le mini-jeu a été désactivé !");
                    Bukkit.getServer().getOnlinePlayers().forEach(player -> player.showTitle(Title.title(title, subtitle, times)));

				} else if(args.length != 0) { p.sendMessage(main.prefix + ChatColor.RED + "Essayez /disablegame sans arguments"); }

			} else { p.sendMessage(main.prefix + ChatColor.RED + "Vous n'avez pas les permissions requises !"); }

			return true;

		} else {

			if(args.length == 0) {

                main.manager.isDisabled(false); //Active le Mini-Jeux
                sender.sendMessage(main.prefix + ChatColor.GREEN + "Le mini-jeu a été désactivé !");
                Bukkit.getServer().getOnlinePlayers().forEach(player -> player.showTitle(Title.title(title, subtitle, times)));

			} else if(args.length != 0) { sender.sendMessage(main.prefix + ChatColor.RED + "Essayez /disablegame sans arguments"); }
		}

		return false;
	}

	/**********************************************************/
	/* PARTIE COMMANDE POUR DÉSACTIVER LE MINI-JEUX DU PLUGIN */
    /*********************************************************/
}
