/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * SimulatorFrame.java
 *
 * Created on Jul 19, 2011, 10:52:39 PM
 */
package statemachinedesigner;

import java.awt.BorderLayout;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;

/**
 *
 * @author Henry
 */
public class SimulatorFrame extends javax.swing.JFrame {

    /** Creates new form SimulatorFrame */
    public SimulatorFrame() {
        _controller = new SimulatorController(this);
        initComponents();
        _designInputModel = new DefaultListModel();
        wordList.setModel(_designInputModel);

//        EditorApplet editPanel = new EditorApplet(_controller);
//              designInputPanel.setLayout(new BorderLayout());
//        designInputPanel.add(editPanel.getContentPane(), BorderLayout.CENTER);

    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainTabbedPane = new javax.swing.JTabbedPane();
        designPanel = new javax.swing.JPanel();
        designResultPanel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jLabel3 = new javax.swing.JLabel();
        inputTextField = new javax.swing.JTextField();
        jScrollPane3 = new javax.swing.JScrollPane();
        wordList = new javax.swing.JList();
        designInputPanel = new javax.swing.JPanel();
        addButton = new javax.swing.JButton();
        removeButton = new javax.swing.JButton();
        generateButton = new javax.swing.JButton();
        testPanel = new javax.swing.JPanel();
        testInputScrollPane = new javax.swing.JScrollPane();
        designTextArea = new javax.swing.JTextArea();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        testResultsScrollPane = new javax.swing.JScrollPane();
        resultsTextArea = new javax.swing.JTextArea();
        runSimulationButton = new javax.swing.JButton();
        intermediateStepsCheckbox = new javax.swing.JCheckBox();
        finalReportersCheckbox = new javax.swing.JCheckBox();
        statusLabel = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        designResultPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);

        jLabel3.setText("Result:");

