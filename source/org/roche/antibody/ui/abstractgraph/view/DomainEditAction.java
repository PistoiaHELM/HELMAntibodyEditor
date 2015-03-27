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

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

import org.roche.antibody.model.antibody.Domain;
import org.roche.antibody.ui.components.AntibodyEditorPane;

/**
 * {@code DomainEditAction}: action for editing the selected domain.
 * 
 * @author <b>Stefan Klostermann:</b> Stefan DOT Klostermann AT roche DOT com, Roche Pharma Research and Early
 *         Development - Informatics, Roche Innovation Center Penzberg
 * @author <b>Stefan Zilch:</b> stefan_dieter DOT zilch AT contractors DOT roche DOT com
 * 
 * @version $Id: DomainEditAction.java 15173 2015-03-05 16:18:13Z schirmb $
 */
public class DomainEditAction extends AbstractAction {

  /** */
  private static final long serialVersionUID = 1L;

  private AntibodyEditorPane editor;

  private Domain domain;

  public DomainEditAction(AntibodyEditorPane editor, Domain domain) {
    super("Edit Domain");
    this.domain = domain;
    this.editor = editor;
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    try {
      editor.getGraphSyncer().sendToMacroMolecularEditor(domain);
    } catch (Exception e1) {
      e1.printStackTrace();
      JOptionPane.showMessageDialog(editor, "Syncronization failed: "
          + e1.getMessage(), "Syncronization failed",
          JOptionPane.ERROR_MESSAGE);
    }
// editor.getSequenceEditorExtensionMenu().setEnabled(true);

    editor.updateGraphLayout();
  }

}
