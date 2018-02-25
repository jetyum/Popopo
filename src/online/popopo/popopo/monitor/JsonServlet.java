package online.popopo.popopo.monitor;

import com.google.gson.Gson;
import com.sun.management.OperatingSystemMXBean;
import net.minecraft.server.v1_12_R1.MinecraftServer;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.management.ManagementFactory;
import java.util.*;

public class JsonServlet extends HttpServlet {
    private Object getRamInfo() {
        Map<String, Object> m = new HashMap<>();
        Runtime r = Runtime.getRuntime();

        long reserve = r.totalMemory();
        long use = reserve - r.freeMemory();
        long max = r.maxMemory();

        m.put("use", use);
        m.put("reserve", reserve);
        m.put("max", max);

        return m;
    }

    private Object getCpuInfo() {
        Map<String, Object> m = new HashMap<>();
        Runtime r = Runtime.getRuntime();
        OperatingSystemMXBean b = (OperatingSystemMXBean)
                ManagementFactory.getOperatingSystemMXBean();
        double rate = b.getSystemCpuLoad();
        int core = r.availableProcessors();

        @SuppressWarnings("deprecation")
        double tps = MinecraftServer.getServer().recentTps[0];

        m.put("rate", rate);
        m.put("core", core);
        m.put("tps", tps);

        return m;
    }

    private Object getPlayersInfo() {
        Server s = Bukkit.getServer();
        List<Map<String, Object>> l = new ArrayList<>();

        for (OfflinePlayer p : s.getOfflinePlayers()) {
            Map<String, Object> m = new HashMap<>();

            m.put("name", p.getName());
            m.put("online", p.isOnline());

            long now = System.currentTimeMillis();
            long last = p.getLastPlayed();

            m.put("time", (now - last) / 1000);
            l.add(m);
        }

        return l;
    }

    @Override
    public void doGet(HttpServletRequest req,
                      HttpServletResponse res) throws IOException {
        PrintWriter out = res.getWriter();
        Map<String, Object> map = new HashMap<>();

        map.put("ram", getRamInfo());
        map.put("cpu", getCpuInfo());
        map.put("players", getPlayersInfo());
        out.println(new Gson().toJson(map));
    }
}
