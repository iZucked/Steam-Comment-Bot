/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.util;

import java.util.Collections;
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
		this.assignments = sortAssignments(assignments, portModel, dateProvider);
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

		Collections.sort(sortedElements, (a, b) -> {

			final OrderingHint hint = AssignmentEditorHelper.checkOrdering(a, b);
			switch (hint) {
			case AFTER:
				return 1;
			case BEFORE:
				return -1;
			case AMBIGUOUS:
			default:
				int c = Integer.compare(a.getSequenceHint(), b.getSequenceHint());
				if (c != 0) {
					return c;
				}
				c = a.getStartWindow().getSecond().compareTo(b.getStartWindow().getSecond());
				if (c != 0) {
					return c;
				}
//				c = a.getName().compareTo(b.getName());
				return c;
			}

		});
		// Unwrap list
		return sortedElements.stream().map(e -> e.getAssignableElement()).collect(Collectors.toList());
	}
}
