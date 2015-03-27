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
package com.quattroresearch.antibody.plugin;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

/**
 * 
 * {@code QRClassLoader} loads class from a list of jar files. Derived from a solution by <a
 * href="http://weblogs.java.net/blog/malenkov/archive/2008/07/how_to_load_cla.html">Sergey Malenkov</a>
 * 
 * @author <b>Stefan Klostermann:</b> Stefan DOT Klostermann AT roche DOT com, Roche Pharma Research and Early
 *         Development - Informatics, Roche Innovation Center Penzberg
 * @author <b>Marco Erdmann</b> erdmann AT quattro-research DOT com, quattro research GmbH
 * @author <b>Marco Lanig:</b> lanig AT quattro-research DOT com, quattro research GmbH
 * 
 * @version $Id: QRClassLoader.java 15209 2015-03-09 10:24:30Z schirmb $
 */
public class QRClassLoader extends ClassLoader {

  List<File> jarFiles;

  public QRClassLoader(List<File> jarFiles) {
    this.jarFiles = jarFiles;
  }

  @Override
  protected Class<?> findClass(String name) throws ClassNotFoundException {
    Class<?> alreadyLoadedClass = findLoadedClass(name);
    if (alreadyLoadedClass != null) {
      return alreadyLoadedClass;
    }

    try {
      for (File jar : jarFiles) {
        Class<?> clazz = findClassInJarFile(name, jar);

        if (clazz == null) {
          continue;
        } else {
          return clazz;
        }
      }
      throw new ClassNotFoundException("Class " + name + " not found in given list of jarFiles.");
    } catch (IOException exception) {
      throw new ClassNotFoundException(name, exception);
    }
  }

  private Class<?> findClassInJarFile(String name, File jar) throws ZipException, IOException {
    ZipFile jarFile = null;
    jarFile = new ZipFile(jar);
    ZipEntry entry = jarFile
        .getEntry(name.replace('.', '/') + ".class");

    if (entry == null) {
      return null;
    } else {
      byte[] buffer = new byte[1024];
      InputStream in = jarFile.getInputStream(entry);
      ByteArrayOutputStream out = new ByteArrayOutputStream(
          buffer.length);
      int length = in.read(buffer);
      while (length > 0) {
        out.write(buffer, 0, length);
        length = in.read(buffer);
      }
      return defineClass(name, out.toByteArray(), 0, out.size());
    }
  }

}
