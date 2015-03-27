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
import org.roche.antibody.model.antibody.ChainType;
import org.roche.antibody.model.antibody.Domain;
import org.roche.antibody.model.antibody.DomainType;
import org.roche.antibody.model.antibody.HumannessType;
import org.roche.antibody.services.AbConst;

import y.view.LineType;
import y.view.ShapeNodeRealizer;

/**
 * {@code DomainNodeRealizer} Realizer for a {@link Domain}
 * 
 * @author <b>Stefan Klostermann:</b> Stefan DOT Klostermann AT roche DOT com, Roche Pharma Research and Early
 *         Development - Informatics, Roche Innovation Center Penzberg
 * @author <b>Stefan Zilch:</b> stefan_dieter DOT zilch AT contractors DOT roche DOT com
 * @author <b>Marco Lanig:</b> lanig AT quattro-research DOT com, quattro research GmbH
 * 
 * @version $Id: DomainNodeRealizer.java 15173 2015-03-05 16:18:13Z schirmb $
 */
public class DomainNodeRealizer extends ShapeNodeRealizer implements AbstractGraphElementInitializer {

  private static final int DEFAULT_HEIGHT = 40;

  private static final int DEFAULT_WIDTH = 160;

  private static final int HINGE_HEIGHT = 30;

  private static final int HINGE_WIDTH = 112;

  public static final Color COLOR_DEFAULT = new Color(0xD6D6D6);

  // orange, light blue, light green, light purple
  public static final Color[] PARATOPE_COLORS = new Color[] {new Color(0xFFA459),
      new Color(0xA8F6FF), new Color(0xD2FF9B), new Color(0xEAABFF)};

  // red, dark blue, dark green, dark purple
  public static final Color[] PARATOPE_COLORS_DARK = new Color[] {new Color(0xFF6B6B),
      new Color(0xA1B2E6), new Color(0x9BBF6F), new Color(0xCC79E0)};

  boolean initialized = false;

  public DomainNodeRealizer() {
    super();
    initFromMap();
  }

  public DomainNodeRealizer(Domain d) {
    super();
    initFromMap(d);
  }

  public void initFromMap() {
    try {
      Domain domain = (Domain) getNode().getGraph()
          .getDataProvider(AbConst.NODE_TO_SEQUENCE_KEY)
          .get(getNode());
      initFromMap(domain);
    } catch (NullPointerException e) {
      // Not yet ready for initialization
    }
  }

  public void initFromMap(Domain domain) {
    if (domain != null) {
      findFillColor(domain);
      findHumannessFeatures(domain);
      findShape(domain);
      findBorderLine(domain);
      setLabelText(buildLabel(domain));
      findSize(domain);
      initialized = true;
    }
  }

  @Override
  protected void paintNode(Graphics2D graph) {
    // ML 2014-03-21 multiple refreshing errors. Fast workaround needed for demo.
    // TODO restructure refreshing (when label, connections, etc. change)
    // if (!initialized) {
    initFromMap();
    // }

    super.paintNode(graph);
  }

  private void findFillColor(Domain domain) {
    if (domain.getParatope() != null) {
      fillParatopeColor(domain, domain.getParatope());
      return;
    } else {

      // color it too, when adjacent N-terminal domain is part of a paratope
      int domainIndex = domain.getPeptide().getDomainIndex(domain);
      if (domainIndex > 0) {
        Integer adjacentParatopeNTerm = domain.getPeptide().getDomains().get(domainIndex - 1).getParatope();
        if (adjacentParatopeNTerm != null) {
          fillParatopeColor(domain, adjacentParatopeNTerm);
          return;
        }
      }
    }

    setFillColor(COLOR_DEFAULT);
  }

  private void fillParatopeColor(Domain domain, Integer paratopeIndex) {
    switch (domain.getChainType()) {
    case HEAVY:
      setFillColor(PARATOPE_COLORS_DARK[paratopeIndex - 1]);
      break;
    case KAPPA:
    case LAMBDA:
      setFillColor(PARATOPE_COLORS[paratopeIndex - 1]);
      break;
    default:
      setFillColor(COLOR_DEFAULT);
      break;
    }
  }

  /**
   * Changes Domain Realizer depending on humanness.
   * 
   * @param domain
   */
  private void findHumannessFeatures(Domain domain) {
    if (!domain.getHumanessType().equals(HumannessType.HUMAN)) {
      setFillColor2(Color.WHITE);
    }
  }

  private void findShape(Domain domain) {
    if (domain.isConstant() || domain.isHinge()) {
      setShapeType(ROUND_RECT);
    }
    if (domain.isUnknownDomainType()) {
      setShapeType(ELLIPSE);
    }
    if (domain.isVariable()) {
      setShapeType(OCTAGON);
    }
  }

  private void findBorderLine(Domain domain) {
    if (domain.getDomainType().equals(DomainType.HINGE) || domain.getDomainType().equals(DomainType.NONE)) {
      // no border not possible --> color it white to make it invisible
      this.setLineColor(Color.WHITE);
    } else {
      this.setLineColor(Color.BLACK);

      if (domain.getChainType() == ChainType.HEAVY) {
        this.setLineType(LineType.LINE_2);
      } else if (domain.getChainType() == ChainType.NONE) {
        this.setLineType(LineType.DOTTED_4);
      } else {
        this.setLineType(LineType.DASHED_2);
      }

    }

    if (domain.getFreeCysteinPositions().size() != 0) {
      this.setLineColor(Color.RED);
    }
  }

  private void findSize(Domain domain) {
    if (domain.isHinge()) {
      setSize(HINGE_WIDTH, HINGE_HEIGHT);
    } else {
      setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }
  }

  private String buildLabel(Domain domain) {
    String result = domain.getUserLabel();
    if (StringUtils.isNotBlank(domain.getUserComment())) {
      result += "\n" + domain.getUserComment();
    }
    return result;
  }

}
