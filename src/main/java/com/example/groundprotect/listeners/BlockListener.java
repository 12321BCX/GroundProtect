package com.example.groundprotect.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.Material;
import com.example.groundprotect.managers.ProtectionManager;

public class BlockListener implements Listener {

    private final ProtectionManager protectionManager;

    public BlockListener(ProtectionManager protectionManager) {
        this.protectionManager = protectionManager;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (protectionManager.isBlockProtected(event.getBlock())) {
            event.setCancelled(true);
            event.getPlayer().sendMessage("§c此方塊受到保護");
        }
    }

    @EventHandler
    public void onBlockBurn(BlockBurnEvent event) {
        if (protectionManager.isBlockProtected(event.getBlock())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockFade(BlockFadeEvent event) {
        if (protectionManager.isBlockProtected(event.getBlock())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockExplode(BlockExplodeEvent event) {
        event.blockList().removeIf(block -> {
            if (protectionManager.isBlockProtected(block)) {
                return true;
            }
            return false;
        });
    }

    @EventHandler
    public void onBlockPistonExtend(BlockPistonExtendEvent event) {
        for (org.bukkit.block.Block block : event.getBlocks()) {
            if (protectionManager.isBlockProtected(block)) {
                event.setCancelled(true);
                return;
            }
        }
    }

    @EventHandler
    public void onBlockPistonRetract(BlockPistonRetractEvent event) {
        for (org.bukkit.block.Block block : event.getBlocks()) {
            if (protectionManager.isBlockProtected(block)) {
                event.setCancelled(true);
                return;
            }
        }
    }

    @EventHandler
    public void onBlockFromTo(BlockFromToEvent event) {
        if (protectionManager.isBlockProtected(event.getToBlock())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockPhysics(BlockPhysicsEvent event) {
        if (protectionManager.isBlockProtected(event.getBlock())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockGrow(BlockGrowEvent event) {
        if (protectionManager.isBlockProtected(event.getBlock())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockSpread(BlockSpreadEvent event) {
         if (protectionManager.isBlockProtected(event.getBlock())) {
            event.setCancelled(true);
        }
    }
}
