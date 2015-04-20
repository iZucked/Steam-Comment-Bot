/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.util;

import org.joda.time.DateTime;

import com.mmxlabs.models.lng.cargo.AssignableElement;

/**
 * Comparator implementation to sort {@link AssignableElement} by time windows, then by sequence hint for overlapping elements. by sequence number.
 */
public class AssignableElementDateComparator implements IAssignableElementComparator {
	@Override
	public int compare(final AssignableElement arg0, final AssignableElement arg1) {
		final DateTime start0 = getStartDate(arg0);
		final DateTime start1 = getStartDate(arg1);
		final DateTime end0 = getEndDate(arg0);
		final DateTime end1 = getEndDate(arg1);

		final boolean null0 = start0 == null || end0 == null;
		final boolean null1 = start1 == null || end1 == null;

		if (null0) {
			if (null1) {
				return 0;
			} else {
				return -1;
			}
		} else if (null1) {
			return 1;
		}

		// if two assignments don't overlap, sort by start date. Otherwise, sort
		if (overlaps(start0, end0, start1, end1)) {
			return ((Integer) arg0.getSequenceHint()).compareTo(arg1.getSequenceHint());
		} else {
			return start0.compareTo(start1);
		}
	}

	protected boolean overlaps(final DateTime start0, final DateTime end0, final DateTime start1, final DateTime end1) {
		return !(end0.isBefore(start1) || end1.isBefore(start0));
	}

	protected DateTime getStartDate(final AssignableElement element) {
		return AssignmentEditorHelper.getStartDate(element);
	}

	protected DateTime getEndDate(final AssignableElement element) {
		return AssignmentEditorHelper.getEndDate(element);

	}
}
