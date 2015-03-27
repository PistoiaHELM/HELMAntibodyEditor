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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import org.helm.notation.model.Attachment;
import org.helm.notation.model.Monomer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import chemaxon.formats.MolImporter;
import chemaxon.marvin.plugin.PluginException;
import chemaxon.struc.MolAtom;
import chemaxon.struc.MolBond;
import chemaxon.struc.Molecule;

/**
 * 
 * {@code MonomerUtils} Contains methods needed for monomer registration
 * 
 * @author <b>Jutta Fichtner:</b> fichtner AT quattro-research DOT com
 * @version $Id: MonomerUtils.java 15173 2015-03-05 16:18:13Z schirmb $
 */
public class MonomerUtils {

  /** The Logger for this class */
  private static final Logger LOG = LoggerFactory.getLogger(MonomerUtils.class);

  private static Map<String, Integer> molAtomMap = new HashMap<String, Integer>() {

    private static final long serialVersionUID = 1L;

    {
      put("H", 1);
      put("He", 2);
      put("Li", 3);
      put("Be", 4);
      put("B", 5);
      put("C", 6);
      put("N", 7);
      put("O", 8);
      put("F", 9);
      put("Ne", 10);
      put("Na", 11);
      put("Mg", 12);
      put("Al", 13);
      put("Si", 14);
      put("P", 15);
      put("S", 16);
      put("Cl", 17);
      put("Ar", 18);
    }
  };

  private static Pattern elementSymbolPattern = Pattern.compile("[A-Z]{1}[a-z]?");

  private final static String R_GROUP = "R#";

  /**
   * Takes the given {@code Monomer} and converts it into a {@code Molecule} to retrieve the molfile for this monomer
   * without r-groups and retrieves the {@code Monomer} with the new molfile. The r-groups will be replaced with the
   * leaving groups (e.g. OH, H, ...).
   * 
   * @param m the monomer
   * @return monomer with molfile without r-groups
   * @throws Exception
   */
  public static Monomer removeRGroupsfromMolfile(Monomer m) throws Exception {
    String molfile = m.getMolfile();

    Molecule molecule = null;
    InputStream is = null;
    try {
      is = new ByteArrayInputStream(molfile.getBytes());
      MolImporter importer = new MolImporter(is);
      molecule = importer.read();
    } finally {
      is.close();
    }

    // save all bonds
    MolBond[] bonds = molecule.getBondArray();

    Map<Integer, String> leavingGroups = extractLeavingGroups(m);

    for (int i = 0; i < molecule.getNodeCount(); i++) {
      LOG.debug(((MolAtom) molecule.getNode(i)).getSymbol());
      if (((MolAtom) molecule.getNode(i)).getSymbol().equalsIgnoreCase(R_GROUP)) {
        LOG.debug(((MolAtom) molecule.getNode(i)).getSymbol() + "");
        MolAtom oldMolAtom = (MolAtom) molecule.getNode(i);
        LOG.debug("R-Group: " + oldMolAtom.getRgroup());
        MolAtom newMolAtom =
            new MolAtom(molAtomMap.get(leavingGroups.get(oldMolAtom.getRgroup())), oldMolAtom.getX(),
                oldMolAtom.getY(), oldMolAtom.getZ());

        // Adapt bonds array to newMolAtom
        for (int j = 0; j < bonds.length; j++) {
          if (bonds[j].getAtom1().equals(oldMolAtom)) {
            bonds[j] = new MolBond(newMolAtom, bonds[j].getAtom2(), bonds[j].getFlags());
          }
          if (bonds[j].getAtom2().equals(oldMolAtom)) {
            bonds[j] = new MolBond(bonds[j].getAtom1(), newMolAtom, bonds[j].getFlags());
          }
        }
        molecule.setNode(i, newMolAtom);
        LOG.debug(((MolAtom) molecule.getNode(i)).getSymbol() + "");
      }
    }
    // remove all old bonds and add the new ones
    molecule.removeAllEdges();
    for (int i = 0; i < bonds.length; i++) {
      molecule.add(bonds[i]);
    }

    m.setMolfile(new String(molecule.toBinFormat("mol")));
    LOG.debug(m.getMolfile());
    return m;
  }

  /**
   * Returns a map containing the r-groups with their mol formula.
   * 
   * @param m the monomer
   * @return map<Integer, String> key is the r_group and value the r-group mol formula
   * @throws IOException
   * @throws PluginException
   */
  public static Map<Integer, String> extractLeavingGroups(Monomer m) throws Exception {
    Map<Integer, String> leavingGroups = new HashMap<Integer, String>();
    if (m.getAttachment(Attachment.BACKBONE_MONOMER_LEFT_ATTACHEMENT) != null
        && m.getAttachment(Attachment.BACKBONE_MONOMER_LEFT_ATTACHEMENT).getCapGroupName() != null) {
      leavingGroups.put(1, m.getAttachment(Attachment.BACKBONE_MONOMER_LEFT_ATTACHEMENT).getCapGroupName());
    }
    if (m.getAttachment(Attachment.BACKBONE_MONOMER_RIGHT_ATTACHEMENT) != null
        && m.getAttachment(Attachment.BACKBONE_MONOMER_RIGHT_ATTACHEMENT).getCapGroupName() != null) {
      leavingGroups.put(2, m.getAttachment(Attachment.BACKBONE_MONOMER_RIGHT_ATTACHEMENT).getCapGroupName());
    }
    if (m.getAttachment(Attachment.BACKBONE_MONOMER_BRANCH_ATTACHEMENT) != null
        && m.getAttachment(Attachment.BACKBONE_MONOMER_BRANCH_ATTACHEMENT).getCapGroupName() != null) {
      leavingGroups.put(3, m.getAttachment(Attachment.BACKBONE_MONOMER_BRANCH_ATTACHEMENT).getCapGroupName());
    }

    for (Integer i : leavingGroups.keySet()) {
      LOG.debug(i + " : " + leavingGroups.get(i));
      if (leavingGroups.get(i).equalsIgnoreCase("HO") || leavingGroups.get(i).equalsIgnoreCase("OH")) {
        leavingGroups.put(i, "O");
        LOG.debug(i + " : " + leavingGroups.get(i));
      } else if (!(elementSymbolPattern.matcher(leavingGroups.get(i)).matches())) {
        throw new Exception("The following leaving group " + leavingGroups.get(i)
            + " can not yet be resolved to a element symbol. Please contact your administrator");
      }
    }

    return leavingGroups;
  }

}
