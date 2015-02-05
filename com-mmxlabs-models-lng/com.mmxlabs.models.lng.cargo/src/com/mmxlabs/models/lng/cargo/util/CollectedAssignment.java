/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.mmxlabs.models.lng.cargo.AssignableElement;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
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
	private final ArrayList<AssignableElement> assignedObjects = new ArrayList<AssignableElement>();
	private List<AssignableElement> assignments = null;
	private final Integer spotIndex;

	public CollectedAssignment(final List<AssignableElement> assignments, final VesselAvailability vesselAvailability) {
		this.vesselAvailability = vesselAvailability;
		this.charterInMarket = null;
		this.spotIndex = null;
		this.assignments = assignments;
		sortAssignments(new AssignableElementDateComparator());
	}

	public CollectedAssignment(final List<AssignableElement> assignments, final CharterInMarket charterInMarket, final int spotIndex) {
		this.vesselAvailability = null;
		this.charterInMarket = charterInMarket;
		this.assignments = assignments;
		this.spotIndex = spotIndex;
		sortAssignments(new AssignableElementDateComparator());
	}

	public CollectedAssignment(final List<AssignableElement> assignments, final VesselAvailability vesselAvailability, final IAssignableElementComparator comparator) {
		this.vesselAvailability = vesselAvailability;
		this.charterInMarket = null;
		this.spotIndex = null;
		this.assignments = assignments;
		sortAssignments(comparator);
	}

	public CollectedAssignment(final List<AssignableElement> assignments, final CharterInMarket charterInMarket, final int spotIndex, final IAssignableElementComparator comparator) {
		this.vesselAvailability = null;
		this.charterInMarket = charterInMarket;
		this.assignments = assignments;
		this.spotIndex = spotIndex;
		sortAssignments(comparator);
	}

	private void sortAssignments(final IAssignableElementComparator comparator) {

		Collections.sort(assignments, comparator);

		for (final AssignableElement ea : assignments) {
			assignedObjects.add(ea);
		}
	}

	public List<AssignableElement> getAssignedObjects() {
		return Collections.unmodifiableList(assignedObjects);
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
}
