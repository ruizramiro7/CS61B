import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class regexPuzzlesTest {

    @Test
    public void testUrls() {
        String[] urls = new String[4];
        urls[0] = ("(randomstuff1234https://www.eecs.berkeley.edu/blah.htmlyoullneverfindyourextracredit)");
        urls[1] = ("(https://en.wikipedia.org/greed.htmltry23andfindmenow)");
        urls[2] = ("(http://www.cs61bl.github.io/yolo.html)");
        urls[3] = ("Not a domain name");
        System.out.println(RegexPuzzles.urlRegex(urls));
    }

}
