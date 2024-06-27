package fr.TheSakyo.EvhoArena.types;

import org.bukkit.Location;

public class Lobby {

    private Location location;
    private String name;

    /****************************/

    public Lobby(Location location, String name) {

        this.location = location;
        this.name = name;
    }

    /****************************/

    public Location getLocation() { return location; }
    public String getName() { return name; }
}
