/**
 * TetrisTest.java
 * 
 * Created on 2-Nov-04
 */
package org.oversoul.tetris.util;

import org.oversoul.networking.NetUtil;

/**
 * Test class for testing parts of Tetris.
 * 
 * @author 		nyef
 * @date		2-Nov-04, 11:56:38 PM
 * @version 	1.0
 */
public class TetrisTest {

	/**
	 * Constructor for TetrisTest.java
	 */
	public TetrisTest() {
	}

	public static void main(String[] args) {

		byte[] b = new byte[] {6};	
		NetUtil.bit2byte(b, 0, false, 0);
		NetUtil.bit2byte(b, 0, false, 2);
		NetUtil.bit2byte(b, 0, false, 4);
		NetUtil.bit2byte(b, 0, true, 6);
		
	}
}
