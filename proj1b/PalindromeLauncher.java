import static org.junit.Assert.*;
import org.junit.Test;
/**
 * Created by molly on 2/9/17.
 */
public class PalindromeLauncher {
    @Test
    public void test() {
        OffByN offby5 = new OffByN(4);
        System.out.print(offby5.equalChars('a', 'e'));
        //System.out.print(Palindrome.isPalindrome("flake", offby5));
    }
}
