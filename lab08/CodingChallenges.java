import java.util.HashSet;
import java.util.HashMap;

public class CodingChallenges {

    /**
     * Return the missing number from an array of length N containing all the
     * values from 0 to N except for one missing number.
     */
    public static int missingNumber(int[] values) {

        HashSet<Integer> set = new HashSet<>();
        for (int i: values){
            set.add(i);
        }
        for (int i = 0; i < values.length; ++i) {
            if (!set.contains(i)) {
               return i;
            }
        }
        return -1;
    }

    /**
     * Returns true if and only if two integers in the array sum up to n.
     * Assume all values in the array are unique.
     */
    public static boolean sumTo(int[] values, int n) {
        // TODO
        HashSet<Integer> set = new HashSet<>();
        for (int i: values){
            set.add(i);
        }
        for (int v: values) {
            if (n - v != v && set.contains(n - v)) {
                return true;
            }
        }
        return false;
    }

    private static HashMap<Character, Integer> buildCharMap(String s) {
        HashMap<Character, Integer> m = new HashMap<>();
        for (char c: s.toCharArray()) {
            if (m.containsKey(c)) {
                m.replace(c, m.get(c) + 1);
            }
            else {
                m.put(c, 1);
            }
        }
        return m;
    }

    /**
     * Returns true if and only if s1 is a permutation of s2. s1 is a
     * permutation of s2 if it has the same number of each character as s2.
     */
    public static boolean isPermutation(String s1, String s2) {
        // TODO
        HashMap<Character, Integer> map1 = buildCharMap(s1);
        HashMap<Character, Integer> map2 = buildCharMap(s2);
        return map1.equals(map2);
    }
}