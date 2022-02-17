import java.util.EmptyStackException;
import java.util.Stack;

public class BlockStack implements Comparable<BlockStack> {
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

    int size() {
        return this._stack.size();
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

    /**
     * @return stack with the Character [from-to] like [A-Z] with from at the bottom
     *         of the stack
     */
    static public BlockStack SpecialBlockStack(Character from, Character to) {
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        int start = alphabet.indexOf(Character.toUpperCase(from));
        int end = alphabet.indexOf(Character.toUpperCase(to));
        return new BlockStack(alphabet.substring(start, end + 1));
    }

    /**
     * 
     * @param m
     * @return a solved stack for the problem with size m (number of blocks is m)
     */
    static public BlockStack DesiredStack(int m) {
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        return new BlockStack(alphabet.substring(0, m));
    }

    /**
     * loop over stacks to check for equality
     * 
     * @param other
     * @return true if equal false if not
     */
    private boolean equalStacks(BlockStack other) {
        if (this._stack.size() != other._stack.size())
            return false;
        for (int i = 0; i < this._stack.size(); i++) {
            if (!this._stack.get(i).equals(other._stack.get(i))) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        else if (obj == null)
            return false;
        else if (obj instanceof BlockStack)
            return this.equalStacks((BlockStack) obj);
        else
            return false;
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

    @Override
    public int compareTo(BlockStack other) {
        return (this.size() == other.size() && this.size() > 0)
                ? this._stack.elementAt(0).compareTo(other._stack.elementAt(0))
                : other.size() - this.size();
    }
}
