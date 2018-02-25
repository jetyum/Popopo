package online.popopo.popopo.system;

import com.sun.management.OperatingSystemMXBean;
import online.popopo.api.function.Variable;
import online.popopo.api.wrapper.command.Command;
import online.popopo.api.wrapper.command.CommandManager;
import online.popopo.api.wrapper.command.SubCommand;
import online.popopo.api.notice.Notice;
import online.popopo.api.function.Function;

import java.lang.management.ManagementFactory;

@Command(name = "system")
public class SystemFunc extends Function {
    private static final String[] byteUnit = {"B", "kB", "MB", "GB", "TB"};

    @Variable
    private CommandManager commandManager;

    @Override
    public void enable() {
        commandManager.register(this);
    }

    @SubCommand(name = "cpu")
    public void showCpu(Notice n) {
        OperatingSystemMXBean bean = (OperatingSystemMXBean)
                ManagementFactory.getOperatingSystemMXBean();
        double rate = bean.getSystemCpuLoad() * 100;
        StringBuilder buf = new StringBuilder();

        buf.append("Current usage is ");
        buf.append((int) rate);
        buf.append("%");

        n.info("CPU", buf.toString());
    }

    private String memoryToString(long m) {
        int i = 0;
        double memory = m;

        while (memory > 1024) {
            memory /= 1024;
            i++;
        }

        String s = String.format("%.2f", memory);

        return i < 5 ? s + byteUnit[i] : "error";
    }

    @SubCommand(name = "ram")
    public void showRam(Notice n) {
        long total = Runtime.getRuntime().totalMemory();
        long free = Runtime.getRuntime().freeMemory();
        long used = total - free;
        int rate = (int) (used * 100 / total);

        n.info("RAM", "Current usage is " + rate
                + "% (" + memoryToString(used) + "/"
                + memoryToString(total) + ")");
    }
}
