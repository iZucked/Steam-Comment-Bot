/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.standard.inventory;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.ToIntFunction;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swtchart.Chart;
import org.eclipse.swtchart.IAxisSet;
import org.eclipse.swtchart.IBarSeries;
import org.eclipse.swtchart.ISeries;
import org.eclipse.swtchart.ISeries.SeriesType;
import org.eclipse.swtchart.ISeriesSet;

import com.mmxlabs.lingo.reports.views.standard.InventoryReport.OverliftChartMode;
import com.mmxlabs.models.lng.commercial.BaseLegalEntity;

public class OverliftChartModeAction extends Action {

	@NonNull
	private final Chart overliftChart;
	@NonNull
	private OverliftChartMode chartMode = OverliftChartMode.NON_CUMULATIVE;
	private OverliftChartState state = null;

	public OverliftChartModeAction(@NonNull final Chart overliftChart) {
		super("", IAction.AS_PUSH_BUTTON);
		setText(String.format("Mode: %s", getModeText()));
		this.overliftChart = overliftChart;
	}

	private String getModeText() {
		if (chartMode == OverliftChartMode.CUMULATIVE) {
			return "additive";
		} else {
			return "standard";
		}
	}

	public void setState(OverliftChartState state) {
		this.state = state;
	}

	public void clear() {
		this.state = null;
	}

	@Override
	public void run() {
		final int modeIdx = (chartMode.ordinal() + 1) % OverliftChartMode.values().length;
		chartMode = OverliftChartMode.values()[modeIdx];
		setText(String.format("Mode: %s", getModeText()));
		clearChartData(overliftChart.getSeriesSet());
		updateChartData();
		finaliseMULLChart(overliftChart, "Month", "Underlift/Overlift");
	}

	private ToIntFunction<MullInformation> getValueExtractor() {
		return this.chartMode == OverliftChartMode.CUMULATIVE ? MullInformation::getOverliftCF : MullInformation::getOverlift;
	}

	public void updateChartData() {
		if (state != null) {
			setMULLChartData(overliftChart, state.getXCategoryLabels(), state.getEntitiesOrdered(), state.getMullData(), getValueExtractor());
		}
	}

	private void clearChartData(final ISeriesSet seriesSet) {
		final Set<String> names = new HashSet<>();
		for (final ISeries s : seriesSet.getSeries()) {
			names.add(s.getId());
		}
		names.forEach(seriesSet::deleteSeries);
	}

	private void finaliseMULLChart(final Chart chart, final String xLabel, final String yLabel) {
		final IAxisSet axisSet = chart.getAxisSet();
		axisSet.getXAxis(0).getTick().setForeground(Display.getDefault().getSystemColor(SWT.COLOR_BLACK));
		axisSet.getXAxis(0).getTitle().setForeground(Display.getDefault().getSystemColor(SWT.COLOR_BLACK));
		axisSet.getXAxis(0).getTitle().setText(xLabel);

		axisSet.getYAxis(0).getTick().setForeground(Display.getDefault().getSystemColor(SWT.COLOR_BLACK));
		axisSet.getYAxis(0).getTitle().setForeground(Display.getDefault().getSystemColor(SWT.COLOR_BLACK));
		axisSet.getYAxis(0).getTitle().setText(yLabel);

		axisSet.adjustRange();
		chart.updateLayout();
		chart.redraw();
	}

	private void setMULLChartData(final Chart chart, final String[] xCategoryLabels, final List<BaseLegalEntity> entitiesOrdered, final List<MullInformation> mullInformationList,
			final ToIntFunction<MullInformation> valueExtractor) {
		chart.getAxisSet().getXAxis(0).enableCategory(true);
		chart.getAxisSet().getXAxis(0).setCategorySeries(xCategoryLabels);

		final Map<BaseLegalEntity, List<Integer>> barSeriesData = new HashMap<>();
		for (final BaseLegalEntity entity : entitiesOrdered) {
			barSeriesData.put(entity, new LinkedList<>());
		}

		final Iterator<MullInformation> iterMullList2 = mullInformationList.iterator();
		while (iterMullList2.hasNext()) {
			for (final BaseLegalEntity entity : entitiesOrdered) {
				final MullInformation currMull = iterMullList2.next();
				barSeriesData.get(entity).add(valueExtractor.applyAsInt(currMull));
			}
		}

		final Color[] colourSequence = state.getOverliftChartColourSequence();
		int colourIndex = 0;
		for (final BaseLegalEntity entity : entitiesOrdered) {
			final List<Integer> intSeries = barSeriesData.get(entity);
			final double[] doubleSeries = new double[intSeries.size()];
			int seriesIndex = 0;
			final Iterator<Integer> iterIntSeries = intSeries.iterator();
			while (iterIntSeries.hasNext()) {
				doubleSeries[seriesIndex] = iterIntSeries.next().doubleValue();
				++seriesIndex;
			}
			IBarSeries currentEntitySeries = (IBarSeries) chart.getSeriesSet().createSeries(SeriesType.BAR, entity.getName());
			currentEntitySeries.setYSeries(doubleSeries);
			currentEntitySeries.setBarColor(colourSequence[colourIndex]);
			++colourIndex;
			colourIndex %= colourSequence.length;
		}
	}
}
