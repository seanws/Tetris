/*******************************************************************************
 * Copyright 2005, CHISEL Group, University of Victoria, Victoria, BC, Canada.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     The Chisel Group, University of Victoria
 *******************************************************************************/
package org.oversoul.tetris;


/**
 * 
 * 
 * @author ccallendar
 */
public interface IDebugger {

	/** Handles a debug message. */
	void debug(String msg);
	
	/** Handles a debug exception. */
	void debug(Exception e);
	
	/** Handles a message and an exception. */
	void debug(String msg, Exception e);
	
}
