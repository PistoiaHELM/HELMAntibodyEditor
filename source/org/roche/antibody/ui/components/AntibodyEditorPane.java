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
package org.roche.antibody.ui.components;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dialog;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.geom.Point2D;
import java.util.Map;

import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import org.roche.antibody.model.antibody.Antibody;
import org.roche.antibody.model.antibody.Connection;
import org.roche.antibody.model.antibody.Domain;
import org.roche.antibody.model.antibody.Sequence;
import org.roche.antibody.services.AbConst;
import org.roche.antibody.services.AbstractGraphLayoutService;
import org.roche.antibody.services.AbstractGraphService;
import org.roche.antibody.services.antibody.AntibodyService;
import org.roche.antibody.services.graphsynchronizer.GraphSynchronizer;
import org.roche.antibody.ui.abstractgraph.DomainNodeRealizer;
import org.roche.antibody.ui.abstractgraph.view.AntibodyEditMode;
import org.roche.antibody.ui.abstractgraph.view.GraphDeleteAction;
import org.roche.antibody.ui.propertytyble.DomainTableCellRenderer;
import org.roche.antibody.ui.propertytyble.DomainTableModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import y.base.DataProvider;
import y.base.Edge;
import y.base.GraphEvent;
import y.base.GraphListener;
import y.base.Node;
import y.layout.CanonicMultiStageLayouter;
import y.view.BridgeCalculator;
import y.view.DefaultGraph2DRenderer;
import y.view.EditMode;
import y.view.Graph2D;
import y.view.Graph2DView;
import y.view.Graph2DViewActions;
import y.view.Graph2DViewMouseWheelZoomListener;
import y.view.NodeRealizer;

import com.quattroresearch.antibody.paratopedetection.ParatopeDetection;
import com.quattroresearch.antibody.paratopedetection.ParatopeDetectionException;

/**
 * JDialog class for antibody view editor. The data from sequences recognition would then be passed here and saved into
 * an Antibody instance (one of this instance's fields. This dialog instance should be released (including the resources
 * thereof) whenever it is closed. Some essential elements of this class include : <br> 1.) Swing elements include
 * panels and for each two important views and data managements entities : Schematic View (Graph2DViewDialog) and Vector
 * View (Graph2DViewDialog). <br> 2.) Data representation element such as HELM Notation view and Domain properties view
 * 
 * 
 * 
 * @author <b>Stefan Klostermann:</b> Stefan DOT Klostermann AT roche DOT com, Roche Pharma Research and Early
 *         Development - Informatics, Roche Innovation Center Penzberg
 * @author raharjap
 * 
 */
public class AntibodyEditorPane extends JPanel implements GraphListener {

  private static final Logger LOG = LoggerFactory.getLogger(AntibodyEditorPane.class);

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  private static final Border DEFAULT_BORDER = new CompoundBorder(
      new EtchedBorder(EtchedBorder.LOWERED, null, null), null);

  protected Graph2DView abstractAntibodyView;

  protected Graph2DView anotherView;

  private JFrame parentFrame;

  private Antibody antibody;

  private EditMode editMode;

  private JTable propertyTable;

  private JSplitPane pnlEast;

  private JPanel graphicsPane;

  protected Map<Node, Point2D> originalLayout;

  private Graph2D abstractGraph;

  private CanonicMultiStageLayouter abstractGraphLayouter;

  private DomainTableModel antibodyPropertyModel;

  private GraphSynchronizer graphSyncer;

  private boolean isBackToDomainEditorEnabled;

  /**
   * Constructor
   * 
   * @param parent caller's top-level component
   * @param modal modality of callee (this instance) passed to superclass' constructor
   * @param peptideList peptideList passed over by caller's routine
   */
  public AntibodyEditorPane(JFrame parentFrame) {
    this.parentFrame = parentFrame;
    this.setLayout(new BorderLayout());
    initComponents();

// this.sequenceEditorExtensionMenu = new SyncBackMenuExtension(mainEditor, this);
// CancelABEditingMenuExtension extension = new CancelABEditingMenuExtension(mainEditor, sequenceEditorExtensionMenu);

// mainEditor.getPopup().add(this.sequenceEditorExtensionMenu);
// mainEditor.getPopup().add(extension);

  }

