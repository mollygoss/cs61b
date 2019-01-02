import static org.junit.Assert.*;
import org.junit.Test;

/**
 * Created by molly on 2/9/17.
 */
public class OffByOneLauncher {
    @Test
    public void test(){
        OffByOne obo = new OffByOne();
        System.out.print(obo.equalChars('c', 'd'));
    }
}
