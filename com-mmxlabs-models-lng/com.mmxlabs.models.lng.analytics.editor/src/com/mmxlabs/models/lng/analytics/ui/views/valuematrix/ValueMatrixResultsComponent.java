/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.ui.views.valuematrix;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.ToDoubleFunction;
import java.util.function.ToLongFunction;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.resource.LocalResourceManager;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ColumnViewerToolTipSupport;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.nebula.jface.gridviewer.GridTableViewer;
import org.eclipse.nebula.jface.gridviewer.GridViewerColumn;
import org.eclipse.nebula.jface.gridviewer.internal.CellSelection;
import org.eclipse.nebula.widgets.grid.DataVisualizer;
import org.eclipse.nebula.widgets.grid.Grid;
import org.eclipse.nebula.widgets.grid.GridColumn;
import org.eclipse.nebula.widgets.grid.GridColumnGroup;
import org.eclipse.nebula.widgets.grid.GridItem;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.ui.forms.events.IExpansionListener;
import org.eclipse.ui.forms.widgets.ExpandableComposite;

import com.mmxlabs.models.lng.analytics.SwapValueMatrixModel;
import com.mmxlabs.models.lng.analytics.SwapValueMatrixResult;
import com.mmxlabs.models.lng.analytics.SwapValueMatrixResultSet;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioElementNameHelper;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.tabular.GridViewerHelper;
import com.mmxlabs.models.ui.tabular.renderers.CellAsRowHeaderRenderer;
import com.mmxlabs.models.ui.tabular.renderers.CenteringColumnGroupHeaderRenderer;
import com.mmxlabs.models.ui.tabular.renderers.ColumnGroupHeaderRenderer;
import com.mmxlabs.models.ui.tabular.renderers.ColumnImageCenterHeaderRenderer;
import com.mmxlabs.rcp.icons.lingo.CommonImages;
import com.mmxlabs.rcp.icons.lingo.CommonImages.IconMode;
import com.mmxlabs.rcp.icons.lingo.CommonImages.IconPaths;

public class ValueMatrixResultsComponent extends AbstractValueMatrixComponent {

	private static final String UNITS_DOLLAR_PER_MMBTU = "$/mmBtu";
	private static final String UNITS_TBTU = "tBtu";
	private static final String UNITS_MILLION_DOLLARS = "$m";
	private static final String TAB = "    ";

	private static final double PNL_SCALEDOWN_FACTOR = 1_000_000.0;
	private static final double VOL_SCALEDOWN_FACTOR = 1_000_000.0;

	private static final String KEY_MATRIX_COLUMN_OFFSET = "value-matrix-column-offset";
	private static final String KEY_MATRIX_ROW_OFFSET = "value-matrix-row-offset";

	private GridTableViewer valueMatrixViewer;
	private GridTableViewer summaryTableViewer;
	private GridTableViewer diffSummaryTableViewer;

	private Composite resultsComposite;

	private Composite matrixComposite;
	private Composite summaryComposite;
	private Composite diffSummaryComposite;

	private Runnable recalculateScrollbar;

	private LocalResourceManager localResourceManager = null;
	private Color[] colourSequence = null;
	private int nextColourIndex = 0;

	private boolean showingGroups = false;
	private boolean highlightLosses = false;

	private Map<SwapValueChangeKey, Integer> changeToColourIndex = new HashMap<>();

	private class DiffSummaryRow {
		String rowHeader;
		boolean ignoreBase;
		boolean ignoreDiff;
		boolean isEmpty;
		double baseValue;
		double swapValue;
		double swapMinusBase;
		boolean grey;
		boolean baseGrey;
		boolean swapGrey;
		boolean highlightDiff;
	}

	public ValueMatrixResultsComponent(@NonNull final IScenarioEditingLocation scenarioEditingLocation, @NonNull final Supplier<SwapValueMatrixModel> modelProvider,
			final Runnable recalculateScrollbar) {
		super(scenarioEditingLocation, modelProvider);
		this.recalculateScrollbar = recalculateScrollbar;

	}

	private void highlightCell(final ViewerCell cell) {
		final ISelection selection = valueMatrixViewer.getSelection();
		if (selection instanceof CellSelection cellSelection && cellSelection.size() == 1) {
			final Object firstElement = cellSelection.getFirstElement();
			final List<?> selectedIndices = cellSelection.getIndices(firstElement);
			if (firstElement instanceof SwapValueMatrixResult[] resultArr && selectedIndices.size() == 1) {
				final int arrIndex = (int) selectedIndices.get(0);
				if (arrIndex > 1) {
					final SwapValueMatrixResult result = resultArr[arrIndex - 2];
					cell.setBackground(colourSequence[changeToColourIndex.get(new SwapValueChangeKey(result))]);
				}
			}
		}
	}

