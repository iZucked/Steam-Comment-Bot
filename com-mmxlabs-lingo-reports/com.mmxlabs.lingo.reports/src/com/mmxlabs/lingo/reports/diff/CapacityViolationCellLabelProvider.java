/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.diff;

import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ViewerCell;

import com.mmxlabs.lingo.reports.diff.utils.ChangeDescriptionUtil;
import com.mmxlabs.lingo.reports.views.schedule.model.Row;

public class CapacityViolationCellLabelProvider extends CellLabelProvider {
	@Override
	public void update(final ViewerCell cell) {
		final Object object = cell.getElement();
		if (object instanceof Row) {
			final Row row = (Row) object;
			cell.setText(ChangeDescriptionUtil.getCapacityViolationDescription(row));
		} else {
			cell.setText("");
		}
	}

}
