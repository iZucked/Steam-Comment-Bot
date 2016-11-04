package com.mmxlabs.models.lng.analytics.ui.views;

import java.util.EnumSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ColumnViewerToolTipSupport;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.nebula.jface.gridviewer.GridTableViewer;
import org.eclipse.nebula.jface.gridviewer.GridTreeViewer;
import org.eclipse.nebula.jface.gridviewer.GridViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.ui.forms.events.IExpansionListener;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.analytics.AnalysisResultRow;
import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.BreakEvenResult;
import com.mmxlabs.models.lng.analytics.MultipleResultGrouper;
import com.mmxlabs.models.lng.analytics.OptionAnalysisModel;
import com.mmxlabs.models.lng.analytics.ResultContainer;
import com.mmxlabs.models.lng.analytics.ResultSet;
import com.mmxlabs.models.lng.analytics.ui.views.OptionModellerView.SectionType;
import com.mmxlabs.models.lng.analytics.ui.views.formatters.BuyOptionDescriptionFormatter;
import com.mmxlabs.models.lng.analytics.ui.views.formatters.ResultDetailsDescriptionFormatter;
import com.mmxlabs.models.lng.analytics.ui.views.formatters.SellOptionDescriptionFormatter;
import com.mmxlabs.models.lng.analytics.ui.views.formatters.ShippingOptionDescriptionFormatter;
import com.mmxlabs.models.lng.analytics.ui.views.providers.ResultsFormatterLabelProvider;
import com.mmxlabs.models.lng.analytics.ui.views.providers.ResultsViewerContentProvider;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.tabular.GridViewerHelper;
import com.mmxlabs.models.ui.tabular.renderers.ColumnHeaderRenderer;
import com.mmxlabs.rcp.common.RunnerHelper;

public class ResultsComponent extends AbstractSandboxComponent {

	private GridTreeViewer resultsViewer;
	private ResultsSetWiringDiagram resultsDiagram;

	private boolean filterConstantRows = false;

	private final Image image_filter;
	private final Image image_grey_filter;
	private final List<GridViewerColumn> sorterColumns = new LinkedList<>();
	private GridTableViewer sorter;
	private Composite clientArea;
	private ExpandableComposite expandable;
	private OptionModellerView optionModellerView;

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
				public Point computeSize(int wHint, int hHint) {
					Point p = super.computeSize(wHint, hHint);
					// p.x = Math.max(200, p.x);
					p.y = 30;// (sorter == null || sorter.getGrid().isDisposed()) ? 16 : sorter.getGrid().getHeaderHeight();
					return p;
				}

