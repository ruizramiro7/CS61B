import org.junit.Test;
import static org.junit.Assert.*;

public class CodingChallengesTest {

    @Test
    public void missingNumberTest() {
        int[] test1 = {0, 1, 2, 3, 5};
        int[] test2 = {1, 2, 3, 4, 5};
        int[] test3 = {0, 1, 2, 3, 4};
        int[] test4 = {4, 5, 3, 2, 0};

        assertEquals(4, CodingChallenges.missingNumber(test1));
        assertEquals(0, CodingChallenges.missingNumber(test2));
        assertEquals(-1, CodingChallenges.missingNumber(test3));
        assertEquals(1, CodingChallenges.missingNumber(test4));
    }

    @Test
    public void sumToTest() {
        int[] test1 = {0, 1, 2, 3, 4};
        assertEquals(true, CodingChallenges.sumTo(test1, 3));
        assertEquals(true, CodingChallenges.sumTo(test1, 4));
        assertEquals(true, CodingChallenges.sumTo(test1, 5));
        assertEquals(false, CodingChallenges.sumTo(test1, 8));
    }

    @Test
    public void isPermutationTest() {
        String test1a = "abcd";
        String test1b = "badc";
        assertEquals(true, CodingChallenges.isPermutation(test1a, test1b));
    }
}
