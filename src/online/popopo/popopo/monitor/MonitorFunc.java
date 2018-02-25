package online.popopo.popopo.monitor;

import online.popopo.api.function.Function;
import online.popopo.api.function.Variable;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.resource.Resource;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.*;

public class MonitorFunc extends Function {
    private static final int PORT = 8123;

    @Variable
    private Plugin plugin;

    private BukkitRunnable task = null;

    @Override
    public void enable() {
        File f = new File(plugin.getDataFolder(), "/web");

        if (!f.exists()) try {
            URI uri = getClass().getResource("/web/").toURI();

            Files.walk(Paths.get(uri)).forEach((p) -> {
                String s = p.toString().substring(1);

                if (!s.endsWith("/")) {
                    plugin.saveResource(s, false);
                }
            });
        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
        }

        Log.setLog(new QuietLogger());

        Server s = new Server(PORT);
        ServletContextHandler h = new ServletContextHandler(
                ServletContextHandler.SESSIONS
        );

        s.setHandler(h);
        h.setContextPath("/");
        h.setBaseResource(Resource.newResource(f));
        h.addServlet(MainServlet.class, "/");
        h.addServlet(JsonServlet.class, "/value.json");
        task = new HttpServer(s);
        task.runTaskAsynchronously(plugin);
    }

    @Override
    public void disable() {
        if (task != null) task.cancel();
    }

    class HttpServer extends BukkitRunnable {
        private final Server server;

        HttpServer(Server s) {
            this.server = s;
        }

        @Override
        public void run() {
            try {
                server.start();
                server.join();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void cancel() {
            try {
                server.stop();
                server.destroy();
            } catch (Exception e) {
                e.printStackTrace();
            }

            super.cancel();
        }
    }
}
