/* PARTIE IMPORTATIONS + PACKAGE DE LA CLASS */

package fr.TheSakyo.EvhoArena.commands;

import fr.TheSakyo.EvhoUtility.config.ConfigFile;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.TheSakyo.EvhoArena.ArenaMain;
import org.bukkit.ChatColor;

/* PARTIE IMPORTATIONS + PACKAGE DE LA CLASS */

public class UnSetGameCommand implements CommandExecutor {

	/* Récupère la class "Main" */
	private ArenaMain main;
	public UnSetGameCommand(ArenaMain pluginMain) { this.main = pluginMain; }
	/* Récupère la class "Main" */



	/*********************************************/
	/* PARTIE COMMANDE POUR POINT DE SPAWN JEUX */
    /*********************************************/
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String alias, String[] args) {
		
		if(sender instanceof Player p) {

			if(!p.hasPermission("evhoarena.game")) {
				
				p.sendMessage(main.prefix + ChatColor.RED + "Vous n'avez pas les permissions requises !");
				return true;
			}
		}
				
		if(args.length == 1) {
			
			if(args[0].equalsIgnoreCase("blue")) {
				
				try {

					World BlueWorld = Bukkit.getServer().getWorld(ConfigFile.getString(main.config, "game.blue.World"));

					Double BlueX = ConfigFile.getDouble(main.config, "game.blue.X");
					Double BlueY = ConfigFile.getDouble(main.config, "game.blue.Y");
					Double BlueZ = ConfigFile.getDouble(main.config, "game.blue.Z");
					Float BlueYaw = Float.valueOf(ConfigFile.getString(main.config, "game.blue.Yaw"));
					Float BluePitch = Float.valueOf(ConfigFile.getString(main.config, "game.blue.Pitch"));

					Location locBlue = new Location(BlueWorld, BlueX, BlueY, BlueZ, BlueYaw, BluePitch);

					if(main.manager.getSpawns().contains(locBlue)) main.manager.getSpawns().remove(locBlue);

					ConfigFile.set(main.config, "game.blue.World", null);
					ConfigFile.set(main.config, "game.blue.X", null);
					ConfigFile.set(main.config, "game.blue.Y", null);
					ConfigFile.set(main.config, "game.blue.Z", null);
					ConfigFile.set(main.config, "game.blue.Yaw", null);
					ConfigFile.set(main.config, "game.blue.Pitch", null);

					sender.sendMessage(main.prefix + ChatColor.GREEN + "Point de spawn jeu supprimé pour les " + ChatColor.AQUA + "bleus" + ChatColor.GREEN + " !");

					ConfigFile.saveConfig(main.config);

				} catch(IllegalArgumentException | NullPointerException e) { sender.sendMessage(main.prefix + ChatColor.RED + "Point de spawn jeu non éxistant pour les " + ChatColor.AQUA + "bleus" + ChatColor.RED + " !"); }

				return true;

			} else if(args[0].equalsIgnoreCase("red")) {
				
				try {

					World RedWorld = Bukkit.getServer().getWorld(ConfigFile.getString(main.config, "game.red.World"));

					Double RedX = ConfigFile.getDouble(main.config, "game.red.X");
					Double RedY = ConfigFile.getDouble(main.config, "game.red.Y");
					Double RedZ = ConfigFile.getDouble(main.config, "game.red.Z");
					Float RedYaw = Float.valueOf(ConfigFile.getString(main.config, "game.red.Yaw"));
					Float RedPitch = Float.valueOf(ConfigFile.getString(main.config, "game.red.Pitch"));

					Location locRed = new Location(RedWorld, RedX, RedY, RedZ, RedYaw, RedPitch);

					if(main.manager.getSpawns().contains(locRed)) main.manager.getSpawns().remove(locRed);

					ConfigFile.set(main.config, "game.red.World", null);
					ConfigFile.set(main.config, "game.red.X", null);
					ConfigFile.set(main.config, "game.red.Y", null);
					ConfigFile.set(main.config, "game.red.Z", null);
					ConfigFile.set(main.config, "game.red.Yaw", null);
					ConfigFile.set(main.config, "game.red.Pitch", null);

					sender.sendMessage(main.prefix + ChatColor.GREEN + "Point de spawn jeu supprimé pour les " + ChatColor.RED + "rouges" + ChatColor.GREEN + " !");
					ConfigFile.saveConfig(main.config);

				} catch(IllegalArgumentException | NullPointerException e) { sender.sendMessage(main.prefix + ChatColor.RED + "Point de spawn jeu non éxistant pour les " + ChatColor.DARK_RED + "rouges" + ChatColor.RED + " !"); }

				return true;

			} else { Bukkit.getServer().dispatchCommand(sender, "setgame"); return true; }

		} else if(args.length != 1) { sender.sendMessage(main.prefix + ChatColor.RED + "Essayez /unsetgame <blue> ou <red>"); }

		return false;
	}
	
	/*********************************************/
	/* PARTIE COMMANDE POUR POINT DE SPAWN JEUX */
    /*********************************************/

}
