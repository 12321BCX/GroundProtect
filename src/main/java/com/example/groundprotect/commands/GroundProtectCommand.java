package com.example.groundprotect.commands;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import com.example.groundprotect.managers.ProtectionManager;
import com.example.groundprotect.managers.SelectionManager;
import com.example.groundprotect.managers.DataManager;
import java.util.ArrayList;
import java.util.List;

public class GroundProtectCommand implements CommandExecutor {

    private final JavaPlugin plugin;
    private final ProtectionManager protectionManager;
    private final SelectionManager selectionManager;
    private final DataManager dataManager;

    public GroundProtectCommand(JavaPlugin plugin, ProtectionManager protectionManager, 
                               SelectionManager selectionManager, DataManager dataManager) {
        this.plugin = plugin;
        this.protectionManager = protectionManager;
        this.selectionManager = selectionManager;
        this.dataManager = dataManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§c只有玩家可以使用此指令");
            return true;
        }

        Player player = (Player) sender;

        if (args.length == 0) {
            showHelp(player);
            return true;
        }

        String subCommand = args[0].toLowerCase();

        switch (subCommand) {
            case "wand":
                return handleWand(player);
            case "lock":
                return handleLock(player, args);
            case "unlock":
                return handleUnlock(player);
            case "remove":
                return handleRemove(player);
            case "info":
                return handleInfo(player);
            case "pos1":
                return handlePos1(player);
            case "pos2":
                return handlePos2(player);
            case "reload":
                return handleReload(player);
            default:
                showHelp(player);
                return true;
        }
    }

    private boolean handleWand(Player player) {
        if (!player.hasPermission("groundprotect.admin.wand")) {
            player.sendMessage(getPrefix() + "§c你沒有權限");
            return true;
        }

        ItemStack wand = new ItemStack(Material.STICK, 1);
        ItemMeta meta = wand.getItemMeta();
        if (meta != null) {
            meta.setDisplayName("§aGroundProtect 選取工具");
            List<String> lore = new ArrayList<>();
            lore.add("§7左鍵選取方塊");
            lore.add("§7右鍵查看資訊");
            meta.setLore(lore);
            wand.setItemMeta(meta);
        }

        player.getInventory().addItem(wand);
        player.sendMessage(getPrefix() + "§a已獲得選取工具");
        return true;
    }

    private boolean handleLock(Player player, String[] args) {
        if (!player.hasPermission("groundprotect.admin.lock")) {
            player.sendMessage(getPrefix() + "§c你沒有權限");
            return true;
        }

        String blockType = args.length > 1 ? args[1] : "all";

        if (selectionManager.hasPos1(player) && selectionManager.hasPos2(player)) {
            Location pos1 = selectionManager.getPos1(player);
            Location pos2 = selectionManager.getPos2(player);
            protectionManager.lockRegion(pos1, pos2, blockType);
            player.sendMessage(getPrefix() + "§a區域已鎖定");
            selectionManager.clearSelection(player);
            return true;
        }

        if (selectionManager.hasSelectedBlock(player)) {
            Location location = selectionManager.getSelectedBlock(player);
            protectionManager.lockBlock(location, blockType);
            player.sendMessage(getPrefix() + "§a方塊已鎖定");
            return true;
        }

        player.sendMessage(getPrefix() + "§c請先選取方塊或設置區域");
        return true;
    }

    private boolean handleUnlock(Player player) {
        if (!player.hasPermission("groundprotect.admin.unlock")) {
            player.sendMessage(getPrefix() + "§c你沒有權限");
            return true;
        }

        if (!selectionManager.hasSelectedBlock(player)) {
            player.sendMessage(getPrefix() + "§c請先選取方塊");
            return true;
        }

        Location location = selectionManager.getSelectedBlock(player);
        protectionManager.unlockBlock(location);
        player.sendMessage(getPrefix() + "§a方塊已解除鎖定");
        return true;
    }

    private boolean handleRemove(Player player) {
        if (!player.hasPermission("groundprotect.admin.remove")) {
            player.sendMessage(getPrefix() + "§c你沒有權限");
            return true;
        }

        if (!selectionManager.hasSelectedBlock(player)) {
            player.sendMessage(getPrefix() + "§c請先選取方塊");
            return true;
        }

        Location location = selectionManager.getSelectedBlock(player);
        protectionManager.removeProtection(location);
        player.sendMessage(getPrefix() + "§c保護資料已刪除");
        return true;
    }

    private boolean handleInfo(Player player) {
        if (!player.hasPermission("groundprotect.use")) {
            player.sendMessage(getPrefix() + "§c你沒有權限");
            return true;
        }

        if (!selectionManager.hasSelectedBlock(player)) {
            player.sendMessage(getPrefix() + "§c請先選取方塊");
            return true;
        }

        Location location = selectionManager.getSelectedBlock(player);
        ProtectionManager.ProtectedBlock pb = protectionManager.getProtection(location);

        player.sendMessage(getPrefix() + "\n§e--- 方塊資訊 ---");
        player.sendMessage("§7世界: §f" + location.getWorld().getName());
        player.sendMessage("§7座標: §f" + location.getBlockX() + ", " + location.getBlockY() + ", " + location.getBlockZ());
        player.sendMessage("§7方塊類型: §f" + location.getBlock().getType());

        if (pb != null) {
            player.sendMessage("§7保護類型: §f" + pb.blockType);
            player.sendMessage("§7狀態: §a已保護");
        } else {
            player.sendMessage("§7狀態: §c未保護");
        }
        player.sendMessage("§e--- 結束 ---\n");
        return true;
    }

    private boolean handlePos1(Player player) {
        if (!player.hasPermission("groundprotect.admin.lock")) {
            player.sendMessage(getPrefix() + "§c你沒有權限");
            return true;
        }

        Location location = player.getLocation();
        selectionManager.setPos1(player, location);
        player.sendMessage(getPrefix() + "§a第一點已設置: " + location.getBlockX() + " " + location.getBlockY() + " " + location.getBlockZ());
        return true;
    }

    private boolean handlePos2(Player player) {
        if (!player.hasPermission("groundprotect.admin.lock")) {
            player.sendMessage(getPrefix() + "§c你沒有權限");
            return true;
        }

        Location location = player.getLocation();
        selectionManager.setPos2(player, location);
        player.sendMessage(getPrefix() + "§a第二點已設置: " + location.getBlockX() + " " + location.getBlockY() + " " + location.getBlockZ());
        return true;
    }

    private boolean handleReload(Player player) {
        if (!player.hasPermission("groundprotect.admin.reload")) {
            player.sendMessage(getPrefix() + "§c你沒有權限");
            return true;
        }

        plugin.reloadConfig();
        dataManager.loadData();
        player.sendMessage(getPrefix() + "§a設定已重新讀取");
        return true;
    }

    private void showHelp(Player player) {
        player.sendMessage("\n§e=== GroundProtect 幫助 ===");
        if (player.hasPermission("groundprotect.admin.wand")) {
            player.sendMessage("§a/gp wand §7- 獲得選取工具");
        }
        if (player.hasPermission("groundprotect.admin.lock")) {
            player.sendMessage("§a/gp lock [blockType] §7- 鎖定方塊或區域");
            player.sendMessage("§a/gp pos1 §7- 設置區域第一點");
            player.sendMessage("§a/gp pos2 §7- 設置區域第二點");
        }
        if (player.hasPermission("groundprotect.admin.unlock")) {
            player.sendMessage("§a/gp unlock §7- 解除鎖定");
        }
        if (player.hasPermission("groundprotect.admin.remove")) {
            player.sendMessage("§a/gp remove §7- 刪除保護資料");
        }
        if (player.hasPermission("groundprotect.use")) {
            player.sendMessage("§a/gp info §7- 查看保護資訊");
        }
        if (player.hasPermission("groundprotect.admin.reload")) {
            player.sendMessage("§a/gp reload §7- 重新讀取設定");
        }
        player.sendMessage("§e==================\n");
    }

    private String getPrefix() {
        return "§a[GroundProtect]§r ";
    }
}