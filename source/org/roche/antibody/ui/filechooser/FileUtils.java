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
package org.roche.antibody.ui.filechooser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * {@code FileUtils} contains methods to check the file format
 * 
 * @author <b>Jutta Fichtner:</b> fichtner AT quattro-research DOT com
 * @version $Id: FileUtils.java 15173 2015-03-05 16:18:13Z schirmb $
 */
public class FileUtils {

  /** The Logger for this class */
  @SuppressWarnings("unused")
  private static final Logger LOG = LoggerFactory.getLogger(FileUtils.class);

  private final static String PATTERN_HEADER =
      "(LOCUS\\p{Blank}+)([A-Za-z0-9\\p{Punct}]+)(\\p{Blank}+)([0-9]+)(\\p{Blank}+)(bp|aa)(\\p{Blank}+)(ds-DNA|DNA)?(\\p{Blank}*)(circular)?(.*)";

  /**
   * Checks if the given file is a VNTFile that contains DNA by reference to the LOCUS tag in the .gb file
   * 
   * @param file the file to be checked.
   * @return true if the given file is a genbank file that contains DNA.
   * @throws IOException
   */
  public static boolean isVNTDNAFile(String file) throws IOException {
    Pattern pattern = Pattern.compile(PATTERN_HEADER);
    BufferedReader readbuffer = null;
    try {
      readbuffer = new BufferedReader(new StringReader(file));
      String strRead;
      strRead = readbuffer.readLine();
      Matcher matcher = pattern.matcher(strRead);

      return (matcher.matches() && matcher.group(6).equalsIgnoreCase("bp") && (matcher.group(8).equalsIgnoreCase("DNA") || matcher.group(8).equalsIgnoreCase("ds-DNA")));
    } finally {
      readbuffer.close();
    }
  }

  /**
   * Checks if the given file is a VNTFile that contains a protein by reference to the LOCUS tag in the .gb file
   * 
   * @param file the file to be checked.
   * @return true if the given file is a genbank file that contains a protein.
   * @throws IOException
   */
  public static boolean isVNTProteinFile(String file) throws IOException {
    Pattern pattern = Pattern.compile(PATTERN_HEADER);
    BufferedReader readbuffer = null;
    try {
      readbuffer = new BufferedReader(new StringReader(file));
      String strRead;
      strRead = readbuffer.readLine();
      Matcher matcher = pattern.matcher(strRead);

      return (matcher.matches() && matcher.group(6).equalsIgnoreCase("aa"));
    } finally {
      readbuffer.close();
    }
  }

  /**
   * Checks if the input really starts with a '>' character. Has to be refined eventually
   * 
   * @param arg the uploaded sequence
   * @return true or false
   */
  public static boolean isFastaFile(String arg) {
    return (arg.trim().indexOf(">") == 0);
  }

}
