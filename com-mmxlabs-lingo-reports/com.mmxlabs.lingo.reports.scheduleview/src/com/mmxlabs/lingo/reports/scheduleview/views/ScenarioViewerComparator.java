/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.scheduleview.views;

import java.util.Collection;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;

import com.mmxlabs.lingo.reports.services.ISelectedDataProvider;
import com.mmxlabs.lingo.reports.services.ScenarioComparisonService;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.InventoryEvents;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.SequenceType;
import com.mmxlabs.models.lng.schedule.util.CombinedSequence;

/**
 * {@link ViewerComparator} to sort the {@link SchedulerView} contents.
 * Implementation sort vessels alphabetically grouped into fleet and spot
 * vessels. There are currently two sort modes on top of this;
 * {@link Mode#STACK} will show multiple scenarios in sequence.
 * {@link Mode#INTERLEAVE} will show the same vessel for multiple scenarios
 * side-by-side.
 * 
 * @author Simon Goodall
 * 
 */
public class ScenarioViewerComparator extends ViewerComparator {

	public enum Mode {
		STACK("Stack"), INTERLEAVE("Interleave");

		private final String displayName;

		private Mode(final String displayName) {
			this.displayName = displayName;
		}

		public final String getDisplayName() {
			return displayName;
		}
	}

	private enum Type {
		INVENTORY, DES, FOB, FLEET, CHARTER
	}

	private Mode mode = Mode.INTERLEAVE;

	@NonNull
	private final ScenarioComparisonService selectedScenariosService;

	public ScenarioViewerComparator(@NonNull final ScenarioComparisonService selectedScenariosService) {
		this.selectedScenariosService = selectedScenariosService;
	}

	protected final Mode getMode() {
		return mode;
	}

	protected final void setMode(final Mode mode) {
		this.mode = mode;
	}

	@Override
	public int category(Object element) {

		if (element instanceof CombinedSequence combinedSequence) {
			if (!combinedSequence.getSequences().isEmpty()) {
				element = combinedSequence.getSequences().get(0);
			}
		}

		// Use hashcode of resource as sort key
		if (element instanceof EObject eObj) {
			return eObj.eContainer().hashCode();
		}

		return super.category(element);
	}

	private @NonNull String getSequenceName(final Object obj) {
		if (obj instanceof final CombinedSequence combinedSequence) {
			final Vessel vessel = combinedSequence.getVessel();
			if (vessel != null) {
				final String name = vessel.getName();
				if (name != null) {
					return name;
				}
			}
		} else if (obj instanceof final Sequence s) {
			String name = s.getVesselAvailability() == null ? s.getName() : s.getVesselAvailability().getVessel().getName();

			if (s.getSequenceType() == SequenceType.ROUND_TRIP) {
				// Add the event name to help sort order
				final Event event = s.getEvents().get(0);
				name += event.name();
			}

			if (name != null) {
				return name + s.getSpotIndex();
			}
		}
		return "";
	}

	private Type getSequenceType(final Object obj) {
		if (obj instanceof CombinedSequence) {
			return Type.FLEET;
		} else if (obj instanceof final Sequence s) {
			if (s.isFleetVessel()) {
				return Type.FLEET;
			} else if (s.isSetCharterInMarket()) {
				return Type.CHARTER;
			} else if (s.getName().contains("DES")) {
				return Type.DES;
			} else if (s.getName().contains("FOB")) {
				return Type.FOB;
			}
		} else if (obj instanceof InventoryEvents) {
			return Type.INVENTORY;
		}
		return Type.FLEET;
	}

