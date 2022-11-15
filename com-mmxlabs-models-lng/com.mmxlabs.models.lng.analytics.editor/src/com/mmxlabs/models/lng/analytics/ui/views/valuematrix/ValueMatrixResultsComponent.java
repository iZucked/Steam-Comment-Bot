package com.mmxlabs.models.lng.analytics.ui.views.valuematrix;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ColumnViewerToolTipSupport;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.nebula.jface.gridviewer.GridTableViewer;
import org.eclipse.nebula.jface.gridviewer.GridViewerColumn;
import org.eclipse.nebula.jface.gridviewer.internal.CellSelection;
import org.eclipse.nebula.widgets.grid.GridColumn;
import org.eclipse.nebula.widgets.grid.GridColumnGroup;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.forms.events.IExpansionListener;
import org.eclipse.ui.forms.widgets.ExpandableComposite;

import com.mmxlabs.models.lng.analytics.SwapValueMatrixModel;
import com.mmxlabs.models.lng.analytics.SwapValueMatrixResult;
import com.mmxlabs.models.lng.analytics.SwapValueMatrixResultSet;
import com.mmxlabs.models.lng.analytics.ui.views.sandbox.components.SandboxUIHelper;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioElementNameHelper;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.tabular.GridViewerHelper;
import com.mmxlabs.models.ui.tabular.renderers.CenteringColumnGroupHeaderRenderer;
import com.mmxlabs.models.ui.tabular.renderers.ColumnGroupHeaderRenderer;

public class ValueMatrixResultsComponent extends AbstractValueMatrixComponent {

	private final boolean SHOW_ALTERNATIVES = false;

	private final double PNL_SCALEDOWN_FACTOR = 1_000_000.0;
	private final double VOL_SCALEDOWN_FACTOR = 1_000_000.0;

	protected SandboxUIHelper sandboxUIHelper;

	private GridTableViewer valueMatrixViewer;
	private GridTableViewer summaryTableViewer;

	private Composite resultsComposite;

	private Composite matrixComposite;

	private Composite summaryComposite;

	private class SummaryRow {
		boolean hasAlternative;
		boolean empty;
		String rowHeader;
		double value;
		double alternative;
		String units;
	}

	public ValueMatrixResultsComponent(@NonNull final IScenarioEditingLocation scenarioEditingLocation, final Map<Object, IStatus> validationErrors,
			@NonNull final Supplier<SwapValueMatrixModel> modelProvider) {
		super(scenarioEditingLocation, validationErrors, modelProvider);
	}

	private Control createResultsComposite(final Composite parent) {
		resultsComposite = new Composite(parent, SWT.NONE);
		resultsComposite.setLayout(GridLayoutFactory.swtDefaults().margins(0, 0).numColumns(2).create());
		resultsComposite.setLayoutData(GridDataFactory.swtDefaults().align(SWT.LEFT, SWT.TOP).create());
		matrixComposite = new Composite(resultsComposite, SWT.NONE);
		matrixComposite.setLayout(GridLayoutFactory.swtDefaults().margins(0, 0).create());
		summaryComposite = new Composite(resultsComposite, SWT.NONE);
		summaryComposite.setLayout(GridLayoutFactory.swtDefaults().margins(20, 0).create());
		summaryComposite.setLayoutData(GridDataFactory.swtDefaults().align(SWT.LEFT, SWT.TOP).create());
//		summaryComposite.setLayoutData

		final GridTableViewer viewer = new GridTableViewer(summaryComposite, SWT.NONE);
		ColumnViewerToolTipSupport.enableFor(viewer);

		GridViewerHelper.configureLookAndFeel(viewer);
		viewer.getGrid().setHeaderVisible(true);
		viewer.getGrid().setRowHeaderVisible(true);

		viewer.getGrid().setCellSelectionEnabled(true);
		viewer.setContentProvider(new ArrayContentProvider());
		viewer.setRowHeaderLabelProvider(new CellLabelProvider() {

			@Override
			public void update(ViewerCell cell) {
				final Object element = cell.getElement();
				if (element instanceof @NonNull final SummaryRow row) {
					if (!row.empty) {
						cell.setText(row.rowHeader);
					} else {
						cell.setText("");
					}
				}
			}
		});
		summaryTableViewer = viewer;

		{
			final GridViewerColumn gvc = new GridViewerColumn(viewer, SWT.CENTER);
			gvc.getColumn().setTree(false);
			GridViewerHelper.configureLookAndFeel(gvc);
			gvc.getColumn().setText("");
			gvc.getColumn().setWidth(40);
			gvc.getColumn().setDetail(true);
			gvc.getColumn().setSummary(true);
			gvc.setLabelProvider(new CellLabelProvider() {
				@Override
				public void update(ViewerCell cell) {
					if (cell.getElement() instanceof @NonNull final SummaryRow row && !row.empty) {
						if (SHOW_ALTERNATIVES && row.hasAlternative) {
							cell.setText(String.format("%.2f/%.2f", row.value, row.alternative));
						} else {
							cell.setText(String.format("%.2f", row.value));
						}
					} else {
						cell.setText("");
					}
				}
			});
		}
		{
			final GridViewerColumn gvc = new GridViewerColumn(viewer, SWT.CENTER);
			gvc.getColumn().setTree(false);
			GridViewerHelper.configureLookAndFeel(gvc);
			gvc.getColumn().setText("");
			gvc.getColumn().setWidth(40);
			gvc.getColumn().setDetail(true);
			gvc.getColumn().setSummary(true);
			gvc.setLabelProvider(new CellLabelProvider() {
				@Override
				public void update(ViewerCell cell) {
					if (cell.getElement() instanceof @NonNull final SummaryRow row && !row.empty) {
						cell.setText(row.units);
					} else {
						cell.setText("");
					}
				}
			});
		}
		matrixComposite.setVisible(true);
		summaryComposite.setVisible(false);
//		summaryTableViewer.getGrid().setVisible(false);
//		final GridTreeViewer viewer = new GridTreeViewer(resultsComposite, SWT.NONE);
//		ColumnViewerToolTipSupport.enableFor(viewer);
//		
//		GridViewerHelper.configureLookAndFeel(viewer);
//		viewer.getGrid().setHeaderVisible(true);
//		viewer.getGrid().setRowHeaderVisible(true);
//
//		viewer.getGrid().setCellSelectionEnabled(true);
//		valueMatrixViewer = viewer;
		inputWants.add(model -> {
			refresh(model);
		});
		return resultsComposite;
	}

