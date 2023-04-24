package fr.TheSakyo.EvhoArena.registering;

import fr.TheSakyo.EvhoArena.ArenaMain;
import fr.TheSakyo.EvhoArena.events.GDamageListener;
import fr.TheSakyo.EvhoArena.events.GInventoryListener;
import fr.TheSakyo.EvhoArena.events.GListener;
import fr.TheSakyo.EvhoArena.events.GPlayerListener;
import org.bukkit.plugin.PluginManager;

public class RegisterEvents {

    public static void init(ArenaMain main, PluginManager pluginManager) {

        pluginManager.registerEvents(new GPlayerListener(main), main); //Création des évenements du joueurs
        pluginManager.registerEvents(new GDamageListener(main), main); //Création des évenements des dommages
        pluginManager.registerEvents(new GInventoryListener(main), main); //Création des évenements du joueurs

        pluginManager.registerEvents(new GListener(main), main); //Création des évenements utiles
    }
}
