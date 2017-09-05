package online.popopo.popopo.input;

import online.popopo.common.message.Formatter;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class InputListener implements Listener {
    private final Formatter decorator;

    public InputListener(JavaPlugin p, Formatter d) {
        this.decorator = d;

        Bukkit.getPluginManager().registerEvents(this, p);
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent e) {
        e.setMessage(decorator.standard(e.getMessage()));
    }

    @EventHandler
    public void onSighChange(SignChangeEvent e) {
        String[] lines = e.getLines();

        for (int i = 0; i < lines.length; i++) {
            lines[i] = decorator.standard(lines[i]);
        }
    }
}
