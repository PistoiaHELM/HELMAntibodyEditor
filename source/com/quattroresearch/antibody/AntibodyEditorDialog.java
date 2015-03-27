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

import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.prefs.Preferences;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.roche.antibody.services.PreferencesService;

/**
 * Dialog for Antibody-Chain Input <p> Implements all Buttons for "Antibody Sequence Editor", content panel is
 * AntibodyEditor. Calls AntibodyFindDialog.
 * 
 * @author <b>Anne Mund</b>, quattro research GmbH
 * 
 */

public class AntibodyEditorDialog extends javax.swing.JDialog {

  /** Generated UID */
  private static final long serialVersionUID = -906998216945176364L;

  /** The Logger for this class */
  // private static final Logger logger = LoggerFactory
  // .getLogger(AntibodyEditorDialog.class);

  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JButton closeButton;

  private javax.swing.JButton clearButton;

  private javax.swing.JButton findButton;

  private JButton openButton;

  private JScrollPane scroller;

  private AntibodyEditor antibodyEditor;

// private MacromoleculeEditor editor;
  private JFrame parentFrame;

  private AntibodyFindDialog findDialog;

  // End of variables declaration//GEN-END:variables

  public AntibodyEditorDialog(JFrame parentFrame, boolean modal) {
    super(parentFrame, modal);

    this.parentFrame = parentFrame;
    customInit();
    initComponents();
  }

  protected JFrame getParentFrame() {
    return parentFrame;
  }

  private void customInit() {
    antibodyEditor = new AntibodyEditor();
    JPanel contentPanel = new JPanel();
    contentPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());
    contentPanel.setLayout(new java.awt.BorderLayout());
    contentPanel.add(antibodyEditor);
    scroller = new JScrollPane(contentPanel);
  }

  // <editor-fold defaultstate="collapsed"
  // desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {

    closeButton = new javax.swing.JButton();
    clearButton = new javax.swing.JButton();
    findButton = new javax.swing.JButton();
    openButton = new JButton();

    setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
    setTitle("Antibody Sequence Editor");
    setMinimumSize(new java.awt.Dimension(700, 500));

    closeButton.setText("Close");
    closeButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        closeButtonActionPerformed(evt);
      }
    });

    clearButton.setText("Clear All");
    clearButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        clearButtonActionPerformed(evt);
      }
    });

    findButton.setText("Find Domains");
    findButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        findButtonActionPerformed();
      }
    });

    openButton.setText("Open File");
    openButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        openButtonActionPerformed(evt);
      }
    });

    org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(
        getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(layout
        .createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
        .add(org.jdesktop.layout.GroupLayout.TRAILING,
            layout.createSequentialGroup()
                .add(openButton)
                .addContainerGap()
                .add(clearButton)
                .addPreferredGap(
                    org.jdesktop.layout.LayoutStyle.RELATED,
                    520, Short.MAX_VALUE).add(findButton)
                .addContainerGap().add(closeButton)
                .addContainerGap())
        .add(scroller, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
            710, Short.MAX_VALUE));
    layout.setVerticalGroup(layout
        .createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
        .add(org.jdesktop.layout.GroupLayout.TRAILING,
            layout.createSequentialGroup()
                .add(scroller,
                    org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
                    300, Short.MAX_VALUE)
                .addPreferredGap(
                    org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout
                    .createParallelGroup(
                        org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(closeButton).add(openButton)
                    .add(clearButton).add(findButton))
                .addContainerGap()));

    pack();
  }// </editor-fold>//GEN-END:initComponents

  private void closeButtonActionPerformed(java.awt.event.ActionEvent evt) {
    // GEN-FIRST:event_closeButtonActionPerformed
    setVisible(false);
  }// GEN-LAST:event_closeButtonActionPerformed

  private void clearButtonActionPerformed(java.awt.event.ActionEvent evt) {
    antibodyEditor.clearAll();
  }

  /**
   * Displays Dialog for domain-matching. <p> If there already exists a FindDialog, it is dismissed because the data
   * could have changed.
   * 
   */

  private void findButtonActionPerformed() {
    if (findDialog == null) {
      findDialog = new AntibodyFindDialog(this, false);
    }
    try {
      this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
      if (findDialog.processData(antibodyEditor.getNames(),
          antibodyEditor.getChains())) {
        findDialog.setLocationRelativeTo(parentFrame);
        findDialog.setVisible(true);
        this.setVisible(false);
      }
    } catch (FileNotFoundException e) {
      e.printStackTrace();
      JOptionPane.showMessageDialog(parentFrame, "An error occurred while initiating domain detection.", "Domain Detection failed", JOptionPane.ERROR_MESSAGE);
    } finally {
      this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }

  }

  public void openButtonActionPerformed(ActionEvent e) {
    // Handle open button action.
    Preferences userPref = PreferencesService.getInstance().getUserPrefs();
    JFileChooser chooser = new JFileChooser();
    chooser.setCurrentDirectory(new File(userPref.get(
        PreferencesService.LAST_FILE_FOLDER,
        System.getProperty("user.home"))));
    int returnVal = chooser.showOpenDialog(this);

    if (returnVal == JFileChooser.APPROVE_OPTION) {
      File file = chooser.getSelectedFile();
      List<String> foundNames = new ArrayList<String>();
      List<String> foundChains = new ArrayList<String>();
      List<String>[] result = null;
      try {
        result = antibodyEditor.readFile(file);

        foundNames.addAll(result[0]);
        foundChains.addAll(result[1]);
        antibodyEditor.setChains(foundNames, foundChains);
      } catch (Exception e1) {
        e1.printStackTrace();
      }
      userPref.put(PreferencesService.LAST_FILE_FOLDER, file.getParent());
    }

  }

}