package online.popopo.popopo.protection.listener;

import online.popopo.popopo.protection.Judge;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;
import org.bukkit.plugin.Plugin;

import static online.popopo.popopo.protection.License.*;

public class PlayerListener implements Listener {
    private final Judge judge;

    public PlayerListener(Plugin p, Judge j) {
        this.judge = j;

        Bukkit.getPluginManager().registerEvents(this, p);
    }

    private boolean canPlayerClickBlock(Player p, Block b) {
        return judge.can(p, b, PLAYER_CLICK_BLOCK);
    }

    private boolean canPlayerClickEntity(Player p, Entity e) {
        return judge.can(p, e, PLAYER_CLICK_ENTITY);
    }

    private boolean canPlayerEnterVehicle(Player p, Vehicle v) {
        return judge.can(p, v, PLAYER_ENTER_VEHICLE);
    }

    private boolean canPlayerExitVehicle(Player p, Vehicle v) {
        return judge.can(p, v, PLAYER_EXIT_VEHICLE);
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockClick(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        Block b = e.getClickedBlock();

        switch (e.getAction()) {
            case RIGHT_CLICK_BLOCK:
            case LEFT_CLICK_BLOCK:
                e.setCancelled(!canPlayerClickBlock(p, b));

                return;
            default:
        }
    }

    @EventHandler
    public void onEntityClick(PlayerInteractEntityEvent e) {
        Player p = e.getPlayer();
        Entity entity = e.getRightClicked();

        e.setCancelled(!canPlayerClickEntity(p, entity));
    }

    @EventHandler
    public void onVehicleEnter(VehicleEnterEvent e) {
        if (e.getEntered() instanceof Player) {
            Player p = (Player) e.getEntered();
            Vehicle v = e.getVehicle();

            if (!canPlayerEnterVehicle(p, v)) {
                e.setCancelled(true);
                v.eject();
            }
        }
    }

    @EventHandler
    public void onVehicleExit(VehicleExitEvent e) {
        if (e.getExited() instanceof Player) {
            Player p = (Player) e.getExited();
            Vehicle v = e.getVehicle();

            if (!canPlayerExitVehicle(p, v)) {
                e.setCancelled(true);
                v.addPassenger(p);
            }
        }
    }
}
