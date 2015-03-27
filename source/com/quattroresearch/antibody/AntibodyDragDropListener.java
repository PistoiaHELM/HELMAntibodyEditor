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

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Drag&Drop Listener for AntibodyEditor only accepts copy drops, other methods unused
 * 
 * @author <b>Anne Mund</b>, quattro research GmbH
 *
 */
public class AntibodyDragDropListener implements DropTargetListener {

  private AntibodyEditor antibodyEditor;

  public AntibodyDragDropListener(AntibodyEditor aE) {
    this.antibodyEditor = aE;
  }

  @Override
  public void dragEnter(DropTargetDragEvent dtde) {

  }

  @Override
  public void dragExit(DropTargetEvent dte) {

  }

  @Override
  public void dragOver(DropTargetDragEvent dtde) {

  }

  @SuppressWarnings("unchecked")
  @Override
  public void drop(DropTargetDropEvent dtde) {
    Transferable transferable = dtde.getTransferable();
    // get data formats
    DataFlavor[] formats = transferable.getTransferDataFlavors();
    for (DataFlavor format : formats) {
      try {
        // dropped items could be files
        if (format.isFlavorJavaFileListType()) {
          // make list of all found chains in textfiles
          List<String> foundnames = new ArrayList<String>();
          List<String> foundchains = new ArrayList<String>();
          List<String>[] result;

          // Accept copy drops
          dtde.acceptDrop(DnDConstants.ACTION_COPY);
          List<File> files = (List<File>) transferable.getTransferData(format);
          for (File file : files) {
            result = antibodyEditor.readFile(file);

            foundnames.addAll(result[0]);
            foundchains.addAll(result[1]);
          }
          antibodyEditor.setChains(foundnames, foundchains);
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    dtde.dropComplete(true);
  }

  @Override
  public void dropActionChanged(DropTargetDragEvent dtde) {

  }

}
