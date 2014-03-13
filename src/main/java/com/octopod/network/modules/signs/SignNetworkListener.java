package com.octopod.network.modules.signs;

import com.octopod.network.events.EventHandler;
import com.octopod.network.events.player.NetworkPlayerJoinEvent;
import com.octopod.network.events.player.NetworkPlayerLeaveEvent;
import com.octopod.network.events.player.NetworkPlayerRedirectEvent;

import java.util.ArrayList;

/**
 * @author Octopod
 *         Created on 3/12/14
 */
public class SignNetworkListener {

    private void updateSigns(String server) {
        SignFormat format = new SignFormat(server);
        ArrayList<SignLocation> signs = SignPlugin.self.getDatabase().getSigns(server);
        if(signs != null) {
            for(SignLocation sign: signs) {
                SignPlugin.self.updateSign(sign, format);
            }
        }
    }

    @EventHandler
    public void onPlayerJoin(NetworkPlayerJoinEvent event) {
        updateSigns(event.getServer());
    }

    @EventHandler
    public void onPlayerRedirect(NetworkPlayerRedirectEvent event) {
        updateSigns(event.getFromServer());
        updateSigns(event.getServer());
    }

    @EventHandler
    public void onPlayerQuit(NetworkPlayerLeaveEvent event) {
        updateSigns(event.getServer());
    }

}
