/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.changeset;

public final class ChangeSetViewEventConstants {

	private static final String EVENT_PREFIX = "mmxlabs-change-set-view-events-";

	public static final String EVENT_EXPAND_ALL = EVENT_PREFIX + "expand-allDiff";
	public static final String EVENT_TOGGLE_ALTERNATIVE_MODE = EVENT_PREFIX + "toggle-compare-to-base";
	public static final String EVENT_TOGGLE_FILTER_NON_STRUCTURAL_CHANGES = EVENT_PREFIX + "toggle-filter-non-structural-changes";
	public static final String EVENT_TOGGLE_FILTER_NEGATIVE_PNL_CHANGES = EVENT_PREFIX + "toggle-filter-negative-pnl-changes";
	public static final String EVENT_ANALYSE_ACTION_SETS = EVENT_PREFIX + "analyse-action-sets";
	public static final String EVENT_ANALYSE_CHANGE_SETS = EVENT_PREFIX + "analyse-change-sets";
	public static final String EVENT_ANALYSE_INSERTION_PLANS = "analyse-insertion-plans";

	public static final String EVENT_SWITCH_GROUP_BY_MODE = EVENT_PREFIX + "switch-group-by-mode";
	public static final String EVENT_SWITCH_TARGET_SLOT = EVENT_PREFIX + "switch-target-slot";

	public static final String EVENT_SET_VIEW_MODE = EVENT_PREFIX + "set-view-mode";

}
