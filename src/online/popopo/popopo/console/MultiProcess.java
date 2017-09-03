package online.popopo.popopo.console;

import online.popopo.common.message.Caster;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MultiProcess implements Closeable {
    private final JavaPlugin plugin;
    private final Map<String, Process> processes;
    private final Map<String, File> directories;

    public MultiProcess(JavaPlugin plugin) {
        this.plugin = plugin;
        this.processes = new ConcurrentHashMap<>();
        this.directories = new ConcurrentHashMap<>();
    }

    public Map<String, Process> getProcesses() {
        return this.processes;
    }

    public Map<String, File> getDirectories() {
        return this.directories;
    }

    public boolean exec(Caster c, String user, String cmd) {
        if (this.processes.containsKey(user)) {
            return false;
        }

        String[] a = {"/bin/bash", "-c", cmd + ";pwd"};
        ProcessBuilder builder = new ProcessBuilder(a);

        if (this.directories.containsKey(user)) {
            builder.directory(this.directories.get(user));
        }

        try {
            c.good("$", cmd);
            this.processes.put(user, builder.start());
            new ProcessRunner(this, user, c)
                    .runTaskAsynchronously(this.plugin);

            return true;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean destroy(String user) {
        if (this.processes.containsKey(user)) {
            this.processes.get(user).destroy();

            return true;
        }

        return false;
    }

    @Override
    public void close() throws IOException {
        for (Process p : this.processes.values()) {
            p.destroy();
        }

        this.processes.clear();
        this.directories.clear();
    }
}
