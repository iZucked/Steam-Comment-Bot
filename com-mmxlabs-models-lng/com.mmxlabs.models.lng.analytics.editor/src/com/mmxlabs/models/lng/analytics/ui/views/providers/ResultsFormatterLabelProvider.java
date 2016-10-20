package com.mmxlabs.models.lng.analytics.ui.views.providers;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ViewerCell;

import com.mmxlabs.models.lng.analytics.AnalysisResultRow;
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
			cell.setText(formatter.render(analysisResultRow.eGet(feature)));
		} else {
			cell.setText("---");
		}
	}
}
