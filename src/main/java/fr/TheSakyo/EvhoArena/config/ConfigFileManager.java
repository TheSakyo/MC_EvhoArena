/* PARTIE IMPORTATIONS + PACKAGE DE LA CLASS */

package fr.TheSakyo.EvhoArena.config;

import fr.TheSakyo.EvhoArena.ArenaMain;
import fr.TheSakyo.EvhoUtility.config.ConfigFile;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

/* PARTIE IMPORTATIONS + PACKAGE DE LA CLASS */

public class ConfigFileManager extends fr.TheSakyo.EvhoUtility.config.ConfigFileManager {

    // Récupère la class "Main" en tant que "static"
    private static ArenaMain mainInstance = ArenaMain.getInstance();


    // ⬇️ *** CHARGEMENT DES FICHIERS DE CONFIGURATIONS D'EVHOARENA *** ⬇️ //
    public static void LoadConfig() {

        // En-Tête du fichier de configuration "config.yml" //
        String[] headerConfig = {
               "| ===== EvhoArena Configuration ===== |",
               " ",
               "*** Configuration Globale ***",
               " ",
               "par TheSakyo",
               " "
        };
        // En-Tête du fichier de configuration "config.yml" //


        // En-Tête du fichier de configuration "playerkills.yml" //
        String[] headerPlayerKills = {
                "| ===== EvhoArena Outils ===== |",
                " ",
                "*** Les kills des joueurs ***",
                " ",
                "par TheSakyo",
                " "
        };
        // En-Tête du fichier de configuration "playerkills.yml" //



        /* Chargement/Création du fichier de configuration 'config.yml' */
        if(getConfigFile(mainInstance.getDataFolder(), "config.yml").exists()) {

            mainInstance.config = getNewConfig(mainInstance.getDataFolder(), "config.yml", headerConfig);
            ConfigFile.reloadConfig(mainInstance.config);

                    /* ------------------------------------------------------------------------ */

            ConfigurationSection keysZone = ConfigFile.getConfigurationSection(mainInstance.config, "zone");
            if(keysZone != null) {

                for(String gameZone : keysZone.getKeys(false)) { mainInstance.manager.addGame(gameZone); }
            }

        } else {

            mainInstance.config = getNewConfig(mainInstance.getDataFolder(), "config.yml", headerConfig);
            ConfigFile.saveConfig(mainInstance.config);
        }
        /* Chargement/Création du fichier de configuration 'config.yml' */



        /* Chargement/Création du fichier de configuration 'playerkills.yml' */
        if(getConfigFile(mainInstance.getDataFolder(), "playerkills.yml").exists()) {

            mainInstance.killconfig = getNewConfig(mainInstance.getDataFolder(), "playerkills.yml", headerPlayerKills);
            ConfigFile.reloadConfig(mainInstance.killconfig);

            ConfigFileManager.LoadKillList();

        } else {

            mainInstance.killconfig = getNewConfig(mainInstance.getDataFolder(), "playerkills.yml", headerPlayerKills);
            ConfigFile.saveConfig(mainInstance.killconfig);
        }
        /* Chargement/Création du fichier de configuration 'playerkills.yml' */


    }
    // ⬆️ *** CHARGEMENT DES FICHIERS DE CONFIGURATIONS D'EVHOARENA *** ⬆️ //


        /* ------------------------------------------------------------- */
        /* ------------------------------------------------------------- */
        /* ------------------------------------------------------------- */


    /**********************************************************************************/
    /* MÉTHODE POUR CHARGER LA LISTE DES "KILLS" DES JOUEURS DEPUIS UN FICHIER CONFIG */
    /**********************************************************************************/
    public static void LoadKillList() {

        // ⬇️ Ajoute les kills des joueurs sauvegardé ⬇️ //
        for(Player p : Bukkit.getServer().getOnlinePlayers()) {

            String playerkill = String.valueOf(ConfigFile.getString(mainInstance.killconfig, p.getUniqueId().toString()));

            if(playerkill == null) return;

            if(playerkill != null) {

                try {

                   int integerkill = Integer.parseInt(playerkill);

                   if(mainInstance.playerKills.containsKey(p.getUniqueId().toString())) mainInstance.playerKills.remove(p.getUniqueId().toString());
                   mainInstance.playerKills.put(p.getUniqueId().toString(), Integer.parseInt(playerkill));

                } catch(NumberFormatException e) { return; }
            }
        }
        // ⬆️ Ajoute les kills des joueurs sauvegardé ⬆️ //
    }
    /**********************************************************************************/
    /* MÉTHODE POUR CHARGER LA LISTE DES "KILLS" DES JOUEURS DEPUIS UN FICHIER CONFIG */
    /**********************************************************************************/
 
}