	private GridTableViewer buildSummaryViewer(final Composite parent, final Listener resizeListener) {
		final GridTableViewer viewer = new GridTableViewer(parent, SWT.NONE);
		ColumnViewerToolTipSupport.enableFor(viewer);

		GridViewerHelper.configureLookAndFeel(viewer);
		viewer.getGrid().setHeaderVisible(true);
		viewer.getGrid().setRowHeaderVisible(true);

		viewer.getGrid().setCellSelectionEnabled(false);
		viewer.setContentProvider(new ArrayContentProvider());
		viewer.setRowHeaderLabelProvider(new CellLabelProvider() {

			@Override
			public void update(ViewerCell cell) {
				final Object element = cell.getElement();
				if (element instanceof @NonNull final ISummaryRow row) {
					cell.setText(row.getRowHeader());
				}
			}
		});

		{
			final GridViewerColumn gvc = new GridViewerColumn(viewer, SWT.CENTER);
			gvc.getColumn().setTree(false);
			GridViewerHelper.configureLookAndFeel(gvc);
			gvc.getColumn().setText("");
			gvc.getColumn().setMinimumWidth(40);
			gvc.setLabelProvider(new CellLabelProvider() {
				@Override
				public void update(ViewerCell cell) {
					if (cell.getElement() instanceof @NonNull final ISummaryRow row) {
						if (row instanceof @NonNull final MergedSummaryRow mergedRow) {
							final DataVisualizer dv = summaryTableViewer.getGrid().getDataVisualizer();
							final GridItem item = (GridItem) cell.getItem();
							dv.setColumnSpan(item, cell.getColumnIndex(), 1);
							cell.setText(String.format("%.2f", mergedRow.getValue()));
							if (showingGroups && mergedRow.shouldHighlight()) {
								highlightCell(cell);
							} else {
								cell.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
							}
						} else if (row instanceof @NonNull final SwapSummaryRow swapRow) {
							cell.setText(String.format("%.2f", swapRow.getSwapValue()));
							if (showingGroups && swapRow.shouldHighlight()) {
								highlightCell(cell);
							} else {
								cell.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
							}
						} else if (row instanceof @NonNull final CombinedSummaryRow combinedRow) {
							cell.setText(String.format("%.2f", combinedRow.getSwapValue()));
							if (showingGroups && combinedRow.shouldHighlightSwap()) {
								highlightCell(cell);
							} else {
								cell.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
							}
						} else if (row instanceof EmptySummaryRow) {
							final DataVisualizer dv = summaryTableViewer.getGrid().getDataVisualizer();
							final GridItem item = (GridItem) cell.getItem();
							dv.setColumnSpan(item, cell.getColumnIndex(), 2);
							cell.setText("");
						} else {
							cell.setText("");
						}
					}
				}
			});
			gvc.getColumn().addListener(SWT.Resize, resizeListener);
		}
		{
			final GridViewerColumn gvc = new GridViewerColumn(viewer, SWT.CENTER);
			gvc.getColumn().setTree(false);
			GridViewerHelper.configureLookAndFeel(gvc);
			gvc.getColumn().setImage(CommonImages.getImage(IconPaths.Pin, IconMode.Enabled));
			gvc.getColumn().setHeaderRenderer(new ColumnImageCenterHeaderRenderer());
			gvc.getColumn().setMinimumWidth(40);
			gvc.setLabelProvider(new CellLabelProvider() {

				@Override
				public void update(ViewerCell cell) {
					if (cell.getElement() instanceof @NonNull final ISummaryRow row) {
						if (row instanceof @NonNull final BaseSummaryRow baseRow) {
							cell.setText(String.format("%.2f", baseRow.getBaseValue()));
							if (showingGroups && baseRow.shouldHighlight()) {
								highlightCell(cell);
							} else {
								cell.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
							}
						} else if (row instanceof @NonNull final CombinedSummaryRow combinedRow) {
							cell.setText(String.format("%.2f", combinedRow.getBaseValue()));
							if (showingGroups && combinedRow.shouldHighlightBase()) {
								highlightCell(cell);
							} else {
								cell.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
							}
						} else if (!(row instanceof MergedSummaryRow)) {
							cell.setText("");
						}
					}
				}
			});
			gvc.getColumn().addListener(SWT.Resize, resizeListener);
		}
		{
			final GridViewerColumn gvc = new GridViewerColumn(viewer, SWT.CENTER);
			gvc.getColumn().setTree(false);
			GridViewerHelper.configureLookAndFeel(gvc);
			gvc.getColumn().setText("Units");
			gvc.getColumn().setMinimumWidth(40);
			gvc.setLabelProvider(new CellLabelProvider() {
				@Override
				public void update(ViewerCell cell) {
					cell.setText(cell.getElement() instanceof @NonNull final IUnitSummaryRow row ? row.getUnits() : "");
				}
			});
			gvc.getColumn().addListener(SWT.Resize, resizeListener);
		}
		return viewer;
	}

