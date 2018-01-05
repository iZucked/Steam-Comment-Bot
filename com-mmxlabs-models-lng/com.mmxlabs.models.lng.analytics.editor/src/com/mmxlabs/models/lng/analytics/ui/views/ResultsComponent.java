/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.ui.views;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.ColumnViewerToolTipSupport;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.nebula.jface.gridviewer.GridTreeViewer;
import org.eclipse.nebula.jface.gridviewer.GridViewerColumn;
import org.eclipse.nebula.widgets.grid.GridColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.forms.events.IExpansionListener;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import com.mmxlabs.models.lng.analytics.AnalysisResultRow;
import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.BreakEvenResult;
import com.mmxlabs.models.lng.analytics.OptionAnalysisModel;
import com.mmxlabs.models.lng.analytics.ProfitAndLossResult;
import com.mmxlabs.models.lng.analytics.Result;
import com.mmxlabs.models.lng.analytics.ResultContainer;
import com.mmxlabs.models.lng.analytics.ResultSet;
import com.mmxlabs.models.lng.analytics.ui.views.formatters.BEPriceResultDetailsDescriptionFormatter;
import com.mmxlabs.models.lng.analytics.ui.views.formatters.BuyOptionDescriptionFormatter;
import com.mmxlabs.models.lng.analytics.ui.views.formatters.CargoResultDetailsDescriptionFormatter;
import com.mmxlabs.models.lng.analytics.ui.views.formatters.SellOptionDescriptionFormatter;
import com.mmxlabs.models.lng.analytics.ui.views.formatters.ShippingOptionDescriptionFormatter;
import com.mmxlabs.models.lng.analytics.ui.views.providers.ResultsFormatterLabelProvider;
import com.mmxlabs.models.lng.analytics.ui.views.providers.ResultsViewerContentProvider;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.tabular.GridViewerHelper;
import com.mmxlabs.models.ui.tabular.renderers.ColumnHeaderRenderer;
import com.mmxlabs.rcp.common.RunnerHelper;
import com.mmxlabs.rcp.common.ServiceHelper;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.ui.IScenarioServiceSelectionProvider;
import com.mmxlabs.scenario.service.ui.ScenarioResult;

public class ResultsComponent extends AbstractSandboxComponent {

	private GridTreeViewer resultsViewer;
	private ResultsSetWiringDiagram resultsDiagram;

	private final boolean filterConstantRows = false;

	private final Image image_filter;
	private final Image image_grey_filter;
	private Composite clientArea;
	private ExpandableComposite expandable;
	private OptionModellerView optionModellerView;

	// column sorting
	final ArrayList<GridColumn> columnSortOrder = new ArrayList<GridColumn>();
	boolean sortDescending = false;
	private MenuManager mgr;

	protected ResultsComponent(@NonNull final IScenarioEditingLocation scenarioEditingLocation, final Map<Object, IStatus> validationErrors,
			@NonNull final Supplier<OptionAnalysisModel> modelProvider) {
		super(scenarioEditingLocation, validationErrors, modelProvider);
		final ImageDescriptor generate_desc = AbstractUIPlugin.imageDescriptorFromPlugin("com.mmxlabs.models.lng.analytics.editor", "icons/filter.gif");
		image_filter = generate_desc.createImage();
		image_grey_filter = ImageDescriptor.createWithFlags(generate_desc, SWT.IMAGE_GRAY).createImage();
	}