	private void createValueMatrix(@NonNull final SwapValueMatrixModel model, final List<Integer> sortedColumnNumbers) {
		final GridTableViewer viewer = new GridTableViewer(matrixComposite, SWT.SINGLE);
		ColumnViewerToolTipSupport.enableFor(viewer);

		GridViewerHelper.configureLookAndFeel(viewer);
		viewer.getGrid().setHeaderVisible(true);
		viewer.getGrid().setRowHeaderVisible(true);

		viewer.getGrid().setCellSelectionEnabled(true);
		viewer.setContentProvider(new ArrayContentProvider());
		valueMatrixViewer = viewer;
		viewer.setRowHeaderLabelProvider(new CellLabelProvider() {

			@Override
			public void update(ViewerCell cell) {
				final Object element = cell.getElement();
				if (element instanceof @NonNull final SwapValueMatrixResult[] result) {
					if (result.length > 0) {
						cell.setText(Integer.toString(result[0].getSwapMarketPrice()));
					}
				}
			}
		});

		final GridColumnGroup valueMatrixColumnGroup = new GridColumnGroup(viewer.getGrid(), SWT.CENTER);
		final DischargeSlot dischargeSlot = model.getBaseDischarge().getSlot();
		final String dischargeName = dischargeSlot == null ? "<Unknown>" : ScenarioElementNameHelper.getName(dischargeSlot, "<Unknown>");
		valueMatrixColumnGroup.setHeaderRenderer(new ColumnGroupHeaderRenderer());
		valueMatrixColumnGroup.setText(String.format("%s sales price", dischargeName));
		final CenteringColumnGroupHeaderRenderer renderer = new CenteringColumnGroupHeaderRenderer();
		valueMatrixColumnGroup.setHeaderRenderer(renderer);
		int index = 0;
		for (final int i : sortedColumnNumbers) {
			final int pIndex = index;
			final GridColumn gc = new GridColumn(valueMatrixColumnGroup, SWT.CENTER);
			final GridViewerColumn gvc = new GridViewerColumn(viewer, gc);
			gvc.getColumn().setTree(false);
			GridViewerHelper.configureLookAndFeel(gvc);
			gvc.getColumn().setText(Integer.toString(i));
			gvc.getColumn().setWidth(40);
			gvc.getColumn().setDetail(true);
			gvc.getColumn().setSummary(true);
			gvc.setLabelProvider(new CellLabelProvider() {
				@Override
				public void update(ViewerCell cell) {
					if (cell.getElement() instanceof SwapValueMatrixResult[] valueArr) {
						final double scaledDownPnlDifference = valueArr[pIndex].getSwapPnlMinusBasePnl() / PNL_SCALEDOWN_FACTOR;
						if (scaledDownPnlDifference < 0.0) {
							cell.setText(String.format("(%.2f)", -scaledDownPnlDifference));
						} else {
							cell.setText(String.format("%.2f", scaledDownPnlDifference));
						}
					} else {
						cell.setText("");
					}
				}
			});
			++index;
		}
		valueMatrixViewer.addSelectionChangedListener(event -> {
			final ISelection selection = event.getSelection();
			boolean showSummary = false;
			if (selection instanceof CellSelection cellSelection && cellSelection.size() == 1) {
				final Object firstElement = cellSelection.getFirstElement();
				final List<?> selectedIndices = cellSelection.getIndices(firstElement);
				if (firstElement instanceof SwapValueMatrixResult[] resultArr && selectedIndices.size() == 1) {
					final int arrIndex = (int) selectedIndices.get(0);
					final SwapValueMatrixResult result = resultArr[arrIndex];
					final List<SummaryRow> rows = buildSummaryRows(result);
					summaryTableViewer.setInput(rows);
					summaryTableViewer.refresh();
					summaryComposite.redraw();
					summaryComposite.requestLayout();
					summaryComposite.layout();
					for (final GridColumn col : summaryTableViewer.getGrid().getColumns()) {
						col.pack();
					}
					summaryTableViewer.refresh();
					summaryComposite.redraw();
					summaryComposite.requestLayout();
					summaryComposite.layout();
					showSummary = true;
				}
			}
			summaryComposite.setVisible(showSummary);
		});
	}

