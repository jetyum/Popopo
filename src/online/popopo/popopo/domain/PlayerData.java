package online.popopo.popopo.domain;

import com.google.common.io.Files;
import online.popopo.common.nbt.NBT;
import online.popopo.common.nbt.NBTReader;
import online.popopo.common.nbt.NBTWriter;
import org.apache.commons.io.IOUtils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import java.io.*;
import java.util.Map;
import java.util.UUID;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class PlayerData {
    private final File dat;

    public PlayerData(Player p, Domain d) {
        File dir = d.getMainWorld().getWorldFolder();
        UUID uid = p.getUniqueId();
        String path = "playerdata/" + uid + ".dat";

        dat = new File(dir, path);
    }

    public NBT readData() throws IOException {
        InputStream in, zip;

        in = new FileInputStream(dat);
        zip = new GZIPInputStream(in);

        NBTReader reader = new NBTReader(zip);
        NBT t = reader.read();

        IOUtils.closeQuietly(reader);

        return t;
    }

    public void writeData(NBT t) throws IOException {
        OutputStream out, zip;

        out = new FileOutputStream(dat);
        zip = new GZIPOutputStream(out);

        NBTWriter writer = new NBTWriter(zip);

        writer.write(t);
        IOUtils.closeQuietly(writer);
    }

    public void resetData() throws IOException {
        NBT t = readData();
        Map<String, NBT> m = t.getCompound();
        GameMode mode = Bukkit.getDefaultGameMode();

        @SuppressWarnings("deprecation")
        int modeType = mode.getValue();

        if (m.containsKey("ActiveEffects")) {
            m.get("ActiveEffects").getList().clear();
        }

        m.get("foodLevel").setValue(20);
        m.get("XpP").setValue((float) 0.0);
        m.get("Score").setValue(0);
        m.get("XpLevel").setValue(0);
        m.get("XpTotal").setValue(0);
        m.get("SelectedItemSlot").setValue(0);
        m.get("Fire").setValue((short) -20);
        m.get("Health").setValue((float) 20.0);
        m.get("Air").setValue((short) 300);
        m.get("Inventory").getList().clear();
        m.get("EnderItems").getList().clear();
        m.get("playerGameType").setValue(modeType);
        t.setValue(m);
        writeData(t);
    }

    public void swapData(PlayerData d) throws IOException {
        if (!dat.equals(d.dat)) {
            if (dat.exists()) {
                String path = dat.getPath();
                File tmp = new File(path + ".tmp");

                Files.move(dat, tmp);
                Files.move(d.dat, dat);
                Files.move(tmp, d.dat);
            } else {
                Files.copy(d.dat, dat);
                d.resetData();
            }
        }
    }
}
