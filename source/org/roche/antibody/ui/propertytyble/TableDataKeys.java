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
package org.roche.antibody.ui.propertytyble;

/**
 * All used KEYS in the {@link DomainTableModel} should be stored here. Only this way, it is possible to store them with
 * the generated GraphML and refer to this information later on!
 * 
 * @author <b>Stefan Klostermann:</b> Stefan DOT Klostermann AT roche DOT com, Roche Pharma Research and Early
 *         Development - Informatics, Roche Innovation Center Penzberg
 * @author <b>Clemens Wrzodek:</b> clemens DOT wrzodek AT roche DOT com
 * 
 * @version $Id$
 */
public final class TableDataKeys {

  // "> CHAIN"
  public static final String NAME = "NAME";

  public static final String SEQUENCE_LENGTH = "SEQUENCE LENGTH";

  public static final String SEQUENCE = "SEQUENCE";

  public static final String ORIGINAL_SEQUENCE = "ORIGINAL SEQUENCE";

  // "> DOMAIN"
  public static final String USER_LABEL = "USER LABEL";

  public static final String USER_COMMENT = "USER COMMENT";

  public static final String START_POS_ON_CHAIN = "START POS ON CHAIN";

  public static final String END_POS_ON_CHAIN = "END POS ON CHAIN";

  public static final String START_POS_ON_TEMPLATE = "START POS ON TEMPLATE";

  public static final String END_POS_ON_TEMPLATE = "END POS ON TEMPLATE";

  public static final String LENGTH = "LENGTH";

  public static final String IDENTITY_TO_LIB_DOM = "IDENTITY TO LIB. DOM.";

  public static final String COVERAGE_OF_LIB_DOM = "COVERAGE OF LIB. DOM.";

  public static final String LEADING_SEQUENCE = "LEADING SEQUENCE";

  public static final String SEQUENCE_DE_FACTO = "SEQUENCE (DE FACTO)";

  public static final String TRAILING_SEQUENCE = "TRAILING SEQUENCE";

  public static final String PARATOPE = "Paratope";

  /**
   * XXX: "> ANNOTATIONS" Stored with prefix "ANNOTATION_" in the Library.
   */
  public static final String ANNOTATION_PREFIX = "ANNOTATION_";

  /**
   * XXX: "> MUTATIONS" Stored with prefix "MUTATION_" in the Library.
   */
  public static final String MUTATION_PREFIX = "MUTATION_";

  /**
   * XXX: "> UNKNOWN MUTATIONS" Stored with prefix "UNKNOWN_MUTATION_" in the Library.
   */
  public static final String UNKNOWN_MUTATION_PREFIX = "UNKNOWN_MUTATION_";

  // "> CYS BRIDGES"
  public static final String CYS_POS = "CYS POS";

  public static final String CYS_BRIDGES = "CYS BRIDGES";

  public static final String FREE_CYS = "FREE CYS";

  // "> OTHER BRIDGES"
  public static final String GENERAL = "GENERAL";

  public static final String RECOGNIZED_DOMAIN_FROM_LIBRARY = "> RECOGNIZED DOMAIN FROM LIBRARY";

  public static final String NAME_IN_LIB = "NAME IN LIB";

  public static final String SHORT_NAME = "SHORT NAME";

  public static final String SPECIES = "SPECIES";

  public static final String HUMANNESS = "HUMANNESS";

  public static final String CHAIN = "CHAIN";

  public static final String DOMAIN_TYPE = "DOMAIN TYPE";

  public static final String SEQUENCE_LENGTH_IN_LIB = "SEQUENCE LENGTH IN LIB";

  public static final String CYS_COUNT = "CYS COUNT";

  public static final String PATTERN = "PATTERN";

  public static final String COMMENT = "COMMENT";

  public static final String SEQUENCE_TEMPLATE = "SEQUENCE (TEMPLATE)";

  /**
   * @return all keys for the "> Chain" section in the table.
   */
  public static String[] getChainSectionKeys() {
    return new String[] {NAME, SEQUENCE_LENGTH, SEQUENCE, ORIGINAL_SEQUENCE};
  }

  /**
   * @return all keys for the "> Domain" section in the table. Note that USER_LABEL and USER_COMMENT should be EDITABLE!
   */
  public static String[] getDomainSectionKeys() {
    return new String[] {USER_LABEL, USER_COMMENT, START_POS_ON_CHAIN, END_POS_ON_CHAIN, LENGTH, START_POS_ON_TEMPLATE,
        END_POS_ON_TEMPLATE, IDENTITY_TO_LIB_DOM,
        COVERAGE_OF_LIB_DOM, LEADING_SEQUENCE, SEQUENCE_DE_FACTO, TRAILING_SEQUENCE, PARATOPE};
  }

  /**
   * @return all keys for the "> CysBridgeData" ection in the table.
   */
  public static String[] getCysBridgeSectionKeys() {
    return new String[] {
        // > CysBridgeData
        CYS_POS, CYS_BRIDGES, FREE_CYS
    };
  }

  /**
   * @return all keys for the "> OTHER BRIDGES" section in the table.
   */
  public static String[] getOtherBridgeSectionKeys() {
    return new String[] {
        // > OTHER BRIDGES data
        GENERAL
    };
  }

  /**
   * @return all keys for the "> RECOGNIZED DOMAIN FROM LIBRARY" section in the table.
   */
  public static String[] getLibrarySectionKeys() {
    return new String[] {
        // > RECOGNIZED DOMAIN FROM LIBRARY data
        NAME_IN_LIB, SHORT_NAME, SPECIES, HUMANNESS, CHAIN, DOMAIN_TYPE, SEQUENCE_LENGTH_IN_LIB, CYS_COUNT, PATTERN,
        COMMENT, SEQUENCE_TEMPLATE
    };
  }
}
