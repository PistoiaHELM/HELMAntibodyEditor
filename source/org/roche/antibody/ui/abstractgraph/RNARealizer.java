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
package org.roche.antibody.ui.abstractgraph;

import java.awt.Color;
import java.awt.Graphics2D;

import org.apache.commons.lang.StringUtils;
import org.roche.antibody.model.antibody.RNA;
import org.roche.antibody.services.AbConst;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import y.view.ShapeNodeRealizer;

/**
 * {@code RNARealizer}
 * 
 * Realizer for {@link RNA} Elements
 * 
 * @author <b>Stefan Klostermann:</b> Stefan DOT Klostermann AT roche DOT com, Roche Pharma Research and Early
 *         Development - Informatics, Roche Innovation Center Penzberg
 * @author <b>Stefan Zilch:</b> stefan_dieter DOT zilch AT contractors DOT roche DOT com
 * 
 * @version $Id: RNARealizer.java 15173 2015-03-05 16:18:13Z schirmb $
 */
public class RNARealizer extends ShapeNodeRealizer implements AbstractGraphElementInitializer {

  /** The Logger for this class */
  private static final Logger LOG = LoggerFactory.getLogger(RNARealizer.class);

  private static final int DEFAULT_HEIGHT = 20;

  private static final int DEFAULT_WIDTH = 80;

  boolean initialized = false;

  public RNARealizer() {
    super();
    initFromMap();
  }

  public void initFromMap() {
    try {
      setShapeType(PARALLELOGRAM);
      setFillColor(Color.GREEN);
      setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);

      RNA mol = (RNA) getNode().getGraph()
          .getDataProvider(AbConst.NODE_TO_SEQUENCE_KEY)
          .get(getNode());
      if (mol != null) {
        // JF 2014-06-16: Changed label to number of nucleid acids + bp (base pair)
        int countNucleidAcids = StringUtils.countMatches(mol.getName(), "(");
        StringBuilder sb = new StringBuilder();
        sb.append("NN (");
        sb.append(countNucleidAcids);
        sb.append("bp)");
        setLabelText(sb.toString());
        initialized = true;
      }

    } catch (NullPointerException e) {
      // Not yet ready for initialization
    }
  }

  @Override
  protected void paintNode(Graphics2D graph) {
    if (!initialized) {
      initFromMap();
    }
    super.paintNode(graph);
  }

}
