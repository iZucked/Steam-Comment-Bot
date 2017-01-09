/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package org.eclipse.nebula.widgets.ganttchart;

import org.eclipse.swt.graphics.Color;

/**
 * Interface representing all methods that use colored fills.
 *  
 * @author Emil
 *
 */
public interface IFillBackgroundColors {
	
	/**
	 * The top background gradient color used for drawing the Saturday column.
	 * 
	 * @return Color
	 */
	Color getSaturdayBackgroundColorTop();
	
	/**
	 * The bottom background gradient color used for drawing the Saturday column.
	 * 
	 * @return Color
	 */
	Color getSaturdayBackgroundColorBottom();
	
	/**
	 * The top background gradient color used for drawing the Sunday column.
	 * 
	 * @return Color
	 */
	Color getSundayBackgroundColorTop();

	/**
	 * The bottom background gradient color used for drawing the Sunday column.
	 * 
	 * @return Color
	 */
	Color getSundayBackgroundColorBottom();
	
	/**
	 * The bottom background gradient color used for drawing the weekday column.
	 * 
	 * @return Color
	 */
	Color getWeekdayBackgroundColorBottom();	

	/**
	 * The top background gradient color used for drawing the weekday column.
	 * 
	 * @return Color
	 */
	Color getWeekdayBackgroundColorTop();

	/**
	 * The top background gradient color used for drawing selected columns.
	 * 
	 * @return
	 */
	Color getSelectedDayColorTop();
	
	/**
	 * The bottom background gradient color used for drawing selected columns.
	 *  
	 * @return Color
	 */
	Color getSelectedDayColorBottom();

	/**
	 * The top background gradient color used for drawing selected columns in the header section.
	 * 
	 * @return Color
	 */
	Color getSelectedDayHeaderColorTop();

	/**
	 * The bottom background gradient color used for drawing selected columns in the header section.
	 * 
	 * @return Color
	 */
	Color getSelectedDayHeaderColorBottom();
}
