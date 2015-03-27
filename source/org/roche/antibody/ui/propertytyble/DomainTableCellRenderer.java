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

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

/**
 * {@code DomainPropertyCellRenderer}
 * 
 * Cell-Renderer to layout and design the propertyTable a bit.
 * 
 * @author <b>Stefan Klostermann:</b> Stefan DOT Klostermann AT roche DOT com, Roche Pharma Research and Early
 *         Development - Informatics, Roche Innovation Center Penzberg
 * @author <b>Stefan Zilch:</b> stefan_dieter DOT zilch AT contractors DOT roche DOT com
 * @version $Id: DomainTableCellRenderer.java 15173 2015-03-05 16:18:13Z schirmb $
 */
public class DomainTableCellRenderer implements TableCellRenderer {

  @Override
  public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
      int row, int column) {
    if (value == null) {
      value = new TableValueRow();
    }
    TableValueRow element = (TableValueRow) value;
    JLabel result = new JLabel();
    result.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
    result.setOpaque(true);
    result.setBackground(Color.WHITE);
    if (column == DomainTableModel.PROPERTY_COLUMN) {
      result.setText(element.getName());
      result.setFont(result.getFont().deriveFont(Font.BOLD));
    }
    else {
      result.setText(element.getValue());
      result.setToolTipText(element.getValue());
    }

    if (element.isTitle()) {
      result.setBackground(Color.LIGHT_GRAY);
    }
    
    if (element.isEditable()) {
      result.setBackground(new Color(0xFFFFE0));
    }
    return result;
  }

}
