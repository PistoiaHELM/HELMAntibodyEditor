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

import org.roche.antibody.services.AbConst;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import y.base.DataProvider;
import y.base.Edge;
import y.base.Node;
import y.view.Graph2D;
import y.view.Graph2DViewActions;

/**
 * {@code GraphDeleteAction}
 * 
 * This is the specific DeleteAction applied for the Antibody graph. We can only delete CysteinBridges. All other
 * elements are not allowed to delete. This action is required for Keyboard Usage
 * 
 * @author <b>Stefan Klostermann:</b> Stefan DOT Klostermann AT roche DOT com, Roche Pharma Research and Early
 *         Development - Informatics, Roche Innovation Center Penzberg
 * @author <b>Stefan Zilch:</b> stefan_dieter DOT zilch AT contractors DOT roche DOT com
 * 
 * @version $Id: GraphDeleteAction.java 15173 2015-03-05 16:18:13Z schirmb $
 */
public class GraphDeleteAction extends Graph2DViewActions.DeleteSelectionAction {

  /** The Logger for this class */
  private static final Logger LOG = LoggerFactory.getLogger(GraphDeleteAction.class);

  /** */
  private static final long serialVersionUID = 1366134263005029056L;

  public GraphDeleteAction() {
    super();
    setDeletionMask(TYPE_EDGE | TYPE_SIMPLE_NODE);
  }

  @Override
  protected boolean acceptEdge(Graph2D graph, Edge edge) {
    // only delete edges, which have a model attached (cysteinbridges).
    boolean readyToDelete = super.acceptEdge(graph, edge);
    DataProvider map = graph.getDataProvider(AbConst.EDGE_TO_CONNECTION_KEY);
    if (map.get(edge) != null) {
      return readyToDelete & true;
    }
    return false;
  }

  @Override
  protected boolean acceptNode(Graph2D graph, Node node) {
    boolean readyToDelete = super.acceptNode(graph, node);
    LOG.trace("Node selected: {} -> {} readyToDelete: {}", node, graph.isSelected(node), readyToDelete);
    DataProvider deletable = graph.getDataProvider(AbConst.NODE_DELETABLE_KEY);
    if (deletable.getBool(node)) {
      return readyToDelete & true;
    }
    return false;
  }

}
