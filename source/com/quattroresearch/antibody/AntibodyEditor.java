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

import java.awt.HeadlessException;
import java.awt.dnd.DropTarget;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.roche.antibody.services.UIService;
import org.roche.antibody.ui.components.AntibodyEditorAccess;
import org.roche.antibody.ui.components.AntibodyEditorPane;

import com.quattroresearch.antibody.plugin.PluginLoader;

/**
 * Content Panel of "Antibody Sequence Editor" <p> Displays arbitrary number of chains to be searched against DB.
 * Accepts drag&drop File-Input.
 * 
 * @author <b>Anne Mund</b>, quattro research GmbH
 */
public class AntibodyEditor extends JPanel {

  /** Generated UID */
  private static final long serialVersionUID = -7323309152562400371L;

  private static final String HELP_TEXT = "Load files by OpenFileDialog or Drag&Drop: ";

  // declaration of variables
  private List<String> chainNames;

  private List<String> chainSequences;

  private AntibodyDragDropListener dragDropListener;

  public AntibodyEditor() {
    initDragDrop();
    initComponents();
  }

  private void initComponents() {
    this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

    chainNames = new ArrayList<String>();
    chainSequences = new ArrayList<String>();
    placeChainField(HELP_TEXT, "");

  }

  private void initDragDrop() {
    dragDropListener = new AntibodyDragDropListener(this);
    new DropTarget(this, dragDropListener);
  }

  public List<String> getChains() {
    return chainSequences;
  }

  public List<String> getNames() {
    return chainNames;
  }

  public void setChains(List<String> namelist, List<String> sequencelist) {
    for (int i = 0; i < namelist.size(); i++) {
      placeChainField(namelist.get(i), sequencelist.get(i));
    }
  }

  public void clearAll() {
    AntibodyEditorPane pane = AntibodyEditorAccess.getInstance().getAntibodyEditorPane();
    if (pane != null) {
      AntibodyEditorAccess.getInstance().getAntibodyEditorPane().setModel(null);
    }

    this.removeAll();
    initComponents();
  }

  /**
   * Adds name and sequence of chain to the content Panel. <p> Method is called for every new chain added, sequence is
   * put in a JTextArea inside a JScrollPane.
   * 
   * @param name Name of chain; for gp-Files filename
   * @param sequence Sequence of amino acids
   */
  public void placeChainField(String name, String sequence) {
    if (chainNames.size() == 1 && chainNames.get(0).equals(HELP_TEXT)) {
      this.removeAll();
      chainNames = new ArrayList<String>();
      chainSequences = new ArrayList<String>();
    }

    // remember name + sequence
    chainNames.add(name);
    chainSequences.add(sequence);

    // display name + sequence
    JLabel label = new JLabel(name);
    this.add(label);
    JTextArea area = new JTextArea(3, 30);
    area.setText(sequence);
    area.setEditable(false);
    area.setLineWrap(true);
    JScrollPane scroll = new JScrollPane(area);
    this.add(scroll);
    this.validate();
    // this.repaint();

    // enable drag&drop in TextArea
    new DropTarget(area, dragDropListener);
  }

  /**
   * Takes a file and gives it to the appropriate parser. Opens Message Dialog on wrong file extension.
   * 
   * @param file .txt, .gp or .fa File to parse
   * @return (List of chainnames, List of chainsequences)
   * @throws IllegalArgumentException thrown when file type is unknown
   * @throws IOException
   * @throws HeadlessException
   */
  public List<String>[] readFile(File file) throws IllegalArgumentException, HeadlessException, IOException {
    ISequenceFileReader readerPlugin = PluginLoader.getInstance().loadSequenceFileReaderPlugin();
    if (readerPlugin != null) {
      return readerPlugin.read(UIService.getInstance().getMainFrame(), file);
    } else {
      return new SequenceFileReader().read(UIService.getInstance().getMainFrame(), file);
    }
  }

}
