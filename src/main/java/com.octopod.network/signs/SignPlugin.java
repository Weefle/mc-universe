package com.octopod.network.signs;

import com.octopod.network.NetworkPlus;
import com.octopod.network.Util;
import com.octopod.octal.common.IOUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Sign;
import org.bukkit.event.block.SignChangeEvent;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Octopod
 *         Created on 3/8/14
 */
public class SignPlugin {

    /**
     * The file to use for sign location storage.
     */
    private static final File databaseFile = new File(NetworkPlus.getDataFolder(), "Signs/signs.db");

    /**
     * The current SignPlugin instance.
     */
    public static SignPlugin instance;

    /**
     * The current SignDatabase instance.
     */
    private static SignDatabase database;

    public static void resetDatabase() {
        NetworkPlus.getLogger().i("Resetting &6NetSign &7database...");
        database = new SignDatabase();
        saveDatabase();
    }

    public static SignDatabase getDatabase() {
        return database;
    }

    public static void saveDatabase() {
        try {
            Util.write(databaseFile, NetworkPlus.gson().toJson(database));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void updateSign(SignChangeEvent event, SignFormat format) {
        event.setLine(0, format.getLine(0));
        event.setLine(1, format.getLine(1));
        event.setLine(2, format.getLine(2));
        event.setLine(3, format.getLine(3));
    }

    public static void updateSign(SignLocation loc, SignFormat format) {
        Location location = loc.toLocation();
        try {
            Sign sign = (Sign)location.getBlock().getState();
            sign.setLine(0, format.getLine(0));
            sign.setLine(1, format.getLine(1));
            sign.setLine(2, format.getLine(2));
            sign.setLine(3, format.getLine(3));
            sign.update();
        } catch (Exception e) {
            database.removeSign(location);
        }
    }

    public static void updateAllSigns() {
        for(Map.Entry<String, ArrayList<SignLocation>> e: getDatabase().getSignMap().entrySet()) {

            String server = e.getKey();
            SignFormat format = new SignFormat(server);
            List<SignLocation> signLocations = e.getValue();

            for(SignLocation signLocation: signLocations)
                updateSign(signLocation, format);
        }
    }

    public SignPlugin() {

        instance = this;

        if(!databaseFile.exists()) {
            resetDatabase();
        } else {
            try {
                FileInputStream is = new FileInputStream(databaseFile);
                StringBuilder sb = new StringBuilder();
                int ch;
                while((ch = is.read()) != -1) sb.append((char)ch);
                IOUtils.closeSilent(is);
                database = NetworkPlus.gson().fromJson(sb.toString(), SignDatabase.class);
            } catch (Exception e) {
                NetworkPlus.getLogger().i("An error has occured while deserializing signs.db!");
                resetDatabase();
            }
        }

        Bukkit.getPluginManager().registerEvents(new SignBukkitListener(), NetworkPlus.getPlugin());
        NetworkPlus.getEventManager().registerListener(new SignNetworkListener());

        updateAllSigns();

        NetworkPlus.getLogger().i("&6NetSign &7functionality experimental!");

    }

}
