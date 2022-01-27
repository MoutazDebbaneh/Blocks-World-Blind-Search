import java.util.EmptyStackException;
import java.util.Stack;

public class BlockStack {
    // internal data
    Stack<Character> _stack = new Stack<Character>();

    /**
     * construct a stack from string
     * <p>
     * example:
     * "ABC" give the following stack
     * stack.push('A').push('B').push('C')
     * and so 'C' is on top
     * </p>
     * 
     * @param s
     */
    BlockStack(String s) {
        var upper_s = s.toUpperCase();
        for (int i = 0; i < s.length(); i++) {
            _stack.push(upper_s.charAt(i));
        }
    }

    static public BlockStack SpecialBlockStack(Character from, Character to) {
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        int start = alphabet.indexOf(Character.toUpperCase(from));
        int end = alphabet.indexOf(Character.toUpperCase(to));
        return new BlockStack(alphabet.substring(start, end + 1));
    }

    /**
     * standard simple top operation
     * 
     * @return Character at the top of the stack
     * @throws EmptyStackException
     */
    Character top() throws EmptyStackException {
        return _stack.peek();
    }

    /**
     * 
     * @return new deferent stack without the last element
     * @throws EmptyStackException
     */
    BlockStack pop() throws EmptyStackException {
        if (_stack.size() > 0)
            return new BlockStack(this.toString().substring(0, _stack.size() - 1));
        else
            throw new EmptyStackException();
    }

    /**
     * 
     * @param c
     * @return new deferent stack with the Character c
     */
    BlockStack push(Character c) {
        return new BlockStack(this.toString() + c);
    }

    @Override
    public String toString() {
        var sb = new StringBuilder();
        for (Character character : _stack) {
            sb.append(character);
        }
        return sb.toString();
    }

    public void decoratedPrint() {
        System.out.println("|" + this.toString() + "<-");
    }

    // public static void main(String args[]) {
    // var s = BlockStack.SpecialBlockStack('A', 'L');
    // var ss = BlockStack.SpecialBlockStack('A', 'C');
    // s.decoratedPrint();
    // ss.decoratedPrint();
    // ss = s.pop().pop().pop().pop().pop().pop().pop().pop().pop().pop().push('Z');
    // s.decoratedPrint();
    // ss.decoratedPrint();
    // }

}
