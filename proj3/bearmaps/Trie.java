package bearmaps;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * @source CS61BL Summer 2020 Lab 15
 */
public class Trie {

    public static void main(String... args) {
        Trie t = new Trie();
        t.add("sam", "Sam");
        t.add("sam", "SAM");
        t.add("samuel", "Samuel22");
        t.add("samantha", "Samantha");
        t.add("san jose", "San&Jose");
        t.add("san angelo", "SANangElo");
        t.add("samite", "Sam1te");
        t.add("samson", "Sam$on");
        t.add("alexander");
        for (var s: t.keysWithPrefix("sam")) {
            System.out.print(s + " ");
        }
        System.out.println("");
        for (var s: t.keysWithPrefix("san")) {
            System.out.print(s + " ");
        }
        System.out.println("");
        for (var s: t.keysWithPrefix("al")) {
            System.out.print(s + " ");
        }
        System.out.println("");
        for (var s: t.keysWithPrefix("ni")) {
            System.out.print(s + " ");
        }
        System.out.println("");
    }

    private class TrieNode {
        private Character character;
        private LinkedList<String> words = new LinkedList<>();
        private boolean isEnd;
        private HashMap<Character, TrieNode> children = new HashMap<>();

        public TrieNode(Character character, boolean isEnd) {
            this.character = character;
            this.isEnd = isEnd;
        }

        private void checkBounds(String word, int index) {
            if (index > word.length() - 1 || index < 0) {
                throw new RuntimeException("Out of bounds.");
            }
        }

        public TrieNode getLast(String word, int index) {
            checkBounds(word, index);
            Character c = word.charAt(index);
            if (!children.containsKey(c)) {
                return null;
            }
            if (index == word.length() - 1) {
                return children.get(c);
            }
            return children.get(c).getLast(word, index + 1);
        }

        public void add(String word, String end, int index) {
            checkBounds(word, index);
            Character c = word.charAt(index);
            if (!children.containsKey(c)) {
                children.put(c, new TrieNode(c, false));
            }
            if (index == word.length() - 1) {
                children.get(c).isEnd = true;
                children.get(c).words.add(end);
            }
            else {
                children.get(c).add(word, end, index + 1);
            }
        }

        public LinkedList<String> keysWithPrefix(String prefix, LinkedList<String> list) {
            if (isEnd) {
                for (var w: words) {
                    list.push(w);
                }
                //list.push(word);
            }
            for (Character c: children.keySet()) {
                children.get(c).keysWithPrefix(prefix + c, list);
            }
            return list;
        }
    }

    private TrieNode root;

    public Trie() {
        root = new TrieNode(null, false);
    }

    public void add(String key) {
        root.add(key, key, 0);
    }

    public void add(String key, String end) {
        root.add(key, end, 0);
    }

    public LinkedList<String> keysWithPrefix(String prefix) {
        LinkedList<String> words = new LinkedList<>();
        TrieNode last = root.getLast(prefix, 0);
        if (last == null) {
            return words;
        }
        return last.keysWithPrefix(prefix, words);
    }
}
