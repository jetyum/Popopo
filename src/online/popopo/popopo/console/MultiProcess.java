package online.popopo.popopo.console;

import online.popopo.common.message.Caster;
import org.bukkit.plugin.Plugin;

import java.io.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

class MultiProcess implements Closeable {
    private final Plugin plugin;
    private final Map<String, Process> processes;
    private final Map<String, File> directories;

    MultiProcess(Plugin plugin) {
        this.plugin = plugin;
        this.processes = new ConcurrentHashMap<>();
        this.directories = new ConcurrentHashMap<>();
    }

    Map<String, Process> getProcesses() {
        return processes;
    }

    Map<String, File> getDirectories() {
        return directories;
    }

    boolean exec(Caster c, String user, String cmd) {
        if (processes.containsKey(user)) {
            return false;
        }

        String[] a = {"/bin/bash", "-c", cmd + ";pwd"};
        ProcessBuilder builder = new ProcessBuilder(a);

        if (directories.containsKey(user)) {
            builder.directory(directories.get(user));
        }

        try {
            c.good("$", cmd);
            processes.put(user, builder.start());
            new ProcessRunner(this, user, c)
                    .runTaskAsynchronously(plugin);

            return true;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    boolean destroy(String user) {
        if (processes.containsKey(user)) {
            processes.get(user).destroy();

            return true;
        }

        return false;
    }

    @Override
    public void close() throws IOException {
        for (Process p : processes.values()) {
            p.destroy();
        }

        processes.clear();
        directories.clear();
    }
}
