package online.popopo.api.nbt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NBT {
    public static final int END = 0;
    public static final int BYTE = 1;
    public static final int SHORT = 2;
    public static final int INT = 3;
    public static final int LONG = 4;
    public static final int FLOAT = 5;
    public static final int DOUBLE = 6;
    public static final int BYTE_ARRAY = 7;
    public static final int STRING = 8;
    public static final int LIST = 9;
    public static final int COMPOUND = 10;
    public static final int INT_ARRAY = 11;
    public static final int SHORT_ARRAY = 100;

    private final int type;
    private final String name;

    private Object value;

    public NBT(int type, String name, Object value) {
        this.type = type;
        this.name = name;
        this.value = value;
    }

    public int getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public byte getByte() {
        return Byte.class.cast(value);
    }

    public short getShort() {
        return Short.class.cast(value);
    }

    public int getInt() {
        return Integer.class.cast(value);
    }

    public long getLong() {
        return Long.class.cast(value);
    }

    public float getFloat() {
        return Float.class.cast(value);
    }

    public double getDouble() {
        return Double.class.cast(value);
    }

    public byte[] getByteArray() {
        return byte[].class.cast(value);
    }

    public String getString() {
        return String.class.cast(value);
    }

    public List<NBT> getList() {
        List<NBT> list = new ArrayList<>();

        return list.getClass().cast(value);
    }

    public Map<String, NBT> getCompound() {
        Map<String, NBT> map = new HashMap<>();

        return map.getClass().cast(value);
    }

    public int[] getIntArray() {
        return int[].class.cast(value);
    }

    public short[] getShortArray() {
        return short[].class.cast(value);
    }

    public void setValue(Object v) {
        value = v;
    }
}
