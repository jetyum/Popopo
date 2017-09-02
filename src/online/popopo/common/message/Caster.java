package online.popopo.common.message;

import net.minecraft.server.v1_12_R1.ChatMessageType;
import net.minecraft.server.v1_12_R1.EntityPlayer;
import net.minecraft.server.v1_12_R1.IChatBaseComponent;
import net.minecraft.server.v1_12_R1.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_12_R1.PacketPlayOutChat;
import net.minecraft.server.v1_12_R1.PlayerConnection;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class Caster implements Casting {
    private final Theme theme;
    private final CommandSender sender;

    public Caster(Theme t, CommandSender s) {
        this.theme = t;
        this.sender = s;
    }

    public CommandSender getSender() {
        return this.sender;
    }

    private String buildPrefix(ChatColor c, String s) {
        StringBuilder buf = new StringBuilder();

        buf.append(c);
        buf.append("[");
        buf.append(s);
        buf.append("]");
        buf.append(ChatColor.RESET);

        return buf.toString();
    }

    protected String decorateMessage(String msg) {
        return this.theme.getText() + msg;
    }

    private void cast(ChatColor c, String p, String m) {
        String prefix = buildPrefix(c, p);
        String msg = decorateMessage(m);

        getSender().sendMessage(prefix + " " + msg);
    }

    @Override
    public void info(String prefix, String msg){
        cast(this.theme.getInfo(), prefix, msg);
    }

    @Override
    public void good(String prefix, String msg){
        cast(this.theme.getGood(), prefix, msg);
    }

    @Override
    public void bad(String prefix, String msg){
        cast(this.theme.getBad(), prefix, msg);
    }

    @Override
    public void warning(String prefix, String msg){
        cast(this.theme.getWarning(), prefix, msg);
    }

    public static class PlayerCaster extends Caster {
        public PlayerCaster(Theme t, Player p) {
            super(t, p);
        }

        public void castGameInfo(String msg) {
            CraftPlayer p = ((CraftPlayer) getSender());
            EntityPlayer e = p.getHandle();
            PlayerConnection c = e.playerConnection;
            String s = decorateMessage(msg);
            String r = "{\"text\":\"" + s + "\"}";
            IChatBaseComponent m = ChatSerializer.a(r);
            ChatMessageType t = ChatMessageType.GAME_INFO;

            c.sendPacket(new PacketPlayOutChat(m, t));
        }
    }

    public static Caster newFrom(Theme t, CommandSender s) {
        if (s instanceof Player) {
            return new PlayerCaster(t, (Player) s);
        } else {
            return new Caster(t, s);
        }
    }
}