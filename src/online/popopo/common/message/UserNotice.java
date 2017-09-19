package online.popopo.common.message;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.block.Block;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class UserNotice extends Notice {
    private final CommandSender target;

    UserNotice(Formatter f, CommandSender s) {
        super(f);
        this.target = s;
    }

    public CommandSender getTarget() {
        return target;
    }

    @Override
    public void send(String msg) {
        target.sendMessage(msg);
    }

    public static class PlayerNotice extends UserNotice {
        PlayerNotice(Formatter f, CommandSender s) {
            super(f, s);
        }

        public Player getPlayer() {
            return (Player) getTarget();
        }

        public void toast(String msg) {
            String m = getFormatter().text(msg);
            BaseComponent s = new TextComponent(m);
            ChatMessageType t = ChatMessageType.ACTION_BAR;

            getPlayer().spigot().sendMessage(t, s);
        }
    }

    public static class BlockNotice extends UserNotice {
        BlockNotice(Formatter f, CommandSender s) {
            super(f, s);
        }

        public Block getBlock() {
            BlockCommandSender s;

            s = (BlockCommandSender) getTarget();

            return s.getBlock();
        }
    }

    public static class ConsoleNotice extends UserNotice {
        ConsoleNotice(Formatter f, CommandSender s) {
            super(f, s);
        }
    }
}
