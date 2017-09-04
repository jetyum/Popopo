package online.popopo.popopo.console;

import online.popopo.common.message.Caster;
import org.apache.commons.io.IOUtils;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class ProcessRunner extends BukkitRunnable {
    private final MultiProcess object;
    private final String user;
    private final Caster caster;
    private final BufferedReader msgReader;
    private final BufferedReader errReader;

    public ProcessRunner(MultiProcess o, String user, Caster c) {
        this.object = o;
        this.user = user;
        this.caster = c;

        Process p = o.getProcesses().get(user);

        this.msgReader
                = new BufferedReader(
                        new InputStreamReader(
                                p.getInputStream()));
        this.errReader
                = new BufferedReader(
                        new InputStreamReader(
                                p.getErrorStream()));
    }

    @Override
    public void run() {
        try {
            String m = null;

            while (true) {
                String t = msgReader.readLine();
                String e = errReader.readLine();

                if (e != null) {
                    caster.bad(":", e);
                }

                if (t == null && m != null) {
                    File d = new File(m);

                    if (d.exists()) {
                        object.getDirectories()
                                .put(user, d);
                    }

                    break;
                } else {
                    if (m != null) {
                        caster.info(":", m);
                    }

                    m = t;
                }
            }
        } catch (IOException e) {
            caster.bad("$", "Stopped");
        } finally {
            IOUtils.closeQuietly(msgReader);
            IOUtils.closeQuietly(msgReader);
            object.getProcesses().remove(user);
        }
    }
}
