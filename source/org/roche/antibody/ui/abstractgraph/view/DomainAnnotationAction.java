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
import java.io.FileNotFoundException;
import java.util.ArrayList;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

import org.roche.antibody.model.antibody.Domain;
import org.roche.antibody.services.AbstractGraphService;
import org.roche.antibody.services.PreferencesService;
import org.roche.antibody.ui.abstractgraph.DomainNodeRealizer;
import org.roche.antibody.ui.components.AntibodyEditorPane;

import y.base.Node;

import com.quattroresearch.antibody.DomainDetectionStandalone;

/**
 * {@code DomainAnnotationAction}: action for re-blasting the selected domain.
 * 
 * @author <b>Stefan Klostermann:</b> Stefan DOT Klostermann AT roche DOT com, Roche Pharma Research and Early
 *         Development - Informatics, Roche Innovation Center Penzberg
 * @author <b>Marco Lanig:</b> lanig AT quattro-research DOT com, quattro research GmbH
 */
public class DomainAnnotationAction extends AbstractAction {

  /** Generated UID */
  private static final long serialVersionUID = -8894318297121202684L;

  private AntibodyEditorPane _editor;

  private Domain _domain;

  public DomainAnnotationAction(AntibodyEditorPane editor, Domain domain) {
    super("Annotate Domain");
    this._domain = domain;
    this._editor = editor;
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    try {
      annotateDomain(_editor, _domain);
    } catch (FileNotFoundException e1) {
      JOptionPane.showMessageDialog(_editor.getParent(), "Please check the filepath in the Antibody Editor Settings", "Domain definition file not found", JOptionPane.ERROR_MESSAGE);

    }
  }

  /**
   * Annotates a given domain in given editor. Because it is a static method, you may use it without to instantiate an
   * Action.
   * 
   * @param editor antibody editor
   * @param domain domain to annotate
   * @throws FileNotFoundException
   */
  public static void annotateDomain(AntibodyEditorPane editor, Domain domain) throws FileNotFoundException {
    ArrayList<String> name = new ArrayList<String>(1);
    ArrayList<String> sequence = new ArrayList<String>(1);
    name.add(domain.getName());
    sequence.add(domain.getSequence());

    // ML 2014-03-26: Switched to domain reannotation instead of domain
    // switching
    DomainDetectionStandalone domainDetection = new DomainDetectionStandalone(
        name, sequence, PreferencesService.getInstance()/* , null */);
    try {
      domainDetection.makeBlastDatabase();
    } catch (FileNotFoundException e) {
      JOptionPane.showMessageDialog(editor, "Please check the filepath in the Antibody Editor Settings", "Domain definition file not found", JOptionPane.ERROR_MESSAGE);
      return;
    } catch (Exception e) {
      JOptionPane.showMessageDialog(editor, "Creating blast database failed: " + e.getMessage(), "Blast failed", JOptionPane.ERROR_MESSAGE);
      return;
    }
    try {
      domainDetection.annotateDomain(domain, domain.getPeptide().getSequence());
    } catch (Exception e) {
      JOptionPane.showMessageDialog(editor, "Annotation of domain '" + domain.getName() + "' failed: " + e.getMessage(), "Blast failed", JOptionPane.ERROR_MESSAGE);
      return;
    }

    // ML 2014-03-14: relabel the node and repaint the graph
    Node changedNode = AbstractGraphService
        .findNodeBySequence(editor.getAbstractGraph(), domain);
    DomainNodeRealizer realizer = (DomainNodeRealizer) editor
        .getAbstractGraph().getRealizer(changedNode);
    realizer.initFromMap(domain);

    realizer.repaint();
  }
}
