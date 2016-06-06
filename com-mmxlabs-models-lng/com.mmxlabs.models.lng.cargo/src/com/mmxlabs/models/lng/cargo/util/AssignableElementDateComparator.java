/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.util;

import java.time.ZonedDateTime;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.models.lng.cargo.AssignableElement;

/**
 * Comparator implementation to sort {@link AssignableElement} by time windows, then by sequence hint for overlapping elements. by sequence number.
 */
public class AssignableElementDateComparator implements IAssignableElementComparator {
	@Override
	public int compare(@Nullable final AssignableElement arg0, @Nullable final AssignableElement arg1) {
		final ZonedDateTime start0 = getStartDate(arg0);
		final ZonedDateTime start1 = getStartDate(arg1);
		final ZonedDateTime end0 = getEndDate(arg0);
		final ZonedDateTime end1 = getEndDate(arg1);

		if (start0 == null || end0 == null) {
			if (start1 == null || end1 == null) {
				return 0;
			} else {
				return -1;
			}
		} else if (start1 == null || end1 == null) {
			return 1;
		}

		// if two assignments don't overlap, sort by start date. Otherwise, sort
		if (overlaps(start0, end0, start1, end1) //
				|| contained(start0, end0, start1, end1) //
				|| contained(start1, end1, start0, end0) //
				) {
			return ((Integer) arg0.getSequenceHint()).compareTo(arg1.getSequenceHint());
		} else {
			return start0.compareTo(start1);
		}
	}

	protected boolean overlaps(@NonNull final ZonedDateTime start0, @NonNull final ZonedDateTime end0, @NonNull final ZonedDateTime start1, @NonNull final ZonedDateTime end1) {
		return !(end0.isBefore(start1) || end1.isBefore(start0));
	}
	protected boolean contained(@NonNull final ZonedDateTime start0, @NonNull final ZonedDateTime end0, @NonNull final ZonedDateTime start1, @NonNull final ZonedDateTime end1) {
		return end0.isBefore(end1) && start0.isAfter(start1);
	}

	protected ZonedDateTime getStartDate(@Nullable final AssignableElement element) {

		return AssignmentEditorHelper.getStartDateIgnoreSpots(element);
	}

	protected ZonedDateTime getEndDate(@Nullable final AssignableElement element) {

		return AssignmentEditorHelper.getEndDateIgnoreSpots(element);
	}
}
