/*******************************************************************************
 * Copyright C 2015, quattro research GmbH, Roche pREDi (Roche Innovation Center Penzberg)
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
package com.quattroresearch.antibody;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

/**
 * Message prompt for sequence input <p> Input dialog for sequence input which also calculates the number of Cysteins
 * and the sequence length
 * 
 * @author <b>Stefan Klostermann:</b> Stefan DOT Klostermann AT roche DOT com, Roche Pharma Research and Early
 *         Development - Informatics, Roche Innovation Center Penzberg
 * @author <b>Anne Mund</b>, quattro research GmbH
 * 
 */
public class SequenceEditor extends AbstractCellEditor implements
		TableCellEditor, ActionListener {

	/** Generated UID */
	private static final long serialVersionUID = 2460784291731083999L;
	EnterDomainDialog dialog;
	JButton button;
	String currentSequence;
	int row;

	public SequenceEditor(EnterDomainDialog dialog) {
		this.dialog = dialog;
		button = new JButton();
		button.setActionCommand("edit");
		button.addActionListener(this);
		button.setBorderPainted(false);

	}

	@Override
	/** {@inheritDoc}*/
	public Object getCellEditorValue() {
		return currentSequence;
	}

	@Override
	/** {@inheritDoc}*/
	public Component getTableCellEditorComponent(JTable table, Object value,
			boolean isSelected, int row, int column) {
		this.row = row;
		currentSequence = (String) value;
		return button;
	}

	@Override
	/**
	 * Prompt for sequence String
	 * <p>
	 * Checks whether sequence only contains letters
	 * Also calculates length and Cystein-count.
	 */
	public void actionPerformed(ActionEvent e) {
		if ("edit".equals(e.getActionCommand())) {
			Pattern pattern = Pattern.compile("^[a-zA-Z]+");
			String input = (String) JOptionPane.showInputDialog(
					"Enter Sequence", currentSequence);
			if (input == null) {
				fireEditingStopped();
				return;
			}
			Matcher matcher = pattern.matcher(input);
			while (!matcher.matches()) {
				input = (String) JOptionPane.showInputDialog(
						"The Sequence can only contain letters!", input);
				if (input == null) {
					fireEditingStopped();
					return;
				}
				matcher = pattern.matcher(input);
			}
			currentSequence = input.toUpperCase();
			dialog.calculateCys(currentSequence, row);

			fireEditingStopped();
		}
	}

}
