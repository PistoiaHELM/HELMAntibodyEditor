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

import java.awt.Cursor;
import java.awt.Dialog.ModalityType;
import java.util.logging.Logger;

import org.roche.antibody.ui.components.HELMEditorDialog;

/**
 * 
 * {@code HELMEditorAccessService} encapsulates access to HELMEditor with given
 * helm notation and return value.
 * 
 * @author <b>Stefan Klostermann:</b> Stefan DOT Klostermann AT roche DOT com,
 *         Roche Pharma Research and Early Development - Informatics, Roche
 *         Innovation Center Penzberg
 * @author <b>Marco Lanig:</b> lanig AT quattro-research DOT com, quattro
 *         research GmbH
 * 
 * @version $Id: HELMEditorAccessService.java 15209 2015-03-09 10:24:30Z schirmb
 *          $
 */
public class HELMEditorAccessService {

	/** The Logger for this class */
	// private static final Logger LOG =
	// Logger.getLogger(HELMEditorAccessService.class.toString());

	private static HELMEditorDialog helmEditorDialog;

	public static String openEditor(String helmNotation) {
		try {

			UIService.getInstance().getMainFrame()
					.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

			helmEditorDialog = new HELMEditorDialog(helmNotation);

			helmEditorDialog.setSize(1000, 750);
			helmEditorDialog.setResizable(true);
			helmEditorDialog.setLocationRelativeTo(null);
			helmEditorDialog.setModal(true);

			return helmEditorDialog.showDialog();

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if (helmEditorDialog != null) {
				helmEditorDialog.dispose();
				helmEditorDialog = null;
			}
			UIService
					.getInstance()
					.getMainFrame()
					.setCursor(
							Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}
	}

}
