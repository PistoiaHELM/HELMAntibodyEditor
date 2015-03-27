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
package org.roche.antibody.ui.actions.menu;

import java.awt.event.ActionEvent;

import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JFrame;

import com.quattroresearch.antibody.EnterDomainDialog;

/**
 * {@code AntibodyDomainsLibraryEditorAction}
 * 
 * Action for launching the DomainLibEditor.
 * 
 * @author <b>Stefan Klostermann:</b> Stefan DOT Klostermann AT roche DOT com, Roche Pharma Research and Early
 *         Development - Informatics, Roche Innovation Center Penzberg
 * @author <b>Stefan Zilch:</b> stefan_dieter DOT zilch AT contractors DOT roche DOT com
 * 
 * @version $Id: AntibodyDomainsLibraryEditorAction.java 15173 2015-03-05 16:18:13Z schirmb $
 */
public class AntibodyDomainsLibraryEditorAction extends AbstractEditorAction {

  /** */
  private static final long serialVersionUID = 1L;

  public static final String NAME = "View domain library";

  public static final String SHORT_DESCRIPTION = "Opens the domain library.";

  public static final String IMAGE_PATH = "table.png";

  public AntibodyDomainsLibraryEditorAction(JFrame parentFrame) {
    super(parentFrame, NAME);

    ImageIcon icon = getImageIcon(IMAGE_PATH);
    if (icon != null) {
      this.putValue(Action.SMALL_ICON, icon);
    }
    this.putValue(Action.SHORT_DESCRIPTION, SHORT_DESCRIPTION);
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    EnterDomainDialog enterDialog = new EnterDomainDialog(super.getParentFrame(), false);
    enterDialog.setVisible(true);
    enterDialog.setLocationRelativeTo(super.getParentFrame());

  }

}