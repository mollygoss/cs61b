/**
 * Created by molly on 2/9/17.
 */
public class Palindrome {

    /* creates deque from a one word String */
    public static Deque<Character> wordToDeque(String word) {
        Integer i = 0;
        Deque<Character> deque = new ArrayDequeSolution<>();
        while (i < word.length()) {
            /* this code: "word.charAt()" is from stack overflow */
            deque.addLast(word.charAt(i));
            i += 1;
        }
        return deque;
    }

    /* checks if a one word String is a Palindrome */
    public static boolean isPalindrome(String word) {
        Deque<Character> worddeque = wordToDeque(word);
        return isPalindrome(worddeque);
    }

    /* this helper function to isPalindrome takes in deques */
    private static boolean isPalindrome(Deque<Character> worddeque) {
        if (worddeque.size() < 2) {
            return true;
        } else {
            if (worddeque.removeFirst() == worddeque.removeLast()) {
                return isPalindrome(worddeque);
            } else {
                return false;
            }
        }
    }

    /* this generalizing function can detect off by one palindromes */
    public static boolean isPalindrome(String word, CharacterComparator cc) {
        Deque<Character> term = wordToDeque(word);
        return isPalindrome(term, cc);
    }

    /* this helper function is for off by one palindromes */
    private static boolean isPalindrome(Deque<Character> term, CharacterComparator cc) {
        if (term.size() < 2) {
            return true;
        } else {
            if (cc.equalChars(term.removeLast(), term.removeFirst())) {
                return isPalindrome(term, cc);
            } else {
                return false;
            }
        }
    }
}
