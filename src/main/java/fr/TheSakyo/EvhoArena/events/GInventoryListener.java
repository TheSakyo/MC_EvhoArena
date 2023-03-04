/* PARTIE IMPORTATIONS + PACKAGE DE LA CLASS */

package fr.TheSakyo.EvhoArena.events;

import fr.TheSakyo.EvhoArena.ArenaMain;
import fr.TheSakyo.EvhoArena.enums.GState;
import fr.TheSakyo.EvhoUtility.utils.custom.CustomMethod;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

/* PARTIE IMPORTATIONS + PACKAGE DE LA CLASS */

public class GInventoryListener implements Listener {

    /* Récupère la class "Main" */
    private ArenaMain main;
    public GInventoryListener(ArenaMain pluginMain) { this.main = pluginMain; }
    /* Récupère la class "Main" */



    // Variable ItemStack (Item Lobby) //

    private static ItemStack bed;

    // Variable ItemStack (Item Lobby) //


    /************************/
    /* PARTIE ITEM DU LOBBY */
    /************************/

    // Petite méthode pour définir les items du lobby d'un joueur //
    public static void setItemLobby(Player p) {

        bed = new ItemStack(Material.PURPLE_BED);
        ItemMeta metabed = bed.getItemMeta();

        metabed.displayName(CustomMethod.StringToComponent(ChatColor.GREEN + "Retour Hub/Lobby"));
        metabed.lore(List.of(CustomMethod.StringToComponent(ChatColor.GRAY + "Cliquez sur cet item pour quittez la partie !")));

        bed.setItemMeta(metabed);

        p.getInventory().setItem(8, bed);
    }
    // Petite méthode pour définir les items du lobby d'un joueur //



    // Petite méthode pour récupérer des items du lobby spécifique d'un joueur //
    public static ItemStack getItemLobby(String string) {

        if(string.equals("bed")) { return bed; }

        else { return null; }
    }
    // Petite méthode pour récupérer les items du lobby spécifique d'un joueur //


    /************************/
    /* PARTIE ITEM DU LOBBY */
    /************************/



    // Évènement lorsque le joueur clique dans un inventaire //
    @EventHandler
    public void InvClickEvent(InventoryClickEvent e) {

        if(main.manager.isState(GState.WAITING) || main.manager.isState(GState.STARTING)) {

            if(e.getSlot() == 8) e.setCancelled(true);
        }
    }
    // Évènement lorsque le joueur clique dans un inventaire //



    // Évènement lorsque le joueur clique dans un inventaire //
    @EventHandler
    public void ItemDropEvent(PlayerDropItemEvent e) {

        if(main.manager.isState(GState.WAITING) || main.manager.isState(GState.STARTING)) {

            if(e.getPlayer().getInventory().getHeldItemSlot() == 8) e.setCancelled(true);
        }
    }
    // Évènement lorsque le joueur clique dans un inventaire //



    // Évènement lorsque le joueur clique dans un inventaire //
    @EventHandler
    public void ItemClickEvent(PlayerInteractEvent e) {

        if(main.manager.isState(GState.WAITING) || main.manager.isState(GState.STARTING)) {

            if(e.getPlayer().getInventory().getHeldItemSlot() == 8) {

                e.setCancelled(true);

                if(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) { main.manager.ConnectPlayerToHubServer(e.getPlayer()); }

            }
        }
    }
    // Évènement lorsque le joueur clique dans un inventaire //

}