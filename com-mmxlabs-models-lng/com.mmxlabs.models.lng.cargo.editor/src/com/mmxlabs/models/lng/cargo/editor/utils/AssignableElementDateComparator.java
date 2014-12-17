/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2014
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.editor.utils;

import java.util.Date;

import com.mmxlabs.models.lng.cargo.AssignableElement;

/**
 * Comparator implementation to sort {@link AssignableElement} by time windows, then by sequence hint for overlapping elements. by sequence number.
 */
public class AssignableElementDateComparator implements IAssignableElementComparator {
	@Override
	public int compare(final AssignableElement arg0, final AssignableElement arg1) {
		final Date start0 = getStartDate(arg0);
		final Date start1 = getStartDate(arg1);
		final Date end0 = getEndDate(arg0);
		final Date end1 = getEndDate(arg1);

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

	protected boolean overlaps(final Date start0, final Date end0, final Date start1, final Date end1) {
		return !(end0.before(start1) || end1.before(start0));
	}

	protected Date getStartDate(final AssignableElement element) {
		return AssignmentEditorHelper.getStartDate(element);
	}

	protected Date getEndDate(final AssignableElement element) {
		return AssignmentEditorHelper.getEndDate(element);

	}
}
