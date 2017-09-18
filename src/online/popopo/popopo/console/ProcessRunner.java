package online.popopo.popopo.console;

import org.apache.commons.io.IOUtils;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.*;
import java.util.List;

abstract class ProcessRunner extends BukkitRunnable {
    private final BufferedReader reader;

    ProcessRunner(Process p) {
        InputStream in = p.getInputStream();
        Reader reader = new InputStreamReader(in);

        this.reader = new BufferedReader(reader);
    }

    private List<String> getLines() {
        try {
            return IOUtils.readLines(reader);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public final void run() {
        onFinished(getLines());
        IOUtils.closeQuietly(reader);
    }

    abstract void onFinished(List<String> lines);
}