				public Point computeSize(int wHint, int hHint, boolean changed) {
					Point p = super.computeSize(wHint, hHint, changed);
					// p.x = Math.max(200, p.x);
					p.y = 30;// (sorter == null || sorter.getGrid().isDisposed()) ? 16 : sorter.getGrid().getHeaderHeight();
					return p;
				}
			};
			// clientArea.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_BLUE));
			GridLayout layout = new GridLayout(2, false);
			layout.marginBottom = 0;
			layout.marginHeight = 0;
			layout.marginLeft = 0;
			layout.marginRight = 0;
			layout.marginTop = 0;
			clientArea.setLayout(layout);

			sorter = new GridTableViewer(clientArea, SWT.NONE);
			sorter.getGrid().setHeaderVisible(true);
			sorter.getGrid().setLayoutData(GridDataFactory.fillDefaults().grab(true, false).create());

			final Label c = new Label(clientArea, SWT.NONE);
			c.setToolTipText("Toggle show only B/E cargoes");
			if (filterConstantRows) {
				c.setImage(image_filter);
			} else {
				c.setImage(image_grey_filter);
			}

			clientArea.setLayoutData(GridDataFactory.fillDefaults().hint(200, 30).grab(true, true).create());
			c.setLayoutData(GridDataFactory.swtDefaults().align(SWT.LEFT, SWT.TOP).hint(16, 16).grab(false, false).create());

			c.addMouseListener(new MouseAdapter() {
				public void mouseDown(final MouseEvent e) {
					filterConstantRows = !filterConstantRows;
					if (filterConstantRows) {
						c.setImage(image_filter);
					} else {
						c.setImage(image_grey_filter);
					}
					refresh();
				}

			});
			expandableCompo.setTextClient(clientArea);
		});
		expandable.setExpanded(expanded);
		expandable.addExpansionListener(expansionListener);
	}

	@Override
	public void refresh() {
		resultsViewer.refresh();
		resultsViewer.expandAll();

		sorterColumns.forEach(c -> c.getColumn().dispose());
		sorterColumns.clear();

		final OptionAnalysisModel model = modelProvider.get();
		// Only show the sorter if there are multiple items to sort by
		if (model != null && model.getResultGroups().size() > 1) {
			for (final MultipleResultGrouper g : model.getResultGroups()) {
				final GridViewerColumn gvc = new GridViewerColumn(sorter, SWT.NONE);
				sorterColumns.add(gvc);
				gvc.getColumn().setText(g.getName());
				gvc.getColumn().setMoveable(true);
				gvc.getColumn().pack();
				gvc.getColumn().setData(g);
				gvc.getColumn().addListener(SWT.Move, new Listener() {

					@Override
					public void handleEvent(final Event event) {
						final int[] order = sorter.getGrid().getColumnOrder();
						final List<MultipleResultGrouper> newOrder = new LinkedList<>();
						for (int i = 0; i < sorterColumns.size(); ++i) {
							final GridViewerColumn gvc2 = sorterColumns.get(order[i]);
							final MultipleResultGrouper g2 = (MultipleResultGrouper) gvc2.getColumn().getData();
							newOrder.add(g2);
						}
						final Command command = SetCommand.create(scenarioEditingLocation.getEditingDomain(), model, AnalyticsPackage.Literals.OPTION_ANALYSIS_MODEL__RESULT_GROUPS, newOrder);
						scenarioEditingLocation.getDefaultCommandHandler().handleCommand(command, model,  AnalyticsPackage.Literals.OPTION_ANALYSIS_MODEL__RESULT_GROUPS);
						
						optionModellerView.refreshSections(false, EnumSet.of(SectionType.MIDDLE));
//						resultsViewer.refresh();
//						resultsViewer.expandAll();
					}
				});
			}

		}

//		sorter.getGrid().layout(true);
//		clientArea.layout(true);
//		expandable.pack();
//		expandable.layout(true);
//		expandable.setSize(expandable.computeSize(SWT.DEFAULT, SWT.DEFAULT));
//		expandable.layout();
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
		createColumn(resultsViewer, "Details", new ResultsFormatterLabelProvider(new ResultDetailsDescriptionFormatter(), AnalyticsPackage.Literals.ANALYSIS_RESULT_ROW__RESULT_DETAIL), false,
				AnalyticsPackage.Literals.ANALYSIS_RESULT_ROW__RESULT_DETAIL);

		final MenuManager mgr = new MenuManager();

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
				if (s instanceof IStructuredSelection) {
					final IStructuredSelection structuredSelection = (IStructuredSelection) s;
					final Iterator<?> itr = structuredSelection.iterator();
					while (itr.hasNext()) {
						final Object o = itr.next();
						if (o instanceof AnalysisResultRow) {
							final AnalysisResultRow analysisResultRow = (AnalysisResultRow) o;
							final ResultContainer t = analysisResultRow.getResultDetails();
							if (t != null) {
								if (t.getCargoAllocation() != null) {
									selection.add(t.getCargoAllocation());
								}
								selection.addAll(t.getSlotAllocations());
								selection.addAll(t.getOpenSlotAllocations());
							}
						}
					}
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

		return resultsViewer.getControl();
	}

	@Override
	public void dispose() {
		if (image_filter != null) {
			image_filter.dispose();
		}

		if (image_grey_filter != null) {
			image_grey_filter.dispose();
		}
		super.dispose();
	}

}
