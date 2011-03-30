package scenario.presentation;

import java.util.Date;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Composite;
import org.swtchart.Chart;
import org.swtchart.ILineSeries;
import org.swtchart.ILineSeries.PlotSymbolType;
import org.swtchart.ISeries.SeriesType;

public class ChartViewer {
	final Chart chart;
	private IChartContentProvider contentProvider;

	public ChartViewer(final Composite container) {
		chart = new Chart(container, SWT.NONE);
		chart.getTitle().setVisible(false);
		chart.getAxisSet().getXAxis(0).getTitle().setVisible(false);
		chart.getAxisSet().getYAxis(0).getTitle().setVisible(false);
		chart.getLegend().setVisible(true);
	}

	public void setContentProvider(final IChartContentProvider contentProvider) {
		// delete existing series
		this.contentProvider = contentProvider;
		refresh();
	}

	public void refresh() {
		if (contentProvider == null)
			return;
		for (int i = 0; i < contentProvider.getSeriesCount(); i++) {
			final ILineSeries series = (ILineSeries) chart.getSeriesSet()
					.createSeries(SeriesType.LINE,
							contentProvider.getSeriesName(i));
			
			if (contentProvider.isDateSeries(i)) {
				series.setXDateSeries(contentProvider.getDateXSeries(i));
			} else {
				series.setXSeries(contentProvider.getXSeries(i));
			}
			
			series.setYSeries(contentProvider.getYSeries(i));
		
			series.setLineColor(new Color(chart.getDisplay(), 
					new RGB(360 * i / (float) contentProvider.getSeriesCount(), 1.0f, 1.0f)));
			series.setSymbolSize(0);
			series.setSymbolType(PlotSymbolType.NONE);
		}
		
		
		
		chart.getAxisSet().adjustRange(); // make fit
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
