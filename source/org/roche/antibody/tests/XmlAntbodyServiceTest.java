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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;
import org.roche.antibody.model.antibody.AntibodyContainer;
import org.roche.antibody.services.antibody.AntibodyService;
import org.roche.antibody.services.helmnotation.HelmNotationService;
import org.roche.antibody.services.xml.XmlAntibodyService;

/**
 * {@code XmlAntbodyServiceTest}
 * 
 * @author <b>Stefan Klostermann:</b> Stefan DOT Klostermann AT roche DOT com, Roche Pharma Research and Early
 *         Development - Informatics, Roche Innovation Center Penzberg
 * @author <b>Stefan Zilch:</b> stefan_dieter DOT zilch AT contractors DOT roche DOT com
 * 
 * @version $Id: XmlAntbodyServiceTest.java 15173 2015-03-05 16:18:13Z schirmb $
 */
public class XmlAntbodyServiceTest {

  private XmlAntibodyService xmlService = XmlAntibodyService.getInstance();

  private HelmNotationService helmService = HelmNotationService.getInstance();

  private AntibodyService abService = AntibodyService.getInstance();

  private File xmlFile = new File("test-antibody.xml");

  private String xmlString;

  @Test
  public void testMarshalAndUnmarshal() throws Exception {
    AntibodyContainer container = new AntibodyContainer();
    container.setAntibody(TestSuite.getTestAntibody());
    container.setHelmCode(helmService.toHELMString(abService.toHELM(TestSuite.getTestAntibody())));
    xmlService.marshal(container, xmlFile);
    assertTrue("XML-File was not created!", xmlFile.exists());

    AntibodyContainer abContainer = xmlService.unmarshal(xmlFile);
    assertNotNull("Antibody was null after unmarshal", abContainer.getAntibody());
    assertFalse("HELMCODE was null after unmarshal", StringUtils.isBlank(abContainer.getHelmCode()));
  }

  @Test
  public void testMarshalAndUnmarshalString() throws Exception {
    AntibodyContainer container = new AntibodyContainer();
    container.setAntibody(TestSuite.getTestAntibody());
    container.setHelmCode(helmService.toHELMString(abService.toHELM(TestSuite.getTestAntibody())));
    xmlString = xmlService.marshal(container);
    assertTrue("XML was not created!", xmlString != null);

    AntibodyContainer abContainer = xmlService.unmarshal(xmlString);
    assertNotNull("Antibody was null after unmarshal", abContainer.getAntibody());
    assertFalse("HELMCODE was null after unmarshal", StringUtils.isBlank(abContainer.getHelmCode()));
  }

}
