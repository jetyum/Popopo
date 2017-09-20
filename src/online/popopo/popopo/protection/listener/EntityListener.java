package online.popopo.popopo.protection.listener;

import online.popopo.popopo.protection.Judge;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Hanging;
import org.bukkit.entity.Player;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.hanging.HangingBreakEvent;
import org.bukkit.event.hanging.HangingPlaceEvent;
import org.bukkit.event.vehicle.VehicleCreateEvent;
import org.bukkit.event.vehicle.VehicleDamageEvent;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;

import static online.popopo.popopo.protection.License.*;

public class EntityListener implements Listener {
    private final Judge judge;

    public EntityListener(Judge j) {
        this.judge = j;
    }

    private boolean canAttack(Entity e, Entity target) {
        if (e instanceof Player) {
            Player p = (Player) e;
            String a;

            if (target instanceof Player) {
                a = ATTACK_PLAYER;
            } else {
                a = ATTACK_ENTITY;
            }

            return judge.allows(p, target, a);
        } else {
            String a;

            if (target instanceof Player) {
                a = ATTACK_PLAYER;
            } else {
                a = ATTACK_ENTITY;
            }

            return judge.allows(target, a, OPTION_ENTITY);
        }
    }

    private boolean canSpawnCreature(Location l) {
        return judge.allows(l, SPAWN, OPTION_ENTITY);
    }

    private boolean canChangeVehicle(Vehicle v) {
        return judge.allows(v, CHANGE_VEHICLE, OPTION_ALL);
    }

    private boolean canChangeHanging(Player p, Hanging h) {
        return judge.allows(p, h, CHANGE_HANGING);
    }

    private boolean canBreakHanging(Hanging h) {
        return judge.allows(h, CHANGE_HANGING, OPTION_ALL);
    }

    private boolean canEnterVehicle(Vehicle v) {
        return judge.allows(v, ENTER_VEHICLE, OPTION_ENTITY);
    }

    private boolean canExitVehicle(Vehicle v) {
        return judge.allows(v, EXIT_VEHICLE, OPTION_ENTITY);
    }

    private boolean canIgniteEntity(Entity e) {
        return judge.allows(e, IGNITE_ENTITY, OPTION_ALL);
    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent e) {
        Entity entity = e.getDamager();
        Entity target = e.getEntity();

        e.setCancelled(!canAttack(entity, target));
    }

    @EventHandler
    public void onCreatureSpawn(CreatureSpawnEvent e) {
        Location l = e.getLocation();

        e.setCancelled(!canSpawnCreature(l));
    }

    @EventHandler
    public void onVehicleDamage(VehicleDamageEvent e) {
        Vehicle v = e.getVehicle();

        e.setCancelled(!canChangeVehicle(v));
    }

    @EventHandler
    public void onVehicleCreate(VehicleCreateEvent e) {
        Vehicle v = e.getVehicle();

        e.setCancelled(!canChangeVehicle(v));
    }

    @EventHandler
    public void onHangingBreak(HangingBreakEvent e) {
        Hanging h = e.getEntity();

        switch (e.getCause()) {
            case ENTITY:
                return;
            default:
                e.setCancelled(!canBreakHanging(h));
        }
    }

    @EventHandler
    public void onHangingBreak(HangingBreakByEntityEvent e) {
        Hanging h = e.getEntity();

        if (e.getRemover() instanceof Player) {
            Player p = (Player) e.getRemover();

            e.setCancelled(!canChangeHanging(p, h));
        } else {
            e.setCancelled(!canBreakHanging(h));
        }
    }

    @EventHandler
    public void onHangingPlace(HangingPlaceEvent e) {
        Player p = e.getPlayer();
        Hanging h = e.getEntity();

        e.setCancelled(!canChangeHanging(p, h));
    }

    @EventHandler
    public void onVehicleEnter(VehicleEnterEvent e) {
        if (!(e.getEntered() instanceof Player)) {
            Vehicle v = e.getVehicle();

            e.setCancelled(!canEnterVehicle(v));
        }
    }

    @EventHandler
    public void onVehicleExit(VehicleExitEvent e) {
        if (!(e.getExited() instanceof Player)) {
            Vehicle v = e.getVehicle();

            e.setCancelled(!canExitVehicle(v));
        }
    }

    @EventHandler
    public void onEntityCombust(EntityCombustEvent e) {
        Entity entity = e.getEntity();

        e.setCancelled(!canIgniteEntity(entity));
    }
}
