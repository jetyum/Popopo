package online.popopo.popopo.domain;

import online.popopo.common.nbt.NBT;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.*;
import java.util.Map;
import java.util.UUID;

public abstract class Switcher {
    private final JavaPlugin plugin;
    private final Player player;
    private final Domain from;
    private final Domain to;
    private final PlayerData data;

    public Switcher(JavaPlugin plugin, Player p,
                    Domain from, Domain to) {
        Domain main = Domain.getMain();

        this.plugin = plugin;
        this.player = p;
        this.from = from;
        this.to = to;
        this.data = main.getPlayerData(p);
    }

    private void resetPotionEffect(PotionEffect e) {
        player.removePotionEffect(e.getType());
    }

    private void resetPotionEffects() {
        player.getActivePotionEffects()
                .forEach(this::resetPotionEffect);
    }

    private void fixPlayerData() throws IOException {
        NBT t = data.readData();
        Map<String, NBT> m = t.getCompound();
        UUID uuid = player.getWorld().getUID();
        long most = uuid.getMostSignificantBits();
        long least = uuid.getLeastSignificantBits();

        m.get("WorldUUIDMost").setValue(most);
        m.get("WorldUUIDLeast").setValue(least);
        m.get("PortalCooldown").setValue(900);
        t.setValue(m);
        data.writeData(t);
    }

    private class SwitchingTask extends BukkitRunnable {
        @Override
        public void run() {
            PlayerData fromData, toData;

            fromData = from.getPlayerData(player);
            toData = to.getPlayerData(player);
            preProcess();
            player.saveData();
            resetPotionEffects();

            try {
                fromData.swapData(data);
                toData.swapData(data);
                fixPlayerData();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            new BukkitRunnable() {
                @Override
                public void run() {
                    player.loadData();
                    postProcess();
                }
            }.runTask(plugin);
        }
    }

    public void switchDomain() {
        new SwitchingTask()
                .runTaskAsynchronously(plugin);
    }

    public abstract void preProcess();

    public abstract void postProcess();
}
