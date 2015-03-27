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
 * {@code TableValueRow} Representation of a Table Row in the Properties Table
 * 
 * @author <b>Stefan Klostermann:</b> Stefan DOT Klostermann AT roche DOT com, Roche Pharma Research and Early
 *         Development - Informatics, Roche Innovation Center Penzberg
 * @author <b>Stefan Zilch:</b> stefan_dieter DOT zilch AT contractors DOT roche DOT com
 * 
 * @version $Id: TableValueRow.java 15173 2015-03-05 16:18:13Z schirmb $
 */
class TableValueRow {

  private String name = "";

  private String value = "";

  private boolean isTitle = false;

  private boolean isEditable = false;

  TableValueRow() {

  }

  TableValueRow(String name, String value) {
    this.name = name;
    this.value = value;
  }

  TableValueRow(String name, String value, boolean isEditable) {
    this.name = name;
    this.value = value;
    this.isEditable = isEditable;
  }

  TableValueRow(String name, boolean isTitle) {
    this.name = name;
    this.isTitle = isTitle;
  }

  public String getName() {
    return this.name;
  }

  public String getValue() {
    return this.value;
  }

  public boolean isTitle() {
    return this.isTitle;
  }

  public boolean isEditable() {
    return this.isEditable;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public void setTitle(boolean isTitle) {
    this.isTitle = isTitle;
  }

  public void setEditable(boolean isEditable) {
    this.isEditable = isEditable;
  }

  @Override
  public String toString() {
    return this.value;
  }

}
