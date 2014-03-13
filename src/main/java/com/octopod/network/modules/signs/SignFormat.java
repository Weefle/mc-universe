package com.octopod.network.modules.signs;

import com.octopod.network.ServerInfo;
import com.octopod.network.cache.NetworkPlayerCache;
import com.octopod.network.cache.NetworkServerCache;
import org.bukkit.ChatColor;

/**
 * @author Octopod
 *         Created on 3/12/14
 */
public class SignFormat {

    private static String F1 = "&4+------------+";
    private static String F2 = "&1%server";
    private static String F3 = "&1%players Players";
    private static String F4 = "&4+------------+";

    public String L1, L2, L3, L4;

    private String formatLine(String format, String server) {
        ServerInfo serverInfo = NetworkServerCache.getInfo(server);
        if(serverInfo == null) {
            format = format.replaceAll("%server", server);
            format = format.replaceAll("%players", "0");
        } else {
            format = format.replaceAll("%server", serverInfo.getServerName());
            format = format.replaceAll("%players", String.valueOf(NetworkPlayerCache.totalPlayers(server)));
        }
        return ChatColor.translateAlternateColorCodes('&', format);
    }

    public SignFormat(String server) {
        this(server, F1, F2, F3, F4);
    }

    private SignFormat(String server, String L1, String L2, String L3, String L4) {

        this.L1 = formatLine(L1, server);
        this.L2 = formatLine(L2, server);
        this.L3 = formatLine(L3, server);
        this.L4 = formatLine(L4, server);

    }

    public String getLine(int i) {
        switch(i) {
            default:
            case 0:
                return L1;
            case 1:
                return L2;
            case 2:
                return L3;
            case 3:
                return L4;
        }
    }

}
