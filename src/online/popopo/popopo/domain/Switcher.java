package online.popopo.popopo.domain;

import online.popopo.popopo.common.nbt.NBT;
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
        this.player.removePotionEffect(e.getType());
    }

    private void resetPotionEffects() {
        this.player.getActivePotionEffects()
                .forEach(this::resetPotionEffect);
    }

    private void fixPlayerData() throws IOException {
        NBT t = this.data.readData();
        Map<String, NBT> m = t.getCompound();
        UUID uuid = this.player.getWorld().getUID();
        long most = uuid.getMostSignificantBits();
        long least = uuid.getLeastSignificantBits();

        m.get("WorldUUIDMost").setValue(most);
        m.get("WorldUUIDLeast").setValue(least);
        m.get("PortalCooldown").setValue(900);
        t.setValue(m);
        this.data.writeData(t);
    }

    private class SwitchingTask extends BukkitRunnable {
        private final Switcher instance;

        public SwitchingTask(Switcher i) {
            this.instance = i;
        }

        @Override
        public void run() {
            Switcher i = this.instance;
            PlayerData from, to;

            from = i.from.getPlayerData(i.player);
            to = i.to.getPlayerData(i.player);
            preProcess();
            i.player.saveData();
            resetPotionEffects();

            try {
                from.swapData(i.data);
                to.swapData(i.data);
                fixPlayerData();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            new BukkitRunnable() {
                @Override
                public void run() {
                    i.player.loadData();
                    postProcess();
                }
            }.runTask(i.plugin);
        }
    }

    public void switchDomain() {
        BukkitRunnable r = new SwitchingTask(this);

        r.runTaskAsynchronously(this.plugin);
    }

    public abstract void preProcess();

    public abstract void postProcess();
}
