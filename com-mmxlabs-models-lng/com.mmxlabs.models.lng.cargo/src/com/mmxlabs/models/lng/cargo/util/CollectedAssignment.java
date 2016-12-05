/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.util;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.models.lng.cargo.AssignableElement;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.cargo.util.AssignmentEditorHelper.OrderingHint;
import com.mmxlabs.models.lng.cargo.util.scheduling.WrappedAssignableElement;
import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.mmxcore.NamedObject;

/**
 * Utility class representing a sequence from the input model. Because the input model is now held as per-element assignment classes, this is needed to glom everything together.
 * 
 * @author hinton
 * 
 */
public class CollectedAssignment {

	private final VesselAvailability vesselAvailability;
	private final CharterInMarket charterInMarket;
	private final @NonNull List<@NonNull AssignableElement> assignments;
	private final Integer spotIndex;

	public CollectedAssignment(final @NonNull List<@NonNull AssignableElement> assignments, final @NonNull VesselAvailability vesselAvailability, final @Nullable PortModel portModel,
			final @Nullable IAssignableElementDateProvider dateProvider) {
		this.vesselAvailability = vesselAvailability;
		this.charterInMarket = null;
		this.spotIndex = null;
		this.assignments = sortAssignments(assignments, portModel, dateProvider);
	}

	public CollectedAssignment(final @NonNull List<@NonNull AssignableElement> assignments, final @NonNull CharterInMarket charterInMarket, final int spotIndex, final @Nullable PortModel portModel,
			final @Nullable IAssignableElementDateProvider dateProvider) {
		this.vesselAvailability = null;
		this.charterInMarket = charterInMarket;
		this.spotIndex = spotIndex;
		// -1 is the nominal cargoes, so no need to sort
		this.assignments = spotIndex == -1 ? assignments : sortAssignments(assignments, portModel, dateProvider);
	}

	public @NonNull List<@NonNull AssignableElement> getAssignedObjects() {
		return Collections.unmodifiableList(assignments);
	}

	public boolean isSpotVessel() {
		return vesselAvailability == null;
	}

	public CharterInMarket getCharterInMarket() {
		return charterInMarket;
	}

	public VesselAvailability getVesselAvailability() {
		return vesselAvailability;
	}

	public boolean isSetSpotIndex() {
		return spotIndex != null;
	}

	public int getSpotIndex() {
		return spotIndex == null ? 0 : spotIndex.intValue();
	}

	private static @NonNull List<@NonNull AssignableElement> sortAssignments(@NonNull final List<@NonNull AssignableElement> assignments, final @Nullable PortModel portModel,
			final @Nullable IAssignableElementDateProvider dateProvider) {

		final @NonNull List<@NonNull WrappedAssignableElement> sortedElements = new LinkedList<>();
		for (final AssignableElement ae : assignments) {
			final WrappedAssignableElement e = new WrappedAssignableElement(ae, portModel, dateProvider);
			sortedElements.add(e);
		}
		sortWrappedAssignableElements(sortedElements);
		// Unwrap list
		return sortedElements.stream().map(e -> e.getAssignableElement()).collect(Collectors.toList());
	}

	public static void sortWrappedAssignableElements(final List<@NonNull WrappedAssignableElement> sortedElements) {
		Comparator<? super @NonNull WrappedAssignableElement> comparator = createComparator();
		Collections.sort(sortedElements, comparator);
	}

	public static Comparator<@NonNull WrappedAssignableElement> createComparator() {
		Comparator<@NonNull WrappedAssignableElement> comparator = (a, b) -> {

			final OrderingHint hint = AssignmentEditorHelper.checkOrdering(a, b);
			switch (hint) {
			case AFTER:
				return 1;
			case BEFORE:
				return -1;
			case AMBIGUOUS:
			default:
				int c = 0;
				// Sequence hints of zero are unset, to avoid the sequence hint checks
				// Note is still possible that this can cause comparator contract issues.
				// In BugzId: 2290 a overlaps b and b overlaps c, but a and c do not overlap.
				// The date sorting (used as no overlap) says a<c, however sequence hints (used due to overlap) say b<a and c<b implying c<a!
				if (a.getSequenceHint() != 0 && b.getSequenceHint() != 0) {
					c = Integer.compare(a.getSequenceHint(), b.getSequenceHint());
					if (c != 0) {
						return c;
					}
				}
				c = a.getStartWindow().getSecond().compareTo(b.getStartWindow().getSecond());
				if (c != 0) {
					return c;
				}
				// Sort on name if possible
				if (a instanceof NamedObject && b instanceof NamedObject) {
					String strA = ((NamedObject) a).getName();
					String strB = ((NamedObject) b).getName();
					if (strA != null && strB != null) {
						c = strA.compareTo(strB);
					}
				}
				return c;
			}

		};
		return comparator;
	}
}