	private List<SummaryRow> buildSummaryRows(@NonNull final SwapValueMatrixResult result) {
		final SwapValueMatrixModel model = modelProvider.get();
		if (model == null) {
			return Collections.emptyList();
		}
		final List<SummaryRow> rows = new ArrayList<>();
		final LoadSlot loadSlot = model.getBaseLoad().getSlot();
		final String loadName = loadSlot == null ? "<Unknown>" : ScenarioElementNameHelper.getName(loadSlot, "<Unknown>");
		final DischargeSlot dischargeSlot = model.getBaseDischarge().getSlot();
		final String dischargeName = dischargeSlot == null ? "<Unknown>" : ScenarioElementNameHelper.getName(dischargeSlot, "<Unknown>");
		{
			final SummaryRow row = new SummaryRow();
			row.hasAlternative = true;
			row.rowHeader = String.format("%s price", loadName);
			row.empty = false;
			row.units = "$/mmBtu";
			row.value = result.getSwapFobLoadPrice();
			row.alternative = result.getBaseLoadPrice();
			rows.add(row);
		}
		{
			final SummaryRow row = new SummaryRow();
			row.rowHeader = String.format("%s price", dischargeName);
			row.empty = false;
			row.units = "$/mmBtu";
			row.value = result.getBaseDischargePrice();
			rows.add(row);
		}
		{
			final SummaryRow row = new SummaryRow();
			row.rowHeader = "Market price";
			row.empty = false;
			row.units = "$/mmBtu";
			row.value = result.getSwapMarketPrice();
			rows.add(row);
		}
		{
			final SummaryRow row = new SummaryRow();
			row.rowHeader = "Spread";
			row.empty = false;
			row.units = "$/mmBtu";
			row.value = result.getSwapMarketPrice() - result.getBaseDischargePrice();
			rows.add(row);
		}
		{
			final SummaryRow row = new SummaryRow();
			row.empty = true;
			rows.add(row);
		}
		{
			final SummaryRow row = new SummaryRow();
			row.hasAlternative = true;
			row.rowHeader = "Load vol";
			row.empty = false;
			row.units = "tBtu";
			row.value = result.getSwapFobLoadVolume() / VOL_SCALEDOWN_FACTOR;
			row.alternative = result.getBaseFobLoadVolume() / VOL_SCALEDOWN_FACTOR;
			rows.add(row);
		}
		{
			final SummaryRow row = new SummaryRow();
			row.rowHeader = "Buy vol";
			row.empty = false;
			row.units = "tBtu";
			row.value = result.getMarketBuyVolume() / VOL_SCALEDOWN_FACTOR;
			rows.add(row);
		}
		{
			final SummaryRow row = new SummaryRow();
			row.hasAlternative = true;
			row.rowHeader = "Sell vol";
			row.empty = false;
			row.units = "tBtu";
			row.value = result.getMarketSellVolume() / VOL_SCALEDOWN_FACTOR;
			row.alternative = result.getBaseDesSellVolume() / VOL_SCALEDOWN_FACTOR;
			rows.add(row);
		}
		{
			final SummaryRow row = new SummaryRow();
			row.rowHeader = "Optimised vol";
			row.empty = false;
			row.units = "tBtu";
			row.value = (result.getMarketSellVolume() - result.getMarketBuyVolume()) / VOL_SCALEDOWN_FACTOR;
			rows.add(row);
		}
		{
			final SummaryRow row = new SummaryRow();
			row.rowHeader = "Optimised val";
			row.empty = false;
			row.units = "$m";
			row.value = ((result.getMarketSellVolume() - result.getMarketBuyVolume()) * (result.getSwapMarketPrice() - result.getBaseDischargePrice())) / PNL_SCALEDOWN_FACTOR;
			rows.add(row);
		}
		{
			final SummaryRow row = new SummaryRow();
			row.empty = true;
			rows.add(row);
		}
		{
			final SummaryRow row = new SummaryRow();
			row.rowHeader = "Swap fee";
			row.empty = false;
			row.units = "$/mmBtu";
			row.value = -1 * ((SwapValueMatrixResultSet) result.eContainer()).getSwapFee();
			rows.add(row);
		}
		{
			final SummaryRow row = new SummaryRow();
			row.rowHeader = "Additional cost";
			row.empty = false;
			row.units = "$m";
			row.value = -1 * ((SwapValueMatrixResultSet) result.eContainer()).getSwapFee() * result.getMarketBuyVolume() / PNL_SCALEDOWN_FACTOR;
			rows.add(row);
		}
		{
			final SummaryRow row = new SummaryRow();
			row.empty = true;
			rows.add(row);
		}
		{
			final SummaryRow row = new SummaryRow();
			row.rowHeader = "Net EPL";
			row.empty = false;
			row.units = "$m";
			row.value = (((result.getMarketSellVolume() - result.getMarketBuyVolume()) * (result.getSwapMarketPrice() - result.getBaseDischargePrice()))
					- ((SwapValueMatrixResultSet) result.eContainer()).getSwapFee() * result.getMarketBuyVolume()) / PNL_SCALEDOWN_FACTOR;
			rows.add(row);
		}
		return rows;
	}

