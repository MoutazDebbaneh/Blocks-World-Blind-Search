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
        while (front != null) {
            answer.path.add(front);
            front = front.Parent;
        }
        answer.OpenSetSize = OpenSet.size();
        answer.ClosedSetSize = ClosedSet.size();
        return answer;
    }

    public SolutionWithStatistics solveDFSLimited(int n) {
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
            if (front.depth > n)
                continue;
            var neighbors = front.neighbors();
            for (var neighbor : neighbors) {
                if (!ClosedSet.contains(neighbor.toString())) {
                    OpenSet.push(neighbor);
                    ClosedSet.add(neighbor.toString());
                }
            }
        }
        if (isFinal(front))
            while (front != null) {
                answer.path.add(front);
                front = front.Parent;
            }
        answer.OpenSetSize = OpenSet.size();
        answer.ClosedSetSize = ClosedSet.size();
        return answer;
    }

    public SolutionWithStatistics solveIterativeDeepening() {
        var answer = new SolutionWithStatistics();
        var tmp = new SolutionWithStatistics();
        int limit = 1;
        while (tmp.path.size() == 0) {
            tmp = solveDFSLimited(limit);
            limit++;
            answer.OpenSetSize = Math.max(tmp.OpenSetSize, answer.OpenSetSize);
            answer.ClosedSetSize = Math.max(tmp.ClosedSetSize, answer.ClosedSetSize);
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
        while (front != null) {
            answer.path.add(front);
            front = front.Parent;
        }
        answer.OpenSetSize = OpenSet.size();
        answer.ClosedSetSize = ClosedSet.size();
        return answer;
    }

    public static void main(String[] args) {
        var model = new Solver(new Node("BC|AD|FE"));

        Instant before = Instant.now();
        var bfsAnswer = model.solveBFS();
        Instant after = Instant.now();
        System.out.println("\nBFS \n steps : " + bfsAnswer.path.size());
        System.out.println(" time : " + Duration.between(before, after).toMillis() + " ms");
        System.out.println(
                " rough memory estimate : \n    " + (bfsAnswer.ClosedSetSize + bfsAnswer.OpenSetSize)
                        + " nodes Allocated");
        bfsAnswer.path.get(0).printSolutionsStatistics("Answer");

        before = Instant.now();
        var dfsAnswer = model.solveDFS();
        after = Instant.now();
        System.out.println("\nDFS \n steps : " + dfsAnswer.path.size());
        System.out.println(" time : " + Duration.between(before, after).toMillis() + " ms");
        System.out.println(
                " rough memory estimate : \n    " + (dfsAnswer.ClosedSetSize + dfsAnswer.OpenSetSize)
                        + " nodes Allocated");
        dfsAnswer.path.get(0).printSolutionsStatistics("Answer");

        before = Instant.now();
        var iterDAnswer = model.solveIterativeDeepening();
        after = Instant.now();
        System.out.println("\nIterative Deepening\n steps : " + iterDAnswer.path.size());
        System.out.println(" time : " + Duration.between(before, after).toMillis() + " ms");
        System.out.println(" rough memory estimate : \n    " + (iterDAnswer.ClosedSetSize + iterDAnswer.OpenSetSize)
                + " nodes Allocated");
        iterDAnswer.path.get(0).printSolutionsStatistics("Answer");

        before = Instant.now();
        var UniformAnswer = model.solveUniform();
        after = Instant.now();
        System.out.println("\nUniform Cost\n steps : " + UniformAnswer.path.size());
        System.out.println(" time : " + Duration.between(before, after).toMillis() + " ms");
        System.out.println(" rough memory estimate : \n    " + (UniformAnswer.ClosedSetSize + UniformAnswer.OpenSetSize)
                + " nodes Allocated");
        UniformAnswer.path.get(0).printSolutionsStatistics("Answer");
        System.out.println("\n\n");

    }
}