	@Override
	public int compare(final Viewer viewer, final Object e1, final Object e2) {

		if (mode == Mode.STACK) {
			// Group by scenario
			final int cat1 = category(e1);
			final int cat2 = category(e2);

			if (cat1 != cat2) {
				return cat1 - cat2;
			}

			// Group by fleet/spot
			final Type s1Type = getSequenceType(e1);
			final Type s2Type = getSequenceType(e2);

			if (s1Type != s2Type) {
				return s1Type.ordinal() - s2Type.ordinal();
			}

			// Sort by name
			final String s1Name = getSequenceName(e1);
			final String s2Name = getSequenceName(e2);

			final int c = s1Name.compareTo(s2Name);
			if (c != 0) {
				return c;
			}
			{
				// Add scenario instance name to field if multiple scenarios are selected
				final Object input = viewer.getInput();
				if (input instanceof Collection<?> collection) {

					if (collection.size() > 1) {
						if (selectedScenariosService.getPinned() != null) {
							final ISelectedDataProvider selectedDataProvider = selectedScenariosService.getCurrentSelectedDataProvider();
							if (selectedDataProvider != null) {

								final EObject e1Obj;
								if (e1 instanceof CombinedSequence combinedSequence) {
									if (!combinedSequence.getSequences().isEmpty()) {
										e1Obj = combinedSequence.getSequences().get(0);
									} else {
										e1Obj = null;
									}
								} else if (e1 instanceof EObject eo) {
									e1Obj = eo;
								} else {
									e1Obj = null;
								}

								final EObject e2Obj;

								if (e2 instanceof CombinedSequence combinedSequence) {
									if (!combinedSequence.getSequences().isEmpty()) {
										e2Obj = combinedSequence.getSequences().get(0);
									} else {
										e2Obj = null;
									}
								} else if (e2 instanceof EObject eo) {
									e2Obj = eo;
								} else {
									e2Obj = null;
								}

								final boolean s1Pinned = e1Obj != null && selectedDataProvider.isPinnedObject(e1Obj);
								final boolean s2Pinned = e2Obj != null && selectedDataProvider.isPinnedObject(e2Obj);

								if (s1Pinned != s2Pinned) {
									if (s1Pinned) {
										return -1;
									} else {
										return 1;
									}
								}
							}
						}
					}
				}
			}

		} else if (mode == Mode.INTERLEAVE) {

			// Then order by element order
			if ((e1 instanceof Sequence || e1 instanceof CombinedSequence || e1 instanceof InventoryEvents)
					&& (e2 instanceof Sequence || e2 instanceof CombinedSequence || e2 instanceof InventoryEvents)) {

				// Group by fleet/spot
				final Type s1Type = getSequenceType(e1);
				final Type s2Type = getSequenceType(e2);

				if (s1Type != s2Type) {
					return s1Type.ordinal() - s2Type.ordinal();
				}

				// Sort by name
				// Sort by name
				final String s1Name = getSequenceName(e1);
				final String s2Name = getSequenceName(e2);

				final int c = s1Name.compareTo(s2Name);
				if (c != 0) {
					return c;
				}
				{
					// Add scenario instance name to field if multiple scenarios are selected
					final Object input = viewer.getInput();
					if (input instanceof Collection<?> collection) {

						if (collection.size() > 1) {
							if (selectedScenariosService.getPinned() != null) {
								final ISelectedDataProvider selectedDataProvider = selectedScenariosService.getCurrentSelectedDataProvider();
								if (selectedDataProvider != null) {

									final EObject e1Obj;
									if (e1 instanceof CombinedSequence combinedSequence) {
										if (!combinedSequence.getSequences().isEmpty()) {
											e1Obj = combinedSequence.getSequences().get(0);
										} else {
											e1Obj = null;
										}
									} else if (e1 instanceof EObject eo) {
										e1Obj = eo;
									} else {
										e1Obj = null;
									}

									final EObject e2Obj;

									if (e2 instanceof CombinedSequence combinedSequence) {
										if (!combinedSequence.getSequences().isEmpty()) {
											e2Obj = combinedSequence.getSequences().get(0);
										} else {
											e2Obj = null;
										}
									} else if (e2 instanceof EObject eo) {
										e2Obj = eo;
									} else {
										e2Obj = null;
									}

									final boolean s1Pinned = e1Obj != null && selectedDataProvider.isPinnedObject(e1Obj);
									final boolean s2Pinned = e2Obj != null && selectedDataProvider.isPinnedObject(e2Obj);

									if (s1Pinned != s2Pinned) {
										if (s1Pinned) {
											return -1;
										} else {
											return 1;
										}
									}
								}
							}
						}
					}
				}
			}
			// Group by scenario
			final int cat1 = category(e1);
			final int cat2 = category(e2);
			if (cat1 != cat2) {
				return cat1 - cat2;
			}
		}
		// Otherwise fallback to default behaviour
		return super.compare(viewer, e1, e2);
	}
}