	private void clearValueMatrix() {
		if (valueMatrixViewer != null) {
			valueMatrixViewer.getGrid().dispose();
		}
		valueMatrixViewer = null;
	}

	@Override
	public void createControls(final Composite parent, final boolean expanded, final IExpansionListener expansionListener, final ValueMatrixModellerView valueMatrixModellerView) {
		this.valueMatrixModellerView = valueMatrixModellerView;
		final ExpandableComposite expandableComposite = wrapInExpandable(parent, "Matrix", this::createResultsComposite, null, false);
		expandableComposite.setLayoutData(GridDataFactory.fillDefaults().minSize(SWT.DEFAULT, 200).grab(true, true).create());
		expandableComposite.setExpanded(expanded);
		expandableComposite.addExpansionListener(expansionListener);
	}

	@Override
	public void dispose() {
		clearValueMatrix();
	}

	public void refresh() {
		refresh(modelProvider.get());
	}

	public void refresh(final SwapValueMatrixModel model) {
		clearValueMatrix();
		summaryComposite.setVisible(false);
		if (model != null) {
			final SwapValueMatrixResultSet resultsSet = model.getSwapValueMatrixResult();
			if (resultsSet != null) {
				final Set<Integer> rowNumbers = new HashSet<>();
				final Set<Integer> columnNumbers = new HashSet<>();
				for (final SwapValueMatrixResult result : resultsSet.getResults()) {
					rowNumbers.add(result.getSwapMarketPrice());
					columnNumbers.add(result.getBaseDischargePrice());
				}
				final SwapValueMatrixResult[][] rows = new SwapValueMatrixResult[rowNumbers.size()][columnNumbers.size()];
				final List<Integer> sortedRowNumbers = rowNumbers.stream().sorted().toList();
				final List<Integer> sortedColumnNumbers = columnNumbers.stream().sorted().toList();
				final Map<Integer, Integer> rowNumberToIndex = new HashMap<>();
				final Map<Integer, Integer> columnNumberToIndex = new HashMap<>();
				{
					int i = 0;
					for (final int rowNumber : sortedRowNumbers) {
						rowNumberToIndex.put(rowNumber, i);
						++i;
					}
				}
				{
					int i = 0;
					for (final int columnNumber : sortedColumnNumbers) {
						columnNumberToIndex.put(columnNumber, i);
						++i;
					}
				}
				for (final SwapValueMatrixResult result : resultsSet.getResults()) {
					rows[rowNumberToIndex.get(result.getSwapMarketPrice())][columnNumberToIndex.get(result.getBaseDischargePrice())] = result;
				}
				createValueMatrix(model, sortedColumnNumbers);
				valueMatrixViewer.setInput(rows);
				valueMatrixViewer.refresh();
				resultsComposite.layout();
				resultsComposite.redraw();
				resultsComposite.requestLayout();
			}
		}
	}
}
