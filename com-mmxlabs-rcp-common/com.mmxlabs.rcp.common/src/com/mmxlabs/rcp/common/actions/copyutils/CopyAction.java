/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.rcp.common.actions.copyutils;

import org.eclipse.jface.action.Action;

/**
 * Common code for all copy to clip board type of actions
 * 
 * @author Andrey Popov
 *
 */
public abstract class CopyAction extends Action {
	
	private String[] dateFormatsToProcess = {};
	private String[] dateTimeFormatsToProcess = {};
	
	protected CopyAction(String text) {
		super(text);
	}

	/**
	 * Date formats that are expected to be treated as DateTime objects.
	 * 	 
	 * @return the list of formats that should be treated as Java.time objects.
	 */
	public final String[] getDateFormatsToProcess() {
		return dateFormatsToProcess;
	}

	/**
	 * Please, provide the date formats which you want to be parsed as valid LocalDateTime objects.
	 * That would enable user to paste data in a more standardised way and in a format that is
	 * more friendly to other applications, such as Microsoft Excel.
	 * 
	 * Date formats which are used in a report, but not included here will be considered as strings
	 * and copy pasted without modification. Unless it is a default day-month or month-day
	 * depending on the locale.
	 * 
	 * @param dateFormatsToProcess
	 */
	public final void setDateFormatsToProcess(String... dateFormatsToProcess) {
		this.dateFormatsToProcess = dateFormatsToProcess;
	}

	/**
	 * Same as {@link getDateFormatsToProcess}, but for Date Time, not just Date.
	 */
	public final String[] getDateTimeFormatsToProcess() {
		return dateTimeFormatsToProcess;
	}

	/**
	 * Same as {@link setDateFormatsToProcess}, but for Date Time, not just Date.
	 */
	public final void setDateTimeFormatsToProcess(String... dateTimeFormatsToProcess) {
		this.dateTimeFormatsToProcess = dateTimeFormatsToProcess;
	}
	
	/**
	 * Same as {@link setDateFormatsToProcess}, but builder style.
	 */
	public final CopyAction withDateFormatsToProcess(String... dateFormats) {
		setDateFormatsToProcess(dateFormats);
		return this;
	}
	
	/**
	 * Same as {@link setDateTimeFormatsToProcess}, but builder style.
	 */
	public final CopyAction withDateTimeFormatsToProcess(String... dateTimeFormats) {
		setDateFormatsToProcess(dateTimeFormats);
		return this;
	}
	
	/**
	 * Same as {@link CopyUtils.reformatText} but wrapper
	 */
	public final String reformatText(String itemText) {
		return CopyUtils.reformatText(itemText, this);
	}
}
