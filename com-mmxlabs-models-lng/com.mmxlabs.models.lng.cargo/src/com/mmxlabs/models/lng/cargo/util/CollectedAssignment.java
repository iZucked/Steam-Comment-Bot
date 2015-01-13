/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2014
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.mmxlabs.models.lng.cargo.AssignableElement;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.fleet.VesselClass;

/**
 * Utility class representing a sequence from the input model. Because the input model is now held as per-element assignment classes, this is needed to glom everything together.
 * 
 * @author hinton
 * 
 */
public class CollectedAssignment {

	private final VesselAvailability vesselAvailability;
	private final VesselClass vesselClass;
	private final ArrayList<AssignableElement> assignedObjects = new ArrayList<AssignableElement>();
	private List<AssignableElement> assignments = null;
	private final Integer spotIndex;

	public CollectedAssignment(final List<AssignableElement> assignments, final VesselAvailability vesselAvailability) {
		this.vesselAvailability = vesselAvailability;
		this.vesselClass = null;
		this.spotIndex = null;
		this.assignments = assignments;
		sortAssignments(new AssignableElementDateComparator());
	}

	public CollectedAssignment(final List<AssignableElement> assignments, final VesselClass vesselClass, final int spotIndex) {
		this.vesselAvailability = null;
		this.vesselClass = vesselClass;
		this.assignments = assignments;
		this.spotIndex = spotIndex;
		sortAssignments(new AssignableElementDateComparator());
	}

	public CollectedAssignment(final List<AssignableElement> assignments, final VesselAvailability vesselAvailability, final IAssignableElementComparator comparator) {
		this.vesselAvailability = vesselAvailability;
		this.vesselClass = null;
		this.spotIndex = null;
		this.assignments = assignments;
		sortAssignments(comparator);
	}

	public CollectedAssignment(final List<AssignableElement> assignments, final VesselClass vesselClass, final int spotIndex, final IAssignableElementComparator comparator) {
		this.vesselAvailability = null;
		this.vesselClass = vesselClass;
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

	public VesselClass getVesselClass() {
		return vesselClass;
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
