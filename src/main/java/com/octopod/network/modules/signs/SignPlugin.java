package com.octopod.network.modules.signs;

import com.octopod.network.NetworkDebug;
import com.octopod.network.NetworkPlugin;
import com.octopod.octolib.common.IOUtils;
import org.bukkit.Bukkit;

import java.io.*;

/**
 * @author Octopod
 *         Created on 3/8/14
 */
public class SignPlugin {

    public static SignPlugin self;
    private static final File databaseFile = new File(NetworkPlugin.self.getDataFolder(), "signs.db");
    private static SignDatabase database;

    public SignPlugin() {

        self = this;

        if(!databaseFile.exists()) {
            database = new SignDatabase();
            save();
        } else {
            try {
                FileInputStream is = new FileInputStream(databaseFile);
                StringBuilder sb = new StringBuilder();
                int ch;
                while((ch = is.read()) != -1) sb.append((char)ch);
                IOUtils.closeSilent(is);
                database = SignDatabase.decode(sb.toString());
            } catch (IOException e) {
                database = new SignDatabase();
                save();
            }
        }

        Bukkit.getPluginManager().registerEvents(new SignBukkitListener(), NetworkPlugin.self);

        NetworkDebug.info("&6Net+Sign &7module loaded.");

    }

    public SignDatabase getDatabase() {
        return database;
    }

    public void save() {
        write(databaseFile, SignDatabase.encode(database));
    }

    private void write(File file, String text) {
        try {
            if(!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
            FileOutputStream output = new FileOutputStream(file);
            IOUtils.copy(new ByteArrayInputStream(text.getBytes()), output);
            IOUtils.closeSilent(output);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
