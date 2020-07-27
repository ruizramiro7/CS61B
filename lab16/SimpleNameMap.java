public class SimpleNameMap {

    public static void main(String... args) {
        SimpleNameMap m = new SimpleNameMap();
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
    }

    /* TODO: Instance variables here */
    Entry[] map;
    int size;

    public SimpleNameMap() {
        // TODO: YOUR CODE HERE
        this.map = new Entry[26];
        size = 0;
    }

    /* Returns the number of items contained in this map. */
    public int size() {
        // TODO: YOUR CODE HERE
        return size;
    }

    /* Returns true if the map contains the KEY. */
    public boolean containsKey(String key) {
        // TODO: YOUR CODE HERE
        return get(key) != null;
    }

    /* Returns the value for the specified KEY. If KEY is not found, return
       null. */
    public String get(String key) {
        // TODO: YOUR CODE HERE
        if (map[hash(key)] == null) {
            return null;
        }
        return map[hash(key)].value;
    }

    /* Puts a (KEY, VALUE) pair into this map. If the KEY already exists in the
       SimpleNameMap, replace the current corresponding value with VALUE. */
    public void put(String key, String value) {
        // TODO: YOUR CODE HERE
        int index = hash(key);
        if (map[index] == null) {
            size += 1;
        }
        map[index] = new Entry(key, value);
    }

    /* Removes a single entry, KEY, from this table and return the VALUE if
       successful or NULL otherwise. */
    public String remove(String key) {
        // TODO: YOUR CODE HERE
        int index = hash(key);
        if (map[index] == null) {
            return null;
        }
        size -= 1;
        String value = map[index].value;
        map[index] = null;
        return value;
    }

    /* Converts alphabetical character to an integer in [0, 25] */
    private int hash(String key) {
        return key.charAt(0) - 'A';
    }

    private static class Entry {

        private String key;
        private String value;

        Entry(String key, String value) {
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