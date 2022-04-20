import java.time.Duration;
import java.time.Instant;

import javax.swing.JOptionPane;

/**
 *
 * @author Moutaz
 * @author Nassouh
 */

public class MainFrame extends javax.swing.JFrame {

	public MainFrame() {
		this.setTitle("Blocks World (Cubes World) Solver!");
		initComponents();
		this.setLocationRelativeTo(null);
	}

	private void initComponents() {

		welcomeLabel = new javax.swing.JLabel();
		algoLabel = new javax.swing.JLabel();
		algoComboBox = new javax.swing.JComboBox<>();
		startBtn = new javax.swing.JButton();
		startingPosLabel = new javax.swing.JLabel();
		startPosText = new javax.swing.JTextField();

		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		setResizable(false);

		welcomeLabel.setFont(new java.awt.Font("Dialog", 1, 18));
		welcomeLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		welcomeLabel.setText("Blocks World (Cubes World) Solver!");

		algoLabel.setText("Preferred algorithm:");

		algoComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(
				new String[] { "Breadth-First Search (BFS)", "Depth-First Search (DFS)", "Uniform Cost Search",
						"Depth Limited Search", "Iterative Deepening Search" }));

		startBtn.setFont(new java.awt.Font("Dialog", 0, 18));
		startBtn.setText("Solve");
		startBtn.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				startBtnActionPerformed(evt);
			}
		});

		startingPosLabel.setText("Starting Position:");

		startPosText.setText("BC#AD");

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(
				layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(layout.createSequentialGroup()
								.addContainerGap()
								.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
										.addComponent(welcomeLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 396,
												Short.MAX_VALUE)
										.addGroup(layout.createSequentialGroup()
												.addComponent(algoLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 110,
														javax.swing.GroupLayout.PREFERRED_SIZE)
												.addGroup(layout
														.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
														.addGroup(layout.createSequentialGroup()
																.addGap(24, 24, 24)
																.addComponent(startBtn,
																		javax.swing.GroupLayout.PREFERRED_SIZE, 122,
																		javax.swing.GroupLayout.PREFERRED_SIZE))
														.addGroup(layout.createSequentialGroup()
																.addGap(18, 18, 18)
																.addComponent(algoComboBox, 0,
																		javax.swing.GroupLayout.DEFAULT_SIZE,
																		Short.MAX_VALUE))))
										.addGroup(layout.createSequentialGroup()
												.addComponent(startingPosLabel, javax.swing.GroupLayout.PREFERRED_SIZE,
														110,
														javax.swing.GroupLayout.PREFERRED_SIZE)
												.addGap(18, 18, 18)
												.addComponent(startPosText)))
								.addContainerGap()));
		layout.setVerticalGroup(
				layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(layout.createSequentialGroup()
								.addContainerGap()
								.addComponent(welcomeLabel)
								.addGap(18, 18, 18)
								.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
										.addComponent(algoLabel)
										.addComponent(algoComboBox, javax.swing.GroupLayout.PREFERRED_SIZE,
												javax.swing.GroupLayout.DEFAULT_SIZE,
												javax.swing.GroupLayout.PREFERRED_SIZE))
								.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
								.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
										.addComponent(startingPosLabel)
										.addComponent(startPosText, javax.swing.GroupLayout.PREFERRED_SIZE,
												javax.swing.GroupLayout.DEFAULT_SIZE,
												javax.swing.GroupLayout.PREFERRED_SIZE))
								.addGap(14, 14, 14)
								.addComponent(startBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 43,
										javax.swing.GroupLayout.PREFERRED_SIZE)
								.addContainerGap(14, Short.MAX_VALUE)));

		pack();
	}

	private void startBtnActionPerformed(java.awt.event.ActionEvent evt) {

		String startingString = this.startPosText.getText();
		int algo = this.algoComboBox.getSelectedIndex();
		int limit = -1;

		if (algo == 3) {
			try {
				limit = Integer.valueOf(JOptionPane.showInputDialog("Please enter the preferred maximum depth:"));
				if (limit <= 0)
					throw new Exception();
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, "Please enter a positive natural number.", "Invalid input",
						JOptionPane.ERROR_MESSAGE);
				return;
			}

		}

		var start_state = new Node(startingString);
		var model = new Solver(start_state);

		Instant before = Instant.now();

		// rest count
		Node.nOfNodes = 0;
		Solver.SolutionWithStatistics algoAnswer = switch (algo) {
			case 0 -> model.solveBFS();
			case 1 -> model.solveDFS();
			case 2 -> model.solveUniform();
			case 3 -> model.solveDFSLimited(limit);
			default -> model.solveIterativeDeepening();
		};
		Instant after = Instant.now();
		var result = new StringBuilder();
		result.append(start_state.decoratedString("Start State"));
		if (algoAnswer.isFound) {
			result.append("Search Complete:\n");
			result.append("Depth = " + String.valueOf(model.depth));
			result.append("\ng = " + String.valueOf(model.g) + "\n");
		} else {
			result.append("Solution was not found.\n");
		}
		result.append(" Processed Nodes ~ " + algoAnswer.processedNodes + "\n");
		result.append(" Time : " + Duration.between(before, after).toMillis() + " ms");
		result.append("\n Rough estimate memory required: \n    " + (algoAnswer.ClosedSetSize + algoAnswer.OpenSetSize)
				+ " node size");
		result.append("\n Nodes Created: " + String.valueOf(Node.nOfNodes));
		// algoAnswer.path.get(0).printSolutionsStatistics("Answer");

		JOptionPane.showMessageDialog(this, result);

	}

	/**
	 * @param args the command line arguments
	 */
	public static void main(String args[]) {

		try {
			for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					javax.swing.UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (ClassNotFoundException ex) {
			java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (InstantiationException ex) {
			java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (IllegalAccessException ex) {
			java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (javax.swing.UnsupportedLookAndFeelException ex) {
			java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		}

		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				new MainFrame().setVisible(true);
			}
		});
	}

	private javax.swing.JButton startBtn;
	private javax.swing.JComboBox<String> algoComboBox;
	private javax.swing.JLabel algoLabel;
	private javax.swing.JLabel startingPosLabel;
	private javax.swing.JTextField startPosText;
	private javax.swing.JLabel welcomeLabel;
}
