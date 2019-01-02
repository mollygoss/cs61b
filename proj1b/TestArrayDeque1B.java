import static org.junit.Assert.*;
import org.junit.Test;

/**
 * Created by molly on 2/8/17.
 */

public class TestArrayDeque1B {

    @Test
    public void test() {
        StudentArrayDeque<Integer> student = new StudentArrayDeque<>();
        ArrayDequeSolution<Integer> solution = new ArrayDequeSolution<>();
        OperationSequence stud = new OperationSequence();
        Integer x = 1;
        while (x == 1) {
            Integer random = StdRandom.uniform(4);
            if (random == 0) {
                student.addFirst(random);
                solution.addFirst(random);
                DequeOperation i = new DequeOperation("addFirst", random);
                stud.addOperation(i);
            } else if (random == 1) {
                student.addLast(random);
                solution.addLast(random);
                DequeOperation j = new DequeOperation("addLast", random);
                stud.addOperation(j);
            } else if (random == 2) {
                DequeOperation k = new DequeOperation("removeLast");
                stud.addOperation(k);
                assertEquals(stud.toString(), solution.removeLast(), student.removeLast());
            } else {
                DequeOperation l = new DequeOperation("removeFirst");
                stud.addOperation(l);
                assertEquals(stud.toString(), solution.removeFirst(), student.removeFirst());
            }
        }
    }
}




