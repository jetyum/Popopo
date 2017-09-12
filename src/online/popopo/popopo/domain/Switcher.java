package online.popopo.popopo.domain;

import online.popopo.common.nbt.NBT;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.*;
import java.util.Map;
import java.util.UUID;

abstract class Switcher {
    private final Plugin plugin;
    private final Player player;
    private final Domain from;
    private final Domain to;
    private final PlayerData data;

    Switcher(Plugin plugin, Player p,
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

    void switchDomain() {
        preProcess();
        new SwitchingTask()
                .runTaskAsynchronously(plugin);
    }

    class SwitchingTask extends BukkitRunnable {
        @Override
        public void run() {
            PlayerData fromData, toData;

            fromData = from.getPlayerData(player);
            toData = to.getPlayerData(player);
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

    abstract void preProcess();

    abstract void postProcess();
}
