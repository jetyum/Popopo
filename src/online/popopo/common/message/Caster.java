package online.popopo.common.message;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.block.Block;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class Caster extends Castable {
    private final CommandSender target;

    public Caster(Formatter f, CommandSender s) {
        super(f);
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
        public PlayerCaster(Formatter f, CommandSender s) {
            super(f, s);
        }

        public Player getPlayer() {
            return (Player) getTarget();
        }

        public void castBar(String msg) {
            String m = getFormatter().text(msg);
            BaseComponent s = new TextComponent(m);
            ChatMessageType t = ChatMessageType.ACTION_BAR;

            getPlayer().spigot().sendMessage(t, s);
        }
    }

    public static class BlockCaster extends Caster {
        public BlockCaster(Formatter f, CommandSender s) {
            super(f, s);
        }

        public Block getBlock() {
            BlockCommandSender s;

            s = (BlockCommandSender) getTarget();

            return s.getBlock();
        }
    }

    public static class ConsoleCaster extends Caster {
        public ConsoleCaster(Formatter f, CommandSender s) {
            super(f, s);
        }
    }

    public static Caster newFrom(Formatter f, CommandSender s) {
        if (s instanceof Player) {
            return new PlayerCaster(f, s);
        } else if (s instanceof BlockCommandSender) {
            return new BlockCaster(f, s);
        } else if (s instanceof ConsoleCommandSender) {
            return new ConsoleCaster(f, s);
        } else {
            return new Caster(f, s);
        }
    }
}
