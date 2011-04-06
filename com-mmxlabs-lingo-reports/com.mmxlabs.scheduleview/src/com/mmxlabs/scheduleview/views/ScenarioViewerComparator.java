package com.mmxlabs.scheduleview.views;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;

import scenario.schedule.Sequence;
import scenario.schedule.fleetallocation.SpotVessel;

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
		STACK, INTERLEAVE
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
			final boolean s1Spot = s1.getVessel() instanceof SpotVessel;
			final boolean s2Spot = s2.getVessel() instanceof SpotVessel;

			if (s1Spot != s2Spot) {
				return s1Spot ? 1 : -1;
			}

			// Sort by name
			final int c = s1.getVessel().getName()
					.compareTo(s2.getVessel().getName());
			if (c != 0) {
				return c;
			}
		} else if (mode == Mode.INTERLEAVE) {

			// Then order by element order
			if (e1 instanceof Sequence && e2 instanceof Sequence) {
				final Sequence s1 = (Sequence) e1;
				final Sequence s2 = (Sequence) e2;

				// Group by fleet/spot
				final boolean s1Spot = s1.getVessel() instanceof SpotVessel;
				final boolean s2Spot = s2.getVessel() instanceof SpotVessel;

				if (s1Spot != s2Spot) {
					return s1Spot ? 1 : -1;
				}

				// Sort by name
				final int c = s1.getVessel().getName()
						.compareTo(s2.getVessel().getName());
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