  /**
   * Returns whether the back to domain editor action is allowed or should be greyed out.
   * 
   * @return true if enabled else false
   */
  public boolean getIsBackToDomainEditorEnabled() {
    return isBackToDomainEditorEnabled;
  }

  /**
   * Sets the flag for enabling/disabling the back to domain recognition action.
   * 
   * @param enable true or false
   */
  public void setIsBackToDomainEditorEnabled(boolean enable) {
    isBackToDomainEditorEnabled = enable;
  }

  /**
   * Updates the GraphLayout and also the HELMTextfield
   */
  public void updateGraphLayout() {
    if (abstractGraph != null) {
      AbstractGraphLayoutService.layout(abstractGraph, abstractGraphLayouter);
      abstractAntibodyView.fitContent();
      abstractGraph.updateViews();

      AntibodyEditorAccess.getInstance().setAntibodyEditorPane(this);

      // Re-Detect paratopes(Fragment variables) for correct coloring
      try {
        new ParatopeDetection(AntibodyEditorAccess.getInstance().getAntibodyEditorPane().getAntibody().getPeptides());
      } catch (ParatopeDetectionException e) {
        JOptionPane.showMessageDialog(this, "Paratope Detection failed with message '" + e.getMessage()
            + "'. Please contact your administrator.");
      }
      for (Node node : abstractGraph.getNodeArray()) {
        NodeRealizer realizer = abstractGraph.getRealizer(node);
        if (realizer instanceof DomainNodeRealizer) {
          ((DomainNodeRealizer) abstractGraph.getRealizer(node)).initFromMap();
        }
      }

    }
  }

  /**
   * 
   */
  public void updateLayoutHints() {
    Graph2D graph = getAbstractGraph();
    if (abstractGraph == null) {
      return;
    } else {
      AbstractGraphLayoutService.updateLayoutHints(graph);
    }
  }

  public void setModel(Antibody ab) {
    if (ab == null) {
      this.abstractGraph.clear();
      this.antibody = null;
      return;
    }

    parentFrame.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
    this.antibody = ab;
    this.graphSyncer = new GraphSynchronizer(this, ab);
    createGraph();
// mainEditor.reset();
    parentFrame.setCursor(Cursor.getDefaultCursor());
    updateGraphLayout();
    isBackToDomainEditorEnabled = false;
  }

  /**
   * Same as {@link #createAbstractGraph(Antibody)}
   * 
   * @param peptideList
   */
  private void createGraph() {
    createAbstractGraph(antibody);
  }

  private void createAbstractGraph(Antibody ab) {
    abstractGraph = AbstractGraphService.getGraph(ab);
    abstractAntibodyView.setGraph2D(abstractGraph);
    abstractAntibodyView.getCanvasComponent().addMouseWheelListener(new Graph2DViewMouseWheelZoomListener());
    abstractGraph.addGraphListener(this);
  }

