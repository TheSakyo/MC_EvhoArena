package fr.TheSakyo.EvhoArena.commands;

import fr.TheSakyo.EvhoArena.ArenaMain;
import fr.TheSakyo.EvhoUtility.config.ConfigFile;
import fr.TheSakyo.EvhoUtility.managers.ZoneManager;
import fr.TheSakyo.EvhoUtility.utils.custom.CustomMethod;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.minecraft.ChatFormatting;
import org.bukkit.Bukkit;
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

                p.sendMessage(main.prefix + ChatFormatting.RED + "Vous n'avez pas les permissions requises !");
                return false;
            }
        }

        if(args.length == 0) {

            try {

                sender.sendMessage(ChatFormatting.GRAY + "========= " + main.prefix + ChatFormatting.GRAY + "=========");
                sender.sendMessage(" ");
                sender.sendMessage(" ");

                sender.sendMessage(ChatFormatting.AQUA.toString() + ChatFormatting.UNDERLINE.toString() + "Liste des arênes de jeux :");

                ConfigurationSection keysZone = ConfigFile.getConfigurationSection(main.config, "game");
                for(String gameZone : keysZone.getKeys(false)) {

                    String gameName = ChatFormatting.GOLD.toString() + ChatFormatting.BOLD.toString() + gameZone;
                    sender.sendMessage(" ");

                    if(sender instanceof Player p) {

                        Component componentName = CustomMethod.StringToComponent(gameName);

                        if(ZoneManager.hasRegion(gameZone)) {

                                            /* ---------------------------------------------------- */

                            if(p.getWorld() == Bukkit.getServer().getWorld("evhogame")) {

                                componentName = componentName.clickEvent(ClickEvent.runCommand("/zone teleport " + ChatFormatting.stripFormatting(gameName)));
                                componentName = componentName.hoverEvent(HoverEvent.showText(CustomMethod.StringToComponent(ChatFormatting.GRAY + "Cliquez pour vous y téléporter")));

                            } else {

                                componentName = componentName.hoverEvent(HoverEvent.showText(CustomMethod.StringToComponent(ChatFormatting.RED + "Vous pouvez vous téléporter que si vous êtes dans le monde 'evhogame'")));
                            }

                                            /* ---------------------------------------------------- */

                        } else { componentName = componentName.hoverEvent(HoverEvent.showText(CustomMethod.StringToComponent(ChatFormatting.RED + "La zone n'a aucune région pour le moment"))); }

                            /* --------------------------------------------- */

                        sender.sendMessage(CustomMethod.StringToComponent(ChatFormatting.WHITE + "- ").append(componentName));

                    } else { sender.sendMessage(ChatFormatting.WHITE + "- " + gameName); }
                }

            } catch(Exception e) { sender.sendMessage(main.prefix + ChatFormatting.RED + "Il y a aucune arêne(s) de jeu(x) actuellement !");  }
            return true;

        } else if(args.length != 0) { sender.sendMessage(main.prefix + ChatFormatting.RED + "Essayez /listgame"); }

        return false;

    }
    /***********************************************************/
    /* PARTIE COMMANDE POUR LA LISTE DES POINTS DE SPAWN LOBBY */
    /***********************************************************/
}
