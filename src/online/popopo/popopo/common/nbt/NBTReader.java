package online.popopo.popopo.common.nbt;

import java.io.Closeable;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NBTReader implements Closeable {
    private final DataInputStream in;

    public NBTReader(InputStream in) {
        this.in = new DataInputStream(in);
    }

    private String readName(int type) throws IOException {
        switch (type) {
            case NBT.END:
                return null;
            default:
                return this.in.readUTF();
        }
    }

    private Object readByteArray() throws IOException {
        byte[] v = new byte[this.in.readInt()];

        for (int i = 0; i < v.length; i++) {
            v[i] = this.in.readByte();
        }

        return v;
    }

    private Object readIntArray() throws IOException {
        int[] v = new int[this.in.readInt()];

        for (int i = 0; i < v.length; i++) {
            v[i] = this.in.readInt();
        }

        return v;
    }

    private Object readShortArray() throws IOException {
        short[] v = new short[this.in.readInt()];

        for (int i = 0; i < v.length; i++) {
            v[i] = this.in.readShort();
        }

        return v;
    }

    private Object readList() throws IOException {
        int type = this.in.readByte();
        int size = this.in.readInt();
        List<NBT> v = new ArrayList<>(size);

        for (int i = 0; i < size; i++) {
            v.add(new NBT(type, null, readObject(type)));
        }

        return v;
    }

    private Object readCompound() throws IOException {
        Map<String, NBT> v = new HashMap<>();
        NBT d;

        while ((d = read()).getType() != NBT.END) {
            v.put(d.getName(), d);
        }

        return v;
    }

    private Object readObject(int type) throws IOException {
        if (type == NBT.END) {
            return null;
        } else if (type == NBT.BYTE) {
            return this.in.readByte();
        } else if (type == NBT.SHORT) {
            return this.in.readShort();
        } else if (type == NBT.INT) {
            return this.in.readInt();
        } else if (type == NBT.LONG) {
            return this.in.readLong();
        } else if (type == NBT.FLOAT) {
            return this.in.readFloat();
        } else if (type == NBT.DOUBLE) {
            return this.in.readDouble();
        } else if (type == NBT.BYTE_ARRAY) {
            return readByteArray();
        } else if (type == NBT.STRING) {
            return this.in.readUTF();
        } else if (type == NBT.LIST) {
            return readList();
        } else if (type == NBT.COMPOUND) {
            return readCompound();
        } else if (type == NBT.INT_ARRAY) {
            return readIntArray();
        } else if (type == NBT.SHORT_ARRAY) {
            return readShortArray();
        } else {
            throw new IOException("Invalid type");
        }
    }

    public NBT read() throws IOException {
        int type = this.in.readByte();
        String name = readName(type);
        Object payload = readObject(type);

        return new NBT(type, name, payload);
    }

    @Override
    public void close() throws IOException {
        this.in.close();
    }
}
