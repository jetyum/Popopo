package online.popopo.api.io.tree;

import org.apache.commons.lang3.Validate;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public abstract class Tree {
    private final Tree parent;

    public Tree(Tree t) {
        this.parent = t;
    }

    public abstract void set(String key, Object v);

    public abstract <T> Object get(String key, Class<T> t);

    public abstract Set<String> keys();

    public Tree parent() {
        return parent;
    }

    public Tree child(String key) {
        try {
            Map<String, Object> m = new HashMap<>();
            Object o = get(key, Object.class);

            m = m.getClass().cast(o);

            return new MapTree(this, m);
        } catch (ClassCastException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isEmpty() {
        return keys().isEmpty();
    }

    public boolean contains(String key) {
        return keys().contains(key) || key.isEmpty();
    }

    public class MapTree extends Tree {
        Map<String, Object> map;

        MapTree(Tree t, Map<String, Object> m) {
            super(t);
            this.map = m;
        }

        @Override
        public void set(String key, Object v) {
            Validate.notEmpty(key);

            Class<?> t = v.getClass();

            if (t.isEnum()) {
                v = EnumUtils.getName(v);
            }

            map.put(key, v);
        }

        @Override
        public <T> Object get(String key, Class<T> t) {
            if (key.isEmpty()) return map;

            Object o = map.get(key);

            if (t.isEnum() && o instanceof String) {
                String n = ((String) o).toUpperCase();

                o = EnumUtils.getEnum(t, n);
            }

            return o;
        }

        @Override
        public Set<String> keys() {
            return map.keySet();
        }
    }
}
