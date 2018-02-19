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
    @Variable
    private CommandManager commandManager;

    @Override
    public void enable() {
        commandManager.register(this);
    }

    private String memoryToString(long m) {
        int uint = 0;
        double memory = m;

        while (memory > 1024) {
            memory /= 1024;
            uint += 1;
        }

        String s = String.format("%.2f", memory);

        switch (uint) {
            case 0:
                return s + "B";
            case 1:
                return s + "KB";
            case 2:
                return s + "MB";
            case 3:
                return s + "GB";
            case 4:
                return s + "TB";
            default:
                return "-";
        }
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

    @SubCommand(name = "ram")
    public void showRam(Notice n) {
        long total = Runtime.getRuntime().totalMemory();
        long free = Runtime.getRuntime().freeMemory();
        long used = total - free;
        long rate = used * 100 / total;
        StringBuilder buf = new StringBuilder();

        buf.append("Current usage is ");
        buf.append((int) rate);
        buf.append("% (");
        buf.append(memoryToString(used));
        buf.append("/");
        buf.append(memoryToString(total));
        buf.append(")");

        n.info("RAM", buf.toString());
    }
}
