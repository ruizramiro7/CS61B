package bearmaps;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

/**
 * @source CS61BL Summer 2020 Lab 15
 */
public class Trie {

    private class TrieNode {
        private Character character;
        private HashSet<String> words = new HashSet<>();
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
