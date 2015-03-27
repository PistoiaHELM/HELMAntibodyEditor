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

import java.awt.Dialog;
import java.awt.event.ActionEvent;

import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.ToolTipManager;

import org.roche.antibody.ui.components.AntibodySettingsDialog;

/**
 * @author <b>Stefan Klostermann:</b> Stefan DOT Klostermann AT roche DOT com, Roche Pharma Research and Early
 *         Development - Informatics, Roche Innovation Center Penzberg
 * @author raharjap
 * @author Stefan Zilch - BridgingIT GmbH
 * 
 */
public class EditorSettingsAction extends AbstractEditorAction {

  /** */
  private static final long serialVersionUID = 3915001522538066691L;

  public static final String NAME = "Antibody Editor Settings";

  public static final String SHORT_DESCRIPTION = "Load sequences to create an antibody.";

  public static final String IMAGE_PATH = "gear.png";

  public EditorSettingsAction(JFrame parentFrame) {
    super(parentFrame, NAME);

    ImageIcon icon = getImageIcon(IMAGE_PATH);
    if (icon != null) {
      this.putValue(Action.SMALL_ICON, icon);
    }

    this.putValue(Action.SHORT_DESCRIPTION, SHORT_DESCRIPTION);
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    AntibodySettingsDialog dialog = null;
    int oldDismissDelay = ToolTipManager.sharedInstance().getDismissDelay();
    try {
      ToolTipManager.sharedInstance().setDismissDelay(6000);
      dialog =
          new AntibodySettingsDialog(super.getParentFrame(), Dialog.ModalityType.APPLICATION_MODAL);
      dialog.setLocationRelativeTo(super.getParentFrame());
      dialog.setVisible(true);
    } catch (NullPointerException ex) {
      // no dialog on error --> error handling inside
    } finally {
      ToolTipManager.sharedInstance().setDismissDelay(oldDismissDelay);
    }
  }

}