package online.popopo.popopo.domain;

import com.google.common.io.Files;
import online.popopo.popopo.common.nbt.NBT;
import online.popopo.popopo.common.nbt.NBTReader;
import online.popopo.popopo.common.nbt.NBTWriter;
import org.apache.commons.io.IOUtils;
import org.bukkit.entity.Player;

import java.io.*;
import java.util.Map;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class PlayerData {
    private final File dat;
    private final Player player;

    public PlayerData(File dat, Player p) {
        this.dat = dat;
        this.player = p;
    }

    public File getDataFile() {
        return this.dat;
    }

    public NBT readData() throws IOException {
        InputStream in, zip;

        in = new FileInputStream(this.dat);
        zip = new GZIPInputStream(in);

        NBTReader reader = new NBTReader(zip);
        NBT t = reader.read();

        IOUtils.closeQuietly(reader);

        return t;
    }

    public void writeData(NBT t) throws IOException {
        OutputStream out, zip;

        out = new FileOutputStream(this.dat);
        zip = new GZIPOutputStream(out);

        NBTWriter writer = new NBTWriter(zip);

        writer.write(t);
        IOUtils.closeQuietly(writer);
    }

    public void resetData() throws IOException {
        NBT t = readData();
        Map<String, NBT> m = t.getCompound();

        if (m.containsKey("ActiveEffects")) {
            m.get("ActiveEffects").getList().clear();
        }

        m.get("foodLevel").setValue(20);
        m.get("XpTotal").setValue(0);
        m.get("Fire").setValue((short) -20);
        m.get("Health").setValue((float) 20.0);
        m.get("Air").setValue((short) 300);
        m.get("Inventory").getList().clear();
        t.setValue(m);
        writeData(t);
    }

    public void swapMain() throws IOException {
        Domain d = Domain.getMain();
        PlayerData m = d.getPlayerData(this.player);
        File main = m.getDataFile();

        if (this.dat.equals(main)) {
            return;
        }

        File tmp = new File(dat.getPath() + ".tmp");

        if (this.dat.exists()) {
            Files.move(this.dat, tmp);
            Files.move(main, this.dat);
            Files.move(tmp, main);
        } else {
            Files.copy(main, dat);
            m.resetData();
        }
    }
}
