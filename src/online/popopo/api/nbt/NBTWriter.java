package online.popopo.api.nbt;

import java.io.Closeable;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

public class NBTWriter implements Closeable {
    private final DataOutputStream out;

    public NBTWriter(OutputStream out) {
        this.out = new DataOutputStream(out);
    }

    private void writeName(NBT t) throws IOException {
        switch (t.getType()) {
            case NBT.END:
                return;
            default:
                out.writeUTF(t.getName());
        }
    }

    private void writeByteArray(byte[] v) throws IOException {
        out.writeInt(v.length);

        for (byte i : v) {
            out.writeByte(i);
        }
    }

    private void writeIntArray(int[] v) throws IOException {
        out.writeInt(v.length);

        for (int i : v) {
            out.writeInt(i);
        }
    }

    private void writeShortArray(short[] v) throws IOException {
        out.writeInt(v.length);

        for (short i : v) {
            out.writeShort(i);
        }
    }

    private void writeList(List<NBT> v) throws IOException {
        if (v.isEmpty()) {
            out.writeByte(NBT.END);
        } else {
            out.writeByte(v.get(0).getType());
        }

        out.writeInt(v.size());

        for (NBT e : v) {
            writeObject(e);
        }
    }

    private void writeCompound(Map<String, NBT> v) throws IOException {
        for (NBT e : v.values()) {
            write(e);
        }

        out.writeByte(NBT.END);
    }

    private void writeObject(NBT t) throws IOException {
        if (t.getType() == NBT.END) {
            return;
        } else if (t.getType() == NBT.BYTE) {
            out.writeByte(t.getByte());
        } else if (t.getType() == NBT.SHORT) {
            out.writeShort(t.getShort());
        } else if (t.getType() == NBT.INT) {
            out.writeInt(t.getInt());
        } else if (t.getType() == NBT.LONG) {
            out.writeLong(t.getLong());
        } else if (t.getType() == NBT.FLOAT) {
            out.writeFloat(t.getFloat());
        } else if (t.getType() == NBT.DOUBLE) {
            out.writeDouble(t.getDouble());
        } else if (t.getType() == NBT.BYTE_ARRAY) {
            writeByteArray(t.getByteArray());
        } else if (t.getType() == NBT.STRING) {
            out.writeUTF(t.getString());
        } else if (t.getType() == NBT.LIST) {
            writeList(t.getList());
        } else if (t.getType() == NBT.COMPOUND) {
            writeCompound(t.getCompound());
        } else if (t.getType() == NBT.INT_ARRAY) {
            writeIntArray(t.getIntArray());
        } else if (t.getType() == NBT.SHORT_ARRAY) {
            writeShortArray(t.getShortArray());
        } else {
            throw new IOException("Invalid type");
        }
    }

    public void write(NBT t) throws IOException {
        out.writeByte(t.getType());
        writeName(t);
        writeObject(t);
    }

    @Override
    public void close() throws IOException {
        out.close();
    }
}
