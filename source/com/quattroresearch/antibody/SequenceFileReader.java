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
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.apache.commons.io.FilenameUtils;
import org.roche.antibody.ui.filechooser.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SequenceFileReader implements ISequenceFileReader {

  /** The Logger for this class */
  private static final Logger LOG = LoggerFactory.getLogger(SequenceFileReader.class);

  private JFrame parentFrame;

  /**
   * {@inheritDoc}
   */
  @Override
  public List<String>[] read(JFrame parentFrame, File file) throws IllegalArgumentException, HeadlessException,
      IOException {
    this.parentFrame = parentFrame;

    List<String>[] result;
    if (FileUtils.isFastaFile(getFileContent(file))) {
      result = readFastaFile(file);
    } else if (FileUtils.isVNTProteinFile(getFileContent(file))) {
      result = readGPFile(file);
    } else {
      String exceptionText =
          "The content of the file is not a fasta nor a VNT protein format. Please use only these file formats.";
      JOptionPane.showMessageDialog(parentFrame, exceptionText);
      throw new IllegalArgumentException(exceptionText);
    }
    return result;
  }

  /**
   * Parses .fa Files <p> Parses all chains found in a fasta-formatted file. Sequence can be upper- or lower-case
   * letters, name is everything from ">".
   * 
   * @param file .fa-File to parse
   * @return (List of chainnames, List of chainsequences)
   */
  private List<String>[] readFastaFile(File file) {
    List<String> foundNames = new ArrayList<String>();
    List<String> foundChains = new ArrayList<String>();
    int current = -1;
    BufferedReader br = null;

    try {
      br = new BufferedReader(new FileReader(file));
      String line;
      while ((line = br.readLine()) != null) {
        String lineClean = line.trim();
        if (!lineClean.isEmpty()) {
          if (lineClean.startsWith(">")) {
            foundNames.add(lineClean.split(">")[1]);
            foundChains.add("");
            current++;
          } else {
            foundChains.set(current, foundChains.get(current) + lineClean);
          }
        }
      }
    } catch (FileNotFoundException e) {
      JOptionPane.showMessageDialog(parentFrame, "File " + file.getPath() + " was not found.");
      e.printStackTrace();
    } catch (IOException e) {
      JOptionPane.showMessageDialog(parentFrame, "Could not read file. (Line " + (current + 1) + ")");
      e.printStackTrace();
    } finally {
      if (br != null) {
        try {
          br.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }

    List<String>[] result = new List[2];
    result[0] = foundNames;
    result[1] = foundChains;

    return result;
  }

  /**
   * Parses .gp Files <p> Parses all chains found in a gp-formatted file (usually one). Sequence can be upper- or
   * lower-case letters, chainname is the filename.
   * 
   * @param file .gp-File to parse
   * @return (List of chainnames, List of chainsequences)
   */
  private List<String>[] readGPFile(File file) {
    List<String> foundNames = new ArrayList<String>();
    List<String> foundChains = new ArrayList<String>();
    try {
      FileInputStream fis = new FileInputStream(file);
      DataInputStream in = new DataInputStream(fis);
      BufferedReader br = new BufferedReader(new InputStreamReader(in));
      String strLine;
      StringBuilder buildChain = new StringBuilder();
      Pattern startofSequence = Pattern.compile("^ORIGIN.*");
      Pattern dataline = Pattern.compile("^(\\s*[A-Z]{2,}\\s{3,})|(//).*");
      Pattern chain = Pattern.compile("^\\s*\\d+\\s+([A-Za-z ]+)$");
      boolean readSequence = false;
      Matcher matcher;

      // extract all Chains from file
      while ((strLine = br.readLine()) != null) {
        matcher = startofSequence.matcher(strLine);
        if (matcher.matches()) {
          readSequence = true;
        }

        if (readSequence) {
          matcher = chain.matcher(strLine.replaceAll("\\*", ""));
          if (matcher.matches()) {
            buildChain.append(matcher.group(1).replaceAll(" ", ""));
          }

          matcher = dataline.matcher(strLine);
          if (matcher.matches()) {
            foundChains.add(buildChain.toString().toUpperCase());
            buildChain = new StringBuilder();
            readSequence = false;
          }
        }
      }

      String filename = FilenameUtils.removeExtension(file.getName());
      if (foundChains.size() == 1) {
        foundNames.add(filename);
      } else {
        for (int i = 0; i < foundChains.size(); i++) {
          foundNames.add(filename + String.valueOf(i));
        }
      }

      in.close();
    } catch (IOException x) {
      System.err.format("IOException: %s%n", x);
    }

    List<String>[] result = new List[2];
    result[0] = foundNames;
    result[1] = foundChains;

    return result;
  }

  private String getFileContent(File file) throws IOException {
    String lineSeparator = System.getProperty("line.separator");
    StringBuilder fileContent = new StringBuilder();
    BufferedReader reader = null;
    try {
      reader = new BufferedReader(new FileReader(file));
      String line;
      while ((line = reader.readLine()) != null)
        fileContent.append(line);
      fileContent.append(lineSeparator);
      return fileContent.toString();

    } finally {
      if (reader != null) {
        reader.close();
      }
    }
  }
}
