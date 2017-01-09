/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.ui.views.providers;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.nebula.widgets.grid.GridItem;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.widgets.Display;

import com.mmxlabs.models.lng.analytics.AnalysisResultRow;
import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.ResultSet;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.SlotAllocationType;
import com.mmxlabs.models.ui.tabular.ICellRenderer;

public class ResultsFormatterLabelProvider extends CellLabelProvider {

	private final ICellRenderer formatter;
	private final EStructuralFeature feature;
	private Font boldFont;

	public ResultsFormatterLabelProvider(final ICellRenderer formatter, final EStructuralFeature feature) {
		this.formatter = formatter;
		this.feature = feature;
		{
			final Font systemFont = Display.getDefault().getSystemFont();
			// Clone the font data
			final FontData fd = new FontData(systemFont.getFontData()[0].toString());
			// Set the bold bit.
			fd.setStyle(fd.getStyle() | SWT.BOLD);
			boldFont = new Font(Display.getDefault(), fd);
		}

	}

	@Override
	public void update(final ViewerCell cell) {
		Object element = cell.getElement();

		cell.setText("");
		cell.setFont(null);
		if (feature == AnalyticsPackage.eINSTANCE.getAnalysisResultRow_BuyOption()) {
			if (element instanceof ResultSet) {
				cell.setText("");
				((GridItem) cell.getItem()).setColumnSpan(0, 4);
				return;
			}
		}

		if (element instanceof AnalysisResultRow) {
			AnalysisResultRow analysisResultRow = (AnalysisResultRow) element;
			@Nullable
			String baseLabel = formatter.render(analysisResultRow.eGet(feature));
			if (baseLabel != null) {
				boolean hasLateness = false;
				boolean hasCapacity = false;
				if (feature == AnalyticsPackage.Literals.ANALYSIS_RESULT_ROW__BUY_OPTION || feature == AnalyticsPackage.Literals.ANALYSIS_RESULT_ROW__SELL_OPTION) {
					for (SlotAllocation s : analysisResultRow.getResultDetails().getSlotAllocations()) {
						if (feature == AnalyticsPackage.Literals.ANALYSIS_RESULT_ROW__BUY_OPTION && s.getSlotAllocationType() == SlotAllocationType.PURCHASE) {
							hasLateness |= s.getSlotVisit().getLateness() != null && s.getSlotVisit().getLateness().getLatenessInHours() > 0;
							hasCapacity |= s.getSlotVisit().getViolations().size() > 0;
						} else if (feature == AnalyticsPackage.Literals.ANALYSIS_RESULT_ROW__SELL_OPTION && s.getSlotAllocationType() == SlotAllocationType.SALE) {
							hasLateness |= s.getSlotVisit().getLateness() != null && s.getSlotVisit().getLateness().getLatenessInHours() > 0;
							hasCapacity |= s.getSlotVisit().getViolations().size() > 0;
						}
					}
				}

				StringBuilder sb = new StringBuilder();
				sb.append(baseLabel);
				if (hasLateness) {
					sb.append(" (L)");
				}
				if (hasCapacity) {
					sb.append(" (C)");
				}

				cell.setText(sb.toString());
			} else {
				cell.setText(baseLabel);
			}
		} else {
			// cell.setText("---");
		}
	}

	@Override
	public void dispose() {
		boldFont.dispose();
		super.dispose();
	}

}
