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

import org.roche.antibody.model.antibody.GeneralConnection;
import org.roche.antibody.services.AbConst;

import y.base.DataProvider;
import y.view.GenericEdgeRealizer;

/**
 * Extended realizer class representing the edges connecting all domains within antibody chain (non-cystein connection)
 * 
 * @author <b>Stefan Klostermann:</b> Stefan DOT Klostermann AT roche DOT com, Roche Pharma Research and Early
 *         Development - Informatics, Roche Innovation Center Penzberg
 * @author raharjap
 * 
 * @version $Id: GeneralConnectionEdgeRealizer.java 15173 2015-03-05 16:18:13Z schirmb $
 */
public class GeneralConnectionEdgeRealizer extends GenericEdgeRealizer implements AbstractGraphElementInitializer {
	
	/**
	 * Constructor
	 */
	public GeneralConnectionEdgeRealizer() {
    initFromMap();
	}

  @Override
  public void paint(Graphics2D gfx) {
    initFromMap();
    super.paint(gfx);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void initFromMap() {
    try {
      setLineColor(Color.BLUE);

      DataProvider cysBridgeMap = getEdge().getGraph().getDataProvider(AbConst.EDGE_TO_CONNECTION_KEY);
      GeneralConnection bridge = (GeneralConnection) cysBridgeMap.get(getEdge());
      super.setLabelText(bridge.getSourcePosition() + ":" + bridge.getSourceRest() + "-" + bridge.getTargetPosition()
          + ":" + bridge.getTargetRest());
      getLabel().setFontSize(8);
      
    } catch (Exception e) {
      // Not yet ready for initialization
    }
  }
}
