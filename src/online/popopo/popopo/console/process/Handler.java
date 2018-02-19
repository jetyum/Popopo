package online.popopo.popopo.console.process;

import online.popopo.api.notice.Notice;
import org.bukkit.plugin.Plugin;

import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Handler implements Closeable {
    private final Plugin plugin;
    private final Map<String, Process> processes;
    private final Map<String, File> directories;

    public Handler(Plugin plugin) {
        this.plugin = plugin;
        this.processes = new ConcurrentHashMap<>();
        this.directories = new ConcurrentHashMap<>();
    }

    private Process start(String user, String cmd) {
        try {
            ProcessBuilder b = new ProcessBuilder();

            b.command("/bin/bash", "-c", cmd + ";pwd");
            b.redirectErrorStream(true);

            if (directories.containsKey(user)) {
                b.directory(directories.get(user));
            }

            return b.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void setDir(List<String> lines, String user) {
        int end = lines.size() - 1;
        File dir = new File(lines.get(end));

        if (dir.exists()) {
            directories.put(user, dir);
        }
    }

    public boolean exec(Notice n, String user, String cmd) {
        if (processes.containsKey(user)) {
            return false;
        }

        Process p = start(user, cmd);

        n.good("$", cmd);
        processes.put(user, p);
        new Runner(p) {
            @Override
            public void onFinished(List<String> lines) {
                processes.remove(user);

                if (lines.isEmpty()) {
                    n.info("$", "Nothing to display");
                } else {
                    setDir(lines, user);

                    for (String s : lines) {
                        n.info(":", s);
                    }
                }
            }
        }.runTaskAsynchronously(plugin);

        return true;
    }

    public boolean destroy(String user) {
        if (processes.containsKey(user)) {
            new Killer(processes.get(user))
                    .runTaskAsynchronously(plugin);

            return true;
        } else {
            return false;
        }
    }

    @Override
    public void close() throws IOException {
        for (String user : processes.keySet()) {
            destroy(user);
        }

        processes.clear();
        directories.clear();
    }
}
