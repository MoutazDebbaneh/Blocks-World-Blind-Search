
/**
 *
 * @author Moutaz
 * @author Nassouh
 */

public class MainFrame extends javax.swing.JFrame {

    public MainFrame() {
        initComponents();
        this.setTitle("Blocks World (Cubes World) Solver");
    }

    private void initComponents() {

        welcomeLabel = new javax.swing.JLabel();
        nOfCubesLabel = new javax.swing.JLabel();
        nOfCubesSpinner = new javax.swing.JSpinner();
        algoLabel = new javax.swing.JLabel();
        algoComboBox = new javax.swing.JComboBox<>();
        solveButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        welcomeLabel.setFont(new java.awt.Font("Dialog", 1, 18));
        welcomeLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        welcomeLabel.setText("Blocks World (Cubes World) Solver!");

        nOfCubesLabel.setText("Number of cubes (n):");

        nOfCubesSpinner.setModel(new javax.swing.SpinnerNumberModel(0, 0, 26, 1));
        nOfCubesSpinner.setEditor(new javax.swing.JSpinner.NumberEditor(nOfCubesSpinner, ""));
        nOfCubesSpinner.setName("");
        ((javax.swing.text.DefaultFormatter) ((javax.swing.JSpinner.DefaultEditor) nOfCubesSpinner.getEditor())
                .getTextField().getFormatter()).setAllowsInvalid(false);

        algoLabel.setText("Preffered alogrithm:");

        algoComboBox.setModel(
                new javax.swing.DefaultComboBoxModel<>(
                        new String[] { "Breadth-First Search (BFS)", "Depth-First Search (DFS)", "Uniform Cost Search",
                                "Depth Limited Search", "Iterative Deepening Search" }));

        solveButton.setFont(new java.awt.Font("Dialog", 0, 18));
        solveButton.setText("Solve");
        solveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                solveButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGap(0, 143, Short.MAX_VALUE)
                                .addComponent(solveButton, javax.swing.GroupLayout.PREFERRED_SIZE, 122,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(143, 143, 143))
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(welcomeLabel, javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING,
                                                                false)
                                                        .addComponent(algoLabel,
                                                                javax.swing.GroupLayout.Alignment.LEADING,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(nOfCubesLabel,
                                                                javax.swing.GroupLayout.Alignment.LEADING,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                                .addGap(18, 18, 18)
                                                .addGroup(layout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(algoComboBox, 0,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addComponent(nOfCubesSpinner,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE, 55,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addGap(0, 0, Short.MAX_VALUE)))))
                                .addContainerGap()));
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(welcomeLabel)
                                .addGap(27, 27, 27)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(nOfCubesLabel)
                                        .addComponent(nOfCubesSpinner, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(algoLabel)
                                        .addComponent(algoComboBox, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addComponent(solveButton, javax.swing.GroupLayout.PREFERRED_SIZE, 43,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(19, Short.MAX_VALUE)));

        pack();
    }

    private void solveButtonActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
    }

    public static void main(String args[]) {

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainFrame().setVisible(true);
            }
        });
    }

    // Variables declaration
    private javax.swing.JButton solveButton;
    private javax.swing.JComboBox<String> algoComboBox;
    private javax.swing.JLabel nOfCubesLabel;
    private javax.swing.JLabel algoLabel;
    private javax.swing.JSpinner nOfCubesSpinner;
    private javax.swing.JLabel welcomeLabel;

}
