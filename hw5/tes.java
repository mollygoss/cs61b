/**
 * Created by molly on 4/26/17.
 */
public class tes {

    public tes() {

    }
    public static void lets(int y, int x) {
        if (y == 3) {
            System.out.println("hello");
        }
        if (x == 4) {
            System.out.println("also");
        }
        if (y == 3 && x == 4) {
            System.out.println("yup");
            if (y == 3) {
                System.out.println("still in here");
            }
        }
        else {
            System.out.println("null");
        }
    }

    public static void main(String[] args) {
        lets(3, 4);
    }
}
