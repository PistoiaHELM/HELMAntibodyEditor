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
import java.io.File;

import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.roche.antibody.model.antibody.AntibodyContainer;
import org.roche.antibody.services.UIService;
import org.roche.antibody.services.xml.XmlAntibodyService;
import org.roche.antibody.ui.components.AntibodyEditorPane;
import org.roche.antibody.ui.filechooser.XmlFileChooser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@code AntibodyLoadXmlAction}
 * 
 * Action for loading an XML Antibody-File from the Mainmenu
 * 
 * @author <b>Stefan Klostermann:</b> Stefan DOT Klostermann AT roche DOT com, Roche Pharma Research and Early
 *         Development - Informatics, Roche Innovation Center Penzberg
 * @author <b>Stefan Zilch:</b> stefan_dieter DOT zilch AT contractors DOT roche DOT com
 * 
 * @version $Id: AntibodyLoadXmlAction.java 15173 2015-03-05 16:18:13Z schirmb $
 */
public class AntibodyLoadXmlAction extends AbstractEditorAction {

  /** */
  private static final long serialVersionUID = 8270310476746408309L;

  /** The Logger for this class */
  private static final Logger LOG = LoggerFactory.getLogger(AntibodyLoadXmlAction.class);

  public static final String NAME = "Load Antibody From XML...";

  public static final String SHORT_DESCRIPTION = "Loads a saved antibody.";

  public static final String IMAGE_PATH = "open-folder.png";

  private XmlAntibodyService xmlService = XmlAntibodyService.getInstance();

  public AntibodyLoadXmlAction(JFrame parentFrame) {
    super(parentFrame, NAME);

    ImageIcon icon = getImageIcon(IMAGE_PATH);
    if (icon != null) {
      this.putValue(Action.SMALL_ICON, icon);
    }
    this.putValue(Action.SHORT_DESCRIPTION, SHORT_DESCRIPTION);
  }

  public void actionPerformed(ActionEvent e) {

    XmlFileChooser dialog = new XmlFileChooser();
    if (dialog.showOpenDialog(super.getParentFrame()) == JFileChooser.APPROVE_OPTION) {
      File abFile = dialog.getSelectedFile();

      AntibodyContainer newAbContainer = null;
      try {
        newAbContainer = xmlService.unmarshal(abFile);
      } catch (Exception e1) {
        JOptionPane.showMessageDialog(super.getParentFrame(),
            "Could not create antibody from given file!", "Error",
            JOptionPane.ERROR_MESSAGE);
        LOG.error("Could not load Antibody-XML from disk! {}", e1);
        return;
      }
      AntibodyEditorPane pane = UIService.getInstance()
          .addAntibodyViewEditor(null);
      pane.setModel(newAbContainer.getAntibody());

    }
  }

}