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

public class SetLobbyCommand implements CommandExecutor {

	/* Récupère la class "Main" */
	private ArenaMain main;
	public SetLobbyCommand(ArenaMain pluginMain) { this.main = pluginMain; }
	/* Récupère la class "Main" */
	
	
	/*********************************************/
	/* PARTIE COMMANDE POUR POINT DE SPAWN LOBBY */ 
    /*********************************************/
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String alias, String[] args) {
		
		if(sender instanceof Player p) {

			if(p.hasPermission("evhoarena.lobby")) {
				
				if(args.length == 1) {

					String name = args[0].replaceAll("[^a-zA-Z0-9]", ""); // Récupère le nom de la zone

					if(ZoneManager.isExist(name) && ZoneManager.hasRegion(name)) {

						try {

							World World = Bukkit.getServer().getWorld(ConfigFile.getString(main.config, "lobby." + name + ".World"));

							Double X = ConfigFile.getDouble(main.config, "lobby." + name + ".X");
							Double Y = ConfigFile.getDouble(main.config, "lobby." + name + ".Y");
							Double Z = ConfigFile.getDouble(main.config, "lobby." + name + ".Z");
							Float Yaw = Float.valueOf(ConfigFile.getString(main.config, "lobby." + name + ".Yaw"));
							Float Pitch = Float.valueOf(ConfigFile.getString(main.config, "lobby." + name + ".Pitch"));

							new Location(World, X, Y, Z, Yaw, Pitch);

							p.sendMessage(main.prefix + ChatFormatting.RED + "Point de spawn lobby déjà éxistant ! Essayez '/unsetlobby " + name + "' pour le supprimer et ensuite réessayez");

						} catch(IllegalArgumentException | NullPointerException e) {

							ConfigFile.set(main.config, "lobby." + name + ".World", p.getLocation().getWorld().getName());
							ConfigFile.set(main.config, "lobby." + name + ".X", p.getLocation().getX());
							ConfigFile.set(main.config, "lobby." + name + ".Y", p.getLocation().getY());
							ConfigFile.set(main.config, "lobby." + name + ".Z", p.getLocation().getZ());
							ConfigFile.set(main.config, "lobby." + name + ".Yaw", p.getLocation().getYaw() + "f");
							ConfigFile.set(main.config, "lobby." + name + ".Pitch", p.getLocation().getPitch() + "f");

							ConfigFile.saveConfig(main.config);

							p.sendMessage(main.prefix + ChatFormatting.GREEN + "Point de spawn lobby pour la zone de jeu '" + name + "' définit !");
							return true;
						}

					} else {

						p.sendMessage(ChatFormatting.RED + "La zone de jeu '" + name + "' est introuvable veuillez d'abord la définir avec /setzonegame <zoneName> <pos1> ou <pos2>");
					}
				
				} else { p.sendMessage(main.prefix + ChatFormatting.RED + "Essayez /setlobby <zoneName>"); }
				
			} else { p.sendMessage(main.prefix + ChatFormatting.RED + "Vous n'avez pas les permissions requises !"); }

		} else { sender.sendMessage(main.prefix + "Vous devez être en jeu pour définir un spawn lobby !"); }
		return false;
	}
	
	/*********************************************/
	/* PARTIE COMMANDE POUR POINT DE SPAWN LOBBY */ 
    /*********************************************/
}
