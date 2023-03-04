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

public class UnSetLobbyCommand implements CommandExecutor {

	/* Récupère la class "Main" */
	private ArenaMain main;
	public UnSetLobbyCommand(ArenaMain pluginMain) { this.main = pluginMain; }
	/* Récupère la class "Main" */
	
	
	/*********************************************/
	/* PARTIE COMMANDE POUR POINT DE SPAWN LOBBY */ 
    /*********************************************/
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String alias, String[] args) {
		
		if(sender instanceof Player p) {

			if(!p.hasPermission("evhoarena.lobby")) {
				
				p.sendMessage(main.prefix + ChatColor.RED + "Vous n'avez pas les permissions requises !");
				
				return true;
			}
		}
				
		if(args.length == 0) {
			
			try {
			
				World World = Bukkit.getServer().getWorld(ConfigFile.getString(main.config, "lobby.World"));
				
				Double X = ConfigFile.getDouble(main.config, "lobby.X");
				Double Y = ConfigFile.getDouble(main.config, "lobby.Y");
				Double Z = ConfigFile.getDouble(main.config, "lobby.Z");
				Float Yaw = Float.valueOf(ConfigFile.getString(main.config, "lobby.Yaw"));
				Float Pitch = Float.valueOf(ConfigFile.getString(main.config, "lobby.Pitch"));

				new Location(World, X, Y, Z, Yaw, Pitch);

				ConfigFile.set(main.config, "lobby.World", null);
				ConfigFile.set(main.config, "lobby.X", null);
				ConfigFile.set(main.config, "lobby.Y", null);
				ConfigFile.set(main.config, "lobby.Z", null);
				ConfigFile.set(main.config, "lobby.Yaw", null);
				ConfigFile.set(main.config, "lobby.Pitch", null);

				ConfigFile.saveConfig(main.config);

				sender.sendMessage(main.prefix + ChatColor.GREEN + "Point de spawn lobby supprimé !");
			
			} catch(IllegalArgumentException | NullPointerException e) { sender.sendMessage(main.prefix + ChatColor.RED + "Point de spawn lobby non éxistant !");  }
			return true;
		
		} else if(args.length != 0) { sender.sendMessage(main.prefix + ChatColor.RED + "Essayez /unsetlobby sans arguments"); }
				
		return false;
	}
	
	/*********************************************/
	/* PARTIE COMMANDE POUR POINT DE SPAWN LOBBY */ 
    /*********************************************/

}
