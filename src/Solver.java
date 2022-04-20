import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Stack;

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
        while (tmp == null || tmp.isFound == false) {
            tmp = solveDFSLimited(limit);
            answer.ClosedSetSize = Math.max(answer.ClosedSetSize, tmp.ClosedSetSize);
            answer.OpenSetSize = Math.max(answer.OpenSetSize, tmp.OpenSetSize);
            answer.processedNodes = Math.max(answer.processedNodes, tmp.processedNodes);
            limit++;
        }
        if (tmp != null) {
            tmp.isFound = true;
            this.depth = tmp.path.get(0).depth;
            this.g = tmp.path.get(0).g;
        }

        return tmp;
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
}
