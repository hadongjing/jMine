package jmine;

import javax.swing.SwingUtilities;

import cn.hxd.jmine.view.JMineFrame;

/**
 *
 * @author jaux
 */
public class Main {

	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			new JMineFrame().setVisible(true);
		});
	}

}
