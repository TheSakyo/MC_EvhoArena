package fr.TheSakyo.EvhoArena.commands;

import fr.TheSakyo.EvhoArena.ArenaMain;
import fr.TheSakyo.EvhoUtility.config.ConfigFile;
import fr.TheSakyo.EvhoUtility.utils.custom.CustomMethod;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

public class ListLobbyCommand implements CommandExecutor {


	/* Récupère la class "Main" */
	private ArenaMain main;
	public ListLobbyCommand(ArenaMain pluginMain) { this.main = pluginMain; }
	/* Récupère la class "Main" */


	/***********************************************************/
	/* PARTIE COMMANDE POUR LA LISTE DES POINTS DE SPAWN LOBBY */
    /***********************************************************/

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String alias, String[] args) {

		if(sender instanceof Player p) {

			if(!p.hasPermission("evhoarena.lobby")) {

				p.sendMessage(main.prefix + ChatColor.RED + "Vous n'avez pas les permissions requises !");
				return false;
			}
		}

		if(args.length == 0) {

			try {

				sender.sendMessage(ChatColor.GRAY + "========= " + main.prefix + ChatColor.GRAY + "=========");
				sender.sendMessage(" ");
				sender.sendMessage(" ");

				sender.sendMessage(ChatColor.AQUA.toString() + ChatColor.UNDERLINE.toString() + "Liste des lobby de jeux :");


				ConfigurationSection keysZone = ConfigFile.getConfigurationSection(main.config, "lobby");
                for(String lobbyZone : keysZone.getKeys(false)) {

					double x = ConfigFile.getDouble(main.config, "lobby." + lobbyZone + ".X");
					double y = ConfigFile.getDouble(main.config, "lobby." + lobbyZone + ".Y");
					double z = ConfigFile.getDouble(main.config, "lobby." + lobbyZone + ".Z");
					float yaw = Float.parseFloat(ConfigFile.getString(main.config, "lobby." + lobbyZone + ".Yaw"));
					float pitch = Float.parseFloat(ConfigFile.getString(main.config, "lobby." + lobbyZone + ".Pitch"));

						/* --------------------------------------------- */

					String lobbyName = ChatColor.GOLD.toString() + ChatColor.BOLD.toString() + lobbyZone;

					Component componentName = CustomMethod.StringToComponent(lobbyName);
					componentName = componentName.clickEvent(ClickEvent.runCommand("/tp " + String.valueOf(x) + " " + String.valueOf(y) +  " " + String.valueOf(z)));
					componentName = componentName.hoverEvent(HoverEvent.showText(CustomMethod.StringToComponent("Cliquez pour vous y téléporter (vous devez être dans même monde que le lobby)")));

						/* --------------------------------------------- */

					sender.sendMessage(" ");
					sender.sendMessage(ChatColor.WHITE + "- " + lobbyName);
                }

			} catch(Exception e) { sender.sendMessage(main.prefix + ChatColor.RED + "Il y a aucun point de spawn lobby actuellement !");  }
			return true;

		} else if(args.length != 0) { sender.sendMessage(main.prefix + ChatColor.RED + "Essayez /listlobby"); }

		return false;

    }
    /***********************************************************/
	/* PARTIE COMMANDE POUR LA LISTE DES POINTS DE SPAWN LOBBY */
    /***********************************************************/
}
