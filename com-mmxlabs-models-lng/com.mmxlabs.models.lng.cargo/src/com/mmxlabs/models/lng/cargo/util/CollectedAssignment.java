/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.jdt.annotation.NonNull;

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
	// private final ArrayList<AssignableElement> assignedObjects = new ArrayList<AssignableElement>();
	private List<AssignableElement> assignments = null;
	private final Integer spotIndex;

	public CollectedAssignment(final List<AssignableElement> assignments, final VesselAvailability vesselAvailability, PortModel portModel) {
		this.vesselAvailability = vesselAvailability;
		this.charterInMarket = null;
		this.spotIndex = null;
		this.assignments = sortAssignments2(assignments, portModel);
	}

	public CollectedAssignment(final List<AssignableElement> assignments, final CharterInMarket charterInMarket, final int spotIndex, PortModel portModel) {
		this.vesselAvailability = null;
		this.charterInMarket = charterInMarket;
		this.spotIndex = spotIndex;
		this.assignments = sortAssignments2(assignments, portModel);
	}

	public CollectedAssignment(final List<AssignableElement> assignments, final VesselAvailability vesselAvailability, final IAssignableElementComparator comparator) {
		this.vesselAvailability = vesselAvailability;
		this.charterInMarket = null;
		this.spotIndex = null;
		this.assignments = new ArrayList<>(assignments);
//		sortAssignments(comparator);
//		sortAssignments(comparator);
		throw new UnsupportedOperationException();
	}

	public CollectedAssignment(final List<AssignableElement> assignments, final CharterInMarket charterInMarket, final int spotIndex, final IAssignableElementComparator comparator) {
		this.vesselAvailability = null;
		this.charterInMarket = charterInMarket;
		this.assignments = new ArrayList<>(assignments);
		this.spotIndex = spotIndex;
//		sortAssignments(comparator);
		throw new UnsupportedOperationException();
	}

//	private void sortAssignments(final IAssignableElementComparator comparator) {
//		Collections.sort(assignments, comparator);
//	}