        javax.swing.GroupLayout designResultPanelLayout = new javax.swing.GroupLayout(designResultPanel);
        designResultPanel.setLayout(designResultPanelLayout);
        designResultPanelLayout.setHorizontalGroup(
            designResultPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 662, Short.MAX_VALUE)
            .addGroup(designResultPanelLayout.createSequentialGroup()
                .addComponent(jLabel3)
                .addContainerGap())
        );
        designResultPanelLayout.setVerticalGroup(
            designResultPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, designResultPanelLayout.createSequentialGroup()
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        inputTextField.setToolTipText("Use only integers and space characters");
        inputTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                inputTextFieldActionPerformed(evt);
            }
        });

        wordList.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        wordList.setToolTipText("the list of words that the designed state machine will recognize");
        jScrollPane3.setViewportView(wordList);

        designInputPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        javax.swing.GroupLayout designInputPanelLayout = new javax.swing.GroupLayout(designInputPanel);
        designInputPanel.setLayout(designInputPanelLayout);
        designInputPanelLayout.setHorizontalGroup(
            designInputPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 662, Short.MAX_VALUE)
        );
        designInputPanelLayout.setVerticalGroup(
            designInputPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 257, Short.MAX_VALUE)
        );

        addButton.setText("Add");
        addButton.setToolTipText("add a word to the wordlist");
        addButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addButtonActionPerformed(evt);
            }
        });

        removeButton.setText("Remove");
        removeButton.setToolTipText("remove selected words");
        removeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeButtonActionPerformed(evt);
            }
        });

        generateButton.setText("Generate");
        generateButton.setToolTipText("Generate a new design");
        generateButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                generateButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout designPanelLayout = new javax.swing.GroupLayout(designPanel);
        designPanel.setLayout(designPanelLayout);
        designPanelLayout.setHorizontalGroup(
            designPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, designPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(designPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(designInputPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(designResultPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, designPanelLayout.createSequentialGroup()
                        .addGroup(designPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 583, Short.MAX_VALUE)
                            .addComponent(inputTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 583, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(designPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(generateButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(removeButton, javax.swing.GroupLayout.DEFAULT_SIZE, 77, Short.MAX_VALUE)
                            .addComponent(addButton, javax.swing.GroupLayout.DEFAULT_SIZE, 77, Short.MAX_VALUE))))
                .addContainerGap())
        );
        designPanelLayout.setVerticalGroup(
            designPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(designPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(designPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(designPanelLayout.createSequentialGroup()
                        .addComponent(inputTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 24, Short.MAX_VALUE)
                        .addGap(3, 3, 3))
                    .addGroup(designPanelLayout.createSequentialGroup()
                        .addComponent(addButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                .addGroup(designPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, designPanelLayout.createSequentialGroup()
                        .addComponent(removeButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(generateButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                    .addGroup(designPanelLayout.createSequentialGroup()
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 142, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                .addComponent(designInputPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(designResultPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        mainTabbedPane.addTab("Design", designPanel);

        designTextArea.setColumns(20);
        designTextArea.setRows(5);
        testInputScrollPane.setViewportView(designTextArea);

        jLabel1.setText("Design");

        jLabel2.setText("Results");

        resultsTextArea.setColumns(20);
        resultsTextArea.setEditable(false);
        resultsTextArea.setRows(5);
        testResultsScrollPane.setViewportView(resultsTextArea);

        runSimulationButton.setText("Run Simulation");
        runSimulationButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runSimulationButtonActionPerformed(evt);
            }
        });

        intermediateStepsCheckbox.setText("Show intermediate steps");

        finalReportersCheckbox.setSelected(true);
        finalReportersCheckbox.setText("All Reporters final");

        statusLabel.setText("Status: waiting...");
        statusLabel.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        javax.swing.GroupLayout testPanelLayout = new javax.swing.GroupLayout(testPanel);
        testPanel.setLayout(testPanelLayout);
        testPanelLayout.setHorizontalGroup(
            testPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(testPanelLayout.createSequentialGroup()
                .addGroup(testPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(testPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(testPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(testPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(testResultsScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 627, Short.MAX_VALUE)
                            .addComponent(testInputScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 627, Short.MAX_VALUE)))
                    .addGroup(testPanelLayout.createSequentialGroup()
                        .addGap(74, 74, 74)
                        .addGroup(testPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(statusLabel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, testPanelLayout.createSequentialGroup()
                                .addComponent(finalReportersCheckbox)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(intermediateStepsCheckbox)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 245, Short.MAX_VALUE)
                        .addComponent(runSimulationButton)))
                .addContainerGap())
        );
        testPanelLayout.setVerticalGroup(
            testPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(testPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(testPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(testInputScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(testPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(testResultsScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 360, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 36, Short.MAX_VALUE)
                .addComponent(statusLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(testPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(runSimulationButton)
                    .addComponent(finalReportersCheckbox)
                    .addComponent(intermediateStepsCheckbox))
                .addContainerGap())
        );

        mainTabbedPane.addTab("Test", testPanel);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(mainTabbedPane, javax.swing.GroupLayout.DEFAULT_SIZE, 691, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(mainTabbedPane, javax.swing.GroupLayout.DEFAULT_SIZE, 595, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    public boolean isFinalReporter() {
        return finalReportersCheckbox.isSelected();
    }

    public boolean isShowIntermediate() {
        return intermediateStepsCheckbox.isSelected();
    }
    private void runSimulationButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_runSimulationButtonActionPerformed
        //input will have components split by spaces;
        //p# will denote a promoter
        //@# denotes an invertase coding region
        //I# will denote an invertase site
        //' will denote an inverted site, otherwise components are assumed to have normal orientation
        //simulation is not case sensitive; interally converted to lower case
        resultsTextArea.setText("");
        String result = _controller.startSimulation(designTextArea.getText());
        resultsTextArea.setText(result);

    }//GEN-LAST:event_runSimulationButtonActionPerformed

    private void inputTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_inputTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_inputTextFieldActionPerformed

    private void addButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addButtonActionPerformed
        String toAdd = inputTextField.getText();
        if (toAdd != null) {
            toAdd = toAdd.trim();
            toAdd = _controller.validateDesignInput(toAdd);
            if (toAdd.contains("invalid")) {
                JOptionPane.showMessageDialog(this, "Input has invalid characters\nRemember, only integers and spaces may be used");
            } else {
                _designInputModel.addElement(toAdd);
                inputTextField.setText("");
            }
            inputTextField.requestFocus();
        }
    }//GEN-LAST:event_addButtonActionPerformed

    private void removeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeButtonActionPerformed
        int[] indices = wordList.getSelectedIndices();
        if (indices.length > 0) {
            for (int i = 0; i < indices.length; i++) {
                System.out.println(indices[i]);
                _designInputModel.remove(indices[i] - i);
            }
            int index = indices[indices.length - 1] - indices.length + 1;
            wordList.setSelectedIndex(index);
            wordList.ensureIndexIsVisible(index);

            indices = null;
        }
    }//GEN-LAST:event_removeButtonActionPerformed

    private void generateButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_generateButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_generateButtonActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new SimulatorFrame().setVisible(true);
            }
        });
    }
    private DefaultListModel _designInputModel;
    private SimulatorController _controller;
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addButton;
    private javax.swing.JPanel designInputPanel;
    private javax.swing.JPanel designPanel;
    private javax.swing.JPanel designResultPanel;
    private javax.swing.JTextArea designTextArea;
    private javax.swing.JCheckBox finalReportersCheckbox;
    private javax.swing.JButton generateButton;
    private javax.swing.JTextField inputTextField;
    private javax.swing.JCheckBox intermediateStepsCheckbox;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTabbedPane mainTabbedPane;
    private javax.swing.JButton removeButton;
    private javax.swing.JTextArea resultsTextArea;
    private javax.swing.JButton runSimulationButton;
    private javax.swing.JLabel statusLabel;
    private javax.swing.JScrollPane testInputScrollPane;
    private javax.swing.JPanel testPanel;
    private javax.swing.JScrollPane testResultsScrollPane;
    private javax.swing.JList wordList;
    // End of variables declaration//GEN-END:variables
}
