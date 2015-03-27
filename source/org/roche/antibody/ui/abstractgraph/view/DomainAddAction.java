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
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

import org.roche.antibody.model.antibody.Domain;
import org.roche.antibody.services.UIService;
import org.roche.antibody.ui.components.AntibodyEditorPane;

/**
 * {@code DomainAddAction}: Action for adding a domain from library to selected domain.
 * 
 * @author <b>Stefan Klostermann:</b> Stefan DOT Klostermann AT roche DOT com, Roche Pharma Research and Early
 *         Development - Informatics, Roche Innovation Center Penzberg
 * @author <b>Marco Lanig:</b> lanig AT quattro-research DOT com, quattro research GmbH
 * 
 */
public class DomainAddAction extends AbstractAction {

  /** Generated UID */
  private static final long serialVersionUID = 6320593579348992920L;

  private AntibodyEditorPane editor;

  private Domain domain;

  private String selectedSequence;

  public DomainAddAction(AntibodyEditorPane editor, Domain domain) {
    super("Add Domain");
    this.domain = domain;
    this.editor = editor;
  }

  public void setSelectedSequence(String selectedSequence) {
    this.selectedSequence = selectedSequence;
  }

  @Override
  public void actionPerformed(ActionEvent e) {

    DomainAddDialog dialog =
        new DomainAddDialog(UIService.getInstance().getMainFrame(), Dialog.ModalityType.APPLICATION_MODAL, this);
    dialog.setLocationRelativeTo(UIService.getInstance().getMainFrame());
    dialog.setVisible(true);

    if (selectedSequence != null) {
      try {
        editor.getGraphSyncer().sendToMacroMolecularEditor(domain,
            selectedSequence);
      } catch (Exception e1) {
        e1.printStackTrace();
        JOptionPane.showMessageDialog(editor, "Syncronization failed: "
            + e1.getMessage(), "Syncronization failed",
            JOptionPane.ERROR_MESSAGE);
      }
      editor.updateGraphLayout();

    }
  }

}
