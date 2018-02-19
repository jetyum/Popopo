package online.popopo.popopo.console.process;

import org.apache.commons.io.IOUtils;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.*;
import java.util.List;

public abstract class Runner extends BukkitRunnable {
    private final BufferedReader reader;

    public Runner(Process p) {
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

    public abstract void onFinished(List<String> lines);
}