	@Override
	public void createControls(final Composite parent, final boolean expanded, final IExpansionListener expansionListener, final OptionModellerView optionModellerView) {

		// GridViewerColumn gvc = new GridViewerColumn(sorter, SWT.NONE);
		// gvc.getColumn().setText("Group by:");

		this.optionModellerView = optionModellerView;
		expandable = wrapInExpandable(parent, "Results", p -> createResultsViewer(p, optionModellerView), expandableCompo -> {

			clientArea = new Composite(expandableCompo, SWT.NONE) {
				// public Point computeSize(final int wHint, final int hHint) {
				// final Point p = super.computeSize(wHint, hHint);
				// // p.x = Math.max(200, p.x);
				// p.y = 30;// (sorter == null || sorter.getGrid().isDisposed()) ? 16 : sorter.getGrid().getHeaderHeight();
				// return p;
				// }
				//
				// public Point computeSize(final int wHint, final int hHint, final boolean changed) {
				// final Point p = super.computeSize(wHint, hHint, changed);
				// // p.x = Math.max(200, p.x);
				// p.y = 30;// (sorter == null || sorter.getGrid().isDisposed()) ? 16 : sorter.getGrid().getHeaderHeight();
				// return p;
				// }
			};
			// clientArea.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_BLUE));
			final GridLayout layout = new GridLayout(2, false);
			layout.marginBottom = 0;
			layout.marginHeight = 0;
			layout.marginLeft = 0;
			layout.marginRight = 0;
			layout.marginTop = 0;
			clientArea.setLayout(layout);

			clientArea.setLayoutData(GridDataFactory.fillDefaults().hint(200, 0).grab(true, true).create());
			// c.setLayoutData(GridDataFactory.swtDefaults().align(SWT.LEFT, SWT.TOP).hint(16, 16).grab(false, false).create());
			//
			// final Label c = new Label(clientArea, SWT.NONE);
			// c.setToolTipText("Toggle show only B/E cargoes");
			// if (filterConstantRows) {
			// c.setImage(image_filter);
			// } else {
			// c.setImage(image_grey_filter);
			// }
			// c.addMouseListener(new MouseAdapter() {
			// public void mouseDown(final MouseEvent e) {
			// filterConstantRows = !filterConstantRows;
			// if (filterConstantRows) {
			// c.setImage(image_filter);
			// } else {
			// c.setImage(image_grey_filter);
			// }
			// refresh();
			// }
			//
			// });
			expandableCompo.setTextClient(clientArea);
		}, false);
		expandable.setLayoutData(GridDataFactory.fillDefaults().minSize(SWT.DEFAULT, 300).grab(false, true).create());
		expandable.setExpanded(expanded);
		expandable.addExpansionListener(expansionListener);
	}

	@Override
	public void refresh() {
		resultsViewer.refresh();
		resultsViewer.expandAll();
	}

