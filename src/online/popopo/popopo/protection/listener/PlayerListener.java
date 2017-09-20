package online.popopo.popopo.protection.listener;

import online.popopo.popopo.protection.Judge;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;

import static online.popopo.popopo.protection.License.*;

public class PlayerListener implements Listener {
    private final Judge judge;

    public PlayerListener(Judge j) {
        this.judge = j;
    }

    private boolean canChangeBlock(Player p, Block b) {
        return judge.allows(p, b, CHANGE_BLOCK);
    }

    private boolean canClickBlock(Player p, Block b) {
        return judge.allows(p, b, CLICK_BLOCK);
    }

    private boolean canClickEntity(Player p, Entity e) {
        return judge.allows(p, e, CLICK_ENTITY);
    }

    private boolean canEnterVehicle(Player p, Vehicle v) {
        return judge.allows(p, v, ENTER_VEHICLE);
    }

    private boolean canExitVehicle(Player p, Vehicle v) {
        return judge.allows(p, v, EXIT_VEHICLE);
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        Block b = e.getBlock();
        Player p = e.getPlayer();

        e.setCancelled(!canChangeBlock(p, b));
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        Block b = e.getBlock();
        Player p = e.getPlayer();

        e.setCancelled(!canChangeBlock(p, b));
    }

    @EventHandler
    public void onBucketEmpty(PlayerBucketEmptyEvent e) {
        Block b = e.getBlockClicked();
        Player p = e.getPlayer();

        e.setCancelled(!canChangeBlock(p, b));
    }

    @EventHandler
    public void onBucketFill(PlayerBucketFillEvent e) {
        Block b = e.getBlockClicked();
        Player p = e.getPlayer();

        e.setCancelled(!canChangeBlock(p, b));
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockClick(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        Block b = e.getClickedBlock();

        switch (e.getAction()) {
            case RIGHT_CLICK_BLOCK:
            case LEFT_CLICK_BLOCK:
                e.setCancelled(!canClickBlock(p, b));

                return;
            default:
        }
    }

    @EventHandler
    public void onEntityClick(PlayerInteractEntityEvent e) {
        Player p = e.getPlayer();
        Entity c = e.getRightClicked();

        e.setCancelled(!canClickEntity(p, c));
    }

    @EventHandler
    public void onVehicleEnter(VehicleEnterEvent e) {
        if (e.getEntered() instanceof Player) {
            Vehicle v = e.getVehicle();
            Player p = (Player) e.getEntered();

            e.setCancelled(!canEnterVehicle(p, v));
        }
    }

    @EventHandler
    public void onVehicleExit(VehicleExitEvent e) {
        if (e.getExited() instanceof Player) {
            Vehicle v = e.getVehicle();
            Player p = (Player) e.getExited();

            e.setCancelled(!canExitVehicle(p, v));
        }
    }
}
