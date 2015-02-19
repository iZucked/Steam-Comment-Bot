/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.diff;

import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ViewerCell;

import com.mmxlabs.lingo.reports.diff.utils.PNLDeltaUtils;
import com.mmxlabs.lingo.reports.views.schedule.model.UserGroup;

public class UserGroupDeltaCellLabelProvider extends CellLabelProvider {
	@Override
	public void update(final ViewerCell cell) {
		final Object object = cell.getElement();
		if (object instanceof UserGroup) {
			final UserGroup group = (UserGroup) object;
			final int delta = PNLDeltaUtils.getPNLDelta(group);
			cell.setText(Integer.toString(delta));
		} else {
			cell.setText("");
		}
	}
}
