package com.example.groundprotect.managers;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import java.io.File;
import java.util.*;

public class DataManager {

    private final JavaPlugin plugin;
    private final File dataFile;
    private FileConfiguration data;
    private final ProtectionManager protectionManager;

    public DataManager(JavaPlugin plugin) {
        this.plugin = plugin;
        this.dataFile = new File(plugin.getDataFolder(), "data.yml");
        
        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdirs();
        }
        
        if (!dataFile.exists()) {
            try {
                dataFile.createNewFile();
            } catch (Exception e) {
                plugin.getLogger().warning("Could not create data.yml: " + e.getMessage());
            }
        }
        
        this.data = YamlConfiguration.loadConfiguration(dataFile);
        this.protectionManager = null;
    }
    
    public DataManager(JavaPlugin plugin, ProtectionManager protectionManager) {
        this.plugin = plugin;
        this.protectionManager = protectionManager;
        this.dataFile = new File(plugin.getDataFolder(), "data.yml");
        
        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdirs();
        }
        
        if (!dataFile.exists()) {
            try {
                dataFile.createNewFile();
            } catch (Exception e) {
                plugin.getLogger().warning("Could not create data.yml: " + e.getMessage());
            }
        }
        
        this.data = YamlConfiguration.loadConfiguration(dataFile);
    }

    public void loadData() {
        try {
            data = YamlConfiguration.loadConfiguration(dataFile);
            
            if (!data.contains("blocks")) {
                return;
            }
            
            Map<String, ProtectionManager.ProtectedBlock> blocks = new HashMap<>();
            
            for (String worldName : data.getConfigurationSection("blocks").getKeys(false)) {
                World world = Bukkit.getWorld(worldName);
                if (world == null) continue;
                
                for (String coordKey : data.getConfigurationSection("blocks." + worldName).getKeys(false)) {
                    String[] coords = coordKey.split(",");
                    int x = Integer.parseInt(coords[0]);
                    int y = Integer.parseInt(coords[1]);
                    int z = Integer.parseInt(coords[2]);
                    
                    String blockType = data.getString("blocks." + worldName + "." + coordKey + ".type", "all");
                    Location location = new Location(world, x, y, z);
                    
                    blocks.put(coordKey, new ProtectionManager.ProtectedBlock(location, blockType));
                }
            }
            
            if (protectionManager != null) {
                protectionManager.setProtectedBlocks(blocks);
            }
            
            plugin.getLogger().info("Loaded " + blocks.size() + " protected blocks");
        } catch (Exception e) {
            plugin.getLogger().warning("Error loading data: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void saveData() {
        try {
            if (protectionManager == null) return;
            
            data = new YamlConfiguration();
            Map<String, ProtectionManager.ProtectedBlock> blocks = protectionManager.getProtectedBlocks();
            
            for (Map.Entry<String, ProtectionManager.ProtectedBlock> entry : blocks.entrySet()) {
                ProtectionManager.ProtectedBlock pb = entry.getValue();
                String worldName = pb.location.getWorld().getName();
                String coordKey = pb.location.getBlockX() + "," + pb.location.getBlockY() + "," + pb.location.getBlockZ();
                
                data.set("blocks." + worldName + "." + coordKey + ".type", pb.blockType);
                data.set("blocks." + worldName + "." + coordKey + ".locked", true);
            }
            
            data.save(dataFile);
            plugin.getLogger().info("Saved " + blocks.size() + " protected blocks");
        } catch (Exception e) {
            plugin.getLogger().warning("Error saving data: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public FileConfiguration getData() {
        return data;
    }
}