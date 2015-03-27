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
package org.roche.antibody;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URL;
import java.util.logging.Logger;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

import org.helm.editor.utility.IconGenerator;
import org.roche.antibody.services.CommandLineParameters;
import org.roche.antibody.services.PreferencesService;
import org.roche.antibody.ui.resources.ResourceProvider;

/**
 * 
 * {@code HELMAntibodyEditor} contains main routine that initializes antibody
 * editor.
 * 
 * @author <b>Stefan Klostermann:</b> Stefan DOT Klostermann AT roche DOT com,
 *         Roche Pharma Research and Early Development - Informatics, Roche
 *         Innovation Center Penzberg
 * @author <b>Marco Lanig:</b> lanig AT quattro-research DOT com, quattro
 *         research GmbH
 * 
 * @version $Id: HELMAntibodyEditor.java 15399 2015-03-23 13:45:45Z schirmb $
 */
public class HELMAntibodyEditor extends JFrame {

	/** default */
	private static final long serialVersionUID = 1L;

	/** The Logger for this class */
	@SuppressWarnings("unused")
	private static final Logger LOG = Logger.getLogger(HELMAntibodyEditor.class
			.toString());

	public HELMAntibodyEditor() throws Exception {
		setTitle(generateApplicationTitle());
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setPreferredSize(new Dimension(1024, 768));

		AntibodyEditorGUI antibodyEditor = new AntibodyEditorGUI(this);
		getContentPane().add(antibodyEditor.getContentPanel());

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				dispose();
				System.exit(0);
			}
		});

		URL url = ResourceProvider.getInstance().get("antibody_32.png");
		Toolkit kit = Toolkit.getDefaultToolkit();
		ImageIcon img = new ImageIcon(kit.createImage(url));
		this.setIconImage(img.getImage());

		pack();
		setExtendedState(MAXIMIZED_BOTH);

	}

	/**
	 * @param args
	 *            the command line arguments
	 */
	public static void main(String args[]) {
		try {
			CommandLineParameters.getInstance().setCommandLineParameters(args);

			HELMAntibodyEditor mEditor = new HELMAntibodyEditor();
			mEditor.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	public static String generateApplicationTitle() {
		String environmentAddition = PreferencesService.getEnvironmentName();
		if (!environmentAddition.isEmpty()) {
			environmentAddition = " - " + environmentAddition;
		}

		return "HELMAntibodyEditor v1.1" + environmentAddition;
	}

}
