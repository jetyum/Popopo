package online.popopo.popopo.system;

import com.sun.management.OperatingSystemMXBean;
import online.popopo.common.command.Argument;
import online.popopo.common.command.Definition;
import online.popopo.common.command.Executor;
import online.popopo.common.message.Caster;

import java.lang.management.ManagementFactory;

public class SystemCommand implements Definition {
    @Override
    public String getCommand() {
        return "system";
    }

    private String memoryToString(long m) {
        int uint = 0;
        long memory = m;

        while (memory > 1024) {
            memory /= 1024;
            uint += 1;
        }

        switch (uint) {
            case 0:
                return memory + "B";
            case 1:
                return memory + "KB";
            case 2:
                return memory + "MB";
            case 3:
                return memory + "GB";
            case 4:
                return memory + "TB";
            default:
                return "-";
        }
    }

    @Executor({"cpu"})
    public void onCpuCommand(Caster c, Argument arg) {
        OperatingSystemMXBean bean = (OperatingSystemMXBean)
                ManagementFactory.getOperatingSystemMXBean();
        double rate = bean.getSystemCpuLoad() * 100;
        StringBuilder buf = new StringBuilder();

        buf.append("Current usage is ");
        buf.append((int) rate);
        buf.append("%");

        c.info("CPU", buf.toString());
    }

    @Executor({"ram"})
    public void onRamCommand(Caster c, Argument arg) {
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

        c.info("RAM", buf.toString());
    }
}
