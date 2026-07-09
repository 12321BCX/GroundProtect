package com.example.groundprotect.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import com.example.groundprotect.GroundProtect;
import com.example.groundprotect.managers.ProtectionManager;
import com.example.groundprotect.managers.SelectionManager;
import com.example.groundprotect.managers.DataManager;

public class InteractListener implements Listener {

    private final GroundProtect plugin;
    private final ProtectionManager protectionManager;
    private final SelectionManager selectionManager;
    private final DataManager dataManager;

    public InteractListener(GroundProtect plugin, ProtectionManager protectionManager,
                           SelectionManager selectionManager, DataManager dataManager) {
        this.plugin = plugin;
        this.protectionManager = protectionManager;
        this.selectionManager = selectionManager;
        this.dataManager = dataManager;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();

        if (item == null || item.getItemMeta() == null) {
            return;
        }

        ItemMeta meta = item.getItemMeta();
        if (meta.getDisplayName() == null || !meta.getDisplayName().contains("GroundProtect")) {
            return;
        }

        if (!event.hasBlock()) {
            return;
        }

        if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
            event.setCancelled(true);
            selectionManager.selectBlock(player, event.getClickedBlock().getLocation());
            player.sendMessage("§a已選取: " + event.getClickedBlock().getWorld().getName() + " " +
                             event.getClickedBlock().getX() + " " +
                             event.getClickedBlock().getY() + " " +
                             event.getClickedBlock().getZ() + " " +
                             event.getClickedBlock().getType());
        }
        else if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            event.setCancelled(true);
            ProtectionManager.ProtectedBlock pb = protectionManager.getProtection(event.getClickedBlock().getLocation());
            
            player.sendMessage("\n§e--- 方塊資訊 ---");
            player.sendMessage("§7世界: §f" + event.getClickedBlock().getWorld().getName());
            player.sendMessage("§7座標: §f" + event.getClickedBlock().getX() + ", " +
                             event.getClickedBlock().getY() + ", " +
                             event.getClickedBlock().getZ());
            player.sendMessage("§7方塊類型: §f" + event.getClickedBlock().getType());
            
            if (pb != null) {
                player.sendMessage("§7保護類型: §f" + pb.blockType);
                player.sendMessage("§7狀態: §a已保護");
            } else {
                player.sendMessage("§7狀態: §c未保護");
            }
            player.sendMessage("§e--- 結束 ---\n");
        }
    }
}