  /**
   * initates swing component
   */
  private void initComponents() {
    abstractGraphLayouter = AbstractGraphLayoutService.createLayouter();
    this.abstractAntibodyView = new Graph2DView();
    this.abstractAntibodyView.setFitContentOnResize(true);
    editMode = new AntibodyEditMode(this);
    abstractAntibodyView.addViewMode(editMode);

    // enable bridges for PolyLineEdgeRealizer
    BridgeCalculator bridgeCalculator = new BridgeCalculator();
    bridgeCalculator.setCrossingMode(BridgeCalculator.CROSSING_MODE_HORIZONTAL_CROSSES_VERTICAL);
    ((DefaultGraph2DRenderer) abstractAntibodyView.getGraph2DRenderer()).setBridgeCalculator(bridgeCalculator);
    configureKeyboardActions();

    this.addComponentListener(new ComponentAdapter() {
      @Override
      public void componentShown(ComponentEvent e) {
        super.componentShown(e);
        LOG.debug("Call componentShown of: {}", this.getClass().getName());
        updateGraphLayout();
      }
    });
    this.setLayout(new BorderLayout());

    // --- START Graphical Representation of Antybody (Center Panel)
    JSplitPane pnlSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
    pnlSplit.setOneTouchExpandable(true);
    this.add(pnlSplit, BorderLayout.CENTER);
    pnlSplit.setLeftComponent(abstractAntibodyView);
    pnlSplit.setResizeWeight(1);
    // --- START Property Table, HELM Notation etc. (Right Panel)
    pnlEast = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
    pnlEast.setBorder(DEFAULT_BORDER);
    pnlEast.setResizeWeight(0.65);
    antibodyPropertyModel = new DomainTableModel();
    antibodyPropertyModel.addTableModelListener(new TableModelListener() {
      @Override
      public void tableChanged(TableModelEvent e) {
        abstractGraph.updateViews();
      }
    });
    propertyTable = new JTable(antibodyPropertyModel);
    propertyTable.setDefaultRenderer(Object.class, new DomainTableCellRenderer());

    JScrollPane spnlPropertyTable = new JScrollPane(propertyTable);
    pnlEast.setTopComponent(spnlPropertyTable);
    pnlSplit.setRightComponent(pnlEast);

    // graphical preview
    graphicsPane = new JPanel(new BorderLayout());
    anotherView = new Graph2DView();
    graphicsPane.add(anotherView, BorderLayout.CENTER);

  }

  public Graph2DView getAbstractAntibodyView() {
    return this.abstractAntibodyView;
  }

  public void updatePane(Domain domain) {
    antibodyPropertyModel.setModel(domain);
  }

  private void configureKeyboardActions() {
    Graph2DViewActions keyboardActions = new Graph2DViewActions(
        abstractAntibodyView);
    ActionMap defaultActions = keyboardActions.createActionMap();
    InputMap defaultInputMap = keyboardActions
        .createDefaultInputMap(defaultActions);
    defaultInputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0),
        Graph2DViewActions.DELETE_SELECTION);
    defaultActions.put(Graph2DViewActions.DELETE_SELECTION,
        new GraphDeleteAction());
    abstractAntibodyView.getCanvasComponent().setActionMap(defaultActions);
    abstractAntibodyView.getCanvasComponent().setInputMap(
        JComponent.WHEN_FOCUSED, defaultInputMap);
  }

  @Override
  public void onGraphEvent(GraphEvent e) {
    // we catch the delete event of an edge. and remove the CysteinBridge
    // from the Model.
    if (e.getType() == GraphEvent.POST_EDGE_REMOVAL) {
      Edge removedEdge = (Edge) e.getData();
      DataProvider edgeToBridge = e.getGraph().getDataProvider(AbConst.EDGE_TO_CONNECTION_KEY);
      Connection bridgeToDelete = (Connection) edgeToBridge.get(removedEdge);
      // can be null, if Edge is a Backbone. This is a logical connection.
      // but it is not contained in the model.
      if (bridgeToDelete != null) {
        antibody.removeConnection(bridgeToDelete);
      }
      updateGraphLayout();
    }

    if (e.getType() == GraphEvent.POST_NODE_REMOVAL) {
      Node removedNode = (Node) e.getData();
      DataProvider nodeToSequence = e.getGraph().getDataProvider(
          AbConst.NODE_TO_SEQUENCE_KEY);
      Sequence seqToDelete = (Sequence) nodeToSequence.get(removedNode);
      AntibodyService.getInstance().removeSequence(seqToDelete, antibody);
      updateGraphLayout();
    }

    if (e.getType() == GraphEvent.EDGE_CREATION
        || e.getType() == GraphEvent.NODE_CREATION) {
      updateGraphLayout();
    }
  }

  public Graph2D getAbstractGraph() {
    return this.abstractGraph;
  }

  public GraphSynchronizer getGraphSyncer() {
    return this.graphSyncer;
  }

  /**
   * @return the current antibody model
   */
  public Antibody getAntibody() {
    return this.antibody;
  }

}
