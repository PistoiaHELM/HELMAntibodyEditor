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
import static org.junit.Assert.assertTrue;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;
import org.roche.antibody.model.antibody.Domain;
import org.roche.antibody.model.antibody.Peptide;
import org.roche.antibody.services.DomainService;

/**
 * {@code DomainServiceTest}
 * 
 * @author <b>Stefan Klostermann:</b> Stefan DOT Klostermann AT roche DOT com, Roche Pharma Research and Early
 *         Development - Informatics, Roche Innovation Center Penzberg
 * @author <b>Stefan Zilch:</b> stefan_dieter DOT zilch AT contractors DOT roche DOT com
 * @version $Id: DomainServiceTest.java 15173 2015-03-05 16:18:13Z schirmb $
 */

public class DomainServiceTest {
  
  private static final int FIRST = 0;
  private static final int SECOND = 1;
  private DomainService ds = DomainService.getInstance();
  
  @Test
  public void testAddDomainFirst() throws Exception {
    String sequenceToTest = "STEFANCAPRILCLARSC";
    String newSequence = "STEFANC";
    Peptide pep = TestSuite.getTestPeptide();
    Domain dom = ds.addAsFirstDomain(newSequence, pep);
    assertTrue(StringUtils.startsWith(dom.getPeptide().getSequence(), newSequence));
    assertEquals("Wrong Sequence in Peptide", sequenceToTest, pep.getSequence());
    assertEquals("Wrong startPosition in Domain", 1, dom.getStartPosition());
    assertEquals("Wrong endPosition in Domain", 7, dom.getEndPosition());
    assertEquals("Wrong Peptide", pep, dom.getPeptide());
    assertEquals("Two domains expected!", 2, pep.getDomains().size());
    assertEquals("Domain is not on first position", dom, pep.getDomains().get(0));
    assertEquals("DomainName should be " + "NN (7AAs)", "NN (7AAs)", dom.getName());
    // we check the index offset of the second domain
    assertEquals(8, pep.getDomains().get(SECOND).getStartPosition());
    assertEquals(18, pep.getDomains().get(SECOND).getEndPosition());
    // we check the connection if indexes were updated
    assertEquals(13, pep.getConnections().get(FIRST).getSourcePosition());
    assertEquals(18, pep.getConnections().get(FIRST).getTargetPosition());
  }

  @Test
  public void testAddDomainLast() throws Exception {
    String sequenceToTest = "APRILCLARSCSTEFANC";
    String newSequence = "STEFANC";
    Peptide pep = TestSuite.getTestPeptide();
    Domain dom = ds.addAsLastDomain(newSequence, pep);
    assertTrue(StringUtils.endsWith(dom.getPeptide().getSequence(), newSequence));
    assertEquals("Wrong Sequence in Peptide", sequenceToTest, pep.getSequence());
    assertEquals("Wrong startPosition in Domain", 12, dom.getStartPosition());
    assertEquals("Wrong endPosition in Domain", 7, dom.getEndPosition());
    assertEquals("Wrong Peptide", pep, dom.getPeptide());
    assertEquals("Two domains expected!", 2, pep.getDomains().size());
    assertEquals("Domain is not on last position", dom, pep.getDomains().get(pep.getDomains().size() - 1));
    assertEquals("DomainName should be " + "NN (7AAs)", "NN (7AAs)", dom.getName());
    // we check the index offset of the second domain
    assertEquals(1, pep.getDomains().get(FIRST).getStartPosition());
    assertEquals(11, pep.getDomains().get(FIRST).getEndPosition());
    // we check the connection if indexes were updated
    assertEquals(6, pep.getConnections().get(FIRST).getSourcePosition());
    assertEquals(11, pep.getConnections().get(FIRST).getTargetPosition());
  }
}
