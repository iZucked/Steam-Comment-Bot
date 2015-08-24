/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.diff;

import org.eclipse.jface.viewers.ISelection;

public final class CompareEventConstants {

	public static final String EVENT_PREFIX = "com-mmxlabs-lingo-reports-diff-events--";

	/**
	 * The {@link DiffGroupView} selection has changed, will post an {@link ISelection} object.
	 */
	public static final String EVENT_CHANGES_SELECTED = EVENT_PREFIX + "changes-selected";

}