//	private static List<AssignableElement> sortAssignments(@NonNull List<AssignableElement> assignments) {
//
//		List<AssignableElement> sortedElements = new LinkedList<>();
//
//		List<AssignableElement> ambiguousElements = new LinkedList<>();
//
//		LOOP_ASSIGNMENTS: for (AssignableElement e : assignments) {
//			if (sortedElements.isEmpty()) {
//				sortedElements.add(e);
//				continue;
//			}
//
//			boolean foundBefore = false;
//			int beforeIdx = 0;
//			LOOP_INSERTION: for (int i = 0; i < sortedElements.size(); ++i) {
//				AssignableElement se = sortedElements.get(i);
//				@NonNull
//				OrderingHint ordering = AssignmentEditorHelper.checkOrdering(e, se);
//				if (ordering == OrderingHint.AMBIGUOUS) {
//					ambiguousElements.add(e);
//					continue LOOP_ASSIGNMENTS;
//				} else if (ordering == OrderingHint.BEFORE) {
//					if (!foundBefore) {
//						beforeIdx = i;
//						foundBefore = true;
//					}
//				} else if (ordering == OrderingHint.AFTER) {
//					if (foundBefore) {
//						// Transitioned from before to after!
//						ambiguousElements.add(e);
//						continue LOOP_ASSIGNMENTS;
//					}
//				} else {
//					throw new IllegalArgumentException();
//				}
//			}
//			if (!foundBefore) {
//				// Assume all hints we "AFTER"
//				sortedElements.add(e);
//			} else {
//				// Insert before this element
//				sortedElements.add(beforeIdx, e);
//			}
//		}
//
//		// Loop over (until we stop inserting) ambiguous elements finding single point insertion places.
//		if (!ambiguousElements.isEmpty()) {
//			int currentSize = 0;
//			while (ambiguousElements.size() != currentSize) {
//				currentSize = ambiguousElements.size();
//				ambiguousElements = insertAmbigiousSimple(sortedElements, ambiguousElements);
//			}
//		}
//
//		// Easy elements have been added.
//		// FIXME: Better insertion!
//		sortedElements.addAll(ambiguousElements);
//
//		return sortedElements;
//	}
//
//	private static List<AssignableElement> insertAmbigiousSimple(List<AssignableElement> sortedElements, List<AssignableElement> ambiguousElements) {
//		int originalSize = sortedElements.size();
//
//		List<AssignableElement> newAmbiguousElements = new LinkedList<>();
//
//		// TODO: HERE What should be do?
//		LOOP_ELEMENTS: for (AssignableElement ambElemen : ambiguousElements) {
//
//			boolean foundBefore = false;
//			boolean foundAfter = false;
//			boolean foundAmbiguous = false;
//			int insertionIdx = -1;
//			OrderingHint lastHint = null;
//			LOOP_INSERTION: for (int i = 0; i < sortedElements.size(); ++i) {
//				AssignableElement e = sortedElements.get(i);
//				OrderingHint hint = AssignmentEditorHelper.checkOrdering(ambElemen, e);
//				try {
//					// Direct insertion position
//					if (hint == OrderingHint.BEFORE) {
//						if (lastHint == null || lastHint == OrderingHint.AFTER) {
//							sortedElements.add(i, ambElemen);
//							continue LOOP_ELEMENTS;
//						}
//					}
//
//					if (i == 0 && hint == OrderingHint.AMBIGUOUS) {
//						insertionIdx = i;
//					} else {
//						if (i > 0 && hint == OrderingHint.AMBIGUOUS) {
//							AssignableElement before = sortedElements.get(i - 1);
//
//							if (AssignmentEditorHelper.checkInsertion(before, ambElemen, e)) {
//								if (insertionIdx != -1) {
//									// Still multiple insertion locations;
//									newAmbiguousElements.add(ambElemen);
//									continue LOOP_ELEMENTS;
//								} else {
//									insertionIdx = i;
//								}
//							}
//						}
//					}
//				} finally {
//					lastHint = hint;
//				}
//			}
//
//			// FIXME: lastHint == AMBIGUOUS?
//			if (lastHint == OrderingHint.AMBIGUOUS) {
//				if (insertionIdx != -1) {
//					// Still multiple insertion locations;
//					newAmbiguousElements.add(ambElemen);
//					continue LOOP_ELEMENTS;
//				} else {
//					insertionIdx = sortedElements.size();
//				}
//			}
//			// If we got here, then there was only one insertion location
//			if (insertionIdx != -1) {
//				if (insertionIdx == sortedElements.size()) {
//					sortedElements.add(ambElemen);
//				} else {
//					sortedElements.add(insertionIdx, ambElemen);
//				}
//				continue LOOP_ELEMENTS;
//			}
//
//		}
//		// Sanity check element count
//		assert (newAmbiguousElements.size() + (sortedElements.size() - originalSize)) == ambiguousElements.size();
//
//		return newAmbiguousElements;
//
//	}

	public List<AssignableElement> getAssignedObjects() {
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

	private static List<AssignableElement> sortAssignments2(@NonNull List<AssignableElement> assignments, PortModel portModel) {

//		if (true) {
			List<WrappedAssignableElement> sortedElements = new LinkedList<>();
			for (AssignableElement ae : assignments) {
				WrappedAssignableElement e = new WrappedAssignableElement(ae, portModel);
				sortedElements.add(e);
			}
			
			Collections.sort(sortedElements, (a,b) -> {
				
				OrderingHint hint = AssignmentEditorHelper.checkOrdering(a, b);
				switch (hint) {
				case AFTER:
					return 1;
				case BEFORE:
					return -1;
				case AMBIGUOUS:
				default:
					return a.getStartWindow().getSecond().compareTo(b.getStartWindow().getSecond());
				
				}
				
				
			});
			return sortedElements.stream().map(e -> e.getAssignableElement()).collect(Collectors.toList());
//		}

//		List<WrappedAssignableElement> sortedElements = new LinkedList<>();
//		List<WrappedAssignableElement> ambiguousElements = new LinkedList<>();
//
//		LOOP_ASSIGNMENTS: for (AssignableElement ae : assignments) {
//			WrappedAssignableElement e = new WrappedAssignableElement(ae, portModel);
//			if (sortedElements.isEmpty()) {
//				sortedElements.add(e);
//				continue;
//			}
//
//			boolean foundBefore = false;
//			int beforeIdx = 0;
//			LOOP_INSERTION: for (int i = 0; i < sortedElements.size(); ++i) {
//				WrappedAssignableElement se = sortedElements.get(i);
//				@NonNull
//				OrderingHint ordering = AssignmentEditorHelper.checkOrdering(e, se);
//				if (ordering == OrderingHint.AMBIGUOUS) {
//					ambiguousElements.add(e);
//					continue LOOP_ASSIGNMENTS;
//				} else if (ordering == OrderingHint.BEFORE) {
//					if (!foundBefore) {
//						beforeIdx = i;
//						foundBefore = true;
//					}
//				} else if (ordering == OrderingHint.AFTER) {
//					if (foundBefore) {
//						// Transitioned from before to after!
//						ambiguousElements.add(e);
//						continue LOOP_ASSIGNMENTS;
//					}
//				} else {
//					throw new IllegalArgumentException();
//				}
//			}
//			if (!foundBefore) {
//				// Assume all hints we "AFTER"
//				sortedElements.add(e);
//			} else {
//				// Insert before this element
//				sortedElements.add(beforeIdx, e);
//			}
//		}
//
//		// Loop over (until we stop inserting) ambiguous elements finding single point insertion places.
//		if (!ambiguousElements.isEmpty()) {
//			int currentSize = 0;
//			while (ambiguousElements.size() != currentSize) {
//				currentSize = ambiguousElements.size();
//				ambiguousElements = insertAmbigiousSimple2(sortedElements, ambiguousElements);
//			}
//		}
//
//		// Easy elements have been added.
//		// FIXME: Better insertion!
//		sortedElements.addAll(ambiguousElements);
//
//		return sortedElements.stream().map(e -> e.getAssignableElement()).collect(Collectors.toList());
	}
//
//	private static List<WrappedAssignableElement> insertAmbigiousSimple2(List<WrappedAssignableElement> sortedElements, List<WrappedAssignableElement> ambiguousElements) {
//		int originalSize = sortedElements.size();
//
//		List<WrappedAssignableElement> newAmbiguousElements = new LinkedList<>();
//
//		// TODO: HERE What should be do?
//		LOOP_ELEMENTS: for (WrappedAssignableElement ambElemen : ambiguousElements) {
//
//			boolean foundBefore = false;
//			boolean foundAfter = false;
//			boolean foundAmbiguous = false;
//			int insertionIdx = -1;
//			OrderingHint lastHint = null;
//			LOOP_INSERTION: for (int i = 0; i < sortedElements.size(); ++i) {
//				WrappedAssignableElement e = sortedElements.get(i);
//				OrderingHint hint = AssignmentEditorHelper.checkOrdering(ambElemen, e);
//				try {
//					// Direct insertion position
//					if (hint == OrderingHint.BEFORE) {
//						if (lastHint == null || lastHint == OrderingHint.AFTER) {
//							sortedElements.add(i, ambElemen);
//							continue LOOP_ELEMENTS;
//						}
//					}
//
//					if (i == 0 && hint == OrderingHint.AMBIGUOUS) {
//						insertionIdx = i;
//					} else {
//						if (i > 0 && hint == OrderingHint.AMBIGUOUS) {
//							WrappedAssignableElement before = sortedElements.get(i - 1);
//
//							if (AssignmentEditorHelper.checkInsertion(before, ambElemen, e)) {
//								if (insertionIdx != -1) {
//									// Still multiple insertion locations;
//									newAmbiguousElements.add(ambElemen);
//									continue LOOP_ELEMENTS;
//								} else {
//									insertionIdx = i;
//								}
//							}
//						}
//					}
//				} finally {
//					lastHint = hint;
//				}
//			}
//
//			// FIXME: lastHint == AMBIGUOUS?
//			if (lastHint == OrderingHint.AMBIGUOUS) {
//				if (insertionIdx != -1) {
//					// Still multiple insertion locations;
//					newAmbiguousElements.add(ambElemen);
//					continue LOOP_ELEMENTS;
//				} else {
//					insertionIdx = sortedElements.size();
//				}
//			}
//			// If we got here, then there was only one insertion location
//			if (insertionIdx != -1) {
//				if (insertionIdx == sortedElements.size()) {
//					sortedElements.add(ambElemen);
//				} else {
//					sortedElements.add(insertionIdx, ambElemen);
//				}
//				continue LOOP_ELEMENTS;
//			}
//
//		}
//		// Sanity check element count
//		assert (newAmbiguousElements.size() + (sortedElements.size() - originalSize)) == ambiguousElements.size();
//
//		return newAmbiguousElements;
//
//	}

}
