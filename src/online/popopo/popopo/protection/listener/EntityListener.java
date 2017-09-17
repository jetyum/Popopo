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
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.hanging.HangingBreakEvent;
import org.bukkit.event.hanging.HangingPlaceEvent;
import org.bukkit.event.vehicle.VehicleCreateEvent;
import org.bukkit.event.vehicle.VehicleDamageEvent;

import static online.popopo.popopo.protection.License.*;

public class EntityListener implements Listener {
    private final Judge judge;

    public EntityListener(Judge j) {
        this.judge = j;
    }

    private boolean canAttack(Entity e, Entity target) {
        if (e instanceof Player) {
            Player p = (Player) e;
            String act;

            if (target instanceof Player) {
                act = PLAYER_ATTACK_PLAYER;
            } else {
                act = PLAYER_ATTACK_ENTITY;
            }

            return judge.allows(p, target, act);
        } else {
            String act;

            if (target instanceof Player) {
                act = ENTITY_ATTACK_PLAYER;
            } else {
                act = ENTITY_ATTACK_ENTITY;
            }

            return judge.allows(target, act);
        }
    }

    private boolean canCreatureSpawn(Location l) {
        return judge.allows(l, CREATURE_SPAWN);
    }

    private boolean canVehicleChange(Vehicle v) {
        return judge.allows(v, VEHICLE_CHANGE);
    }

    private boolean canPlayerChangeHanging(Player p, Hanging h) {
        return judge.allows(p, h, PLAYER_CHANGE_HANGING);
    }

    private boolean canHangingBreak(Hanging h) {
        return judge.allows(h, HANGING_BREAK);
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

        e.setCancelled(!canCreatureSpawn(l));
    }

    @EventHandler
    public void onVehicleDamage(VehicleDamageEvent e) {
        Vehicle v = e.getVehicle();

        e.setCancelled(!canVehicleChange(v));
    }

    @EventHandler
    public void onVehicleCreate(VehicleCreateEvent e) {
        Vehicle v = e.getVehicle();

        e.setCancelled(!canVehicleChange(v));
    }

    @EventHandler
    public void onHangingBreak(HangingBreakEvent e) {
        Hanging h = e.getEntity();

        switch (e.getCause()) {
            case ENTITY:
                return;
            default:
                e.setCancelled(!canHangingBreak(h));
        }
    }

    @EventHandler
    public void onHangingBreak(HangingBreakByEntityEvent e) {
        Hanging h = e.getEntity();

        if (e.getRemover() instanceof Player) {
            Player p = (Player) e.getRemover();

            e.setCancelled(!canPlayerChangeHanging(p, h));
        } else {
            e.setCancelled(!canHangingBreak(h));
        }
    }

    @EventHandler
    public void onHangingPlace(HangingPlaceEvent e) {
        Player p = e.getPlayer();
        Hanging h = e.getEntity();

        e.setCancelled(!canPlayerChangeHanging(p, h));
    }
}
