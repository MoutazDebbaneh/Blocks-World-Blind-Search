import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Stack;
import java.time.*;

public class Solver {
    Node InitialState;
    final Node finalState = new Node(""); // Change if you want a custom final state
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
        int volatileOpenSetSize = 0;
        // time complexity
        int processedNodes = 0;
        boolean isFound = false;
    }

    boolean isFinal(Node State) {
        if (finalState.toString().length() == 0)
            return State.stacks.get(0).size() == State.stacks.size() && State.stacks.get(0).equals(orderedStack);
        else
            return State.equals(finalState);
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
            answer.processedNodes++;
            if (isFinal(front)) {
                answer.isFound = true;
                break;
            }
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

    private Node solveDFSLimitedRecursive(Node currNode, int limit, HashSet<String> ClosedSet,
            SolutionWithStatistics answer) {
        ClosedSet.add(this.InitialState.toString());
        answer.ClosedSetSize = Math.max(answer.ClosedSetSize, ClosedSet.size());
        answer.processedNodes++;
        if (isFinal(currNode)) {
            answer.isFound = true;
            return currNode;
        }
        if (currNode.depth >= limit)
            return null;
        var neighbors = currNode.neighbors();
        // simulate adding neighbors to OpenSet
        answer.volatileOpenSetSize += neighbors.size();
        answer.OpenSetSize = Math.max(answer.OpenSetSize, answer.volatileOpenSetSize);
        for (var neighbor : neighbors) {
            if (!ClosedSet.contains(neighbor.toString())) {
                // simulate removing it from the OpenSet
                answer.volatileOpenSetSize--;
                answer.OpenSetSize = Math.max(answer.OpenSetSize, answer.volatileOpenSetSize);

                var ans = solveDFSLimitedRecursive(neighbor, limit, ClosedSet, answer);
                if (ans != null)
                    return ans;
            }
        }
        ClosedSet.remove(this.InitialState.toString());
        return null;
    }

    public SolutionWithStatistics solveDFSLimited(int limit) {
        var answer = new SolutionWithStatistics();
        Node solutionNode = solveDFSLimitedRecursive(this.InitialState, limit, new HashSet<String>(), answer);
        if (solutionNode != null)
            this.depth = solutionNode.depth;
        if (solutionNode != null)
            this.g = solutionNode.g;
        var tmp = solutionNode;
        while (tmp != null) {
            answer.path.add(tmp);
            tmp = tmp.Parent;
        }
        return answer;
    }

    public SolutionWithStatistics solveIterativeDeepening() {
        var answer = new SolutionWithStatistics();
        SolutionWithStatistics tmp = null;
        int limit = 1;
        while (tmp == null || tmp.path.size() == 0) {
            tmp = solveDFSLimited(limit);
            answer.ClosedSetSize = Math.max(answer.ClosedSetSize, tmp.ClosedSetSize);
            answer.OpenSetSize = Math.max(answer.OpenSetSize, tmp.OpenSetSize);
            answer.processedNodes = Math.max(answer.processedNodes, tmp.processedNodes);
            limit++;
        }
        if (tmp != null) {
            this.depth = tmp.path.get(0).depth;
            this.g = tmp.path.get(0).g;
        }
        answer.path = tmp.path;
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
            answer.processedNodes++;
            if (isFinal(front)) {
                answer.isFound = true;
                break;
            }
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
            answer.processedNodes++;
            if (isFinal(front)) {
                answer.isFound = true;
                break;
            }
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

        var start_state = new Node("BC|ADE");

        start_state.decoratedPrint("start state:");

        var model = new Solver(start_state);

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
        var limitedAnswer = model.solveDFSLimited(20);
        after = Instant.now();
        System.out.println("\nLimited : ");
        System.out.println(" time : " + Duration.between(before, after).toMillis() + " ms");
        System.out.println(" rough memory estimate : \n    " + (limitedAnswer.ClosedSetSize + limitedAnswer.OpenSetSize)
                + " nodes Allocated");
        limitedAnswer.path.get(0).printSolutionsStatistics("Answer");

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