	private Control createResultsViewer(final Composite parent, final OptionModellerView optionModellerView) {

		resultsViewer = new GridTreeViewer(parent, SWT.NONE);
		ColumnViewerToolTipSupport.enableFor(resultsViewer);
		GridViewerHelper.configureLookAndFeel(resultsViewer);
		resultsViewer.getGrid().setHeaderVisible(true);
		resultsViewer.setAutoExpandLevel(TreeViewer.ALL_LEVELS);

		final ViewerFilter constantFilter = new ViewerFilter() {

			@Override
			public boolean select(final Viewer viewer, final Object parentElement, final Object element) {

				if (filterConstantRows) {
					if (element instanceof AnalysisResultRow) {
						final AnalysisResultRow analysisResultRow = (AnalysisResultRow) element;
						if (!(analysisResultRow.getResultDetail() instanceof BreakEvenResult)) {
							final ResultSet resultSet = (ResultSet) analysisResultRow.eContainer();
							if (resultSet.getRows().size() > 1) {
								for (final AnalysisResultRow otherRow : resultSet.getRows()) {
									if (otherRow.getResultDetail() instanceof BreakEvenResult) {
										return false;
									}
								}
							}
						}
					}
				}

				return true;
			}
		};

		resultsViewer.setFilters(new ViewerFilter[] { constantFilter });

		createColumn(resultsViewer, "Buy", new ResultsFormatterLabelProvider(new BuyOptionDescriptionFormatter(), AnalyticsPackage.Literals.ANALYSIS_RESULT_ROW__BUY_OPTION), false,
				AnalyticsPackage.Literals.ANALYSIS_RESULT_ROW__BUY_OPTION);

		{
			final GridViewerColumn gvc = new GridViewerColumn(resultsViewer, SWT.CENTER);
			gvc.getColumn().setHeaderRenderer(new ColumnHeaderRenderer());
			gvc.getColumn().setText("");
			gvc.getColumn().setResizeable(false);
			gvc.getColumn().setWidth(100);
			gvc.setLabelProvider(createWiringColumnLabelProvider());
			this.resultsDiagram = new ResultsSetWiringDiagram(resultsViewer.getGrid(), gvc);
			// gvc.getColumn().setCellRenderer(createCellRenderer());
		}

		createColumn(resultsViewer, "Sell", new ResultsFormatterLabelProvider(new SellOptionDescriptionFormatter(), AnalyticsPackage.Literals.ANALYSIS_RESULT_ROW__SELL_OPTION), false,
				AnalyticsPackage.Literals.ANALYSIS_RESULT_ROW__SELL_OPTION);
		createColumn(resultsViewer, "Shipping", new ResultsFormatterLabelProvider(new ShippingOptionDescriptionFormatter(), AnalyticsPackage.Literals.ANALYSIS_RESULT_ROW__SHIPPING), false,
				AnalyticsPackage.Literals.ANALYSIS_RESULT_ROW__SHIPPING);
		final GridViewerColumn pAndL = createColumn(resultsViewer, "Cargo P&L",
				new ResultsFormatterLabelProvider(new CargoResultDetailsDescriptionFormatter(), AnalyticsPackage.Literals.ANALYSIS_RESULT_ROW__RESULT_DETAIL), false,
				AnalyticsPackage.Literals.ANALYSIS_RESULT_ROW__RESULT_DETAIL);
		final GridViewerColumn bePrice = createColumn(resultsViewer, "B/E Price",
				new ResultsFormatterLabelProvider(new BEPriceResultDetailsDescriptionFormatter(), AnalyticsPackage.Literals.ANALYSIS_RESULT_ROW__RESULT_DETAIL), false,
				AnalyticsPackage.Literals.ANALYSIS_RESULT_ROW__RESULT_DETAIL);

		// set width for pricing columns
		{
			pAndL.getColumn().setWidth(100);
			bePrice.getColumn().setWidth(100);
		}

		mgr = new MenuManager();
		inputWants.add(model -> {
			final IContributionItem[] items = mgr.getItems();
			mgr.removeAll();
			for (final IContributionItem item : items) {
				item.dispose();
			}
		});

		final ResultsContextMenuManager listener = new ResultsContextMenuManager(resultsViewer, scenarioEditingLocation, optionModellerView, mgr);
		inputWants.add(model -> listener.setOptionAnalysisModel(model));
		resultsViewer.getGrid().addMenuDetectListener(listener);

		resultsViewer.setContentProvider(new ResultsViewerContentProvider());
		final ESelectionService selectionService = optionModellerView.getSite().getService(ESelectionService.class);

		resultsViewer.addSelectionChangedListener(new ISelectionChangedListener() {

			@Override
			public void selectionChanged(final SelectionChangedEvent event) {
				final List<Object> selection = new LinkedList<>();
				final ISelection s = resultsViewer.getSelection();
				ScenarioResult baseScenarioResult = null;
				ScenarioResult scenarioResult = null;
				{
					final OptionAnalysisModel model = optionModellerView.getCurrentRoot().get();
					if (model != null) {
						final Result r = model.getBaseCaseResult();
						if (r != null && !r.getResultSets().isEmpty()) {
							final ResultSet baseResult = r.getResultSets().get(0);
							final ScenarioInstance scenarioInstance = scenarioEditingLocation.getScenarioInstance();
							if (scenarioInstance != null) {
								baseScenarioResult = new ScenarioResult(scenarioInstance, baseResult.getScheduleModel());
							}

						}
					}
				}
				if (s instanceof IStructuredSelection) {
					final IStructuredSelection structuredSelection = (IStructuredSelection) s;
					final Iterator<?> itr = structuredSelection.iterator();
					while (itr.hasNext()) {
						final Object o = itr.next();
						if (o instanceof AnalysisResultRow) {
							final AnalysisResultRow analysisResultRow = (AnalysisResultRow) o;
							final ResultSet resultSet = (ResultSet) analysisResultRow.eContainer();
							final ResultContainer t = analysisResultRow.getResultDetails();
							if (t != null) {
								if (t.getCargoAllocation() != null) {
									selection.add(t.getCargoAllocation());
								}
								selection.addAll(t.getSlotAllocations());
								selection.addAll(t.getOpenSlotAllocations());
								if (resultSet != null) {
									final ScenarioInstance scenarioInstance = scenarioEditingLocation.getScenarioInstance();
									if (scenarioInstance != null) {
										scenarioResult = new ScenarioResult(scenarioInstance, resultSet.getScheduleModel());
									}
								}
							}
						}
					}
				}
				// TODO: Check to see if we really need to re-select
				if (scenarioResult != null) {
					final ScenarioResult pScenarioResult = scenarioResult;
					final ScenarioResult pBaseScenarioResult = baseScenarioResult;
					ServiceHelper.withServiceConsumer(IScenarioServiceSelectionProvider.class, service -> {
						if (pBaseScenarioResult != null && pScenarioResult != null) {
							service.setPinnedPair(pBaseScenarioResult, pScenarioResult, true);
						} else if (pScenarioResult != null) {
							// TODO: Need a select one API
							service.deselectAll(true);
							service.select(pScenarioResult, true);
						}
					});
				}

				if (selection != null) {
					selectionService.setPostSelection(new StructuredSelection(selection));
				}
				// Run as async as post selection may well trigger async refreshes
				RunnerHelper.asyncExec(() -> optionModellerView.repackResults());
			}

		});
		inputWants.add(model -> resultsViewer.setInput(model));
		inputWants.add(model -> resultsDiagram.setRoot(model));

		// sort order
		addSortableColumn(resultsViewer, bePrice, bePrice.getColumn());
		addSortableColumn(resultsViewer, pAndL, pAndL.getColumn());

		resultsViewer.setComparator(new ViewerComparator() {
			@Override
			public int compare(final Viewer viewer, final Object e1, final Object e2) {
				int comparable = 0;
				if (e1 instanceof ResultSet && e2 instanceof ResultSet) {
					final ResultSet r1 = (ResultSet) e1;
					final ResultSet r2 = (ResultSet) e2;
					for (final GridColumn gridColumn : getColumnSortOrder()) {
						if (gridColumn == bePrice.getColumn()) {
							final Double breakeven1 = findBreakEvenResult(r1);
							final Double breakeven2 = findBreakEvenResult(r2);
							if (breakeven1 != null && breakeven2 == null) {
								comparable = -1;
							} else if (breakeven1 == null && breakeven2 != null) {
								comparable = 1;
							} else if (breakeven1 == null && breakeven2 == null) {
								comparable = 0;
							} else {
								comparable = Double.compare(breakeven1, breakeven2);
							}
						} else if (gridColumn == pAndL.getColumn()) {
							final Double profit1 = findLargestProfit(r1);
							final Double profit2 = findLargestProfit(r2);
							if (profit1 != null && profit2 == null) {
								return -1;
							} else if (profit1 == null && profit2 != null) {
								return 1;
							} else if (profit1 == null && profit1 == null) {
								comparable = 0;
							} else {
								comparable = Double.compare(profit1, profit2);
							}
						}
						if (comparable != 0) {
							break;
						}
					}
				} else if (e1 instanceof ResultSet) {
					return -1;
				} else if (e2 instanceof ResultSet) {
					return 1;
				}
				return comparable * (isSortDescending() ? -1 : 1);
			}
		});
		return resultsViewer.getControl();
	}

