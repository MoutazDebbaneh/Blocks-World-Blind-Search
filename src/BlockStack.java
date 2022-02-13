import java.util.EmptyStackException;
import java.util.Stack;

public class BlockStack implements Comparable<BlockStack> {
    // internal data
    Stack<Character> _stack = new Stack<Character>();

    static int RefEqualityHit = 0;

    static void showHitStat() {
        System.out.print("BlockStack.RefEqualityHit : ");
        System.out.println(BlockStack.RefEqualityHit);
    }

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
        if (this == obj) {
            BlockStack.RefEqualityHit++;
            return true;
        } else if (obj == null)
            return false;
        else if (obj instanceof BlockStack)
            return this.equalStacks((BlockStack) obj);
        else
            return false;
    }

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

    @Override
    public int compareTo(BlockStack other) {
        return (this.size() == other.size() && this.size() > 0)
                ? this._stack.elementAt(0).compareTo(other._stack.elementAt(0))
                : other.size() - this.size();
    }

    // public static void main(String args[]) {
    // var s = BlockStack.SpecialBlockStack('A', 'L');
    // var ss = BlockStack.SpecialBlockStack('A', 'C');
    // s.decoratedPrint();
    // ss.decoratedPrint();
    // ss = s.pop().pop().pop().pop().pop().pop().pop().pop().pop().pop().push('Z');
    // s.decoratedPrint();
    // ss.decoratedPrint();
    // var a = new BlockStack("");
    // var b = new BlockStack("");
    // var b = new BlockStack("A");
    // var a = new BlockStack("BC");
    // var a = new BlockStack("EF");
    // var b = new BlockStack("AB");
    // var l = new ArrayList<BlockStack>();
    // l.add(a);
    // l.add(b);
    // System.out.println("\nunsorted:");
    // for (BlockStack blockStack : l) {
    // blockStack.decoratedPrint();
    // }
    // Collections.sort(l);
    // System.out.println("\nsorted:");
    // for (BlockStack blockStack : l) {
    // blockStack.decoratedPrint();
    // }
    // }

}
