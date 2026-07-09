package com.example.groundprotect.managers;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.*;

public class ProtectionManager {

    private final JavaPlugin plugin;
    private final DataManager dataManager;
    private final Map<String, ProtectedBlock> protectedBlocks = new HashMap<>();

    public ProtectionManager(JavaPlugin plugin, DataManager dataManager) {
        this.plugin = plugin;
        this.dataManager = dataManager;
    }

    public void lockBlock(Location location, String blockType) {
        String key = getLocationKey(location);
        protectedBlocks.put(key, new ProtectedBlock(location, blockType));
        dataManager.saveData();
    }

    public void unlockBlock(Location location) {
        String key = getLocationKey(location);
        protectedBlocks.remove(key);
        dataManager.saveData();
    }

    public void removeProtection(Location location) {
        String key = getLocationKey(location);
        protectedBlocks.remove(key);
        dataManager.saveData();
    }

    public boolean isBlockProtected(Block block) {
        String key = getLocationKey(block.getLocation());
        ProtectedBlock pb = protectedBlocks.get(key);
        
        if (pb == null) {
            return false;
        }
        
        if (pb.blockType.equals("all")) {
            return true;
        }
        
        return block.getType().toString().toLowerCase().equals(pb.blockType.toLowerCase());
    }

    public boolean isBlockProtected(Location location) {
        String key = getLocationKey(location);
        return protectedBlocks.containsKey(key);
    }

    public void lockRegion(Location pos1, Location pos2, String blockType) {
        int minX = Math.min(pos1.getBlockX(), pos2.getBlockX());
        int maxX = Math.max(pos1.getBlockX(), pos2.getBlockX());
        int minY = Math.min(pos1.getBlockY(), pos2.getBlockY());
        int maxY = Math.max(pos1.getBlockY(), pos2.getBlockY());
        int minZ = Math.min(pos1.getBlockZ(), pos2.getBlockZ());
        int maxZ = Math.max(pos1.getBlockZ(), pos2.getBlockZ());
        
        World world = pos1.getWorld();
        if (world == null) return;
        
        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    Location loc = new Location(world, x, y, z);
                    lockBlock(loc, blockType);
                }
            }
        }
    }

    public ProtectedBlock getProtection(Location location) {
        String key = getLocationKey(location);
        return protectedBlocks.get(key);
    }

    public Map<String, ProtectedBlock> getProtectedBlocks() {
        return new HashMap<>(protectedBlocks);
    }

    public void setProtectedBlocks(Map<String, ProtectedBlock> blocks) {
        protectedBlocks.clear();
        protectedBlocks.putAll(blocks);
    }

    private String getLocationKey(Location location) {
        return location.getWorld().getName() + "," + 
               location.getBlockX() + "," + 
               location.getBlockY() + "," + 
               location.getBlockZ();
    }

    public static class ProtectedBlock {
        public Location location;
        public String blockType;
        public long lockedTime;

        public ProtectedBlock(Location location, String blockType) {
            this.location = location.getBlock().getLocation();
            this.blockType = blockType;
            this.lockedTime = System.currentTimeMillis();
        }
    }
}