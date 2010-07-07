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

	@Override
	public String getZoomMaxText() {
		return "Max";
	}

	@Override
	public String getZoomMinText() {
		return "Min";
	}

	@Override
	public String getZoomLevelText() {
		return "Zoom Level";
	}

	@Override
	public String getDaysPluralText() {
		return "days";
	}

	@Override
	public String getDaysText() {
		return "day";
	}

	@Override
	public String getNotAvailableText() {
		return "n/a";
	}

	@Override
	public String getPercentCompleteText() {
		return "% complete";
	}

	@Override
	public String getPlannedText() {
		return "Planned";
	}

	@Override
	public String getRevisedText() {
		return "Revised";
	}

	@Override
	public String get3DMenuText() {
		return "3D Bars";
	}

	@Override
	public String getDeleteMenuText() {
		return "Delete";
	}

	@Override
	public String getPropertiesMenuText() {
		return "Properties";
	}

	@Override
	public String getShowNumberOfDaysOnEventsMenuText() {
		return "Show Number of Days on Events";
	}

	@Override
	public String getShowPlannedDatesMenuText() {
		return "Show Planned Dates";
	}

	@Override
	public String getZoomInMenuText() {
		return "Zoom In";
	}

	@Override
	public String getZoomOutMenuText() {
		return "Zoom Out";
	}

	@Override
	public String getZoomResetMenuText() {
		return "Reset Zoom Level";
	}

	

}
