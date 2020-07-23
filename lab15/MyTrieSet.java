import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class MyTrieSet implements TrieSet61BL {

    private class MyTrieNode {
        private Character character;
        private boolean isEnd;
        private HashMap<Character, MyTrieNode> children = new HashMap<>();

        public MyTrieNode(Character character, boolean isEnd) {
            this.character = character;
            this.isEnd = isEnd;
        }

        private void checkBounds(String word, int index) {
            if (index > word.length() - 1 || index < 0) {
                throw new RuntimeException("Out of bounds.");
            }
        }

        public boolean contains(String word, int index) {
            checkBounds(word, index);
            Character c = word.charAt(index);
            if (index == word.length() - 1) {
                return children.containsKey(c) && children.get(c).isEnd;
            }
            return children.containsKey(c)
                    && children.get(c).contains(word, index + 1);
        }

        public void add(String word, int index) {
            checkBounds(word, index);
            Character c = word.charAt(index);
            if (!children.containsKey(c)) {
                children.put(c, new MyTrieNode(c, false));
            }
            if (index == word.length() - 1) {
                children.get(c).isEnd = true;
            }
            else {
                children.get(c).add(word, index + 1);
            }
        }

        // Assumes word is contained in Trie
        public MyTrieNode getLast(String word, int index) {
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

        public LinkedList<String> keysWithPrefix(String word, LinkedList<String> list) {
            String newWord;
            for (Character c: children.keySet()) {
                newWord = word + c;
                if (children.get(c).isEnd) {
                    list.push(newWord);
                }
                children.get(c).keysWithPrefix(newWord, list);
            }
            return list;
        }
        public String toString() {
            return String.valueOf(character);
        }
    }

    private MyTrieNode root;

    public MyTrieSet() {
        root = new MyTrieNode(null, false);
    }

    @Override
    public void clear() {
        root.children.clear();
    }

    @Override
    public boolean contains(String key) {
        return root.contains(key, 0);
    }

    @Override
    public void add(String key) {
        root.add(key, 0);
    }

    @Override
    public List<String> keysWithPrefix(String prefix) {
        LinkedList<String> words = new LinkedList<>();
        MyTrieNode last = root.getLast(prefix, 0);
        if (last == null) {
            return words;
        }
        return last.keysWithPrefix(prefix, words);
    }

    @Override
    public String longestPrefixOf(String key) {
        throw new UnsupportedOperationException();
    }
}
