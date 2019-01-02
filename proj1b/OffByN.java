/**
 * Created by molly on 2/10/17.
 */
public class OffByN implements CharacterComparator {
    private Integer n;

    /* constructor for offbyn */
    public OffByN(int N) {
        this.n = N;
    }

    @Override
    public boolean equalChars(char x, char y) {
        return (x - y == n || x - y == -n);
    }
}
