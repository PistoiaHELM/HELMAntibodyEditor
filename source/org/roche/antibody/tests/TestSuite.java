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

import java.util.ArrayList;
import java.util.List;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.roche.antibody.model.antibody.Antibody;
import org.roche.antibody.model.antibody.ChemElement;
import org.roche.antibody.model.antibody.Connection;
import org.roche.antibody.model.antibody.CysteinConnection;
import org.roche.antibody.model.antibody.Domain;
import org.roche.antibody.model.antibody.GeneralConnection;
import org.roche.antibody.model.antibody.Peptide;
import org.roche.antibody.services.antibody.AntibodyService;

/**
 * 
 * {@code TestSuite}
 * 
 * @author raharjap
 * 
 * @version $Id: TestSuite.java 15168 2015-03-05 15:11:26Z schirmb $
 */
@RunWith(Suite.class)
@SuiteClasses({DomainServiceTest.class, XmlAntbodyServiceTest.class, HELMNotationTest.class})
public class TestSuite {

  /**
   * @return testPeptide
   * @throws Exception
   */
  public static Peptide getTestPeptide() throws Exception {
    List<Peptide> pepList = new ArrayList<Peptide>();
    Peptide pep = new Peptide();
    pep.setSequence("APRILCLARSCC");
    Domain dom = new Domain("April|Lars", pep, 1, 12, 1, 12);
    pep.setDomains(new Domain[] {dom});
    // ME 2014-10-10: changed the constructor call.
    ChemElement sm = new ChemElement("Az", "[*]C(=O)CCCN=[N+]=[N-] |$_R1;;;;;;;;$|",
        "\n" +
            "  ACCLDraw10281407312D\n" +
            "\n" +
            "  8  7  0  0  0  0  0  0  0  0999 V2000\n" +
            "   14.5592  -11.0695    0.0000 C   0  0  0  0  0  0  0  0  0  0  0  0\n" +
            "   15.7403  -11.0695    0.0000 O   0  0  0  0  0  0  0  0  0  0  0  0\n" +
            "   13.9687  -12.0923    0.0000 C   0  0  0  0  0  0  0  0  0  0  0  0\n" +
            "   12.7876  -12.0923    0.0000 C   0  0  0  0  0  0  0  0  0  0  0  0\n" +
            "   12.1970  -13.1152    0.0000 C   0  0  0  0  0  0  0  0  0  0  0  0\n" +
            "   11.0159  -13.1152    0.0000 N   0  0  0  0  0  0  0  0  0  0  0  0\n" +
            "   10.4254  -14.1380    0.0000 N   0  3  0  0  0  0  0  0  0  0  0  0\n" +
            "    9.8348  -15.1610    0.0000 N   0  5  0  0  0  0  0  0  0  0  0  0\n" +
            "  1  2  2  0  0  0  0\n" +
            "  1  3  1  0  0  0  0\n" +
            "  3  4  1  0  0  0  0\n" +
            "  4  5  1  0  0  0  0\n" +
            "  5  6  1  0  0  0  0\n" +
            "  6  7  2  0  0  0  0\n" +
            "  7  8  2  0  0  0  0\n" +
            "M  CHG  2   7   1   8  -1\n" +
            "M  END");

    pepList.add(pep);
    Antibody ab = AntibodyService.getInstance().create(pepList);
    Connection conn = new CysteinConnection(6, 11, pep);
    AntibodyService.getInstance().addSequence(sm, ab);
    GeneralConnection gc = new GeneralConnection(pep, sm, 12, 1, "R3", "R1");
    ab.addConnection(conn);
    ab.addConnection(gc);
    return pep;
  }

  public static Antibody getTestAntibody() throws Exception {
    return getTestPeptide().getAntibody();
  }

}
