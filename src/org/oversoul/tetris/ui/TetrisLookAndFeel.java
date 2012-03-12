/**
 * TetrisLookAndFeel.java
 */
package org.oversoul.tetris.ui;

public enum TetrisLookAndFeel {

	//Windows(""),
	WindowsXP("com.sun.java.swing.plaf.windows.WindowsLookAndFeel"),
	Metal("javax.swing.plaf.metal.MetalLookAndFeel"),
	Motif("com.sun.java.swing.plaf.motif.MotifLookAndFeel"),
	GTK("com.sun.java.swing.plaf.gtk.GTKLookAndFeel"),
	Plastic("com.jgoodies.looks.plastic.PlasticLookAndFeel"),
	Plastic3D("com.jgoodies.looks.plastic.Plastic3DLookAndFeel"),
	PlasticXP("com.jgoodies.looks.plastic.PlasticXPLookAndFeel");
	
	private String className;
	
	TetrisLookAndFeel(String className) {
		this.className = className;
	}
	
	public String getClassName() {
		return className;
	}
	
	public String getName() {
		return this.name();
	}
	
	public void load() throws Exception {
		
	}
}

