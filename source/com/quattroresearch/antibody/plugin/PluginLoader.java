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

import java.io.File;
import java.lang.reflect.Constructor;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import org.apache.commons.io.FilenameUtils;
import org.roche.antibody.model.antibody.Domain;
import org.roche.antibody.services.PreferencesService;
import org.roche.antibody.services.UIService;
import org.roche.antibody.ui.actions.menu.AbstractEditorAction;
import org.roche.antibody.ui.toolbar.buttons.ToolBarToggleButton;

import com.quattroresearch.antibody.ISequenceFileReader;

/**
 * 
 * {@code PluginLoader} loads configured plugins from plugin subfolder
 * 
 * @author <b>Stefan Klostermann:</b> Stefan DOT Klostermann AT roche DOT com, Roche Pharma Research and Early
 *         Development - Informatics, Roche Innovation Center Penzberg
 * @author <b>Marco Lanig:</b> lanig AT quattro-research DOT com, quattro research GmbH
 * 
 * @version $Id: PluginLoader.java 15209 2015-03-09 10:24:30Z schirmb $
 */
public class PluginLoader {

  /** The Logger for this class */
  private static final Logger LOG = Logger.getLogger(PluginLoader.class.toString());

  private static PluginLoader _instance;

  private static QRClassLoader classLoader;

  private PluginLoader() {
  }

  public static PluginLoader getInstance() {
    if (_instance == null) {
      _instance = new PluginLoader();
      _instance.initPlugins();
    }
    return _instance;
  }

  /**
   * Searches the plugins and initializes the classloader
   */
  public void initPlugins() {
    List<File> jarFiles = new LinkedList<File>();
    File[] files = new File("./plugins").listFiles();

    jarFiles.addAll(findFilesInDirRecursively(files));

    classLoader = new QRClassLoader(jarFiles);
  }

  private List<File> findFilesInDirRecursively(File[] directory) {
    List<File> filesInDir = new LinkedList<File>();
    for (File file : directory) {
      if (FilenameUtils.getExtension(file.getPath()).equals("jar")) {
        filesInDir.add(file);
      }
      if (file.isDirectory()) {
        filesInDir.addAll(findFilesInDirRecursively(file.listFiles()));
      }
    }
    return filesInDir;
  }

  /**
   * Loads the menu plugins and inserts them into the menu.
   */
  public void loadMenuPlugins() {
    String menuPluginConfig = PreferencesService.getInstance().getApplicationPrefs().getString("plugins.menu");
    if (menuPluginConfig == null) {
      return;
    }
    String[] menuPlugins = menuPluginConfig.split(";");
    for (String singleConfig : menuPlugins) {
      try {

        Class<?> clazz = classLoader.findClass(singleConfig);

        if (clazz != null) {
          Constructor<?> constructor;

          constructor = clazz.getDeclaredConstructor(JFrame.class);

          AbstractEditorAction action =
              (AbstractEditorAction) constructor.newInstance(UIService.getInstance().getMainFrame());

          boolean isAdded = false;
          JMenuBar menubar = UIService.getInstance().getMainFrame().getJMenuBar();
          for (int i = 0; i < menubar.getMenuCount(); i++) {
            JMenu menu = menubar.getMenu(i);
            if (menu.getText().equals(action.getMenuName())) {
              menu.add(action);
              isAdded = true;
              break;
            }
          }
          if (!isAdded) {
            JMenu newMenu = new JMenu(action.getMenuName());
            newMenu.add(action);
            menubar.add(newMenu, menubar.getMenuCount() - 1);
          }
        }
      } catch (ClassNotFoundException exc) {
        System.err.println("Menu Plugin class was not found: " + exc.toString());
      } catch (Exception exc) {
        System.err.println(exc.toString());
      }
    }
  }

  /**
   * Loads the toolbar plugins and inserts them into the toolbar.
   */
  public void loadToolbarPlugins() {
    String toolbarPluginConfig = PreferencesService.getInstance().getApplicationPrefs().getString("plugins.toolbar");
    if (toolbarPluginConfig == null) {
      return;
    }
    String[] toolbarPlugins = toolbarPluginConfig.split(";");
    for (String singleConfig : toolbarPlugins) {
      try {

        Class<?> clazz = classLoader.findClass(singleConfig);

        if (clazz != null) {
          Constructor<?> constructor;

          constructor = clazz.getDeclaredConstructor(JFrame.class);

          ToolBarToggleButton toolBarButton =
              (ToolBarToggleButton) constructor.newInstance(UIService.getInstance().getMainFrame());

          UIService.getInstance().getToolBar().add(toolBarButton);
        }

      } catch (ClassNotFoundException exc) {
        System.err.println("Toolbar Plugin class was not found: " + exc.toString());
      } catch (Exception exc) {
        System.err.println(exc.toString());
      }
    }
  }

  /**
   * Loads the plugins that are offered in a domains context menu.
   * 
   * @param popup
   * @param domain
   */
  public void loadDomainPopupPlugins(JPopupMenu popup, Domain domain) {
    String popupConfig = PreferencesService.getInstance().getApplicationPrefs().getString("plugins.popup.domain");
    if (popupConfig == null) {
      return;
    }
    String[] popupPlugins = popupConfig.split(";");
    try {
      for (String singleConfig : popupPlugins) {

        Class<?> clazz = classLoader.findClass(singleConfig);

        if (clazz != null) {
          Constructor<?> constructor;

          constructor = clazz.getDeclaredConstructor(JFrame.class, Domain.class);

          JMenuItem item =
              (JMenuItem) constructor.newInstance(UIService.getInstance().getMainFrame(), domain);

          popup.add(item);

        }

      }
    } catch (ClassNotFoundException exc) {
      System.err.println("Toolbar Plugin class was not found: " + exc.toString());
    } catch (Exception exc) {
      System.err.println(exc.toString());
    }
  }

  /**
   * Loads custom sequence file reader plugin that is used when opening file for domain detection. When multiple
   * filereaders are configured, only the first is returned.
   * 
   * @return sequence file reader
   */
  public ISequenceFileReader loadSequenceFileReaderPlugin() {
    String readerPluginConfig =
        PreferencesService.getInstance().getApplicationPrefs().getString("plugins.filereader.sequence");
    if (readerPluginConfig == null)
      return null;
    String[] readerPlugins = readerPluginConfig.split(";");
    try {
      for (String singleConfig : readerPlugins) {

        Class<?> clazz = classLoader.findClass(singleConfig);

        if (clazz != null) {
          Constructor<?> constructor;

          constructor = clazz.getDeclaredConstructor();

          ISequenceFileReader reader =
              (ISequenceFileReader) constructor.newInstance();

          return reader;
        }

      }
    } catch (ClassNotFoundException exc) {
      System.err.println("Sequence Reader Plugin class was not found: " + exc.toString());
    } catch (Exception exc) {
      System.err.println(exc.toString());
    }
    return null;
  }
}
