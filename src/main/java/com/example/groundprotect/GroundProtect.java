package com.example.groundprotect;

import org.bukkit.plugin.java.JavaPlugin;
import com.example.groundprotect.managers.ProtectionManager;
import com.example.groundprotect.managers.SelectionManager;
import com.example.groundprotect.managers.DataManager;
import com.example.groundprotect.commands.GroundProtectCommand;
import com.example.groundprotect.listeners.BlockListener;
import com.example.groundprotect.listeners.EntityListener;
import com.example.groundprotect.listeners.InteractListener;

public class GroundProtect extends JavaPlugin {

    private ProtectionManager protectionManager;
    private SelectionManager selectionManager;
    private DataManager dataManager;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        
        this.dataManager = new DataManager(this);
        this.protectionManager = new ProtectionManager(this, dataManager);
        this.selectionManager = new SelectionManager();
        
        dataManager.loadData();
        
        getCommand("gp").setExecutor(new GroundProtectCommand(this, protectionManager, selectionManager, dataManager));
        
        getServer().getPluginManager().registerEvents(new BlockListener(protectionManager), this);
        getServer().getPluginManager().registerEvents(new EntityListener(protectionManager), this);
        getServer().getPluginManager().registerEvents(new InteractListener(this, protectionManager, selectionManager, dataManager), this);
        
        getLogger().info("§a[GroundProtect] 插件已啟動!");
    }

    @Override
    public void onDisable() {
        if (dataManager != null) {
            dataManager.saveData();
        }
        getLogger().info("§c[GroundProtect] 插件已關閉!");
    }

    public ProtectionManager getProtectionManager() {
        return protectionManager;
    }

    public SelectionManager getSelectionManager() {
        return selectionManager;
    }

    public DataManager getDataManager() {
        return dataManager;
    }
}