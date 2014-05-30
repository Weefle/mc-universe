package com.octopod.network.signs;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

/**
 * @author Octopod
 *         Created on 3/11/14
 */
public class SignLocation {

    private int x, y, z;
    private String world;

    public SignLocation(Location loc) {
        this(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ(), loc.getWorld().getName());
    }

    public SignLocation(int x, int y, int z, String world) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.world = world;
    }

    public int X() {return x;}
    public int Y() {return y;}
    public int Z() {return z;}
    public String World() {return world;}

    public Location toLocation() {
        World w = Bukkit.getWorld(world);
        return new Location(w, x, y, z);
    }

    @Override
    public boolean equals(Object o) {
        if(!(o instanceof SignLocation))
            return false;
        SignLocation loc = (SignLocation)o;
        return (loc.X() == x && loc.Y() == y && loc.Z() == z && loc.World().equals(world));
    }

    @Override
    public String toString() {
        return "[" + x + ", " + y + ", " + z + ", " + world + "]";
    }


}
