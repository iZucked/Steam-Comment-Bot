package com.mmxlabs.scheduleview.views;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;

import scenario.schedule.Sequence;

public class ScenarioViewerComparator extends ViewerComparator {

	public enum Mode {
		STACK, INTERLEAVE
	};

	private Mode mode = Mode.STACK;

	protected final Mode getMode() {
		return mode;
	}

	protected final void setMode(Mode mode) {
		this.mode = mode;
	}

	@Override
	public int category(final Object element) {

		// Use hashcode of resource as sort key
		if (element instanceof EObject) {
			return ((EObject) element).eContainer().hashCode();
		}
		// TODO Auto-generated method stub
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

			// Then order by element order
			if (e1 instanceof Sequence && e2 instanceof Sequence) {
				final Sequence s1 = (Sequence) e1;
				final Sequence s2 = (Sequence) e2;

				int c = s1.eContainer().eContents().indexOf(s1)
						- s2.eContainer().eContents().indexOf(s2);
				if (c != 0) {
					return c;
				}
			}
			// Otherwise fallback to default behaviour
		} else if (mode == Mode.INTERLEAVE) {

			// Then order by element order
			if (e1 instanceof Sequence && e2 instanceof Sequence) {
				final Sequence s1 = (Sequence) e1;
				final Sequence s2 = (Sequence) e2;

				int c = s1.eContainer().eContents().indexOf(s1)
						- s2.eContainer().eContents().indexOf(s2);
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

	@Override
	public void sort(final Viewer viewer, final Object[] elements) {
		// TODO Auto-generated method stub
		super.sort(viewer, elements);
	}

	@Override
	public boolean isSorterProperty(final Object element, final String property) {
		// TODO Auto-generated method stub
		return super.isSorterProperty(element, property);
	}
}
