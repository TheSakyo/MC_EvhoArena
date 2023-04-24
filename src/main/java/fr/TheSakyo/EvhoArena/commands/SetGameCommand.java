/* PARTIE IMPORTATIONS + PACKAGE DE LA CLASS */

package fr.TheSakyo.EvhoArena.commands;

import fr.TheSakyo.EvhoUtility.config.ConfigFile;
import fr.TheSakyo.EvhoUtility.managers.ZoneManager;
import net.minecraft.ChatFormatting;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.TheSakyo.EvhoArena.ArenaMain;

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

					String name = args[0].replaceAll("[^a-zA-Z0-9]", ""); // Récupère le nom de la zone

					if(args[1].equalsIgnoreCase("blue")) {

						if(ZoneManager.isExist(name) && ZoneManager.hasRegion(name)) {

							try {

								World BlueWorld = Bukkit.getServer().getWorld(ConfigFile.getString(main.config, "game." + name + ".blue.World"));

								Double BlueX = ConfigFile.getDouble(main.config, "game." + name + ".blue.X");
								Double BlueY = ConfigFile.getDouble(main.config, "game." + name + ".blue.Y");
								Double BlueZ = ConfigFile.getDouble(main.config, "game." + name + ".blue.Z");
								Float BlueYaw = Float.valueOf(ConfigFile.getString(main.config, "game." + name + ".blue.Yaw"));
								Float BluePitch = Float.valueOf(ConfigFile.getString(main.config, "game." + name + ".blue.Pitch"));

								new Location(BlueWorld, BlueX, BlueY, BlueZ, BlueYaw, BluePitch);

								p.sendMessage(main.prefix + ChatFormatting.RED + "Point de spawn de la zone de jeu '" + name + "' déjà éxistant pour les " + ChatFormatting.AQUA + "bleus" + ChatFormatting.RED +  " ! Essayez '/unsetgame " + name + " blue' pour le supprimer et ensuite réessayez");

							} catch(IllegalArgumentException | NullPointerException e) {

								ConfigFile.set(main.config, "game." + name + ".blue.World", p.getLocation().getWorld().getName());
								ConfigFile.set(main.config, "game." + name + ".blue.X", p.getLocation().getX());
								ConfigFile.set(main.config, "game." + name + ".blue.Y", p.getLocation().getY());
								ConfigFile.set(main.config, "game." + name + ".blue.Z", p.getLocation().getZ());
								ConfigFile.set(main.config, "game." + name + ".blue.Yaw", p.getLocation().getYaw() + "f");
								ConfigFile.set(main.config, "game." + name + ".blue.Pitch", p.getLocation().getPitch() + "f");

								p.sendMessage(main.prefix + ChatFormatting.GREEN + "Point de spawn de la zone de jeu '" + name + "' définit pour les " + ChatFormatting.AQUA + "bleus" + ChatFormatting.GREEN + " !");

								ConfigFile.saveConfig(main.config);
								return true;
							}

						} else {

							p.sendMessage(ChatFormatting.RED + "La zone de jeu '" + name + "' est introuvable veuillez d'abord la définir avec /setzonegame <zoneName> <pos1> ou <pos2>");
						}

					} else if(args[1].equalsIgnoreCase("red")) {

						if(ZoneManager.isExist(name) && ZoneManager.hasRegion(name)) {

							try {

								World RedWorld = Bukkit.getServer().getWorld(ConfigFile.getString(main.config, "game." + name + ".red.World"));

								Double RedX = ConfigFile.getDouble(main.config, "game." + name + ".red.X");
								Double RedY = ConfigFile.getDouble(main.config, "game." + name + ".red.Y");
								Double RedZ = ConfigFile.getDouble(main.config, "game." + name + ".red.Z");
								Float RedYaw = Float.valueOf(ConfigFile.getString(main.config, "game." + name + ".red.Yaw"));
								Float RedPitch = Float.valueOf(ConfigFile.getString(main.config, "game." + name + ".red.Pitch"));

								new Location(RedWorld, RedX, RedY, RedZ, RedYaw, RedPitch);

								p.sendMessage(main.prefix + ChatFormatting.RED + "Point spawn de la zone de jeu '" + name + "' déjà éxistant pour les " + ChatFormatting.DARK_RED + "rouges" + ChatFormatting.RED +  " ! Essayez '/unsetgame "  + name +  " red' pour le supprimer et ensuite réessayez");

							} catch(IllegalArgumentException | NullPointerException e) {

								ConfigFile.set(main.config, "game." + name + ".red.World", p.getLocation().getWorld().getName());
								ConfigFile.set(main.config, "game." + name + ".red.X", p.getLocation().getX());
								ConfigFile.set(main.config, "game." + name + ".red.Y", p.getLocation().getY());
								ConfigFile.set(main.config, "game." + name + ".red.Z", p.getLocation().getZ());
								ConfigFile.set(main.config, "game." + name + ".red.Yaw", p.getLocation().getYaw() + "f");
								ConfigFile.set(main.config, "game." + name + ".red.Pitch", p.getLocation().getPitch() + "f");

								p.sendMessage(main.prefix + ChatFormatting.GREEN + "Point spawn de la zone de jeu '" + name + "' définit pour les " + ChatFormatting.DARK_RED + "rouges" + ChatFormatting.GREEN + " !");

								ConfigFile.saveConfig(main.config);
								return true;
							}

						} else {

							p.sendMessage(ChatFormatting.RED + "La zone de jeu '" + name + "' est introuvable veuillez d'abord la définir avec /setzonegame <zoneName> <pos1> ou <pos2>");
						}

					} else { Bukkit.getServer().dispatchCommand(sender, "setgame"); return true; }

				} else { p.sendMessage(main.prefix + ChatFormatting.RED + "Essayez /setgame <zoneName>"); }

			} else { p.sendMessage(main.prefix + ChatFormatting.RED + "Vous n'avez pas les permissions requises !"); }

		} else { sender.sendMessage(main.prefix + "Vous devez être en jeu pour définir un spawn de jeu !"); }
		return false;
	}
	
	/*********************************************/
	/* PARTIE COMMANDE POUR POINT DE SPAWN JEUX  */
    /*********************************************/
}
