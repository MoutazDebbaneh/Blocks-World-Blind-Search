import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Stack;
import java.time.*;

public class Solver {
    Node InitialState;
    static BlockStack orderedStack;
    int depth;
    int g;

    Solver(Node node) {
        this.InitialState = node;
        Integer number = node.stacks.stream()
                .map((b) -> {
                    return b.size();
                })
                .reduce(0, (a, b) -> {
                    return a + b;
                });
        orderedStack = BlockStack.DesiredStack(number);
    }

    public class SolutionWithStatistics {
        ArrayList<Node> path = new ArrayList<Node>();
        int OpenSetSize = 0;
        int ClosedSetSize = 0;
    }

    boolean isFinal(Node State) {
        return State.stacks.get(0).size() == State.stacks.size() && State.stacks.get(0).equals(orderedStack);
    }

    public SolutionWithStatistics solveDFS() {
        InitialState.Parent = null;
        var answer = new SolutionWithStatistics();
        Stack<Node> OpenSet = new Stack<Node>();
        HashSet<String> ClosedSet = new HashSet<String>();
        OpenSet.push(this.InitialState);
        ClosedSet.add(this.InitialState.toString());
        Node front = InitialState;
        while (!OpenSet.empty()) {
            front = OpenSet.pop();
            if (isFinal(front))
                break;
            var neighbors = front.neighbors();
            for (var neighbor : neighbors) {
                if (!ClosedSet.contains(neighbor.toString())) {
                    OpenSet.push(neighbor);
                    ClosedSet.add(neighbor.toString());
                }
            }
        }
        if (front != null) {
            this.depth = front.depth;
            this.g = front.g;
        }
        while (front != null) {
            answer.path.add(front);
            front = front.Parent;
        }
        answer.OpenSetSize = OpenSet.size();
        answer.ClosedSetSize = ClosedSet.size();
        return answer;
    }

    static int callsToSolveDFSLimited = 0;

    public Node solveDFSLimited(Node currNode, int limit, HashSet<String> ClosedSet) {
        callsToSolveDFSLimited++;
        ClosedSet.add(this.InitialState.toString());
        if (isFinal(currNode))
            return currNode;
        if (currNode.depth >= limit)
            return null;
        var neighbors = currNode.neighbors();
        for (var neighbor : neighbors) {
            if (!ClosedSet.contains(neighbor.toString())) {
                var ans = solveDFSLimited(neighbor, limit, ClosedSet);
                if (ans != null)
                    return ans;
            }
        }
        ClosedSet.remove(this.InitialState.toString());
        return null;
    }

    public SolutionWithStatistics solveIterativeDeepening() {
        var answer = new SolutionWithStatistics();
        Node tmp = null;
        int limit = 1;
        // including recursive ones
        callsToSolveDFSLimited = 0;
        while (tmp == null) {
            tmp = solveDFSLimited(this.InitialState, limit, new HashSet<String>());
            limit++;
        }
        answer.OpenSetSize = callsToSolveDFSLimited;
        if (tmp != null) {
            this.depth = tmp.depth;
            this.g = tmp.g;
        }
        while (tmp != null) {
            answer.path.add(tmp);
            tmp = tmp.Parent;
        }
        return answer;
    }

    public SolutionWithStatistics solveBFS() {
        InitialState.Parent = null;
        var answer = new SolutionWithStatistics();
        Queue<Node> OpenSet = new LinkedList<Node>();
        HashSet<String> ClosedSet = new HashSet<String>();
        OpenSet.add(this.InitialState);
        // ClosedSet.add(this.InitialState.toString());
        Node front = InitialState;
        while (!OpenSet.isEmpty()) {
            front = OpenSet.peek();
            ClosedSet.add(front.toString()); // Testing another way
            if (isFinal(front))
                break;
            var neighbors = front.neighbors();
            for (var neighbor : neighbors) {
                if (!ClosedSet.contains(neighbor.toString())) {
                    OpenSet.add(neighbor);
                    // ClosedSet.add(neighbor.toString());
                }
            }
            OpenSet.remove();
        }
        if (front != null) {
            this.depth = front.depth;
            this.g = front.g;
        }
        while (front != null) {
            answer.path.add(front);
            front = front.Parent;
        }
        answer.OpenSetSize = OpenSet.size();
        answer.ClosedSetSize = ClosedSet.size();
        return answer;
    }

