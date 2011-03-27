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

/**
 * This interface represents one displayable item in the chart. It contains some basic methods used to determine how the item is presented.
 * 
 * @author Emil
 *
 */
public interface IGanttChartItem {

	/**
	 * Whether automatic row height is in effect.
	 * 
	 * @return True if row height is done automatically
	 */
	boolean isAutomaticRowHeight();

	/**
	 * Returns the fixed height of the row in pixels, if set.
	 * 
	 * @return Fixed row height in pixels
	 */
	int getFixedRowHeight();
	
	/**
	 * Sets the fixed height of this row in pixels.
	 *  
	 * @param height Height in pixels
	 */
	void setFixedRowHeight(int height);
	
	/**
	 * Flags fixed height off and returns row to automatic row height
	 */
	void setAutomaticRowHeight();
	
}
