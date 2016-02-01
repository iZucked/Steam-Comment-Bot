/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.components;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.viewers.ViewerCell;

/**
 * Interface to return the number of cells this cell belongs to {@link ViewerCell#ABOVE} the current one.
 * 
 * @author sg
 * 
 */
public interface IRowSpanProvider {
	/**
	 * Return 0 by default.
	 * @param cell
	 * @param object
	 * @return
	 */
	int getRowSpan(@NonNull ViewerCell cell, @Nullable Object object);
}
