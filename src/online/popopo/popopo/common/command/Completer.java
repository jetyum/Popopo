package online.popopo.popopo.common.command;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

public class Completer {
    private Map<String, List<HasName>> candidatesMap;

    public Completer() {
        this.candidatesMap = new HashMap<>();
    }

    public boolean registerList(String key, List<HasName> list) {
        if (list.isEmpty()) {
            return false;
        }

        this.candidatesMap.put(key, list);

        return true;
    }

    public List<String> candidateOf(String key) {
        switch (key) {
            case "world":
                return Bukkit.getWorlds().stream()
                        .map(World::getName)
                        .collect(Collectors.toList());
            case "player":
                return Bukkit.getOnlinePlayers().stream()
                        .map(Player::getName)
                        .collect(Collectors.toList());
            case "environment":
                return Arrays.stream(World.Environment.values())
                        .map(e -> e.name().toLowerCase())
                        .collect(Collectors.toList());
            case "material":
                return Arrays.stream(Material.values())
                        .map(e -> e.name().toLowerCase())
                        .collect(Collectors.toList());
            default:
                return this.candidatesMap.get(key).stream()
                        .map(HasName::getName)
                        .collect(Collectors.toList());
        }
    }
}
