package com.octopod.network.modules.signs;

import com.octopod.network.NetworkPlus;
import com.octopod.network.Util;
import com.octopod.octolib.common.IOUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Sign;
import org.bukkit.event.block.SignChangeEvent;

import java.io.*;

/**
 * @author Octopod
 *         Created on 3/8/14
 */
public class SignPlugin {

    public static SignPlugin self;
    private static final File databaseFile = new File(NetworkPlus.getDataFolder(), "Signs/signs.db");
    private static SignDatabase database;

    public void updateSign(SignChangeEvent event, SignFormat format) {
        event.setLine(0, format.getLine(0));
        event.setLine(1, format.getLine(1));
        event.setLine(2, format.getLine(2));
        event.setLine(3, format.getLine(3));
    }

    public void updateSign(SignLocation loc, SignFormat format) {
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

    public SignPlugin() {

        self = this;

        if(!databaseFile.exists()) {
            reset();
        } else {
            try {
                FileInputStream is = new FileInputStream(databaseFile);
                StringBuilder sb = new StringBuilder();
                int ch;
                while((ch = is.read()) != -1) sb.append((char)ch);
                IOUtils.closeSilent(is);
                database = NetworkPlus.gson().fromJson(sb.toString(), SignDatabase.class);
            } catch (Exception e) {
                NetworkPlus.getLogger().info("An error has occured while deserializing signs.db!");
                reset();
            }
        }

        Bukkit.getPluginManager().registerEvents(new SignBukkitListener(), NetworkPlus.getPlugin());
        NetworkPlus.getEventManager().registerListener(new SignNetworkListener());

        NetworkPlus.getLogger().info("&6NetSign &7functionality experimental!");

    }

    public void reset() {
        NetworkPlus.getLogger().info("Resetting &6NetSign &7database...");
        database = new SignDatabase();
        save();
    }

    public SignDatabase getDatabase() {
        return database;
    }

    public void save() {
        try {
            Util.write(databaseFile, NetworkPlus.gson().toJson(database));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
