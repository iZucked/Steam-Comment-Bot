/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.schedule.formatters;

import org.eclipse.jface.viewers.ViewerCell;

import com.mmxlabs.lingo.reports.components.IRowSpanProvider;
import com.mmxlabs.lingo.reports.diff.utils.PNLDeltaUtils;
import com.mmxlabs.lingo.reports.views.formatters.IntegerFormatter;
import com.mmxlabs.lingo.reports.views.schedule.model.CycleGroup;
import com.mmxlabs.lingo.reports.views.schedule.model.Row;
import com.mmxlabs.lingo.reports.views.schedule.model.Table;

public class PNLDeltaFormatter extends IntegerFormatter implements IRowSpanProvider {
	@Override
	public Integer getIntValue(final Object object) {

		if (object instanceof Row) {
			final Row row = (Row) object;

			// In simplePinDiff mode, we can activate row spanning, thus we always show total P&L delta. Otherwise show p&l delta only on the non-reference rows.
			final Table tbl = row.getTable();
			final boolean simplePinDiff = tbl.getScenarios().size() == 2 && tbl.getPinnedScenario() != null;

			// Disabled, see comment above.
			if (!simplePinDiff && row.isReference()) {
				return null;
			}

			final CycleGroup group = row.getCycleGroup();
			if (group != null) {
				int delta = 0;
				for (final Row groupRow : group.getRows()) {
					final Integer pnl = PNLDeltaUtils.getElementProfitAndLoss(groupRow.getTarget());
					if (pnl == null) {
						continue;
					}
					if (groupRow.isReference()) {
						delta -= pnl.intValue();
					} else {
						// Exclude rows from other scenarios. (disabled, see comment above)
						if (!simplePinDiff && groupRow.getSchedule() != row.getSchedule()) {
							continue;
						}
						delta += pnl.intValue();
					}
				}
				return delta;
			}
		}
		return null;
	}

	@Override
	public int getRowSpan(final ViewerCell cell, final Object object) {
		if (object instanceof Row) {
			final Row row = (Row) object;

			final Table tbl = row.getTable();
			if (tbl.getScenarios().size() == 2 && tbl.getPinnedScenario() != null) {
				final CycleGroup group = row.getCycleGroup();
				if (group != null) {
					// Navigate upwards to find the number of rows spanned so far for a given cycle group.
					// Note: This *only* works for displayed cells. Cells which have not been drawn yet (ViewerCell.BELOW) or are not visible (outside of current scroll viewport) will be retuned
					// as a null neighbour.
					{
						int count = 0;
						ViewerCell neighbour = cell.getNeighbor(ViewerCell.ABOVE, false);
						while (neighbour != null && neighbour.getElement() instanceof Row) {
							final Row neighbourRow = (Row) neighbour.getElement();
							if (neighbourRow.getCycleGroup() == group) {
								count++;
								neighbour = neighbour.getNeighbor(ViewerCell.ABOVE, false);
							} else {
								break;
							}
						}
						return count;
					}
				}
			}
		}
		return 0;
	}
}
