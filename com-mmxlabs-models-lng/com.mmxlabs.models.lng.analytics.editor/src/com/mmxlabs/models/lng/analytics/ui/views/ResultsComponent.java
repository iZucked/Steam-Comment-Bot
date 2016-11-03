package com.mmxlabs.models.lng.analytics.ui.views;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ColumnViewerToolTipSupport;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.nebula.jface.gridviewer.GridTreeViewer;
import org.eclipse.nebula.jface.gridviewer.GridViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.events.IExpansionListener;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import com.mmxlabs.models.lng.analytics.AnalysisResultRow;
import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.BreakEvenResult;
import com.mmxlabs.models.lng.analytics.OptionAnalysisModel;
import com.mmxlabs.models.lng.analytics.ResultContainer;
import com.mmxlabs.models.lng.analytics.ResultSet;
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

	private Image image_filter;
	private Image image_grey_filter;

	protected ResultsComponent(@NonNull final IScenarioEditingLocation scenarioEditingLocation, final Map<Object, IStatus> validationErrors,
			@NonNull final Supplier<OptionAnalysisModel> modelProvider) {
		super(scenarioEditingLocation, validationErrors, modelProvider);
		final ImageDescriptor generate_desc = AbstractUIPlugin.imageDescriptorFromPlugin("com.mmxlabs.models.lng.analytics.editor", "icons/filter.gif");
		image_filter = generate_desc.createImage();
		image_grey_filter = ImageDescriptor.createWithFlags(generate_desc, SWT.IMAGE_GRAY).createImage();
	}

	@Override
	public void createControls(final Composite parent, final boolean expanded, final IExpansionListener expansionListener, final OptionModellerView optionModellerView) {
		final ExpandableComposite expandable = wrapInExpandable(parent, "Results", p -> createResultsViewer(p, optionModellerView), expandableCompo -> {
			final Label c = new Label(expandableCompo, SWT.NONE);
			c.setToolTipText("Toggle show only B/E cargoes");
			expandableCompo.setTextClient(c);
			if (filterConstantRows) {
				c.setImage(image_filter);
			} else {
				c.setImage(image_grey_filter);
			}
			c.setLayoutData(GridDataFactory.swtDefaults().align(SWT.LEFT, SWT.TOP).hint(16, 16).grab(true, false).create());

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
		});
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
