/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.scheduleview.views;

import java.util.Collection;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;

import com.mmxlabs.lingo.reports.services.ISelectedDataProvider;
import com.mmxlabs.lingo.reports.services.SelectedScenariosService;
import com.mmxlabs.models.lng.schedule.Sequence;

/**
 * {@link ViewerComparator} to sort the {@link SchedulerView} contents. Implementation sort vessels alphabetically grouped into fleet and spot vessels. There are currently two sort modes on top of
 * this; {@link Mode#STACK} will show multiple scenarios in sequence. {@link Mode#INTERLEAVE} will show the same vessel for multiple scenarios side-by-side.
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
	};

	private enum Type {
		DES, FOB, FLEET, CHARTER
	}

	private Mode mode = Mode.INTERLEAVE;

	@NonNull
	private final SelectedScenariosService selectedScenariosService;

	public ScenarioViewerComparator(@NonNull final SelectedScenariosService selectedScenariosService) {
		this.selectedScenariosService = selectedScenariosService;
	}

	protected final Mode getMode() {
		return mode;
	}

	protected final void setMode(final Mode mode) {
		this.mode = mode;
	}

	@Override
	public int category(final Object element) {

		// Use hashcode of resource as sort key
		if (element instanceof EObject) {
			return ((EObject) element).eContainer().hashCode();
		}

		return super.category(element);
	}

	private Type getSequenceType(final Sequence s) {
		if (s.isFleetVessel()) {
			return Type.FLEET;
		} else if (s.isSetCharterInMarket()) {
			return Type.CHARTER;
		} else if (s.getName().contains("DES")) {
			return Type.DES;
		} else if (s.getName().contains("FOB")) {
			return Type.FOB;
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

			final Sequence s1 = (Sequence) e1;
			final Sequence s2 = (Sequence) e2;

			// Group by fleet/spot
			final Type s1Type = getSequenceType(s1);
			final Type s2Type = getSequenceType(s2);

			if (s1Type != s2Type) {
				return s1Type.ordinal() - s2Type.ordinal();
			}

			// Sort by name
			final String str1 = s1.getVesselAvailability() == null ? s1.getName() : s1.getVesselAvailability().getVessel().getName();
			final String str2 = s2.getVesselAvailability() == null ? s2.getName() : s2.getVesselAvailability().getVessel().getName();

			final int c = str1.compareTo(str2);
			if (c != 0) {
				return c;
			}
			{
				// Add scenario instance name to field if multiple scenarios are selected
				final Object input = viewer.getInput();
				if (input instanceof Collection<?>) {
					final Collection<?> collection = (Collection<?>) input;

					if (collection.size() > 1) {
						if (selectedScenariosService.getPinnedScenario() != null) {
							final ISelectedDataProvider selectedDataProvider = selectedScenariosService.getCurrentSelectedDataProvider();
							if (selectedDataProvider != null) {

								final boolean s1Pinned = selectedDataProvider.isPinnedObject(s1);
								final boolean s2Pinned = selectedDataProvider.isPinnedObject(s2);
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
			if ((e1 instanceof Sequence) && (e2 instanceof Sequence)) {
				final Sequence s1 = (Sequence) e1;
				final Sequence s2 = (Sequence) e2;

				// Group by fleet/spot
				final Type s1Type = getSequenceType(s1);
				final Type s2Type = getSequenceType(s2);

				if (s1Type != s2Type) {
					return s1Type.ordinal() - s2Type.ordinal();
				}

				// Sort by name
				final String str1 = s1.getVesselAvailability() == null ? s1.getName() : s1.getVesselAvailability().getVessel().getName();
				final String str2 = s2.getVesselAvailability() == null ? s2.getName() : s2.getVesselAvailability().getVessel().getName();

				final int c = str1.compareTo(str2);
				if (c != 0) {
					return c;
				}
				{
					// Add scenario instance name to field if multiple scenarios are selected
					final Object input = viewer.getInput();
					if (input instanceof Collection<?>) {
						final Collection<?> collection = (Collection<?>) input;

						if (collection.size() > 1) {
							if (selectedScenariosService.getPinnedScenario() != null) {
								final ISelectedDataProvider selectedDataProvider = selectedScenariosService.getCurrentSelectedDataProvider();
								if (selectedDataProvider != null) {
									final boolean s1Pinned = selectedDataProvider.isPinnedObject(s1);
									final boolean s2Pinned = selectedDataProvider.isPinnedObject(s2);

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
