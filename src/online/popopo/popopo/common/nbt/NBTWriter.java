package online.popopo.popopo.common.nbt;

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
                this.out.writeUTF(t.getName());
        }
    }

    private void writeByteArray(byte[] v) throws IOException {
        this.out.writeInt(v.length);

        for (byte i : v) {
            this.out.writeByte(i);
        }
    }

    private void writeIntArray(int[] v) throws IOException {
        this.out.writeInt(v.length);

        for (int i : v) {
            this.out.writeInt(i);
        }
    }

    private void writeShortArray(short[] v) throws IOException {
        this.out.writeInt(v.length);

        for (short i : v) {
            this.out.writeShort(i);
        }
    }

    private void writeList(List<NBT> v) throws IOException {
        if (v.isEmpty()) {
            this.out.writeByte(NBT.END);
        } else {
            this.out.writeByte(v.get(0).getType());
        }

        this.out.writeInt(v.size());

        for (NBT e : v) {
            writeObject(e);
        }
    }

    private void writeCompound(Map<String, NBT> v) throws IOException {
        for (NBT e : v.values()) {
            write(e);
        }

        this.out.writeByte(NBT.END);
    }

    private void writeObject(NBT t) throws IOException {
        if (t.getType() == NBT.END) {
            return;
        } else if (t.getType() == NBT.BYTE) {
            this.out.writeByte(t.getByte());
        } else if (t.getType() == NBT.SHORT) {
            this.out.writeShort(t.getShort());
        } else if (t.getType() == NBT.INT) {
            this.out.writeInt(t.getInt());
        } else if (t.getType() == NBT.LONG) {
            this.out.writeLong(t.getLong());
        } else if (t.getType() == NBT.FLOAT) {
            this.out.writeFloat(t.getFloat());
        } else if (t.getType() == NBT.DOUBLE) {
            this.out.writeDouble(t.getDouble());
        } else if (t.getType() == NBT.BYTE_ARRAY) {
            writeByteArray(t.getByteArray());
        } else if (t.getType() == NBT.STRING) {
            this.out.writeUTF(t.getString());
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
        this.out.writeByte(t.getType());
        writeName(t);
        writeObject(t);
    }

    @Override
    public void close() throws IOException {
        this.out.close();
    }
}
