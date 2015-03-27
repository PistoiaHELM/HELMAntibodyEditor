/*******************************************************************************
 * Copyright C 2015, Roche pREDi (Roche Innovation Center Penzberg)
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
package org.roche.antibody.ui.abstractgraph.view;

import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;

/**
 * 
 * {@code DomainAddDialog}: Dialog for showing a HELMNotation
 * 
 * @author <b>Stefan Klostermann:</b> Stefan DOT Klostermann AT roche DOT com, Roche Pharma Research and Early
 *         Development - Informatics, Roche Innovation Center Penzberg
 * @author <b>Marco Lanig:</b> lanig AT quattro-research DOT com, quattro research GmbH
 * 
 * @version $Id: ShowHELMDialog.java 15173 2015-03-05 16:18:13Z schirmb $
 */
public class ShowHELMDialog extends JDialog implements ActionListener {

  JTable domainTable;

  private JButton btnOK;

  private JTextArea taHELM;

  /** Generated UID */
  private static final long serialVersionUID = 6102405755459856212L;

  public ShowHELMDialog(JFrame owner, Dialog.ModalityType isModal, String helmCode) {
    super(owner, isModal);

    setLocationRelativeTo(null);

    initComponents();

    taHELM.setText(helmCode);
  }

  /**
   * UI Initialization
   * 
   * @throws FileNotFoundException
   */
  private void initComponents() {
    setResizable(false);
    setTitle("HELM code view");
    setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    getContentPane().setLayout(
        new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
    getContentPane().add(buildViewPane());
    getContentPane().add(buildButtonPanel());
    pack();
  }

  private JPanel buildButtonPanel() {
    JPanel buttonPanel = new JPanel(new FlowLayout());
    btnOK = new JButton("OK");
    btnOK.addActionListener(this);

    buttonPanel.add(btnOK);

    return buttonPanel;
  }

  private JScrollPane buildViewPane() {
    taHELM = new JTextArea();
    taHELM.setLineWrap(true);

    JScrollPane spHELM =
        new JScrollPane(taHELM, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    spHELM.setPreferredSize(new Dimension(600, 300));

    return spHELM;
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    if (e.getSource() == btnOK) {
      this.dispose();
    }

  }
}
