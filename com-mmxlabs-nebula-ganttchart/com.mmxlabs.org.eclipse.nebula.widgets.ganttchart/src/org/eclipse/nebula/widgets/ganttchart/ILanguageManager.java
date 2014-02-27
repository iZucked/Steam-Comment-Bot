/*******************************************************************************
 * Copyright (c) Emil Crumhorn - Hexapixel.com - emil.crumhorn@gmail.com
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    emil.crumhorn@gmail.com - initial API and implementation
 *******************************************************************************/

package org.eclipse.nebula.widgets.ganttchart;

/**
 * Interface that contains methods for returning all string-values used within the chart so that they can be customized to fit different languages easily.
 * 
 * @author Emil
 *
 */
public interface ILanguageManager {

	/**
	 * The text drawn for the zoom level box.
	 * 
	 * @return Text
	 */
	String getZoomLevelText();
	
	/**
	 * The text drawn instead of a number when the zoom level reaches its minimum.
	 * 
	 * @return Text
	 */
	String getZoomMinText();

	/**
	 * The text drawn instead of a number when the zoom level reaches its maximum.
	 * 
	 * @return Text
	 */
	String getZoomMaxText();
	
	/**
	 * The menu item text for the "Zoom in" menu item
	 * 
	 * @return Text
	 */
	String getZoomInMenuText();
	
	/**
	 * The menu item text for the "Zoom out" menu item
	 * 
	 * @return Text
	 */
	String getZoomOutMenuText();
	
	/**
	 * The menu item text for the "Reset Zoom Level" menu item
	 * 
	 * @return Text
	 */
	String getZoomResetMenuText();
	
	/**
	 * The menu item text for the "Show number of days on events" menu item 
	 * 
	 * @return Text
	 */
	String getShowNumberOfDaysOnEventsMenuText();
	
	/**
	 * The menu item text for the the "Show planned dates" menu item 
	 * 
	 * @return Text
	 */
	String getShowPlannedDatesMenuText();
	
	/**
	 * The menu item text for the "3D events" menu item
	 * 
	 * @return Text
	 */
	String get3DMenuText();
	
	/**
	 * The menu item text for the "Delete" menu item
	 * 
	 * @return Text
	 */
	String getDeleteMenuText();
	
	/**
	 * The menu item text for the "Properties" menu item
	 * 
	 * @return Text
	 */
	String getPropertiesMenuText();
	
	/**
	 * The text drawn for the word "Planned", used in tooltips
	 * 
	 * @return Text
	 */
	String getPlannedText();
	
	/**
	 * The text drawn for the word "Revised", used in tooltips 
	 * 
	 * @return Text
	 */
	String getRevisedText();

	/**
	 * The text drawn for the word "day", used in tooltips
	 * 
	 * @return Text
	 */
	String getDaysText();
	
	/**
	 * The text drawn for the word "days", used in tooltips
	 * 
	 * @return Text
	 */
	String getDaysPluralText();
	
	/**
	 * The text drawn for the word "% complete", used in tooltips
	 * 
	 * @return Text
	 */
	String getPercentCompleteText();
	
	/**
	 * The text drawn for the word "n/a", used in tooltips
	 * 
	 * @return Text
	 */
	String getNotAvailableText();
}
