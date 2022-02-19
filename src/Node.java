import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Node implements Comparable<Node> {
	ArrayList<BlockStack> stacks;
	Node Parent = null;
	int depth = 0;
	static int nOfNodes = 0;
	/**
	 * g is the g functions which is the total cost of travelling from the
	 * root/start node to this node
	 */
	int g = 0;

	Node() {
		Node.nOfNodes += 1;
	}

	/**
	 * constructs a Node form a string with the following format
	 * content of stack "|" content of stack
	 * <p>
	 * for example:
	 * <p>
	 * ABC|EF|HG
	 * corresponds to
	 * <p>
	 * C
	 * <p>
	 * B F G
	 * <p>
	 * A E H
	 * <p>
	 * ______
	 * 
	 * 
	 */
	Node(String input) {
		Node.nOfNodes += 1;
		List<String> state = Arrays.asList(input.split("\\|"));
		Integer problemSize = state.stream().map((s) -> {
			return s.length();
		}).reduce((lft, rgt) -> {
			return lft + rgt;
		}).orElse(0);
		this.stacks = new ArrayList<>();
		for (String s : state) {
			this.stacks.add(new BlockStack(s));
		}
		while (this.stacks.size() < problemSize) {
			this.stacks.add(new BlockStack(""));
		}
		Collections.sort(this.stacks);

	}

	private int cost(int from, int to) {
		var newHeight = this.stacks.get(to).size() + 1;
		var oldHeight = this.stacks.get(from).size();
		if (newHeight > oldHeight)
			return newHeight - oldHeight;
		else
			return 2;
	}

	private Node move(int from, int to) {
		var newNode = new Node();
		// here is the game changer !!!
		newNode.stacks = new ArrayList<>(stacks);
		var c = newNode.stacks.get(from).top();
		newNode.stacks.set(from, newNode.stacks.get(from).pop());
		newNode.stacks.set(to, stacks.get(to).push(c));

		Collections.sort(newNode.stacks);

		newNode.g = this.g + cost(from, to);
		newNode.depth = this.depth + 1;
		newNode.Parent = this;
		return newNode;
	}

	ArrayList<Node> neighbors() {
		ArrayList<Node> result = new ArrayList<Node>();
		for (int i = 0; i < stacks.size(); ++i) {
			var stack = stacks.get(i);
			// in this case I can move from this stack
			if (stack.size() > 1) {
				// simply since stack are ordered the number of actions meaningful actions is
				// equal to the number of actual columns + an empty column - the column you are
				// taking a block from
				boolean second_zero_size_stack = false;
				for (int j = 0; j < stacks.size(); j++) {
					// don't take a block and return it to it's place
					if (j == i)
						continue;
					// if you already tried putting the block on the flour don't try it again
					// remember sorted
					if (second_zero_size_stack)
						break;
					if (stacks.get(j).size() == 0)
						second_zero_size_stack = true;
					// target new vertex
					Node neighbor = this.move(i, j);
					result.add(neighbor);

				}
			} else if (stack.size() == 1) {
				for (int j = 0; j < stacks.size(); j++) {
					// don't take a block and return it to it's place
					if (j == i)
						continue;
					// there is no point of moving block to the floor
					// since it's already on the floor
					if (stacks.get(j).size() == 0)
						break;
					// target new vertex
					Node neighbor = this.move(i, j);
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

	void decoratedPrint(String title) {
		System.out.println(title);
		for (BlockStack blockStack : this.stacks) {
			if (blockStack.size() > 0)
				blockStack.decoratedPrint();
		}
	}

	String decoratedString(String title) {
		var sb = new StringBuilder();
		sb.append(title + "\n");
		var max_block_size = this.stacks.stream().reduce(0, (acc, elem) -> {
			return Math.max(acc, elem.size());
		}, (a, b) -> {
			return a + b;
		});
		var cpy = this.stacks;

		for (int i = max_block_size - 1; i >= 0; i--) {
			for (int j = 0; j < cpy.size(); j++) {
				if (i < cpy.get(j).size())
					sb.append(cpy.get(j).get(i));
				else
					sb.append(' ');
				sb.append("  ");
			}
			sb.append('\n');
		}
		for (int j = 0; j < cpy.size() - 1; j++) {
			sb.append("---");
		}
		sb.append("-\n");

		return sb.toString();
	}

	void detailedPrint(String title) {
		System.out.println(title);
		System.out.println("depth = " + this.depth);
		System.out.println("g = " + this.g);
		for (BlockStack blockStack : this.stacks) {
			if (blockStack.size() > 0)
				blockStack.decoratedPrint();
		}
	}

	void printSolutionsStatistics(String title) {
		System.out.println(title);
		System.out.println("depth = " + this.depth);
		System.out.println("g = " + this.g);
	}

	@Override
	public int compareTo(Node o) {
		return this.g - o.g;
	}

	@Override
	public boolean equals(Object other) {
		if (this == other)
			return true;
		else if (other == null)
			return false;
		else if (other instanceof Node) {
			var that = (Node) other;
			if (this.stacks.size() != that.stacks.size())
				return false;
			for (int i = 0; i < this.stacks.size(); i++) {
				if (!this.stacks.get(i).equals(that.stacks.get(i))) {
					return false;
				}
			}
			return true;
		} else
			return false;
	}

	@Override
	public String toString() {
		var sb = new StringBuilder();
		for (int i = 0; i < stacks.size(); i++) {
			for (char c : stacks.get(i)._stack) {
				sb.append(c);
			}
			if (i < stacks.size() - 1)
				sb.append("|");
		}
		return sb.toString();
	}
}
