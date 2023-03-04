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

public class SetGameCommand implements CommandExecutor {
	
	/* Récupère la class "Main" */
	private ArenaMain main;
	public SetGameCommand(ArenaMain pluginMain) { this.main = pluginMain; }
	/* Récupère la class "Main" */

	
	/********************************************/
	/* PARTIE COMMANDE POUR POINT DE SPAWN JEUX */
    /********************************************/
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String alias, String[] args) {
		
		if(sender instanceof Player p) {

			if(p.hasPermission("evhoarena.game")) {
				
				if(args.length == 1) {

					if(args[0].equalsIgnoreCase("blue")) {

						try {

							World BlueWorld = Bukkit.getServer().getWorld(ConfigFile.getString(main.config, "game.blue.World"));

							Double BlueX = ConfigFile.getDouble(main.config, "game.blue.X");
							Double BlueY = ConfigFile.getDouble(main.config, "game.blue.Y");
							Double BlueZ = ConfigFile.getDouble(main.config, "game.blue.Z");
							Float BlueYaw = Float.valueOf(ConfigFile.getString(main.config, "game.blue.Yaw"));
							Float BluePitch = Float.valueOf(ConfigFile.getString(main.config, "game.blue.Pitch"));

							new Location(BlueWorld, BlueX, BlueY, BlueZ, BlueYaw, BluePitch);

							p.sendMessage(main.prefix + ChatColor.RED + "Point de spawn jeu déjà éxistant pour les " + ChatColor.AQUA + "bleus" + ChatColor.RED +  " ! Essayez '/unsetgame blue' pour le supprimer et ensuite réessayez");

						} catch(IllegalArgumentException | NullPointerException e) {

							ConfigFile.set(main.config, "game.blue.World", p.getLocation().getWorld().getName());
							ConfigFile.set(main.config, "game.blue.X", p.getLocation().getX());
							ConfigFile.set(main.config, "game.blue.Y", p.getLocation().getY());
							ConfigFile.set(main.config, "game.blue.Z", p.getLocation().getZ());
							ConfigFile.set(main.config, "game.blue.Yaw", p.getLocation().getYaw() + "f");
							ConfigFile.set(main.config, "game.blue.Pitch", p.getLocation().getPitch() + "f");

							p.sendMessage(main.prefix + ChatColor.GREEN + "Point de spawn jeu définit pour les " + ChatColor.AQUA + "bleus" + ChatColor.GREEN + " !");

							ConfigFile.saveConfig(main.config);
							main.manager.LoadSpawnConfig(p, false, true);
						}

						return true;

					} else if(args[0].equalsIgnoreCase("red")) {

						try {

							World RedWorld = Bukkit.getServer().getWorld(ConfigFile.getString(main.config, "game.red.World"));

							Double RedX = ConfigFile.getDouble(main.config, "game.red.X");
							Double RedY = ConfigFile.getDouble(main.config, "game.red.Y");
							Double RedZ = ConfigFile.getDouble(main.config, "game.red.Z");
							Float RedYaw = Float.valueOf(ConfigFile.getString(main.config, "game.red.Yaw"));
							Float RedPitch = Float.valueOf(ConfigFile.getString(main.config, "game.red.Pitch"));

							new Location(RedWorld, RedX, RedY, RedZ, RedYaw, RedPitch);

							p.sendMessage(main.prefix + ChatColor.RED + "Point de spawn jeu déjà éxistant pour les " + ChatColor.DARK_RED + "rouges" + ChatColor.RED +  " ! Essayez '/unsetgame red' pour le supprimer et ensuite réessayez");

						} catch(IllegalArgumentException | NullPointerException e) {

							ConfigFile.set(main.config, "game.red.World", p.getLocation().getWorld().getName());
							ConfigFile.set(main.config, "game.red.X", p.getLocation().getX());
							ConfigFile.set(main.config, "game.red.Y", p.getLocation().getY());
							ConfigFile.set(main.config, "game.red.Z", p.getLocation().getZ());
							ConfigFile.set(main.config, "game.red.Yaw", p.getLocation().getYaw() + "f");
							ConfigFile.set(main.config, "game.red.Pitch", p.getLocation().getPitch() + "f");

							p.sendMessage(main.prefix + ChatColor.GREEN + "Point de spawn jeu définit pour les " + ChatColor.DARK_RED + "rouges" + ChatColor.GREEN + " !");

							ConfigFile.saveConfig(main.config);
							main.manager.LoadSpawnConfig(p, true, false);

						}

						return true;

					} else { Bukkit.getServer().dispatchCommand(sender, "setgame"); return true; }

				} else if(args.length != 1) { p.sendMessage(main.prefix + ChatColor.RED + "Essayez /setgame <blue> ou <red>"); }

			} else { p.sendMessage(main.prefix + ChatColor.RED + "Vous n'avez pas les permissions requises !"); }
			return true;

		} else { sender.sendMessage(main.prefix + "Vous devez être en jeu pour définit le spawn jeu !"); }
		return false;
	}
	
	/*********************************************/
	/* PARTIE COMMANDE POUR POINT DE SPAWN JEUX  */
    /*********************************************/
}
