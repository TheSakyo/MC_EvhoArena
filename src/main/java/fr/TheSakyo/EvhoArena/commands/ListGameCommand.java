package fr.TheSakyo.EvhoArena.commands;

import fr.TheSakyo.EvhoArena.ArenaMain;
import fr.TheSakyo.EvhoUtility.config.ConfigFile;
import fr.TheSakyo.EvhoUtility.utils.custom.CustomMethod;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

public class ListGameCommand implements CommandExecutor {


    /* Récupère la class "Main" */
    private ArenaMain main;

    public ListGameCommand(ArenaMain pluginMain) { this.main = pluginMain; }
    /* Récupère la class "Main" */


    /***********************************************************/
    /* PARTIE COMMANDE POUR LA LISTE DES POINTS DE SPAWN LOBBY */
    /***********************************************************/

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String alias, String[] args) {

        if(sender instanceof Player p) {

            if(!p.hasPermission("evhoarena.game")) {

                p.sendMessage(main.prefix + ChatColor.RED + "Vous n'avez pas les permissions requises !");
                return false;
            }
        }

        if(args.length == 0) {

            try {

                sender.sendMessage(ChatColor.GRAY + "========= " + main.prefix + ChatColor.GRAY + "=========");
                sender.sendMessage(" ");
                sender.sendMessage(" ");

                sender.sendMessage(ChatColor.AQUA.toString() + ChatColor.UNDERLINE.toString() + "Liste des arênes de jeux :");


                ConfigurationSection keysZone = ConfigFile.getConfigurationSection(main.config, "game");
                for(String gameZone : keysZone.getKeys(false)) {

                         /* --------------------------------------------- */

                    String gameName = ChatColor.GOLD.toString() + ChatColor.BOLD.toString() + gameZone;
                    sender.sendMessage(" ");

                    if(sender instanceof Player p) {

                        Component componentName = CustomMethod.StringToComponent(gameName);

                        if(p.getWorld() == Bukkit.getServer().getWorld("evhogame")) {

                            componentName = componentName.clickEvent(ClickEvent.runCommand("/zone teleport " + gameName));
                            componentName = componentName.hoverEvent(HoverEvent.showText(CustomMethod.StringToComponent(ChatColor.GRAY + "Cliquez pour vous y téléporter")));

                        } else { componentName = componentName.hoverEvent(HoverEvent.showText(CustomMethod.StringToComponent(ChatColor.GRAY + "Vous pouvez vous téléporter que si vous ête dans le monde 'evhogame'"))); }

                            /* --------------------------------------------- */

                        sender.sendMessage(CustomMethod.StringToComponent(ChatColor.WHITE + "- ").append(componentName));

                    } else { sender.sendMessage(ChatColor.WHITE + "- " + gameName); }
                }

            } catch(Exception e) { sender.sendMessage(main.prefix + ChatColor.RED + "Il y a aucune arêne(s) de jeu(x) actuellement !");  }
            return true;

        } else if(args.length != 0) { sender.sendMessage(main.prefix + ChatColor.RED + "Essayez /listgame"); }

        return false;

    }
    /***********************************************************/
    /* PARTIE COMMANDE POUR LA LISTE DES POINTS DE SPAWN LOBBY */
    /***********************************************************/
}
