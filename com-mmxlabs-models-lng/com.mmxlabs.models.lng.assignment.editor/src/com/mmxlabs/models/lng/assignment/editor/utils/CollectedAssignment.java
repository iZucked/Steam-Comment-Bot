/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.assignment.editor.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import com.mmxlabs.models.lng.assignment.ElementAssignment;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.VesselClass;
import com.mmxlabs.models.lng.types.AVesselSet;
import com.mmxlabs.models.mmxcore.UUIDObject;

/**
 * Utility class representing a sequence from the input model.
 * Because the input model is now held as per-element assignment
 * classes, this is needed to glom everything together.
 * @author hinton
 *
 */
public class CollectedAssignment {
	final AVesselSet<Vessel> vessel;
	final ArrayList<UUIDObject> assignedObjects = new ArrayList<UUIDObject>();
	List<ElementAssignment> assignments = null;
	private int spotIndex;
	
	public CollectedAssignment(final List<ElementAssignment> assignments, final AVesselSet<Vessel> vessel, int spotIndex) {
		this.vessel = vessel;
		this.assignments = assignments;
		this.spotIndex = spotIndex;
		sortAssignments();
	}
	
	private void sortAssignments() {
		// if two assignments don't overlap, sort by start date. Otherwise, sort by sequence number.
		Collections.sort(assignments, new Comparator<ElementAssignment>() {
			@Override
			public int compare(final ElementAssignment arg0, final ElementAssignment arg1) {
				final Date start0 = AssignmentEditorHelper.getStartDate(arg0.getAssignedObject());
				final Date start1 = AssignmentEditorHelper.getStartDate(arg1.getAssignedObject());
				final Date end0 = AssignmentEditorHelper.getEndDate(arg0.getAssignedObject());
				final Date end1 = AssignmentEditorHelper.getEndDate(arg1.getAssignedObject());
				
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
				
				if (overlaps(start0, end0, start1, end1)) {
					return ((Integer) arg0.getSequence()).compareTo(arg1.getSequence());
				} else {
					return start0.compareTo(start1);
				}
			}

			private boolean overlaps(final Date start0, final Date end0, final Date start1,final Date end1) {
				return !(end0.before(start1) || end1.before(start0));
			}
		});
		
		for (final ElementAssignment ea : assignments) {
			assignedObjects.add(ea.getAssignedObject());
		}
	}

	public List<UUIDObject> getAssignedObjects() {
		return Collections.unmodifiableList(assignedObjects);
	}
	
	public boolean isSpotVessel() {
		return vessel instanceof VesselClass;
	}
	
	public AVesselSet<Vessel> getVesselOrClass() {
		return vessel;
	}
	
	public int getSpotIndex() {
		return spotIndex;
	}
}
