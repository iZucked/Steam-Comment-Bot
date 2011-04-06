/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package scenario.presentation;

import java.text.DateFormat;
import java.util.Date;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Composite;
import org.swtchart.Chart;
import org.swtchart.ILineSeries;
import org.swtchart.ILineSeries.PlotSymbolType;
import org.swtchart.ISeries;
import org.swtchart.ISeries.SeriesType;

public class ChartViewer {
	final Chart chart;
	private IChartContentProvider contentProvider;

	public ChartViewer(final Composite container) {
		chart = new Chart(container, SWT.NONE);
		chart.getTitle().setVisible(false);
		chart.getAxisSet().getXAxis(0).getTitle().setVisible(true);
		chart.getAxisSet().getYAxis(0).getTitle().setVisible(true);
		chart.getAxisSet().getXAxis(0).getTitle().setText("Date");
		chart.getAxisSet().getYAxis(0).getTitle().setText("$/mmbtu");
		chart.getLegend().setVisible(true);
		
		chart.getAxisSet().getXAxis(0).getTick().setFormat(
				DateFormat.getDateInstance(DateFormat.SHORT));
		
	}

	public void setContentProvider(final IChartContentProvider contentProvider) {
		// delete existing series
		this.contentProvider = contentProvider;
		refresh();
	}

	public void refresh() {
		if (contentProvider == null)
			return;
		
		if (chart.isDisposed()) {
			contentProvider = null; //release
			return;
		}
		
		for (final ISeries series : chart.getSeriesSet().getSeries()) {
			chart.getSeriesSet().deleteSeries(series.getId());
		}
		
		for (int i = 0; i < contentProvider.getSeriesCount(); i++) {
			
			final ILineSeries series = (ILineSeries) chart.getSeriesSet()
					.createSeries(SeriesType.LINE,
							contentProvider.getSeriesName(i));
			if (contentProvider.isDateSeries(i)) {
				final Date[] dates = contentProvider.getDateXSeries(i);
				series.setXDateSeries(dates);
			} else {
				series.setXSeries(contentProvider.getXSeries(i));
			}
			
			series.enableStep(true);
			
			double [] yseries = contentProvider.getYSeries(i);

			series.setYSeries(contentProvider.getYSeries(i));
		
			series.setLineColor(new Color(chart.getDisplay(), 
					new RGB(360 * i / (float) contentProvider.getSeriesCount(), 1.0f, 1.0f)));
			series.setSymbolSize(0);
			series.setSymbolType(PlotSymbolType.NONE);
		}
		
		chart.getAxisSet().adjustRange(); // make fit
//		chart.getAxisSet().getXAxes()[0].setRange(new Range(minX, maxX));
//		chart.getAxisSet().getYAxes()[0].setRange(new Range(0, maxY));
	}

	public interface IChartContentProvider {
		int getSeriesCount();
		double[] getXSeries(int i);
		double[] getYSeries(int i);
		boolean isDateSeries(int i);
		Date[] getDateXSeries(int i);
		String getSeriesName(int i);
	}
}
