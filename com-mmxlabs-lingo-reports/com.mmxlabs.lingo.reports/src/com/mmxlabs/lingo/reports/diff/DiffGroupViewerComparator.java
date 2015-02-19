package com.mmxlabs.lingo.reports.diff;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;

import com.mmxlabs.lingo.reports.diff.utils.PNLDeltaUtils;
import com.mmxlabs.lingo.reports.views.schedule.model.CycleGroup;
import com.mmxlabs.lingo.reports.views.schedule.model.Row;
import com.mmxlabs.lingo.reports.views.schedule.model.UserGroup;

public final class DiffGroupViewerComparator extends ViewerComparator {
	@Override
	public int compare(final Viewer viewer, final Object e1, final Object e2) {

		if (e1 instanceof UserGroup && !(e2 instanceof UserGroup)) {
			return -1;
		}
		if (e2 instanceof UserGroup && !(e1 instanceof UserGroup)) {
			return 1;
		}
		if (e1 instanceof CycleGroup && !(e2 instanceof CycleGroup)) {
			return -1;
		}
		if (e2 instanceof CycleGroup && !(e1 instanceof CycleGroup)) {
			return 1;
		}

		if (e1 instanceof UserGroup && e2 instanceof UserGroup) {
			final UserGroup g1 = (UserGroup) e1;
			final UserGroup g2 = (UserGroup) e2;
			return PNLDeltaUtils.getPNLDelta(g2) - PNLDeltaUtils.getPNLDelta(g1);
		}
		if (e1 instanceof CycleGroup && e2 instanceof CycleGroup) {
			final CycleGroup g1 = (CycleGroup) e1;
			final CycleGroup g2 = (CycleGroup) e2;

			final int c = g2.getChangeType().ordinal() - g1.getChangeType().ordinal();
			if (c != 0) {
				return c;
			}

			return PNLDeltaUtils.getPNLDelta(g2) - PNLDeltaUtils.getPNLDelta(g1);
		}

		if (e1 instanceof Row && e1 instanceof Row) {
			final Row r1 = (Row) e1;
			final Row r2 = (Row) e2;

			final int c = r1.getName().compareTo(r2.getName());
			if (c != 0) {
				return c;
			}

			if (r1.isReference() != r2.isReference()) {
				if (r1.isReference()) {
					return -1;
				} else {
					return 1;
				}
			}
		}

		return super.compare(viewer, e1, e2);
	}
}