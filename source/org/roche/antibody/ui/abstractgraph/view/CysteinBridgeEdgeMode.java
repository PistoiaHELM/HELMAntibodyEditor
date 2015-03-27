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

import javax.swing.JOptionPane;

import org.roche.antibody.model.antibody.CysteinConnection;
import org.roche.antibody.model.antibody.Domain;
import org.roche.antibody.services.AbConst;
import org.roche.antibody.services.DomainService;
import org.roche.antibody.ui.abstractgraph.CysBridgeEdgeRealizer;
import org.roche.antibody.ui.components.CysBridgeDialog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import y.base.DataProvider;
import y.base.Edge;
import y.base.EdgeMap;
import y.base.Node;
import y.view.CreateEdgeMode;
import y.view.EdgeRealizer;
import y.view.Graph2D;

/**
 * {@code CysteinBridgeEdgeMode}
 * 
 * @author <b>Stefan Klostermann:</b> Stefan DOT Klostermann AT roche DOT com, Roche Pharma Research and Early
 *         Development - Informatics, Roche Innovation Center Penzberg
 * @author <b>Stefan Zilch:</b> stefan_dieter DOT zilch AT contractors DOT roche DOT com
 * 
 * @version $Id: CysteinBridgeEdgeMode.java 15173 2015-03-05 16:18:13Z schirmb $
 */
public class CysteinBridgeEdgeMode extends CreateEdgeMode {

  /** The Logger for this class */
  private static final Logger LOG = LoggerFactory.getLogger(CysteinBridgeEdgeMode.class);

  @Override
  protected Edge createEdge(Graph2D graph, Node startNode, Node targetNode, EdgeRealizer realizer) {
    DataProvider nodeToDomain = graph.getDataProvider(AbConst.NODE_TO_SEQUENCE_KEY);
    CysteinConnection createdBridge = null;
    Domain source = (Domain) nodeToDomain.get(startNode);
    Domain target = (Domain) nodeToDomain.get(targetNode);
    // identify and handle IntraBridges
    if (isIntraEdge(source, target)) {
      if (isIntraCysConnectionAllowd(source)) {
        LOG.debug("IntraCysteinBridgeCreation identified. Operation is allowed!");
        createdBridge = createCysteinBridge(source, target);
      }
      else {
        return null;
      }
    } else if (isBridgeAllowd(source, target)) {
      LOG.debug("CysteinBridgeCeation between two different domains identified. Operation is allowed!");
      createdBridge = createCysteinBridge(source, target);
    }
    else {
      LOG.debug("No connection between domain allowed... removing edge!");
      return null;
    }

    if (createdBridge != null) {
      graph.firePreEvent();
      createdBridge.getSource().getAntibody().addConnection(createdBridge);
      EdgeMap map = (EdgeMap) graph.getDataProvider(AbConst.EDGE_TO_CONNECTION_KEY);
      Edge e = super.createEdge(graph, startNode, targetNode, new CysBridgeEdgeRealizer());
      map.set(e, createdBridge);
      graph.firePostEvent();
      return e;
    }
    return null;
  }

  /**
   * @param source {@link Domain}
   * @param target {@link Domain}
   * @return true if source == target
   */
  private boolean isIntraEdge(Domain source, Domain target) {
    if (source == target) {
      return true;
    }
    return false;
  }

  /**
   * Allowd, if it is configured in Preferences and the domain has more than one free cystein.
   * 
   * @param domain {@link Domain}
   * @return true if an IntraCysteinBridge is allowd on this domain
   */
  private boolean isIntraCysConnectionAllowd(Domain domain) {
    return domain.getFreeCysteinPositions().size() > 1;
  }

  /**
   * 
   * A bridge is allowd, if each domain has at least one free cystein and the corresponding peptides are NOT the same.
   * 
   * @param source {@link Domain}
   * @param target {@link Domain}
   * @return true if a cysteinbridge is allowed between both domains
   */
  private boolean isBridgeAllowd(Domain source, Domain target) {
    if (source.getFreeCysteinPositions().isEmpty() || target.getFreeCysteinPositions().isEmpty()) {
      JOptionPane.showMessageDialog(null, "No free cysteins in at least one of the domains!", "Warning", JOptionPane.WARNING_MESSAGE);
      return false;
    }
    return true;
  }

  private CysteinConnection createCysteinBridge(Domain source, Domain target) {
    if (DomainService.getInstance().isAutoconnectable(source, target)) {
      return DomainService.getInstance().autoconnect(source, target);
    }
    CysBridgeDialog dialog = new CysBridgeDialog(source, target);
    if (dialog.showDialog() == CysBridgeDialog.CMD_SUCCESS) {
      return dialog.getCreatedCysBridge();
    }
    return null;
  }

  @Override
  protected boolean acceptTargetNode(Node target, double x, double y) {
    DataProvider sequenceMap = target.getGraph().getDataProvider(AbConst.NODE_TO_SEQUENCE_KEY);
    if (sequenceMap.get(target) instanceof Domain) {
      return true;
    }
    return false;
  }

  @Override
  protected boolean acceptSourceNode(Node source, double x, double y) {
    DataProvider sequenceMap = source.getGraph().getDataProvider(AbConst.NODE_TO_SEQUENCE_KEY);
    if (sequenceMap.get(source) instanceof Domain) {
      return true;
    }
    return false;
  }

}
