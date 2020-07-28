import java.util.Iterator;
import java.util.LinkedList;

public class HashMap<K, V> implements Map61BL<K, V> {

    public static void main(String... args) {
        HashMap m = new HashMap();
        m.put("A", "TestA");
        System.out.println(m.get("A") == "TestA");
        System.out.println(m.get("B") == null);
        m.put("B", "TestB");
        System.out.println(m.get("B") == "TestB");
        System.out.println(m.size() == 2);
        m.remove("B");
        System.out.println(m.get("B") == null);
        System.out.println(m.size() == 1);
        System.out.println(m.containsKey("A") == true);
        System.out.println(m.containsKey("B") == false);
        m.put("Beta", "Test linked list");
        System.out.println(m.containsKey("Beta"));
        System.out.println(m.get("Beta") == "Test linked list");
        System.out.println(m.get("Bot") == null);
        m.put("Zeta", "ZetaValue");
        System.out.println(m.get("Zeta") == "ZetaValue");
        m.put("Zetta", "ZetaValue");
        m.put("Zet", "ZetaValue");
        m.put("Ze", "ZetaValue");
        m.put("Z", "ZetaValue");
        System.out.println(m.capacity == 10);
        m.put("Zetaa", "ZetaValue");
        System.out.println(m.capacity == 20);
        System.out.println(m.size == 8);
    }

    LinkedList<Entry<K, V>>[] map;
    int size;
    int capacity = 16;
    double loadFactor = 7.0 / 10.0;

    public HashMap() {
        this.map = buildMap(capacity);
        size = 0;
        init();
    }

    public HashMap(int initialCapacity) {
        this.capacity = initialCapacity;
        this.map = buildMap(capacity);
        size = 0;
        init();

    }

    public HashMap(int initialCapacity, double loadFactor) {
        this.capacity = initialCapacity;
        this.loadFactor = loadFactor;
        init();
    }

    private void init() {
        this.map = buildMap(capacity);
        size = 0;
    }

    /* Returns the number of items contained in this map. */
    public int size() {
        return size;
    }

    public int capacity() { return capacity; }

    /* Returns true if the map contains the KEY. */
    public boolean containsKey(K key) {
        return get(key) != null;
    }

    /* Returns the value for the specified KEY. If KEY is not found, return
       null. */
    public V get(K key) {
        for (var e: map[hash(key)]) {
            if (e.key.equals(key)) {
                return e.value;
            }
        }
        return null;
    }

    private LinkedList<Entry<K, V>>[] buildMap(int capacity) {
        LinkedList<Entry<K, V>>[] newMap = new LinkedList[capacity];
        for (int i = 0; i < newMap.length; ++i) {
            newMap[i] = new LinkedList<>();
        }
        return newMap;
    }

    private void resize() {
        capacity *= 2;
        size = 0;
        LinkedList<Entry<K, V>>[] oldMap = map;
        map = buildMap(capacity);
        for (int i = 0; i < oldMap.length; ++i) {
            for (var e: oldMap[i]) {
                put(e.key, e.value);
            }
        }
    }

    /* Puts a (KEY, VALUE) pair into this map. If the KEY already exists in the
       SimpleNameMap, replace the current corresponding value with VALUE. */
    public void put(K key, V value) {
        int index = hash(key);
        for (var e: map[index]) {
            if (e.key.equals(key)) {
                e.value = value;
                return;
            }
        }
        if ((double)(size + 1)/ (double)capacity > loadFactor) {
            resize();
        }
        map[hash(key)].add(new Entry(key, value));
        size += 1;
    }

    /* Removes a single entry, KEY, from this table and return the VALUE if
       successful or NULL otherwise. */
    public V remove(K key) {
        int index = hash(key);
        Entry<K, V> entry;
        for (int i = 0; i < map[index].size(); ++i) {
            entry = map[index].get(i);
            if (entry.key.equals(key)) {
                V value = entry.value;
                map[index].remove(i);
                size -= 1;
                return value;
            }
        }
        return null;
    }

    public void clear() {
        for (var ll: map) {
           ll.clear();
        }
        size = 0;
    }

    public boolean remove(K key, V value) {
        return false;
    }

    public Iterator<K> iterator() {
        throw new UnsupportedOperationException();
    }

    /* Converts alphabetical character to an integer in [0, 25] */
    private int hash(K key) {
        return Math.floorMod(key.hashCode(), map.length);
    }

    private static class Entry<K, V> {

        private K key;
        private V value;

        Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }

        /* Returns true if this key matches with the OTHER's key. */
        public boolean keyEquals(Entry other) {
            return key.equals(other.key);
        }

        /* Returns true if both the KEY and the VALUE match. */
        @Override
        public boolean equals(Object other) {
            return (other instanceof Entry
                    && key.equals(((Entry) other).key)
                    && value.equals(((Entry) other).value));
        }

        @Override
        public int hashCode() {return super.hashCode();}
    }
}