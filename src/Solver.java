import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Stack;
import java.util.stream.Collectors;
import java.time.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.math.BigInteger;
import java.util.Collections;

class row {
    String algo;
    String time;
    String roughMemoryEstimate;
    String answerDepth;
    String answerCost;
    String startState;
    String processedNodes;
    String nodeInstancesCreated;
}

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

    static String shuffle(String s) {
        var chars = s.chars().mapToObj(c -> (char) c).collect(Collectors.toList());
        ;
        Collections.shuffle(chars);
        return chars.stream()
                .map(String::valueOf)
                .collect(Collectors.joining());
    }

    private static void out(row r, String path) {
        try {
            // Creates a file in the given path if the file doesn't exists,
            FileWriter myWriter = new FileWriter(path + ".csv", true);
            myWriter.write(
                    r.algo
                            + "," + r.time
                            + "," + r.roughMemoryEstimate
                            + "," + r.answerDepth
                            + "," + r.answerCost
                            + "," + r.startState
                            + "," + r.processedNodes
                            + "," + r.nodeInstancesCreated
                            + "\n");
            myWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void f(String input, String dir) {
        var start_state = new Node(input);
        var model = new Solver(start_state);
        var augDir = "data/" + ((dir != null) ? dir : "") + "/";
        try {
            // Creates a file in the given path if the file doesn't exists,
            if ((new File(augDir + input)).exists())
                return;
            if (dir != null) {
                FileWriter myWriter = new FileWriter(augDir + input + ".csv");
                myWriter.write("algo"
                        + "," + "time"
                        + "," + "roughMemoryEstimate"
                        + "," + "answerDepth"
                        + "," + "answerCost"
                        + "," + "startState"
                        + "," + "processedNodes"
                        + "," + "nodeInstancesCreated"
                        + "\n");
                myWriter.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Instant before = Instant.now();

        row r = new row();
        for (var algoNum = 0; algoNum < 4; algoNum++) {
            Node.nOfNodes = 0;
            Solver.SolutionWithStatistics algoAnswer = switch (algoNum) {
                case 0 -> model.solveBFS();
                case 1 -> model.solveDFS();
                case 2 -> model.solveUniform();
                // case 3 -> model.solveDFSLimited(limit);
                default -> model.solveIterativeDeepening();
            };
            r.algo = switch (algoNum) {
                case 0 -> "BFS";
                case 1 -> "DFS";
                case 2 -> "Uniform";
                default -> "IterativeDeepening";
            };
            Instant after = Instant.now();

            r.startState = (start_state.toString());
            if (algoAnswer.isFound) {
                r.answerDepth = String.valueOf(model.depth);
                r.answerCost = String.valueOf(model.g);
            } else {
            }
            r.processedNodes = String.valueOf(algoAnswer.processedNodes);
            r.time = String.valueOf((double) Duration.between(before, after).toMillis() / 1000);
            r.roughMemoryEstimate = String.valueOf(algoAnswer.ClosedSetSize + algoAnswer.OpenSetSize);
            r.nodeInstancesCreated = String.valueOf(Node.nOfNodes);
            if (dir == null)
                out(r, augDir + "n_" + input.length() / 2);
            else
                out(r, augDir + input);
        }
    }

    public static void main(String[] args) {
        // f("AB#C##");
        String tmp = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String alphabet = (new StringBuffer(tmp)).reverse().toString();
        boolean first = false;
        int fac = 720;
        for (var i = 7; i < 25; i++) {
            var seed = alphabet.substring(tmp.length() - i, tmp.length())
                    + "#".repeat(i - 1);
            var startState = seed + "#";
            fac = fac * i;
            for (var j = (first) ? 38 : 1; j <= fac; j++) {
                System.out.println(i + "> " + j + "/" + String.valueOf(fac));
                var shuffledStartState = shuffle(seed) + "#";
                f(startState, "Related Complexity");
                f(shuffledStartState, null);
            }
            first = false;
        }
    }
}
