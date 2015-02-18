package com.mmxlabs.lingo.reports.diff;

import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ViewerCell;

import com.mmxlabs.lingo.reports.diff.utils.PNLDeltaUtils;
import com.mmxlabs.lingo.reports.views.schedule.model.CycleGroup;

public class CycleGroupDeltaCellLabelProvider extends CellLabelProvider {
	@Override
	public void update(ViewerCell cell) {
		final Object object = cell.getElement();
		if (object instanceof CycleGroup) {
			final CycleGroup group = (CycleGroup) object;
			int delta = PNLDeltaUtils.getPNLDelta(group);
			cell.setText(Integer.toString(delta));

		} else {
			cell.setText("");
		}

	}

}
