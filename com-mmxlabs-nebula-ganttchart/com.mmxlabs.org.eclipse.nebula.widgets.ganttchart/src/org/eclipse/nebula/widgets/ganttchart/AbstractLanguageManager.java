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

public abstract class AbstractLanguageManager implements ILanguageManager {

	public String getZoomMaxText() {
		return "Max";
	}

	public String getZoomMinText() {
		return "Min";
	}

	public String getZoomLevelText() {
		return "Zoom Level";
	}

	public String getDaysPluralText() {
		return "days";
	}

	public String getDaysText() {
		return "day";
	}

	public String getNotAvailableText() {
		return "n/a";
	}

	public String getPercentCompleteText() {
		return "% complete";
	}

	public String getPlannedText() {
		return "Planned";
	}

	public String getRevisedText() {
		return "Revised";
	}

	public String get3DMenuText() {
		return "3D Bars";
	}

	public String getDeleteMenuText() {
		return "Delete";
	}

	public String getPropertiesMenuText() {
		return "Properties";
	}

	public String getShowNumberOfDaysOnEventsMenuText() {
		return "Show Number of Days on Events";
	}

	public String getShowPlannedDatesMenuText() {
		return "Show Planned Dates";
	}

	public String getZoomInMenuText() {
		return "Zoom In";
	}

	public String getZoomOutMenuText() {
		return "Zoom Out";
	}

	public String getZoomResetMenuText() {
		return "Reset Zoom Level";
	}

	

}
