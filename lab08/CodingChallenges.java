import java.util.HashSet;

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

    /**
     * Returns true if and only if s1 is a permutation of s2. s1 is a
     * permutation of s2 if it has the same number of each character as s2.
     */
    public static boolean isPermutation(String s1, String s2) {
        // TODO
        if (s1.length() != s2.length()) {
            return false;
        }

        HashSet<Character>  set1 = new HashSet<>();
        HashSet<Character>  set2 = new HashSet<>();

        for (int i = 0; i < s1.length(); ++i) {
            set1.add(s1.charAt(i));
            set2.add(s2.charAt(i));
        }

        return set1.containsAll(set2);
    }
}