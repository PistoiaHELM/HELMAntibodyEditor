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
import java.net.URL;
import java.util.logging.Logger;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JFrame;

import org.roche.antibody.ui.resources.ResourceProvider;

public class AbstractEditorAction extends AbstractAction {

  /** default */
  private static final long serialVersionUID = 1L;

  /** The Logger for this class */
  private static final Logger LOG = Logger.getLogger(AbstractEditorAction.class.toString());

  private JFrame parentFrame;

  private String menuName = "Unknown Plugins";

  public AbstractEditorAction(JFrame parentFrame, String actionName) {
    super(actionName);

    this.parentFrame = parentFrame;
  }

  protected JFrame getParentFrame() {
    return parentFrame;
  }

  public ImageIcon getImageIcon(String imagePath) {
    URL imageURL = ResourceProvider.getInstance().get(imagePath);
    if (imageURL != null) {
      return new ImageIcon(imageURL);
    }
    return null;
  }

  public void setMenuName(String menuName) {
    this.menuName = menuName;
  }

  public String getMenuName() {
    return menuName;
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    // TODO Auto-generated method stub

  }

}
