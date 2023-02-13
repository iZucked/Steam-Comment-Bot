/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.scheduleview.views;

/**
 * Hierarchical key-value constants for Scheduler view settings, used in
 * IMemento and PreferenceStore.
 * 
 * @author proshun
 */
public final class SchedulerViewConstants {

	private SchedulerViewConstants() {

	}

	public static final String SortMode = "Sort.Mode";
	public static final String SortCategory = "Sort.Category";
	
	public static final String Show_ = "Show.";
	public static final String Show_Canals = "Show.Canals";
	public static final String Show_Nominals = "Show.Nominals";
	public static final String Show_Days = "Show.Days";

	public static final String Highlight_ = "Hi.";
//	public static final String HighlightScheme = "SchedulerView.HighlightScheme";
	public static final String Highlight_Riskies = "Hi.RiskyJourneys";

	public static final String SCHEDULER_VIEW_COLOUR_SCHEME = "SCHEDULER_VIEW_COLOUR_SCHEME";
}
