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
package org.roche.antibody.ui.components;

import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.logging.Logger;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

import org.helm.editor.editor.MacromoleculeEditor;
import org.helm.notation.MonomerException;
import org.helm.notation.NotationException;
import org.jdom.JDOMException;

public class HELMEditorDialog extends JDialog {

  /** Generated UID */
  private static final long serialVersionUID = -1656068018993714117L;

  /** The Logger for this class */
  private static final Logger LOG = Logger.getLogger(HELMEditorDialog.class.toString());

  private String modalResult = null;

  private HELMEditorDialog thisInstance;

  private boolean isFullScreen = false;

  private Rectangle oldBounds;

  private MacromoleculeEditor mme;

  public HELMEditorDialog(String helmString) throws MonomerException, JDOMException, IOException, NotationException {
    thisInstance = this;
    mme = new MacromoleculeEditor();
    mme.reset();
    mme.setNotation(helmString);

    JPanel pnlEditor = new JPanel();
    pnlEditor.setLayout(new BoxLayout(pnlEditor, BoxLayout.Y_AXIS));

    JButton btnFullScreen = new JButton("Toggle FullScreen");
    btnFullScreen.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        if (isFullScreen) {
          thisInstance.setBounds(oldBounds);
        } else {
          oldBounds = new Rectangle(thisInstance.getBounds());
          thisInstance.setBounds(GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds());
        }
        isFullScreen = !isFullScreen;
      }
    });
    JButton btnOK = new JButton("Accept Changes");
    btnOK.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        modalResult = mme.getNotation();
        closeDialog();
      }
    });
    JButton btnCancel = new JButton("Cancel");
    btnCancel.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        modalResult = null;
        closeDialog();
      }
    });

    JPanel buttonPanel = new JPanel();
    buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
    buttonPanel.add(Box.createHorizontalGlue());
    buttonPanel.add(btnFullScreen);
    buttonPanel.add(Box.createHorizontalStrut(5));
    buttonPanel.add(btnOK);
    buttonPanel.add(Box.createHorizontalStrut(5));
    buttonPanel.add(btnCancel);
    buttonPanel.add(Box.createHorizontalStrut(5));

    pnlEditor.add(mme.getContentComponent());
    pnlEditor.add(buttonPanel);

    add(pnlEditor);

  }

  public String showDialog() {
    setVisible(true);
    return modalResult;
  }

  private void closeDialog() {
    mme.dispose();
    mme = null;
    setVisible(false);
    dispose();
  }
}