    public SolutionWithStatistics solveUniform() {
        InitialState.Parent = null;
        var answer = new SolutionWithStatistics();
        PriorityQueue<Node> OpenSet = new PriorityQueue<Node>();
        HashSet<String> ClosedSet = new HashSet<String>();
        OpenSet.add(this.InitialState);
        ClosedSet.add(this.InitialState.toString());
        Node front = InitialState;
        while (!OpenSet.isEmpty()) {
            front = OpenSet.peek();
            if (isFinal(front))
                break;
            var neighbors = front.neighbors();
            for (var neighbor : neighbors) {
                if (!ClosedSet.contains(neighbor.toString())) {
                    OpenSet.add(neighbor);
                    ClosedSet.add(neighbor.toString());
                }
            }
            OpenSet.remove();
        }
        if (front != null) {
            this.depth = front.depth;
            this.g = front.g;
        }
        while (front != null) {
            answer.path.add(front);
            front = front.Parent;
        }
        answer.OpenSetSize = OpenSet.size();
        answer.ClosedSetSize = ClosedSet.size();
        return answer;
    }

    public static void main(String[] args) {

        BlockStack bs = BlockStack.DesiredStack(5);
        System.out.println(bs.toString());
        System.out.println(bs.top());
        System.out.println(bs.pop());

        // var start_state = new Node("BC|AD");
        // var start_state = new Node("BC|ADE");
        var start_state = new Node("BA|C");
        start_state.decoratedPrint("start state:");

        var model = new Solver(start_state);

        orderedStack.decoratedPrint();

        Instant before = Instant.now();
        var bfsAnswer = model.solveBFS();
        Instant after = Instant.now();
        System.out.println("\nBFS");
        System.out.println(" time : " + Duration.between(before, after).toMillis() + " ms");
        System.out.println(
                " rough memory estimate : \n    " + (bfsAnswer.ClosedSetSize + bfsAnswer.OpenSetSize)
                        + " nodes Allocated");
        System.out.println("Nodes Created: " + String.valueOf(Node.nOfNodes));
        bfsAnswer.path.get(0).printSolutionsStatistics("Answer");

        before = Instant.now();
        var dfsAnswer = model.solveDFS();
        after = Instant.now();
        System.out.println("\nDFS");
        System.out.println(" time : " + Duration.between(before, after).toMillis() + " ms");
        System.out.println(
                " rough memory estimate : \n    " + (dfsAnswer.ClosedSetSize + dfsAnswer.OpenSetSize)
                        + " nodes Allocated");
        dfsAnswer.path.get(0).printSolutionsStatistics("Answer");

        before = Instant.now();
        var iterDAnswer = model.solveIterativeDeepening();
        after = Instant.now();
        System.out.println("\nIterative Deepening");
        System.out.println(" time : " + Duration.between(before, after).toMillis() + " ms");
        System.out.println(" rough memory estimate : \n    " + (iterDAnswer.ClosedSetSize + iterDAnswer.OpenSetSize)
                + " nodes Allocated");
        iterDAnswer.path.get(0).printSolutionsStatistics("Answer");

        before = Instant.now();
        var UniformAnswer = model.solveUniform();
        after = Instant.now();
        System.out.println("\nUniform Cost");
        System.out.println(" time : " + Duration.between(before, after).toMillis() + " ms");
        System.out.println(" rough memory estimate : \n    " + (UniformAnswer.ClosedSetSize + UniformAnswer.OpenSetSize)
                + " nodes Allocated");
        UniformAnswer.path.get(0).printSolutionsStatistics("Answer");
        System.out.println("\n\n");

    }
}
