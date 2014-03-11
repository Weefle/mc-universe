package com.octopod.network.modules.signs;

import org.bukkit.Location;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * @author Octopod
 *         Created on 3/11/14
 */
public class SignLocationList {

    private ArrayList<Location> locList = new ArrayList<>();

    public void add(Location loc) {
        locList.add(loc);
    }

    public boolean remove(Location loc) {
        return locList.remove(loc);
    }

    public boolean contains(Location loc) {
        return locList.contains(loc);
    }

    public Iterator<Location> iterator() {
        return locList.iterator();
    }

}
