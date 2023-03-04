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
				
				if(args.length == 0) {

					try {

						World World = Bukkit.getServer().getWorld(ConfigFile.getString(main.config, "lobby.World"));

						Double X = ConfigFile.getDouble(main.config, "lobby.X");
						Double Y = ConfigFile.getDouble(main.config, "lobby.Y");
						Double Z = ConfigFile.getDouble(main.config, "lobby.Z");
						Float Yaw = Float.valueOf(ConfigFile.getString(main.config, "lobby.Yaw"));
						Float Pitch = Float.valueOf(ConfigFile.getString(main.config, "lobby.Pitch"));

						new Location(World, X, Y, Z, Yaw, Pitch);

						p.sendMessage(main.prefix + ChatColor.RED + "Point de spawn lobby déjà éxistant ! Essayez '/unsetlobby' pour le supprimer et ensuite réessayez");

					} catch(IllegalArgumentException | NullPointerException e) {
					
						ConfigFile.set(main.config, "lobby.World", p.getLocation().getWorld().getName());
						ConfigFile.set(main.config, "lobby.X", p.getLocation().getX());
						ConfigFile.set(main.config, "lobby.Y", p.getLocation().getY());
						ConfigFile.set(main.config, "lobby.Z", p.getLocation().getZ());
						ConfigFile.set(main.config, "lobby.Yaw", p.getLocation().getYaw() + "f");
						ConfigFile.set(main.config, "lobby.Pitch", p.getLocation().getPitch() + "f");

						ConfigFile.saveConfig(main.config);

						p.sendMessage(main.prefix + ChatColor.GREEN + "Point de spawn lobby définit !");

					}
				
				} else if(args.length != 0) { p.sendMessage(main.prefix + ChatColor.RED + "Essayez /setlobby sans arguments"); }
				
			} else { p.sendMessage(main.prefix + ChatColor.RED + "Vous n'avez pas les permissions requises !"); }
			return true;

		} else { sender.sendMessage(main.prefix + "Vous devez être en jeu pour définit le spawn lobby !"); }
		return false;
	}
	
	/*********************************************/
	/* PARTIE COMMANDE POUR POINT DE SPAWN LOBBY */ 
    /*********************************************/
}
