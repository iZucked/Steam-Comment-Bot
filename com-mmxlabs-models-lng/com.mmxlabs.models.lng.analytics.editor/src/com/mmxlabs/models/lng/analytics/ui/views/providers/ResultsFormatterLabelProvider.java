package com.mmxlabs.models.lng.analytics.ui.views.providers;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ViewerCell;

import com.mmxlabs.models.lng.analytics.AnalysisResultRow;
import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.SlotAllocationType;
import com.mmxlabs.models.ui.tabular.ICellRenderer;

public class ResultsFormatterLabelProvider extends CellLabelProvider {

	private final ICellRenderer formatter;
	private final EStructuralFeature feature;

	public ResultsFormatterLabelProvider(final ICellRenderer formatter, final EStructuralFeature feature) {
		this.formatter = formatter;
		this.feature = feature;

	}

	@Override
	public void update(final ViewerCell cell) {
		Object element = cell.getElement();
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
			cell.setText("---");
		}
	}
}
