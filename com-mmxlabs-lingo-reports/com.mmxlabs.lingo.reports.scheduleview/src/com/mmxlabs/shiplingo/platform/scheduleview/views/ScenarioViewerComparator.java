/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.platform.scheduleview.views;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;

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

	private Mode mode = Mode.STACK;

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
			final boolean s1Spot = s1.isSetVesselClass();
			final boolean s2Spot = s2.isSetVesselClass();

			if (s1Spot != s2Spot) {
				return s1Spot ? 1 : -1;
			}

			// Sort by name
			final String str1 = s1.getVesselAvailability() == null ? null : s1.getVesselAvailability().getVessel().getName();
			final String str2 = s2.getVesselAvailability() == null ? null : s2.getVesselAvailability().getVessel().getName();

			final int c = str1 == null ? -1 : (str2 == null ? 1 : str1.compareTo(str2));
			if (c != 0) {
				return c;
			}
		} else if (mode == Mode.INTERLEAVE) {

			// Then order by element order
			if ((e1 instanceof Sequence) && (e2 instanceof Sequence)) {
				final Sequence s1 = (Sequence) e1;
				final Sequence s2 = (Sequence) e2;

				// Group by fleet/spot
				final boolean s1Spot = s1.isSetVesselClass();
				final boolean s2Spot = s2.isSetVesselClass();

				if (s1Spot != s2Spot) {
					return s1Spot ? 1 : -1;
				}

				// Sort by name
				final String name1 = s1.getName();
				final String name2 = s2.getName();

				final int c = name1.compareTo(name2);
				if (c != 0) {
					return c;
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
