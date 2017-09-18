package online.popopo.popopo.protection.listener;

import online.popopo.popopo.protection.Judge;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;

import java.util.List;

import static online.popopo.popopo.protection.License.*;

public class ExplosionListener implements Listener {
    private final Judge judge;

    public ExplosionListener(Judge j) {
        this.judge = j;
    }

    private boolean canEntityExplosion(Location l) {
        return judge.allows(l, EXPLOSION, OPTION_ENTITY);
    }

    private boolean canBlockExplosion(Location l) {
        return judge.allows(l, EXPLOSION, OPTION_BLOCK);
    }



    @EventHandler
    public void onExplosionPrime(ExplosionPrimeEvent e) {
        Location l = e.getEntity().getLocation();

        e.setCancelled(!canEntityExplosion(l));
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
}
