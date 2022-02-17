import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Stack;

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

    boolean isFinal(Node State) {
        return State.stacks.get(0).size() == State.stacks.size() && State.stacks.get(0).equals(orderedStack);
    }

    public ArrayList<Node> solveDFS() {
        InitialState.Parent = null;
        var answer = new ArrayList<Node>();
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
            answer.add(front);
            front = front.Parent;
        }
        return answer;
    }

    public ArrayList<Node> solveDFSLimited(int n) {
        InitialState.Parent = null;
        var answer = new ArrayList<Node>();
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
                answer.add(front);
                front = front.Parent;
            }
        return answer;
    }

    public ArrayList<Node> solveIterativeDeepening() {
        var answer = new ArrayList<Node>();
        int limit = 1;
        while (answer.size() == 0) {
            answer = solveDFSLimited(limit);
            limit++;
        }
        return answer;
    }

    public ArrayList<Node> solveBFS() {
        InitialState.Parent = null;
        var answer = new ArrayList<Node>();
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
            answer.add(front);
            front = front.Parent;
        }
        return answer;
    }

    public ArrayList<Node> solveUniform() {
        InitialState.Parent = null;
        var answer = new ArrayList<Node>();
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
            answer.add(front);
            front = front.Parent;
        }
        return answer;
    }

    public static void main(String[] args) {
        var model = new Solver(new Node("BC|AD|FE"));

        var bfsAnswer = model.solveBFS();
        System.out.print("model.solveBFS() steps : ");
        System.out.println(bfsAnswer.size());
        bfsAnswer.get(0).printSolutionsStatistics("Target");

        var dfsAnswer = model.solveDFS();
        System.out.print("model.solveDFS() steps : ");
        System.out.println(dfsAnswer.size());
        dfsAnswer.get(0).printSolutionsStatistics("Target");

        var iterDAnswer = model.solveIterativeDeepening();
        System.out.print("model.solveIterativeDeepening() steps : ");
        System.out.println(iterDAnswer.size());
        iterDAnswer.get(0).printSolutionsStatistics("Target");

        var UniformAnswer = model.solveUniform();
        System.out.print("model.solveUniform() steps : ");
        System.out.println(UniformAnswer.size());
        UniformAnswer.get(0).printSolutionsStatistics("Target");

    }
}
