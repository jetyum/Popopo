package online.popopo.popopo.domain;

import net.minecraft.server.v1_12_R1.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;

import static net.minecraft.server.v1_12_R1.PacketPlayOutPlayerInfo.EnumPlayerInfoAction.*;

public class TeleportListener implements Listener {
    private final Plugin plugin;
    private final Switcher switcher;

    public TeleportListener(Plugin p) {
        this.plugin = p;
        this.switcher = new Switcher(p);
    }

    private boolean canSwitch(Domain a, Domain b) {
        return a.available() && b.available();
    }

    private void trySwitch(PlayerTeleportEvent e) {
        Player p = e.getPlayer();
        Domain from = new Domain(e.getFrom());
        Domain to = new Domain(e.getTo());

        if (from.equals(to)) return;

        if (canSwitch(from, to)) {
            switcher.switchTo(p, from, to,
                    () -> updateTabList(p, to),
                    () -> p.teleport(e.getTo()));
            e.setCancelled(true);
        } else {
            plugin.getLogger().warning("Can't switching!");
        }
    }

    private void setListName(Player target, Player p,
                             String listName) {
        EntityPlayer ep = ((CraftPlayer) p).getHandle();
        EntityPlayer t = ((CraftPlayer) target).getHandle();
        IChatBaseComponent c = ep.listName;
        Packet packet;

        ep.listName = new ChatComponentText(listName);
        packet = new PacketPlayOutPlayerInfo(
                UPDATE_DISPLAY_NAME, ep);
        t.playerConnection.sendPacket(packet);
        ep.listName = c;
    }

    private void updateTabList(Player p, Domain d) {
        for (Player e : Bukkit.getOnlinePlayers()) {
            if (e.equals(p)) continue;

            ChatColor c = ChatColor.WHITE;

            if (!new Domain(e.getWorld()).equals(d)) {
                c = ChatColor.GRAY;
            }

            setListName(e, p, c + p.getName());
            setListName(p, e, c + e.getName());
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        BukkitScheduler s = Bukkit.getScheduler();

        s.runTaskAsynchronously(plugin, () -> {
            Player p = e.getPlayer();

            updateTabList(p, new Domain(p.getWorld()));
        });
    }

    @EventHandler
    public void onTeleport(PlayerTeleportEvent e) {
        Player p = e.getPlayer();
        int state = switcher.getState(p);

        if (state == Switcher.SWITCHING) {
            e.setCancelled(true);
        } else if (state == Switcher.NONE) {
            trySwitch(e);
        }
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent e) {
        Player p = e.getPlayer();
        Domain from = new Domain(p.getWorld());
        Domain to = new Domain(e.getRespawnLocation());

        if (canSwitch(from, to)) {
            Location l = from.getSpawnLocation();

            e.setRespawnLocation(l);
        }
    }
}
