package com.example.groundprotect.managers;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import java.util.*;

public class SelectionManager {

    private final Map<UUID, Location> pos1 = new HashMap<>();
    private final Map<UUID, Location> pos2 = new HashMap<>();
    private final Map<UUID, Location> selectedBlock = new HashMap<>();

    public void setPos1(Player player, Location location) {
        pos1.put(player.getUniqueId(), location.clone());
    }

    public void setPos2(Player player, Location location) {
        pos2.put(player.getUniqueId(), location.clone());
    }

    public void selectBlock(Player player, Location location) {
        selectedBlock.put(player.getUniqueId(), location.clone());
    }

    public Location getPos1(Player player) {
        return pos1.get(player.getUniqueId());
    }

    public Location getPos2(Player player) {
        return pos2.get(player.getUniqueId());
    }

    public Location getSelectedBlock(Player player) {
        return selectedBlock.get(player.getUniqueId());
    }

    public boolean hasPos1(Player player) {
        return pos1.containsKey(player.getUniqueId());
    }

    public boolean hasPos2(Player player) {
        return pos2.containsKey(player.getUniqueId());
    }

    public boolean hasSelectedBlock(Player player) {
        return selectedBlock.containsKey(player.getUniqueId());
    }

    public void clearSelection(Player player) {
        UUID uuid = player.getUniqueId();
        pos1.remove(uuid);
        pos2.remove(uuid);
        selectedBlock.remove(uuid);
    }
}