	private GridTableViewer buildDiffSummaryViewer(final Composite parent, final Listener resizeListener) {
		final GridTableViewer viewer = new GridTableViewer(parent, SWT.NONE);
		ColumnViewerToolTipSupport.enableFor(viewer);

		GridViewerHelper.configureLookAndFeel(viewer);
		viewer.getGrid().setHeaderVisible(true);
		viewer.getGrid().setRowHeaderVisible(true);

		viewer.getGrid().setCellSelectionEnabled(false);
		viewer.setContentProvider(new ArrayContentProvider());
		viewer.setRowHeaderLabelProvider(new CellLabelProvider() {

			@Override
			public void update(ViewerCell cell) {
				final Object element = cell.getElement();
				if (element instanceof @NonNull final DiffSummaryRow row) {
					cell.setText(row.rowHeader);
				}
			}
		});
		{
			final GridViewerColumn gvc = new GridViewerColumn(viewer, SWT.CENTER);
			gvc.getColumn().setTree(false);
			GridViewerHelper.configureLookAndFeel(gvc);
			gvc.getColumn().setText("");
			gvc.getColumn().setMinimumWidth(40);
			gvc.setLabelProvider(new CellLabelProvider() {

				@Override
				public void update(ViewerCell cell) {
					final Object element = cell.getElement();
					if (element instanceof @NonNull final DiffSummaryRow row) {
						if (!row.isEmpty) {
							cell.setText(String.format("%.2f", row.swapValue));
							if (row.grey || row.swapGrey) {
								cell.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_GRAY));
							}
						} else {
							cell.setText("");
							final DataVisualizer dv = diffSummaryTableViewer.getGrid().getDataVisualizer();
							final GridItem item = (GridItem) cell.getItem();
							dv.setColumnSpan(item, cell.getColumnIndex(), 2);
						}
					}

				}
			});
			gvc.getColumn().addListener(SWT.Resize, resizeListener);
		}
		{
			final GridViewerColumn gvc = new GridViewerColumn(viewer, SWT.CENTER);
			gvc.getColumn().setTree(false);
			GridViewerHelper.configureLookAndFeel(gvc);
			gvc.getColumn().setImage(CommonImages.getImage(IconPaths.Pin, IconMode.Enabled));
			gvc.getColumn().setHeaderRenderer(new ColumnImageCenterHeaderRenderer());
			gvc.getColumn().setMinimumWidth(40);
			gvc.setLabelProvider(new CellLabelProvider() {

				@Override
				public void update(ViewerCell cell) {
					final Object element = cell.getElement();
					if (element instanceof @NonNull final DiffSummaryRow row) {
						if (!row.isEmpty && !row.ignoreBase) {
							cell.setText(String.format("%.2f", row.baseValue));
							if (row.grey || row.baseGrey) {
								cell.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_GRAY));
							}
						}
					}
				}
			});
			gvc.getColumn().addListener(SWT.Resize, resizeListener);
		}
		{
			final GridViewerColumn gvc = new GridViewerColumn(viewer, SWT.CENTER);
			gvc.getColumn().setTree(false);
			GridViewerHelper.configureLookAndFeel(gvc);
			gvc.getColumn().setImage(CommonImages.getImage(IconPaths.Delta, IconMode.Enabled));
			gvc.getColumn().setHeaderRenderer(new ColumnImageCenterHeaderRenderer());
			gvc.getColumn().setMinimumWidth(40);
			gvc.setLabelProvider(new CellLabelProvider() {

				@Override
				public void update(ViewerCell cell) {
					final Object element = cell.getElement();
					if (element instanceof @NonNull final DiffSummaryRow row) {
						if (!row.isEmpty && !row.ignoreDiff) {
							cell.setText(String.format("%.2f", row.swapMinusBase));
							if (row.grey) {
								cell.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_GRAY));
							}
							if (showingGroups && row.highlightDiff) {
								highlightCell(cell);
							} else {
								cell.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
							}
						}
					}
				}
			});
			gvc.getColumn().addListener(SWT.Resize, resizeListener);
		}
		return viewer;
	}

	private Control createResultsComposite(final Composite parent) {
		resultsComposite = new Composite(parent, SWT.NONE);
		resultsComposite.setLayout(GridLayoutFactory.swtDefaults().margins(0, 0).numColumns(3).create());
		resultsComposite.setLayoutData(GridDataFactory.swtDefaults().align(SWT.LEFT, SWT.TOP).minSize(SWT.DEFAULT, SWT.DEFAULT).create());
		matrixComposite = new Composite(resultsComposite, SWT.NONE);
		matrixComposite.setLayoutData(GridDataFactory.swtDefaults().align(SWT.LEFT, SWT.TOP).create());
		matrixComposite.setLayout(GridLayoutFactory.swtDefaults().margins(0, 0).create());
		summaryComposite = new Composite(resultsComposite, SWT.NONE);
		summaryComposite.setLayout(GridLayoutFactory.swtDefaults().margins(20, 0).create());
		summaryComposite.setLayoutData(GridDataFactory.swtDefaults().align(SWT.LEFT, SWT.TOP).minSize(500, SWT.DEFAULT).create());
		diffSummaryComposite = new Composite(resultsComposite, SWT.NONE);
		diffSummaryComposite.setLayout(GridLayoutFactory.swtDefaults().margins(20, 0).create());
		diffSummaryComposite.setLayoutData(GridDataFactory.swtDefaults().align(SWT.LEFT, SWT.TOP).minSize(500, SWT.DEFAULT).create());

		final Listener resizeListener = new Listener() {
			@Override
			public void handleEvent(Event event) {
				matrixComposite.requestLayout();
				matrixComposite.redraw();
				resultsComposite.requestLayout();
				resultsComposite.redraw();
			}
		};
		summaryTableViewer = buildSummaryViewer(summaryComposite, resizeListener);
		diffSummaryTableViewer = buildDiffSummaryViewer(diffSummaryComposite, resizeListener);

		matrixComposite.setVisible(true);
		summaryComposite.setVisible(false);
		diffSummaryComposite.setVisible(false);
		inputWants.add(this::refresh);
		return resultsComposite;
	}

	private void createValueMatrix(@NonNull final SwapValueMatrixModel model, final List<Integer> sortedColumnNumbers) {
		final GridTableViewer viewer = new GridTableViewer(matrixComposite, SWT.SINGLE);
		ColumnViewerToolTipSupport.enableFor(viewer);

		GridViewerHelper.configureLookAndFeel(viewer);
		viewer.getGrid().setHeaderVisible(true);

		viewer.getGrid().setCellSelectionEnabled(true);
		viewer.setContentProvider(new ArrayContentProvider());
		valueMatrixViewer = viewer;
		viewer.getGrid().setRowHeaderVisible(false);

		final Listener resizeListener = event -> {
			matrixComposite.requestLayout();
			matrixComposite.redraw();
			resultsComposite.requestLayout();
			resultsComposite.redraw();
		};

		{
			final GridViewerColumn gvc = new GridViewerColumn(viewer, SWT.CENTER);
			gvc.getColumn().setTree(false);
			GridViewerHelper.configureLookAndFeel(gvc);
			gvc.getColumn().setText("");
			gvc.getColumn().setMinimumWidth(40);
			gvc.getColumn().setDetail(true);
			gvc.getColumn().setSummary(true);
			gvc.getColumn().setCellRenderer(new ValueMatrixRowGroupCellRenderer());
			gvc.getColumn().setCellSelectionEnabled(false);
			gvc.setLabelProvider(new CellLabelProvider() {
				@Override
				public void update(ViewerCell cell) {
					if (valueMatrixViewer.getInput() instanceof Object[] results && results.length > 0) {
						final int numRows = results.length;
						final DataVisualizer dv = valueMatrixViewer.getGrid().getDataVisualizer();
						final GridItem item = (GridItem) cell.getItem();
						dv.setRowSpan(item, cell.getColumnIndex(), numRows - 1);
						cell.setText("Market price");
					}
				}
			});
			gvc.getColumn().addListener(SWT.Resize, resizeListener);
		}

		{
			final GridViewerColumn gvc = new GridViewerColumn(viewer, SWT.CENTER);
			gvc.getColumn().setTree(false);
			GridViewerHelper.configureLookAndFeel(gvc);
			gvc.getColumn().setText("");
			gvc.getColumn().setMinimumWidth(40);
			gvc.getColumn().setDetail(true);
			gvc.getColumn().setSummary(true);
			gvc.getColumn().setCellRenderer(new CellAsRowHeaderRenderer() {
				@Override
				protected boolean isDrawAsHover() {
					return false;
				}

				@Override
				public boolean isCellFocus() {
					return false;
				}
			});
			gvc.getColumn().setCellSelectionEnabled(false);
			gvc.setLabelProvider(new CellLabelProvider() {
				@Override
				public void update(ViewerCell cell) {
					final Object element = cell.getElement();
					if (element instanceof @NonNull final SwapValueMatrixResult[] result && result.length > 0) {
						cell.setText(Integer.toString(result[0].getSwapMarketPrice()));
					}
				}
			});
			gvc.getColumn().addListener(SWT.Resize, resizeListener);
		}

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
			gvc.getColumn().setMinimumWidth(40);
			gvc.getColumn().setDetail(true);
			gvc.getColumn().setSummary(true);
			gvc.setLabelProvider(new CellLabelProvider() {
				@Override
				public void update(ViewerCell cell) {
					String cellText = "";
					if (cell.getElement() instanceof SwapValueMatrixResult[] valueArr) {
						final double scaledDownPnlDifference = valueArr[pIndex].getSwapPnlMinusBasePnl() / PNL_SCALEDOWN_FACTOR;
						if (scaledDownPnlDifference < 0.0) {
							cellText = String.format("(%.2f)", -scaledDownPnlDifference);
							if (highlightLosses) {
								cell.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_RED));
							} else {
								cell.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_BLACK));
							}
						} else {
							cellText = String.format("%.2f", scaledDownPnlDifference);
						}
						if (showingGroups) {
							final SwapValueChangeKey key = new SwapValueChangeKey(valueArr[pIndex]);
							final int index = changeToColourIndex.computeIfAbsent(key, k -> {
								final int retVal = nextColourIndex;
								++nextColourIndex;
								nextColourIndex %= colourSequence.length;
								return retVal;
							});
							cell.setBackground(colourSequence[index]);
						} else {
							cell.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
						}
					}
					cell.setText(cellText);
				}
			});
			gvc.getColumn().addListener(SWT.Resize, resizeListener);
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
					// Account for hacked "row headers"
					if (arrIndex > 1) {
						final SwapValueMatrixResult result = resultArr[arrIndex - 2];
						final List<ISummaryRow> rows = buildSummaryRows(result);
						summaryTableViewer.setInput(rows);
						summaryTableViewer.refresh();
						final List<DiffSummaryRow> diffRows = buildDiffSummaryRows(result);
						diffSummaryTableViewer.setInput(diffRows);
						diffSummaryTableViewer.refresh();
						showSummary = true;
						final Grid summaryGrid = summaryTableViewer.getGrid();
						summaryGrid.pack();
						summaryGrid.requestLayout();
						summaryGrid.redraw();
						final Grid diffSummaryGrid = diffSummaryTableViewer.getGrid();
						diffSummaryGrid.pack();
						diffSummaryGrid.requestLayout();
						diffSummaryGrid.redraw();
						if (recalculateScrollbar != null) {
							recalculateScrollbar.run();
						}
					}
				}
			}
			summaryComposite.setVisible(showSummary);
			diffSummaryComposite.setVisible(showSummary);
		});
	}

	private ISummaryRow buildSummaryRow(final SwapValueMatrixResult result, final ValueMatrixResultDiffData diffData, @NonNull final String rowHeader,
			final ToDoubleFunction<SwapValueMatrixResult> baseValueFetcher, final ToDoubleFunction<SwapValueMatrixResult> swapValueFetcher, @NonNull final String units,
			final Predicate<ValueMatrixResultDiffData> hasBaseDiffFetcher, final Predicate<ValueMatrixResultDiffData> hasSwapDiffFetcher) {
		final double baseVal = baseValueFetcher.applyAsDouble(result);
		final double swapVal = swapValueFetcher.applyAsDouble(result);
		final boolean hasBaseDiff = hasBaseDiffFetcher.test(diffData);
		final boolean hasSwapDiff = hasSwapDiffFetcher.test(diffData);
		return baseVal == swapVal ? new MergedSummaryRow(rowHeader, units, swapVal, hasBaseDiff || hasSwapDiff) : new CombinedSummaryRow(rowHeader, units, baseVal, swapVal, hasBaseDiff, hasSwapDiff);
	}

	private ISummaryRow buildSummaryRow(final SwapValueMatrixResult result, final ValueMatrixResultDiffData diffData, @NonNull final String rowHeader,
			final ToLongFunction<SwapValueMatrixResult> baseValueFetcher, final ToLongFunction<SwapValueMatrixResult> swapValueFetcher, @NonNull final String units, final double scaleDownFactor,
			final Predicate<ValueMatrixResultDiffData> hasBaseDiffFetcher, final Predicate<ValueMatrixResultDiffData> hasSwapDiffFetcher) {
		final long baseVal = baseValueFetcher.applyAsLong(result);
		final long swapVal = swapValueFetcher.applyAsLong(result);
		final boolean hasBaseDiff = hasBaseDiffFetcher.test(diffData);
		final boolean hasSwapDiff = hasSwapDiffFetcher.test(diffData);
		return baseVal == swapVal ? new MergedSummaryRow(rowHeader, units, swapVal / scaleDownFactor, hasBaseDiff || hasSwapDiff)
				: new CombinedSummaryRow(rowHeader, units, baseVal / scaleDownFactor, swapVal / scaleDownFactor, hasBaseDiff, hasSwapDiff);
	}

	private List<ISummaryRow> buildSummaryRows(@NonNull final SwapValueMatrixResult result) {
		final SwapValueMatrixModel model = modelProvider.get();
		if (model == null) {
			return Collections.emptyList();
		}
		final List<ISummaryRow> rows = new ArrayList<>();
		final LoadSlot loadSlot = model.getBaseLoad().getSlot();
		final String loadName = loadSlot == null ? "<Unknown>" : ScenarioElementNameHelper.getName(loadSlot, "<Unknown>");
		final DischargeSlot dischargeSlot = model.getBaseDischarge().getSlot();
		final String dischargeName = dischargeSlot == null ? "<Unknown>" : ScenarioElementNameHelper.getName(dischargeSlot, "<Unknown>");

		final ValueMatrixResultDiffData diffData = getHighlightData(result);

		rows.add(buildSummaryRow(result, diffData, String.format("%s price", loadName), SwapValueMatrixResult::getBaseLoadPrice, SwapValueMatrixResult::getSwapFobLoadPrice, UNITS_DOLLAR_PER_MMBTU,
				ValueMatrixResultDiffData::hasBaseLoadPriceDiff, ValueMatrixResultDiffData::hasSwapLoadPriceDiff));
		rows.add(new MergedSummaryRow(String.format("%s price", dischargeName), UNITS_DOLLAR_PER_MMBTU, result.getBaseDischargePrice(), false));
		rows.add(new MergedSummaryRow("Market price", UNITS_DOLLAR_PER_MMBTU, result.getSwapMarketPrice(), false));
		rows.add(new MergedSummaryRow("Spread", UNITS_DOLLAR_PER_MMBTU, result.getSwapMarketPrice() - result.getBaseDischargePrice(), false));
		rows.add(EmptySummaryRow.getInstance());
		rows.add(buildSummaryRow(result, diffData, "LT load vol", SwapValueMatrixResult::getBaseFobLoadVolume, SwapValueMatrixResult::getSwapFobLoadVolume, UNITS_TBTU, VOL_SCALEDOWN_FACTOR,
				ValueMatrixResultDiffData::hasBaseLoadVolumeDiff, ValueMatrixResultDiffData::hasSwapLoadVolumeDiff));
		rows.add(buildSummaryRow(result, diffData, "LT sell vol", SwapValueMatrixResult::getBaseDesSellVolume, SwapValueMatrixResult::getMarketBuyVolume, UNITS_TBTU, VOL_SCALEDOWN_FACTOR,
				ValueMatrixResultDiffData::hasBaseDischargeVolumeDiff, ValueMatrixResultDiffData::hasSwapDischargeVolumeDiff));
		rows.add(new SwapSummaryRow("Market sell vol", UNITS_TBTU, result.getMarketSellVolume() / VOL_SCALEDOWN_FACTOR, diffData.hasDesSaleMarketVolumeDiff()));
		rows.add(new SwapSummaryRow("Optimised vol", UNITS_TBTU, (result.getMarketSellVolume() - result.getMarketBuyVolume()) / VOL_SCALEDOWN_FACTOR, false));
		rows.add(new SwapSummaryRow("Optimised val", UNITS_MILLION_DOLLARS,
				((result.getMarketSellVolume() - result.getMarketBuyVolume()) * (result.getSwapMarketPrice() - result.getBaseDischargePrice())) / PNL_SCALEDOWN_FACTOR, false));
		rows.add(EmptySummaryRow.getInstance());
		rows.add(new SwapSummaryRow("Swap fee", UNITS_DOLLAR_PER_MMBTU, -1 * ((SwapValueMatrixResultSet) result.eContainer()).getSwapFee(), false));
		rows.add(new SwapSummaryRow("Additional cost", UNITS_MILLION_DOLLARS, -1 * ((SwapValueMatrixResultSet) result.eContainer()).getSwapFee() * result.getMarketBuyVolume() / PNL_SCALEDOWN_FACTOR,
				false));
		return rows;
	}

	private List<DiffSummaryRow> buildDiffSummaryRows(@NonNull final SwapValueMatrixResult result) {
		final SwapValueMatrixModel model = modelProvider.get();
		if (model == null) {
			return Collections.emptyList();
		}
		final ValueMatrixResultDiffData diffData = getHighlightData(result);
		final List<DiffSummaryRow> rows = new ArrayList<>();
		{
			final String ltLoadName = ScenarioElementNameHelper.getName(model.getBaseLoad().getSlot(), "");
//			final String vesselName = ScenarioElementNameHelper.getName(model.getBaseVesselCharter().getVesselCharter().getVessel(), "");
			final long baseVal = result.getBaseCargoPnl();
			final long swapVal = result.getSwapShippedCargoPnl();
			final long swapMinusBaseVal = swapVal - baseVal;
			final DiffSummaryRow row = new DiffSummaryRow();
			if (!ltLoadName.isBlank()) {
				row.rowHeader = String.format("%s", ltLoadName);
			} else {
				row.rowHeader = "Cargo";
			}
			row.baseValue = baseVal / PNL_SCALEDOWN_FACTOR;
			row.swapValue = swapVal / PNL_SCALEDOWN_FACTOR;
			row.swapMinusBase = swapMinusBaseVal / PNL_SCALEDOWN_FACTOR;
			rows.add(row);
		}
		{
			final long baseVal = result.getBasePurchaseCost();
			final long swapVal = result.getSwapCargoPurchaseCost();
			final long swapMinusBaseVal = swapVal - baseVal;
			final DiffSummaryRow row = new DiffSummaryRow();
			row.rowHeader = TAB + "Purchase";
			row.grey = true;
			row.baseValue = baseVal / PNL_SCALEDOWN_FACTOR;
			row.swapValue = swapVal / PNL_SCALEDOWN_FACTOR;
			row.swapMinusBase = swapMinusBaseVal == 0L ? 0.0 : swapMinusBaseVal / PNL_SCALEDOWN_FACTOR;
			rows.add(row);
		}
		{
			final long baseVal = result.getBaseSaleRevenue();
			final long swapVal = result.getSwapCargoSaleRevenue();
			final long swapMinusBaseVal = swapVal - baseVal;
			final DiffSummaryRow row = new DiffSummaryRow();
			row.rowHeader = TAB + "Sale";
			row.grey = true;
			row.baseValue = baseVal / PNL_SCALEDOWN_FACTOR;
			row.swapValue = swapVal / PNL_SCALEDOWN_FACTOR;
			row.swapMinusBase = swapMinusBaseVal == 0L ? 0.0 : swapMinusBaseVal / PNL_SCALEDOWN_FACTOR;
			rows.add(row);
		}
		{
			final long baseVal = result.getBaseShippingCost();
			final long swapVal = result.getSwapCargoShippingCost();
			final long swapMinusBaseVal = swapVal - baseVal;
			final DiffSummaryRow row = new DiffSummaryRow();
			row.rowHeader = TAB + "Shipping";
			row.grey = true;
			row.baseValue = baseVal / PNL_SCALEDOWN_FACTOR;
			row.swapValue = swapVal / PNL_SCALEDOWN_FACTOR;
			row.swapMinusBase = swapMinusBaseVal == 0L ? 0.0 : swapMinusBaseVal / PNL_SCALEDOWN_FACTOR;
			rows.add(row);
		}
		{
			final long baseVal = result.getBaseAdditionalPnl();
			final long swapVal = result.getSwapCargoAdditionalPnl();
			final long swapMinusBaseVal = swapVal - baseVal;
			if (baseVal > 0L || swapVal > 0L) {
				final DiffSummaryRow row = new DiffSummaryRow();
				row.rowHeader = TAB + "Addn. P&L";
				row.grey = true;
				row.baseValue = baseVal / PNL_SCALEDOWN_FACTOR;
				row.swapValue = swapVal / PNL_SCALEDOWN_FACTOR;
				row.swapMinusBase = swapMinusBaseVal == 0L ? 0.0 : swapMinusBaseVal / PNL_SCALEDOWN_FACTOR;
				rows.add(row);
			}
		}
		{
			final long swapVal = result.getSwapBackfillCargoPnl();
			final DiffSummaryRow row = new DiffSummaryRow();
			row.rowHeader = "Backfill";
			row.swapValue = swapVal / PNL_SCALEDOWN_FACTOR;
			row.swapMinusBase = swapVal / PNL_SCALEDOWN_FACTOR;
			row.ignoreBase = true;
			rows.add(row);
		}
		{
			final long swapVal = result.getSwapBackfillPurchaseCost();
			final DiffSummaryRow row = new DiffSummaryRow();
			row.rowHeader = TAB + "Purchase";
			row.grey = true;
			row.swapValue = swapVal / PNL_SCALEDOWN_FACTOR;
			row.swapMinusBase = swapVal / PNL_SCALEDOWN_FACTOR;
			row.ignoreBase = true;
			rows.add(row);
		}
		{

			final long swapVal = result.getSwapBackfillSaleRevenue();
			final DiffSummaryRow row = new DiffSummaryRow();
			row.rowHeader = TAB + "Sale";
			row.grey = true;
			row.swapValue = swapVal / PNL_SCALEDOWN_FACTOR;
			row.swapMinusBase = swapVal / PNL_SCALEDOWN_FACTOR;
			row.ignoreBase = true;
			rows.add(row);
		}
		{

			final long swapVal = result.getSwapBackfillAdditionalPnl();
			if (swapVal != 0L) {
				final DiffSummaryRow row = new DiffSummaryRow();
				row.rowHeader = TAB + "Addn. P&L";
				row.grey = true;
				row.swapValue = swapVal / PNL_SCALEDOWN_FACTOR;
				row.swapMinusBase = swapVal / PNL_SCALEDOWN_FACTOR;
				row.ignoreBase = true;
				rows.add(row);
			}
		}
		{
			final DiffSummaryRow row = new DiffSummaryRow();
			row.rowHeader = "";
			row.isEmpty = true;
			rows.add(row);
		}
		{
			final long baseVal = result.getBaseCargoPnl();
			final long swapVal = result.getSwapShippedCargoPnl() + result.getSwapBackfillCargoPnl();
			final long swapMinusBaseVal = swapVal - baseVal;
			final DiffSummaryRow row = new DiffSummaryRow();
			row.rowHeader = "Target play";
			row.baseValue = baseVal / PNL_SCALEDOWN_FACTOR;
			row.swapValue = swapVal / PNL_SCALEDOWN_FACTOR;
			row.swapMinusBase = swapMinusBaseVal / PNL_SCALEDOWN_FACTOR;
			rows.add(row);
		}
		{
			final DiffSummaryRow row = new DiffSummaryRow();
			row.rowHeader = "Preceding extra";
			row.baseValue = result.getBasePrecedingPnl() / PNL_SCALEDOWN_FACTOR;
			row.swapValue = result.getSwapPrecedingPnl() / PNL_SCALEDOWN_FACTOR;
			row.swapMinusBase = (result.getSwapPrecedingPnl() - result.getBasePrecedingPnl()) / PNL_SCALEDOWN_FACTOR;
			row.baseGrey = true;
			row.swapGrey = true;
			row.highlightDiff = diffData.hasPrecedingKnockOnDiff();
			rows.add(row);
		}
		{
			final DiffSummaryRow row = new DiffSummaryRow();
			row.rowHeader = "Succeeding extra";
			row.baseValue = result.getBaseSucceedingPnl() / PNL_SCALEDOWN_FACTOR;
			row.swapValue = result.getSwapSucceedingPnl() / PNL_SCALEDOWN_FACTOR;
			row.swapMinusBase = (result.getSwapSucceedingPnl() - result.getBaseSucceedingPnl()) / PNL_SCALEDOWN_FACTOR;
			row.baseGrey = true;
			row.swapGrey = true;
			row.highlightDiff = diffData.hasSucceedingKnockOnDiff();
			rows.add(row);
		}
		{
			final DiffSummaryRow row = new DiffSummaryRow();
			row.rowHeader = "";
			row.isEmpty = true;
			rows.add(row);
		}
		{
			final DiffSummaryRow row = new DiffSummaryRow();
			row.rowHeader = "Portfolio P&L";
			row.baseValue = (result.getBaseCargoPnl() + result.getBasePrecedingPnl() + result.getBaseSucceedingPnl()) / PNL_SCALEDOWN_FACTOR;
			row.swapValue = (result.getSwapShippedCargoPnl() + result.getSwapBackfillCargoPnl() + result.getSwapPrecedingPnl() + result.getSwapSucceedingPnl()) / PNL_SCALEDOWN_FACTOR;
			row.swapMinusBase = result.getSwapPnlMinusBasePnl() / PNL_SCALEDOWN_FACTOR;
			row.baseGrey = true;
			row.swapGrey = true;
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
		if (this.localResourceManager == null) {
			this.localResourceManager = new LocalResourceManager(JFaceResources.getResources(), parent);
			this.colourSequence = LightPastelColourSequence.createColourSequence(localResourceManager);
		}

	}

	@Override
	public void dispose() {
		clearValueMatrix();
		recalculateScrollbar = null;
		localResourceManager.dispose();
	}

	public void refresh() {
		refresh(modelProvider.get());
	}

	public void softRefresh() {
		if (modelProvider.get().getSwapValueMatrixResult() != null) {
			valueMatrixViewer.refresh();
			if (summaryComposite.isVisible()) {
				summaryTableViewer.refresh();
			}
			if (diffSummaryComposite.isVisible()) {
				diffSummaryTableViewer.refresh();
			}
			matrixComposite.layout();
			matrixComposite.redraw();
			resultsComposite.layout();
			resultsComposite.redraw();
			resultsComposite.requestLayout();
		}
	}

	public void hideResultsComponents() {
		matrixComposite.setVisible(false);
		summaryComposite.setVisible(false);
		diffSummaryComposite.setVisible(false);
	}

	public void refresh(final SwapValueMatrixModel model) {
		clearValueMatrix();
		hideResultsComponents();
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
				valueMatrixViewer.setData(KEY_MATRIX_ROW_OFFSET, sortedRowNumbers.get(0));
				valueMatrixViewer.setData(KEY_MATRIX_COLUMN_OFFSET, sortedColumnNumbers.get(0));
				valueMatrixViewer.setInput(rows);
				valueMatrixViewer.refresh();
				for (final GridColumn col : valueMatrixViewer.getGrid().getColumns()) {
					col.pack();
				}
				matrixComposite.setVisible(true);
				matrixComposite.layout();
				matrixComposite.redraw();
				resultsComposite.layout();
				resultsComposite.redraw();
				resultsComposite.requestLayout();
			}
		}
	}

	private ValueMatrixResultDiffData getHighlightData(@NonNull final SwapValueMatrixResult result) {
		boolean hasBaseLoadPriceDiff = false;
		boolean hasSwapLoadPriceDiff = false;
		boolean hasBaseLoadVolumeDiff = false;
		boolean hasSwapLoadVolumeDiff = false;
		boolean hasBaseDischargeVolumeDiff = false;
		boolean hasSwapDischargeVolumeDiff = false;
		boolean hasDesSaleMarketVolumeDiff = false;
		boolean hasPrecedingKnockOnDiff = false;
		boolean hasSucceedingKnockOnDiff = false;

		final int rowOffset = (int) valueMatrixViewer.getData(KEY_MATRIX_ROW_OFFSET);
		final int colOffset = (int) valueMatrixViewer.getData(KEY_MATRIX_COLUMN_OFFSET);
		final int resultRowIndex = result.getSwapMarketPrice() - rowOffset;
		final int resultColIndex = result.getBaseDischargePrice() - colOffset;
		final SwapValueMatrixResult[][] rows = (SwapValueMatrixResult[][]) valueMatrixViewer.getInput();
		for (int i = -1; i <= 1; ++i) {
			final int neighbourRowIndex = resultRowIndex + i;
			if (neighbourRowIndex >= 0 && neighbourRowIndex < rows.length) {
				for (int j = -1; j <= 1; ++j) {
					if (i != 0 || j != 0) {
						final SwapValueMatrixResult[] row = rows[neighbourRowIndex];
						final int neighbourColIndex = resultColIndex + j;
						if (neighbourColIndex >= 0 && neighbourColIndex < row.length) {
							final SwapValueMatrixResult neighbour = row[neighbourColIndex];
							if (result.getBaseLoadPrice() != neighbour.getBaseLoadPrice()) {
								hasBaseLoadPriceDiff = true;
							}
							if (result.getSwapFobLoadPrice() != neighbour.getSwapFobLoadPrice()) {
								hasSwapLoadPriceDiff = true;
							}
							if (result.getBaseFobLoadVolume() != neighbour.getBaseFobLoadVolume()) {
								hasBaseLoadVolumeDiff = true;
							}
							if (result.getSwapFobLoadVolume() != neighbour.getSwapFobLoadVolume()) {
								hasSwapLoadVolumeDiff = true;
							}
							if (result.getBaseDesSellVolume() != neighbour.getBaseDesSellVolume()) {
								hasBaseDischargeVolumeDiff = true;
							}
							if (result.getMarketBuyVolume() != neighbour.getMarketBuyVolume()) {
								hasSwapDischargeVolumeDiff = true;
							}
							if (result.getMarketSellVolume() != neighbour.getMarketSellVolume()) {
								hasDesSaleMarketVolumeDiff = true;
							}
							if (result.getSwapPrecedingPnl() - result.getBasePrecedingPnl() != neighbour.getSwapPrecedingPnl() - neighbour.getBasePrecedingPnl()) {
								hasPrecedingKnockOnDiff = true;
							}
							if (result.getSwapSucceedingPnl() - result.getBaseSucceedingPnl() != neighbour.getSwapSucceedingPnl() - neighbour.getBaseSucceedingPnl()) {
								hasSucceedingKnockOnDiff = true;
							}
						}
					}
				}
			}
		}
		return new ValueMatrixResultDiffData(hasBaseLoadPriceDiff, hasSwapLoadPriceDiff, hasBaseLoadVolumeDiff, hasSwapLoadVolumeDiff, hasBaseDischargeVolumeDiff, hasSwapDischargeVolumeDiff,
				hasDesSaleMarketVolumeDiff, hasPrecedingKnockOnDiff, hasSucceedingKnockOnDiff);
	}

	public boolean isShowingGroups() {
		return showingGroups;
	}

	public void toggleShowGroups() {
		showingGroups = !showingGroups;
	}

	public boolean isHighlightingLosses() {
		return highlightLosses;
	}

	public void toggleHighlightLosses() {
		highlightLosses = !highlightLosses;
	}
}