	private Double findBreakEvenResult(final ResultSet rs) {
		for (final AnalysisResultRow analysisResultRow : rs.getRows()) {
			if (analysisResultRow.getResultDetail() instanceof BreakEvenResult) {
				return ((BreakEvenResult) analysisResultRow.getResultDetail()).getPrice();
			}
		}
		return null;
	}

	private Double findLargestProfit(final ResultSet rs) {
		@NonNull
		final List<ProfitAndLossResult> results = rs.getRows().stream().filter(r -> r.getResultDetail() instanceof ProfitAndLossResult) //
				.map(r -> (ProfitAndLossResult) r.getResultDetail()) //
				.sorted((a, b) -> Double.compare(a.getValue(), b.getValue())) //
				.collect(Collectors.toList());

		if (results.size() == 0) {
			return null;
		} else {
			return results.get(results.size() - 1).getValue();
		}
	}

	public void addSortableColumn(final ColumnViewer viewer, final GridViewerColumn column, final GridColumn tColumn) {
		if (getColumnSortOrder().contains(tColumn)) {
			return;
		}

		getColumnSortOrder().add(tColumn);

		column.getColumn().addSelectionListener(new SelectionListener() {
			@Override
			public void widgetDefaultSelected(final SelectionEvent e) {
			}

			@Override
			public void widgetSelected(final SelectionEvent e) {

				sortColumnsBy(tColumn);
				viewer.refresh(false);
			}

		});
	}

	public void sortColumnsBy(final GridColumn tColumn) {
		if (getColumnSortOrder().get(0) == tColumn) {
			setSortDescending(!isSortDescending());
		} else {
			setSortDescending(false);
			getColumnSortOrder().get(0).setSort(SWT.NONE);
			getColumnSortOrder().remove(tColumn);
			getColumnSortOrder().add(0, tColumn);
		}
		tColumn.setSort(isSortDescending() ? SWT.UP : SWT.DOWN);
	}

	public ArrayList<GridColumn> getColumnSortOrder() {
		return columnSortOrder;
	}

	public void setSortDescending(final boolean sortDescending) {
		this.sortDescending = sortDescending;
	}

	public boolean isSortDescending() {
		return sortDescending;
	}

	@Override
	public void dispose() {
		if (image_filter != null) {
			image_filter.dispose();
		}

		if (image_grey_filter != null) {
			image_grey_filter.dispose();
		}

		if (mgr != null) {
			mgr.dispose();
		}
		super.dispose();
	}

}
