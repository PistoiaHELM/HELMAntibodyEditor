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
package org.roche.antibody.tests;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.roche.antibody.services.helmnotation.model.HELMConnection;
import org.roche.antibody.services.helmnotation.model.HELMElement;
import org.roche.antibody.services.helmnotation.model.HELMPeptide;
import org.roche.antibody.services.helmnotation.model.HELMRna;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@code HELMNotationTest}
 * 
 * @author <b>Stefan Klostermann:</b> Stefan DOT Klostermann AT roche DOT com, Roche Pharma Research and Early
 *         Development - Informatics, Roche Innovation Center Penzberg
 * @author <b>Stefan Zilch:</b> stefan_dieter DOT zilch AT contractors DOT roche DOT com
 * 
 * @version $Id: HELMNotationTest.java 15173 2015-03-05 16:18:13Z schirmb $
 */
public class HELMNotationTest {

  /** The Logger for this class */
  private static final Logger LOG = LoggerFactory.getLogger(HELMNotationTest.class);

  @Test
  public void testEqualConnection() {
    HELMElement rna = new HELMRna();
    HELMPeptide pep = new HELMPeptide("ABCDEFGHIJKLMN");
    HELMConnection conSourceTarget = new HELMConnection(rna, 1, "R1", pep, 3, "R3");
    HELMConnection conTargetSource = new HELMConnection(pep, 3, "R3", rna, 1, "R1");
    assertEquals("Connections are not equal", conSourceTarget, conTargetSource);
    assertEquals("Hash is different!", conSourceTarget.hashCode(), conTargetSource.hashCode());
  }

}
