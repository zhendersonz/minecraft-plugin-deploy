package com.{{groupId}}.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class {{mainListenerClass}} implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        event.getPlayer().sendMessage("§aBem-vindo ao servidor!");
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        // cleanup se necessario
    }
}
