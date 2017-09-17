package online.popopo.popopo.protection.listener;

import online.popopo.popopo.protection.Judge;
import online.popopo.popopo.protection.License;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.plugin.Plugin;

import java.util.List;

import static online.popopo.popopo.protection.License.*;

public class ExplosionListener implements Listener {
    private final Judge judge;

    public ExplosionListener(Plugin p, Judge j) {
        this.judge = j;

        Bukkit.getPluginManager().registerEvents(this, p);
    }

    private boolean canEntityExplosion(Location l) {
        return judge.can(l, ENTITY_EXPLOSION);
    }

    private boolean canBlockExplosion(Location l) {
        return judge.can(l, BLOCK_EXPLOSION);
    }

    private boolean canEntityBlastBlock(List<Block> list) {
        for (Block b : list) {
            Location l = b.getLocation();

            if (!canEntityExplosion(l)) return false;
        }

        return true;
    }

    private boolean canBlockBlastBlock(List<Block> list) {
        for (Block b : list) {
            Location l = b.getLocation();

            if (!canBlockExplosion(l)) return false;
        }

        return true;
    }

    @EventHandler
    public void onEntityExplosionPrime(ExplosionPrimeEvent e) {
        Location l = e.getEntity().getLocation();

        e.setCancelled(!canEntityExplosion(l));
    }

    @EventHandler
    public void onEntityExplosion(EntityExplodeEvent e) {
        List<Block> list = e.blockList();

        e.setCancelled(!canEntityBlastBlock(list));
    }

    @EventHandler
    public void onBlockExplosion(BlockExplodeEvent e) {
        Location l = e.getBlock().getLocation();

        if (!canBlockExplosion(l)) {
            e.setCancelled(true);
        } else {
            List<Block> list = e.blockList();

            e.setCancelled(!canBlockBlastBlock(list));
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent e) {
        Location l = e.getEntity().getLocation();

        switch (e.getCause()) {
            case ENTITY_EXPLOSION:
                e.setCancelled(!canEntityExplosion(l));

                return;
            case BLOCK_EXPLOSION:
                e.setCancelled(!canBlockExplosion(l));

                return;
            default:
        }
    }
}
