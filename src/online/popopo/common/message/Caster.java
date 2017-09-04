package online.popopo.common.message;

import net.minecraft.server.v1_12_R1.*;
import net.minecraft.server.v1_12_R1.IChatBaseComponent.ChatSerializer;
import org.bukkit.block.Block;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class Caster extends Castable {
    private final CommandSender target;

    public Caster(Theme t, CommandSender s) {
        super(t);
        this.target = s;
    }

    public CommandSender getTarget() {
        return target;
    }

    @Override
    public void cast(String msg) {
        target.sendMessage(msg);
    }

    public static class PlayerCaster extends Caster {
        public PlayerCaster(Theme t, CommandSender s) {
            super(t, s);
        }

        public Player getPlayer() {
            return (Player) getTarget();
        }

        public void castBar(String msg) {
            CraftPlayer p = (CraftPlayer) getPlayer();
            EntityPlayer e = p.getHandle();
            PlayerConnection i = e.playerConnection;
            String m = getTheme().getText() + msg;
            String r = "{\"text\":\"" + m + "\"}";
            IChatBaseComponent s = ChatSerializer.a(r);
            ChatMessageType t = ChatMessageType.GAME_INFO;

            i.sendPacket(new PacketPlayOutChat(s, t));
        }
    }

    public static class BlockCaster extends Caster {
        public BlockCaster(Theme t, CommandSender s) {
            super(t, s);
        }

        public Block getBlock() {
            BlockCommandSender s;

            s = (BlockCommandSender) getTarget();

            return s.getBlock();
        }
    }

    public static class ConsoleCaster extends Caster {
        public ConsoleCaster(Theme t, CommandSender s) {
            super(t, s);
        }
    }

    public static Caster newFrom(Theme t, CommandSender s) {
        if (s instanceof Player) {
            return new PlayerCaster(t, s);
        } else if (s instanceof BlockCommandSender) {
            return new BlockCaster(t, s);
        } else if (s instanceof ConsoleCommandSender) {
            return new ConsoleCaster(t, s);
        } else {
            return new Caster(t, s);
        }
    }
}
