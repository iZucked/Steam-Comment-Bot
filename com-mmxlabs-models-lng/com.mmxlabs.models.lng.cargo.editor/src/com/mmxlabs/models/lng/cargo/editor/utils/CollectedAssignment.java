/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2014
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.editor.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.mmxlabs.models.lng.cargo.AssignableElement;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.VesselClass;
import com.mmxlabs.models.lng.types.AVesselSet;

/**
 * Utility class representing a sequence from the input model. Because the input model is now held as per-element assignment classes, this is needed to glom everything together.
 * 
 * @author hinton
 * 
 */
public class CollectedAssignment {

	final AVesselSet<? extends Vessel> vessel;
	final ArrayList<AssignableElement> assignedObjects = new ArrayList<AssignableElement>();
	List<AssignableElement> assignments = null;
	private final Integer spotIndex;

	public CollectedAssignment(final List<AssignableElement> assignments, final AVesselSet<? extends Vessel> vessel, final Integer spotIndex) {
		this.vessel = vessel;
		this.assignments = assignments;
		this.spotIndex = spotIndex;
		sortAssignments(new AssignableElementDateComparator());
	}

	public CollectedAssignment(final List<AssignableElement> assignments, final AVesselSet<? extends Vessel> vessel, final Integer spotIndex, final IAssignableElementComparator comparator) {
		this.vessel = vessel;
		this.assignments = assignments;
		this.spotIndex = spotIndex;
		sortAssignments(comparator == null ? new AssignableElementDateComparator() : comparator);
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
		return vessel instanceof VesselClass;
	}

	public AVesselSet<? extends Vessel> getVesselOrClass() {
		return vessel;
	}

	public boolean isSetSpotIndex() {
		return spotIndex != null;
	}

	public int getSpotIndex() {
		return spotIndex == null ? 0 : spotIndex.intValue();
	}
}
