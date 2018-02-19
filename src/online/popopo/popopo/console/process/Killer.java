package online.popopo.popopo.console.process;

import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;
import java.lang.reflect.Field;

public class Killer extends BukkitRunnable {
    private final Process process;
    private final int pid;

    public Killer(Process p) {
        try {
            Class t = p.getClass();
            Field f = t.getDeclaredField("pid");

            f.setAccessible(true);

            this.process = p;
            this.pid = f.getInt(p);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        try {
            String c = "ps ho pid --ppid=" + pid;
            String kill = "kill `" + c + "`";
            ProcessBuilder b = new ProcessBuilder();

            b.command("/bin/bash", "-c", kill);
            b.start().waitFor();
            process.destroy();
        } catch (InterruptedException
                | IOException e) {
            e.printStackTrace();
        }
    }
}
