package com.mmxlabs.scheduler.optimiser.moves.util;

import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequenceElement;

public class MoveHandlerHelper {

	@Inject
	private @NonNull MoveHelper helper;

	@NonNull
	public List<ISequenceElement> extractSegment(@NonNull ISequence fromSequence, @NonNull ISequenceElement element) {
		final List<ISequenceElement> orderedCargoElements = new LinkedList<>();
		//// Find full cargo sequence based on element

		// Part 1, find the segment start
		int segmentStart = -1;
		boolean lastElementWasLoad = false;
		for (int i = 0; i < fromSequence.size(); ++i) {
			final ISequenceElement e = fromSequence.get(i);

			if (helper.isLoadSlot(e)) {
				if (!lastElementWasLoad || segmentStart == -1) {
					segmentStart = i;
				}
				lastElementWasLoad = true;
			} else if (!helper.isDischargeSlot(e)) {
				lastElementWasLoad = false;
				segmentStart = -1;
			} else {
				lastElementWasLoad = false;
			}

			if (e == element) {
				if (segmentStart == -1) {
					segmentStart = i;
				}
				break;
			}
		}
		if (segmentStart == -1) {
			throw new IllegalStateException();
			// return null;
		}

		// Part 2 - find full segment extent
		boolean foundDischarge = false;
		for (int i = segmentStart; i < fromSequence.size(); ++i) {
			final ISequenceElement e = fromSequence.get(i);
			// Iterate through elements (they should be loads) until we reach a discharge.
			// The segment ends at the first non-discharge element.
			if (helper.isStartOrEndSlot(e)) {
				break;
			}
			if (!foundDischarge) {
				if (helper.isDischargeSlot(e)) {
					foundDischarge = true;
				} else {
					assert helper.isLoadSlot(e);
				}
			} else {
				if (!helper.isDischargeSlot(e)) {
					break;
				}
			}
			orderedCargoElements.add(e);
		}
		return orderedCargoElements;
	}
}
