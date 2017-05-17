/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.changeset;

public final class ChangeSetViewEventConstants {

	private static final String EVENT_PREFIX = "mmxlabs-change-set-view-events-";

	public static final String EVENT_TOGGLE_COMPARE_TO_BASE = EVENT_PREFIX + "toggle-compare-to-base";
	public static final String EVENT_TOGGLE_FILTER_NON_STRUCTURAL_CHANGES = EVENT_PREFIX + "toggle-filter-non-structural-changes";
	public static final String EVENT_ANALYSE_ACTION_SETS = EVENT_PREFIX + "analyse-action-sets";
	public static final String EVENT_ANALYSE_CHANGE_SETS = EVENT_PREFIX + "analyse-change-sets";
	public static final String EVENT_ANALYSE_INSERTION_PLANS = "analyse-insertion-plans";
	
	public static final String EVENT_TOGGLE_FILTER_INSERTION_CHANGES = EVENT_PREFIX + "toggle-filter-insertion-plan-duplicates";
	public static final String EVENT_SWITCH_GROUP_BY_MODE = EVENT_PREFIX + "switch-group-by-mode";


	public static final String EVENT_SET_VIEW_MODE = EVENT_PREFIX + "set-view-mode";

}
