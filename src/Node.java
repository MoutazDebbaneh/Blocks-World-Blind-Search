import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Node {
	ArrayList<BlockStack> stacks;
	Node Parent;
	int depth;

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

	private static <E> void swap(List<E> list, int i, int j) {
		E e = list.get(i);
		list.set(i, list.get(j));
		list.set(j, e);
	}

	private Node move(int from, int to) {
		var newNode = new Node();
		// here is the game changer !!!
		newNode.stacks = new ArrayList<>(stacks);
		var c = newNode.stacks.get(from).top();
		newNode.stacks.set(from, newNode.stacks.get(from).pop());
		newNode.stacks.set(to, stacks.get(to).push(c));

		Collections.sort(newNode.stacks);

		newNode.depth = this.depth + 1;
		newNode.Parent = this;
		return newNode;
	}

	Set<Node> neighbors() {
		Set<Node> result = new HashSet<Node>();
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
					// result.contains(neighbor) could be unnecessary
					if (!neighbor.equals(this)) {
						// add neighbor to neighbors
						result.add(neighbor);
					}
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
			blockStack.decoratedPrint();
		}
	}

	public Node(ArrayList<String> state) {
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
		assert (this.stacks.size() == problemSize);

	}

	public Node() {
	}

	public static void main(String args[]) {
		var arr = new ArrayList<String>();
		arr.add("BC");
		arr.add("AD");
		arr.add("EF");
		var initialState = new Node(arr);
		initialState.decoratedPrint("initial node");
		int cnt = 0;

		// Stack<Node> OpenSet = new Stack<Node>();
		Set<Node> ClosedSet = new HashSet<Node>();

		for (var neighbor : initialState.neighbors()) {
			if (!ClosedSet.contains(neighbor)) {
				cnt++;
				for (Node neighbor2 : neighbor.neighbors()) {
					if (!ClosedSet.contains(neighbor2)) {
						cnt++;
						for (Node neighbor3 : neighbor2.neighbors()) {
							if (!ClosedSet.contains(neighbor3)) {
								cnt++;
								for (Node neighbor4 : neighbor3.neighbors()) {
									if (!ClosedSet.contains(neighbor4)) {
										cnt++;
										for (Node neighbor5 : neighbor4.neighbors()) {
											cnt++;
											ClosedSet.add(neighbor5);
										}
									}
									ClosedSet.add(neighbor4);
								}
							}
							ClosedSet.add(neighbor3);
						}
					}
					ClosedSet.add(neighbor2);
				}
			}
			ClosedSet.add(neighbor);
		}
		System.out.print("states discover : ");
		System.out.println(cnt);
		BlockStack.showHitStat();
		// this example shows 0.4 hit rate
		// which is a decent optimization
	}

}
