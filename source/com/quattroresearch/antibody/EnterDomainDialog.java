/*******************************************************************************
 * Copyright C 2015, quattro research GmbH, Roche pREDi (Roche Innovation Center Penzberg)
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the
 * Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
 * OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 ******************************************************************************/
package com.quattroresearch.antibody;

import java.io.FileNotFoundException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import org.roche.antibody.services.ConfigFileService;

/**
 * 
 * {@code EnterDomainDialog}
 * 
 * @author <b>Stefan Klostermann:</b> Stefan DOT Klostermann AT roche DOT com, Roche Pharma Research and Early
 *         Development - Informatics, Roche Innovation Center Penzberg
 * @author <b>Anne Mund</b>, quattro research GmbH
 * 
 * @version $Id$
 */
public class EnterDomainDialog extends JDialog {

  /** Generated UID */
  private static final long serialVersionUID = -8824418459276663121L;

  private JButton addButton;

  private JButton confirmButton;

  private JButton leaveButton;

  private JTable table;

  private DefaultTableModel model;

  private final Object[] headers = new Object[] {"Name", "Name Short",
      "Species", "Humanness", "Chain", "Domain", "Sequence", "Length",
      "Cys Count", "Cys Pattern", "Comment", "Priority"};

// private final String[] species = new String[] {"human", "rabbit", "rat",
// "mouse"};

  private final String[] humanness = new String[] {"human", "humanizable",
      "non-human"};

  private final String[] chains = new String[] {"heavy", "kappa", "lambda",
      ""};

  private final String[] domains = new String[] {"hinge", "constant",
      "hinge", ""};

  public EnterDomainDialog(JFrame frame, boolean modal) {
    super(frame, modal);
    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    initTable();
    initComp();
  }

  private void initTable() {
    Object[][] tabledata;
    try {
      tabledata = readLibrary();

      model = new DefaultTableModel(tabledata, headers);
    } catch (FileNotFoundException e) {
      JOptionPane.showMessageDialog(table.getParent(), "Please check the filepath in the Antibody Editor Settings", "Domain definition file not found", JOptionPane.ERROR_MESSAGE);
    } catch (Exception e) {
      JOptionPane.showMessageDialog(table.getParent(), "Reading library failed: " + e.getMessage(), "Reading library failed.", JOptionPane.ERROR_MESSAGE);
    }
    table = new JTable(model);

// TableColumn speciesColumn = table.getColumnModel().getColumn(2);
// JComboBox speciesBox = new JComboBox(species);
// speciesColumn.setCellEditor(new DefaultCellEditor(speciesBox));

    TableColumn humanColumn = table.getColumnModel().getColumn(3);
    JComboBox humanBox = new JComboBox(humanness);
    humanColumn.setCellEditor(new DefaultCellEditor(humanBox));

    TableColumn chainColumn = table.getColumnModel().getColumn(4);
    JComboBox chainBox = new JComboBox(chains);
    chainColumn.setCellEditor(new DefaultCellEditor(chainBox));

    TableColumn domainColumn = table.getColumnModel().getColumn(5);
    JComboBox domainBox = new JComboBox(domains);
    domainColumn.setCellEditor(new DefaultCellEditor(domainBox));

    TableColumn sequenceColumn = table.getColumnModel().getColumn(6);
    sequenceColumn.setCellEditor(new SequenceEditor(this));

    TableColumn cysBondColumn = table.getColumnModel().getColumn(9);
    cysBondColumn.setCellEditor(new CysBondEditor(new JTextField()));

    table.setAutoCreateRowSorter(true);
  }

  private void initComp() {

    addButton = new JButton("Add");
    addButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        addButtonActionPerformed(evt);
      }
    });

    leaveButton = new JButton("Close");
    leaveButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        leaveButtonActionPerformed(evt);
      }
    });

    confirmButton = new JButton("Confirm");
    confirmButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        confirmButtonActionPerformed(evt);
      }
    });

    // check settings, if library is editable
// boolean isEditable = PreferencesService.getInstance().getUserPrefs().getBoolean("ALLOW_LIBRARY_EDITING", false);
    boolean isEditable = false;

    addButton.setEnabled(isEditable);
    confirmButton.setEnabled(isEditable);
    table.setEnabled(isEditable);

    JScrollPane scrollPane = new JScrollPane(table);
    setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
    setTitle("Sequence Library");
    setMinimumSize(new java.awt.Dimension(700, 700));

    org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(
        getContentPane());
    getContentPane().setLayout(layout);
    layout.setAutocreateContainerGaps(true);
    layout.setAutocreateGaps(true);

    layout.setHorizontalGroup(layout
        .createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
        .add(org.jdesktop.layout.GroupLayout.TRAILING,
            layout.createSequentialGroup()
                .addContainerGap()
                .add(addButton)
                .addPreferredGap(
                    org.jdesktop.layout.LayoutStyle.RELATED,
                    520, Short.MAX_VALUE)
                .add(confirmButton).addContainerGap()
                .add(leaveButton).addContainerGap())
        .add(table.getTableHeader())
        .add(scrollPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
            710, Short.MAX_VALUE));
    layout.setVerticalGroup(layout
        .createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
        .add(org.jdesktop.layout.GroupLayout.TRAILING,
            layout.createSequentialGroup()
                .add(table.getTableHeader())
                .add(scrollPane,
                    org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
                    300, Short.MAX_VALUE)
                .addPreferredGap(
                    org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout
                    .createParallelGroup(
                        org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(addButton).add(confirmButton)
                    .add(leaveButton)).addContainerGap()));

    pack();
  }

  private Object[][] readLibrary() throws Exception {
    // refresh lib
    ConfigFileService.getInstance().fetchDomainLibrary();
    return ConfigFileService.getInstance().getCachedDomainLibAsRawData();
  }

  public void calculateCys(String sequence, int row) {
    Pattern c = Pattern.compile("C");
    Matcher matcher = c.matcher(sequence);
    int occurences = 0;
    while (matcher.find())
      occurences++;

    table.setValueAt(occurences, row, 8);
    table.setValueAt(sequence.length(), row, 7);
  }

  /**
   * @param evt
   */
  private void addButtonActionPerformed(java.awt.event.ActionEvent evt) {
    JOptionPane.showMessageDialog(null, "Not implemented!",
        "Error", JOptionPane.ERROR_MESSAGE);
// model.addRow(new Object[model.getColumnCount()]);
// int i = model.getRowCount() - 1;
// table.getSelectionModel().setSelectionInterval(i, i);
// table.scrollRectToVisible(new Rectangle(table.getCellRect(i, 0, true)));
  }

  private void confirmButtonActionPerformed(java.awt.event.ActionEvent evt) {
    JOptionPane.showMessageDialog(null, "Not implemented!",
        "Error", JOptionPane.ERROR_MESSAGE);
  }

  private void leaveButtonActionPerformed(java.awt.event.ActionEvent evt) {
    this.dispose();
  }

}
