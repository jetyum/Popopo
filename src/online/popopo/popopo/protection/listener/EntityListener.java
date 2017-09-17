package online.popopo.popopo.protection.listener;

import online.popopo.popopo.protection.Judge;
import online.popopo.popopo.protection.License;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.vehicle.VehicleCreateEvent;
import org.bukkit.event.vehicle.VehicleDamageEvent;
import org.bukkit.plugin.Plugin;

import static online.popopo.popopo.protection.License.*;

public class EntityListener implements Listener {
    private final Judge judge;

    public EntityListener(Plugin p, Judge j) {
        this.judge = j;

        Bukkit.getPluginManager().registerEvents(this, p);
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

            return judge.can(p, target.getLocation(), act);
        } else {
            String act;

            if (target instanceof Player) {
                act = ENTITY_ATTACK_PLAYER;
            } else {
                act = ENTITY_ATTACK_ENTITY;
            }

            return judge.can(target.getLocation(), act);
        }
    }

    private boolean canCreatureSpawn(Location l) {
        return judge.can(l, CREATURE_SPAWN);
    }

    private boolean canVehicleChange(Vehicle v) {
        Location l = v.getLocation();

        return judge.can(l, VEHICLE_CHANGE);
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
}
