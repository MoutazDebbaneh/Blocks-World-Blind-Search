import java.util.ArrayList;
import java.util.List;

public class Node {
    ArrayList<BlockStack> stacks;
    Node Parent;
    int depth;

    private static <E> void swap(List<E> list, int i, int j) {
        E e = list.get(i);
        list.set(i, list.get(j));
        list.set(j, e);
    }

    private Node move(int from, int to) {
        var newNode = new Node();
        newNode.stacks = new ArrayList<>(stacks);
        var c = newNode.stacks.get(from).top();
        newNode.stacks.set(from, newNode.stacks.get(from).pop());
        newNode.stacks.set(to, stacks.get(to).push(c));

        // TODO: sort newNode.stacks in-place using
        // opt-1: starts java list sort
        // opt-2:
        // swap from with from+1 while the order is not correct
        // swap to with to-1 while the order isn't correct

        newNode.depth = this.depth + 1;
        newNode.Parent = this;
        return newNode;
    }

    ArrayList<Node> neighbors() {
        var result = new ArrayList<Node>();
        for (int i = 0; i < stacks.size(); ++i) {
            var stack = stacks.get(i);
            // in this case I can move from this stack
            if (stack.size() > 0) {
                boolean second_zero_size_stack = false;
                for (int j = 0; j < stacks.size(); j++) {
                    if (j == i)
                        continue;
                    if (second_zero_size_stack)
                        break;
                    if (stacks.get(j).size() == 0)
                        second_zero_size_stack = true;
                    // target new vertex
                    Node neighbor = this.move(i, j);
                    // add neighbor to neighbors
                    result.add(neighbor);
                }
            } else {
                // here since stack are probably sorted by size
                // I can safely leave the loop
                break;
            }
        }
        return result;
    }
}
