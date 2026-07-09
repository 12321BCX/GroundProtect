package com.example.groundprotect.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.block.Block;
import com.example.groundprotect.managers.ProtectionManager;

public class EntityListener implements Listener {

    private final ProtectionManager protectionManager;

    public EntityListener(ProtectionManager protectionManager) {
        this.protectionManager = protectionManager;
    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event) {
        event.blockList().removeIf(block -> {
            if (protectionManager.isBlockProtected(block)) {
                return true;
            }
            return false;
        });
    }

    @EventHandler
    public void onEntityChangeBlock(EntityChangeBlockEvent event) {
        Block block = event.getBlock();
        if (protectionManager.isBlockProtected(block)) {
            event.setCancelled(true);
        }
    }
}