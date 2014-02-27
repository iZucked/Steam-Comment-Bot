/*******************************************************************************
 * Copyright (c) Emil Crumhorn - Hexapixel.com - emil.crumhorn@gmail.com
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    emil.crumhorn@gmail.com - initial API and implementation
 *******************************************************************************/ 

package org.eclipse.nebula.widgets.ganttchart;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.Shell;

public class Utils {

	/**
	 * Takes a font and gives it a bold typeface.
	 * 
	 * @param font Font to modify
	 * @return Font with bold typeface 
	 */
	public static Font applyBoldFont(final Font font) {
		if (font == null) {
			return null;
		}

		final FontData[] fontDataArray = font.getFontData();
		if (fontDataArray == null) {
			return null;
		}
		for (int index = 0; index < fontDataArray.length; index++) {
		    final FontData fData = fontDataArray[index];
			fData.setStyle(SWT.BOLD);
		}

		return new Font(Display.getDefault(), fontDataArray);
	}

	/**
	 * Applies a certain font size to a font.
	 * 
	 * @param font Font to modify
	 * @param size New font size
	 * @return Font with new font size
	 */
	public static Font applyFontSize(final Font font, final int size) {
		if (font == null) {
			return null;
		}

		final FontData[] fontDataArray = font.getFontData();
		if (fontDataArray == null) {
			return null;
		}
		for (int index = 0; index < fontDataArray.length; index++) {
		    final FontData fData = fontDataArray[index];
			fData.setHeight(size);
		}

		return new Font(Display.getDefault(), fontDataArray);
	}

	/**
	 * Centers a dialog (Shell) on the <b>primary</b> (active) display.
	 * 
	 * @param shell Shell to center on screen
	 * @see Shell
	 */
	public static void centerDialogOnScreen(final Shell shell) {
		// do it by monitor to support dual-head cards and still center things correctly onto the screen people are on.
	    final Monitor monitor = Display.getDefault().getPrimaryMonitor();
	    final Rectangle bounds = monitor.getBounds();

	    final int screen_x = bounds.width;
	    final int screen_y = bounds.height;

		shell.setLocation(screen_x / 2 - (shell.getBounds().width / 2), screen_y / 2 - (shell.getBounds().height / 2));
	}
}
