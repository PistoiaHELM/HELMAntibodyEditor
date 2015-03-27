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

import java.awt.Cursor;
import java.awt.Dialog.ModalityType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import org.roche.antibody.model.antibody.Domain;
import org.roche.antibody.model.antibody.Sequence;
import org.roche.antibody.services.AbConst;
import org.roche.antibody.services.AbstractGraphService;
import org.roche.antibody.services.UIService;
import org.roche.antibody.services.antibody.AntibodyService;
import org.roche.antibody.ui.abstractgraph.DomainEdgeRealizer;
import org.roche.antibody.ui.abstractgraph.GeneralConnectionEdgeRealizer;
import org.roche.antibody.ui.components.AntibodyEditorAccess;
import org.roche.antibody.ui.components.AntibodyEditorPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import y.base.Edge;
import y.base.Node;
import y.view.GenericEdgeRealizer;
import y.view.PopupMode;

import com.quattroresearch.antibody.plugin.PluginLoader;

/**
 * {@code NodePopupMode}
 * 
 * @author <b>Stefan Klostermann:</b> Stefan DOT Klostermann AT roche DOT com, Roche Pharma Research and Early
 *         Development - Informatics, Roche Innovation Center Penzberg
 * @author <b>Stefan Zilch:</b> stefan_dieter DOT zilch AT contractors DOT roche DOT com
 * @author <b>Marco Erdmann:</b> erdmann AT quattro-research DOT com, quattro research GmbH
 * 
 * @version $Id: GraphPopupMode.java 15302 2015-03-16 08:45:32Z schirmb $
 */
public class GraphPopupMode extends PopupMode {

  /** The Logger for this class */
  private static final Logger LOG = LoggerFactory.getLogger(GraphPopupMode.class);

  private AntibodyEditorPane editor;

  public GraphPopupMode(AntibodyEditorPane editor) {
    this.editor = editor;
  }

  @Override
  public JPopupMenu getNodePopup(Node node) {

    boolean isEditable = false;
    if (hasEdgeByType(node, new DomainEdgeRealizer())) {
      // domain is editable, when it is connected to a real peptide
      isEditable = true;
    } else if (!isEditable) {
      // when not connected to real peptide: only editable when no general connection edge connected
      isEditable = !hasEdgeByType(node, new GeneralConnectionEdgeRealizer());
    }

    Sequence seq = (Sequence) node.getGraph().getDataProvider(AbConst.NODE_TO_SEQUENCE_KEY).get(node);
    JPopupMenu popup = null;
    if (seq instanceof Domain) {
      popup = new JPopupMenu();
      // ML 2015-02-11: sending additional sequences to HELMEditor currently not implemented
      // popup.add(new DomainAddAction(editor, (Domain) seq));

      DomainEditAction editAction = new DomainEditAction(editor, (Domain) seq);
      // only editable when not part of a 'chemical' addend
      editAction.setEnabled(isEditable);
      popup.add(editAction);

      popup.add(new DomainAnnotationAction(editor, (Domain) seq));

      try {
        UIService.getInstance().getMainFrame().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        PluginLoader.getInstance().loadDomainPopupPlugins(popup, (Domain) seq);
      } finally {
        UIService.getInstance().getMainFrame().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
      }
    }

    return popup;
  }

  private boolean hasEdgeByType(Node node, GenericEdgeRealizer edgeType) {
    Edge[] allEdges = node.getGraph().getEdgeArray();
    for (Edge edge : allEdges) {
      if ((edge.source() != edge.target()) && (edge.source() == node || edge.target() == node)) {
        if (editor.getAbstractGraph().getRealizer(edge).getClass().equals(edgeType.getClass())) {
          return true;
        }
      }
    }

    return false;
  }

  @Override
  public JPopupMenu getPaperPopup(double x, double y) {
    JPopupMenu graphContextMenu = new JPopupMenu();
    JMenuItem item = new JMenuItem("Back to Domain Recognition");
    if (UIService.getInstance().getAntibodyFindDialog() == null || !editor.getIsBackToDomainEditorEnabled()) {
      item.setEnabled(false);
    }
    item.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {
        UIService.getInstance().getAntibodyFindDialog().setVisible(true);
      }
    });
    graphContextMenu.add(item);

    item = new JMenuItem("Reset Layout");
    item.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {
        editor.updateGraphLayout();
      }
    });
    graphContextMenu.add(item);
    item = new JMenuItem("Reset Cystein Bridges");
    item.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {
        AbstractGraphService.getInstance().resetCysteinBridges(editor.getAbstractGraph());
        editor.getAbstractGraph().updateViews();
      }
    });
    graphContextMenu.add(item);
    item = new JMenuItem("Reset Cystein Bridges + Layout");
    item.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {
        AbstractGraphService.getInstance().resetCysteinBridges(editor.getAbstractGraph());
        editor.updateGraphLayout();
        editor.getAbstractGraph().updateViews();
      }
    });
    graphContextMenu.add(item);
    item = new JMenuItem("Show HELM");
    item.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {

        String helm =
            AntibodyService.getInstance().toHELMString(AntibodyEditorAccess.getInstance().getAntibodyEditorPane().getAntibody());
        ShowHELMDialog shdia =
            new ShowHELMDialog(UIService.getInstance().getMainFrame(), ModalityType.APPLICATION_MODAL, helm);
        shdia.setVisible(true);
      }
    });
    graphContextMenu.add(item);

    return graphContextMenu;
  }
}
