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
package org.roche.antibody.services;

import org.roche.antibody.model.antibody.ChemElement;
import org.roche.antibody.model.antibody.Domain;
import org.roche.antibody.model.antibody.Peptide;
import org.roche.antibody.model.antibody.RNA;
import org.roche.antibody.model.antibody.Sequence;
import org.roche.antibody.services.helmnotation.HelmNotationService;
import org.roche.antibody.services.helmnotation.model.HELMChem;
import org.roche.antibody.services.helmnotation.model.HELMElement;
import org.roche.antibody.services.helmnotation.model.HELMPeptide;
import org.roche.antibody.services.helmnotation.model.HELMRna;

/**
 * {@code SequenceService}
 * 
 * @author <b>Stefan Klostermann:</b> Stefan DOT Klostermann AT roche DOT com, Roche Pharma Research and Early
 *         Development - Informatics, Roche Innovation Center Penzberg
 * @author <b>Stefan Zilch:</b> stefan_dieter DOT zilch AT contractors DOT roche DOT com
 * @version $Id: SequenceService.java 15173 2015-03-05 16:18:13Z schirmb $
 */
public class SequenceService {

  private HelmNotationService hs = HelmNotationService.getInstance();

  /** static Singleton instance */
  private static SequenceService instance;

  /** Private constructor for singleton */
  private SequenceService() {
  }

  /** Static getter method for retrieving the singleton instance */
  public synchronized static SequenceService getInstance() {
    if (instance == null) {
      instance = new SequenceService();
    }
    return instance;
  }

  /**
   * converts a {@link Sequence} to a {@link HELMElement}
   * 
   * @param sequence {@link Sequence}
   * @return {@link HELMElement}
   */
  public HELMElement toHELM(Sequence sequence) {
    HELMElement element = null;
    if (sequence instanceof Peptide || sequence instanceof Domain) {
      element = new HELMPeptide();
      element.setSequenceRepresentation(hs.simpleSequenceToSequenceRepresentation(sequence.getSequence()));
    }

    if (sequence instanceof RNA) {
      element = new HELMRna();
      element.setSequenceRepresentation(sequence.getSequence());
    }

    if (sequence instanceof ChemElement) {
      element = new HELMChem();
      element.setSequenceRepresentation(sequence.getSequence());
    }
    return element;
  }
}
