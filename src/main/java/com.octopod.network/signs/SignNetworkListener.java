package com.octopod.network.signs;

import com.octopod.network.events.EventHandler;
import com.octopod.network.events.player.NetworkPlayerJoinEvent;
import com.octopod.network.events.player.NetworkPlayerLeaveEvent;

import java.util.ArrayList;

/**
 * @author Octopod
 *         Created on 3/12/14
 */
public class SignNetworkListener {

    private void updateSigns(String server) {
        SignFormat format = new SignFormat(server);
        ArrayList<SignLocation> signs = SignPlugin.getDatabase().getSigns(server);
        if(signs != null) {
            for(SignLocation sign: signs) {
                SignPlugin.updateSign(sign, format);
            }
        }
    }

    @EventHandler
    public void onPlayerJoin(NetworkPlayerJoinEvent event) {
        updateSigns(event.getServer());
    }

    @EventHandler
    public void onPlayerQuit(NetworkPlayerLeaveEvent event) {
        updateSigns(event.getServer());
    }

}
