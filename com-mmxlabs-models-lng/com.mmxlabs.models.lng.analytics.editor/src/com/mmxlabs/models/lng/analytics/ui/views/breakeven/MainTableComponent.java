/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
/**
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.ui.views.breakeven;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ColumnViewerToolTipSupport;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.nebula.jface.gridviewer.GridTreeViewer;
import org.eclipse.nebula.jface.gridviewer.GridViewerColumn;
import org.eclipse.nebula.widgets.grid.GridColumn;
import org.eclipse.nebula.widgets.grid.GridColumnGroup;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.MouseTrackListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.events.IExpansionListener;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import com.mmxlabs.models.lng.analytics.BreakEvenAnalysisModel;
import com.mmxlabs.models.lng.analytics.BreakEvenAnalysisResult;
import com.mmxlabs.models.lng.analytics.BreakEvenAnalysisResultSet;
import com.mmxlabs.models.lng.analytics.BreakEvenAnalysisRow;
import com.mmxlabs.models.lng.analytics.ui.views.evaluators.BreakEvenSandboxEvaluator;
import com.mmxlabs.models.lng.analytics.ui.views.formatters.BuyOptionDescriptionFormatter;
import com.mmxlabs.models.lng.analytics.ui.views.formatters.SellOptionDescriptionFormatter;
import com.mmxlabs.models.lng.analytics.ui.views.formatters.ShippingOptionDescriptionFormatter;
import com.mmxlabs.models.lng.spotmarkets.DESPurchaseMarket;
import com.mmxlabs.models.lng.spotmarkets.FOBPurchasesMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotMarket;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.tabular.GridViewerHelper;
import com.mmxlabs.models.ui.tabular.renderers.ColumnHeaderRenderer;
import com.mmxlabs.rcp.common.RunnerHelper;
import com.mmxlabs.rcp.common.dnd.BasicDragSource;

public class MainTableComponent extends AbstractBreakEvenComponent {

	private final List<GridColumn> dynamicColumns = new LinkedList<>();
	private GridTreeViewer tableViewer;
	private MainTableWiringDiagram partialCaseDiagram;

	private final Image image_generate;
	private final Image image_grey_generate;
	private boolean isValid = true;
	private Label generateButton;
	private Consumer<BreakEvenAnalysisModel> refreshDynamicColumns;

	protected MainTableComponent(@NonNull final IScenarioEditingLocation scenarioEditingLocation, final Map<Object, IStatus> validationErrors,
			@NonNull final Supplier<BreakEvenAnalysisModel> modelProvider) {
		super(scenarioEditingLocation, validationErrors, modelProvider);
		final ImageDescriptor generate_desc = AbstractUIPlugin.imageDescriptorFromPlugin("com.mmxlabs.models.lng.analytics.editor", "icons/sandbox_generate.gif");
		image_generate = generate_desc.createImage();
		image_grey_generate = ImageDescriptor.createWithFlags(generate_desc, SWT.IMAGE_GRAY).createImage();

	}

	@Override
	public void createControls(final Composite main_parent, final boolean expanded, final IExpansionListener expansionListener, final BreakEvenModellerView breakEvenModellerView) {
		{
			final ExpandableComposite expandable = wrapInExpandable(main_parent, "Options", p -> createViewer(p), expandableCompo -> {

				// Add in a double click handler to make this control maximised in the sash control.
				// Based on EMFViewerPane, but changed the for loop.
				expandableCompo.addMouseListener(hookMaximiseListener(expandableCompo));

				final Transfer[] types = new Transfer[] { LocalSelectionTransfer.getTransfer() };
				final MainTableDropTargetListener listener = new MainTableDropTargetListener(scenarioEditingLocation, tableViewer, () -> {
					refreshDynamicColumns.accept((BreakEvenAnalysisModel) tableViewer.getInput());
					tableViewer.refresh();
				});
				inputWants.add(model -> listener.setBreakEvenAnalysisModel(model));
				// Control control = getControl();
				final DropTarget dropTarget = new DropTarget(expandableCompo, DND.DROP_MOVE | DND.DROP_LINK);
				dropTarget.setTransfer(types);
				dropTarget.addDropListener(listener);
			}, false);
			expandable.setLayoutData(GridDataFactory.fillDefaults().minSize(SWT.DEFAULT, 300).grab(true, true).create());

			hookOpenEditor(tableViewer);
			//
			// final Transfer[] types = new Transfer[] { LocalSelectionTransfer.getTransfer() };
			// final PartialCaseDropTargetListener listener = new PartialCaseDropTargetListener(scenarioEditingLocation, tableViewer);
			// inputWants.add(model -> listener.setOptionAnalysisModel(model));
			// tableViewer.addDropSupport(DND.DROP_MOVE | DND.DROP_LINK, types, listener);
			// expandable.setExpanded(expanded);
			//
			// expandable.addExpansionListener(expansionListener);
		}

		{
			final Composite generateComposite = new Composite(main_parent, SWT.NONE);
			GridDataFactory.generate(generateComposite, 2, 1);

			generateComposite.setLayout(new GridLayout(1, true));

			generateButton = new Label(generateComposite, SWT.NONE);
			generateButton.setLayoutData(GridDataFactory.swtDefaults().align(SWT.CENTER, SWT.CENTER).grab(true, false).create());
			generateButton.setImage(image_grey_generate);
			generateButton.addMouseListener(new MouseListener() {

				@Override
				public void mouseDown(final MouseEvent e) {
					final BreakEvenAnalysisModel m = modelProvider.get();
					if (isValid && m != null) {
						BusyIndicator.showWhile(PlatformUI.getWorkbench().getDisplay(), () -> BreakEvenSandboxEvaluator.evaluate(scenarioEditingLocation, m));
						tableViewer.setInput(m);
					}
				}

				@Override
				public void mouseDoubleClick(final MouseEvent e) {

				}

				@Override
				public void mouseUp(final MouseEvent e) {

				}
			});
			generateButton.addMouseTrackListener(new MouseTrackListener() {

				@Override
				public void mouseHover(final MouseEvent e) {
				}

				@Override
				public void mouseExit(final MouseEvent e) {
					generateButton.setImage(image_grey_generate);
				}

				@Override
				public void mouseEnter(final MouseEvent e) {
					generateButton.setImage(image_generate);
				}
			});
		}
	}

	private Control createViewer(final Composite parent) {
		tableViewer = new GridTreeViewer(parent, SWT.SINGLE | SWT.NONE | SWT.WRAP);
		ColumnViewerToolTipSupport.enableFor(tableViewer);

		GridViewerHelper.configureLookAndFeel(tableViewer);
		tableViewer.getGrid().setHeaderVisible(true);
		tableViewer.getGrid().setCellSelectionEnabled(true);
		tableViewer.getGrid().setSelectionEnabled(false);

		final Point[] cell = new Point[1];

		tableViewer.getGrid().addMouseListener(new MouseAdapter() {

			@Override
			public void mouseUp(final MouseEvent e) {
				cell[0] = null;
			}

			@Override
			public void mouseDown(final MouseEvent e) {
				cell[0] = tableViewer.getGrid().getCell(new Point(e.x, e.y));
			}
 
		});

		tableViewer.getGrid().addMouseMoveListener(new MouseMoveListener() {

			@Override
			public void mouseMove(final MouseEvent e) {
				if (e.stateMask > 0) {
					tableViewer.getGrid().deselectAllCells();
					if (cell[0] != null) {
						tableViewer.getGrid().selectCell(cell[0]);
					} else {
						tableViewer.getGrid().deselectAllCells();
					}
				}

			}
		});

		tableViewer.getGrid().setAutoHeight(true);
		tableViewer.getGrid().setRowHeaderVisible(true);

		final GridColumnGroup lhsMarkets = new GridColumnGroup(tableViewer.getGrid(), SWT.CENTER);
		GridViewerHelper.configureLookAndFeel(lhsMarkets);
		lhsMarkets.setText("Purchase markets");
		{
			// Dummy column to pin lhs markets to lhs
			final GridColumn gc = new GridColumn(lhsMarkets, SWT.CENTER);
			final GridViewerColumn gvc = new GridViewerColumn(tableViewer, gc);
			gvc.getColumn().setHeaderRenderer(new ColumnHeaderRenderer());
			gvc.getColumn().setText("");
			gvc.getColumn().setResizeable(false);
			gvc.getColumn().setVisible(true);
			gvc.getColumn().setWidth(1);
			gvc.setLabelProvider(new ColumnLabelProvider());
		}

		createColumn(tableViewer, "Buy", new BuyOptionDescriptionFormatter(), false).getColumn().setWordWrap(true);

		{
			final GridViewerColumn gvc = new GridViewerColumn(tableViewer, SWT.NONE);
			gvc.getColumn().setHeaderRenderer(new ColumnHeaderRenderer());
			gvc.getColumn().setText("Price");
			gvc.getColumn().setWordWrap(true);

			gvc.getColumn().setWidth(60);
			gvc.setLabelProvider(new ColumnLabelProvider() {
				@Override
				public void update(final ViewerCell cell) {

					cell.setText("");

					final Object element = cell.getElement();
					if (element instanceof BreakEvenAnalysisRow row) {
						for (final BreakEvenAnalysisResultSet rs : row.getRhsResults()) {
							if (rs.getBasedOn() != null && rs.getBasedOn() == row.getRhsBasedOn()) {
								cell.setText(String.format("%.2f", rs.getPrice()));
							}
						}
					}
				}
			});
		}

		{
			final GridViewerColumn gvc = new GridViewerColumn(tableViewer, SWT.CENTER);
			gvc.getColumn().setHeaderRenderer(new ColumnHeaderRenderer());
			gvc.getColumn().setText("");
			gvc.getColumn().setResizeable(false);
			gvc.getColumn().setWidth(100);
			gvc.setLabelProvider(createWiringColumnLabelProvider());
			this.partialCaseDiagram = new MainTableWiringDiagram(tableViewer.getGrid(), gvc);
			// gvc.getColumn().setCellRenderer(createCellRenderer());
		}

		createColumn(tableViewer, "Sell", new SellOptionDescriptionFormatter(), false).getColumn().setWordWrap(true);
		{
			final GridViewerColumn gvc = new GridViewerColumn(tableViewer, SWT.NONE);
			gvc.getColumn().setHeaderRenderer(new ColumnHeaderRenderer());
			gvc.getColumn().setText("Price");
			gvc.getColumn().setWordWrap(true);

			gvc.getColumn().setWidth(60);
			gvc.setLabelProvider(new ColumnLabelProvider() {
				@Override
				public void update(final ViewerCell cell) {

					cell.setText("");

					final Object element = cell.getElement();
					if (element instanceof BreakEvenAnalysisRow) {
						final BreakEvenAnalysisRow row = (BreakEvenAnalysisRow) element;
						for (final BreakEvenAnalysisResultSet rs : row.getLhsResults()) {
							if (rs.getBasedOn() == row.getLhsBasedOn()) {
								cell.setText(String.format("%.2f", rs.getPrice()));
							}
						}
					}
				}
			});
		}

		createColumn(tableViewer, "Shipping", new ShippingOptionDescriptionFormatter(), false).getColumn().setWordWrap(true);

		final GridColumnGroup rhsMarkets = new GridColumnGroup(tableViewer.getGrid(), SWT.CENTER);
		GridViewerHelper.configureLookAndFeel(rhsMarkets);
		rhsMarkets.setText("Sales markets");

		refreshDynamicColumns = (m) -> {
			dynamicColumns.forEach(g -> g.dispose());
			dynamicColumns.clear();

			final List<SpotMarket> markets = m == null ? Collections.emptyList()
					: m.getMarkets().stream() //
							.sorted((a, b) -> a.getName().compareToIgnoreCase(b.getName())) //
							.collect(Collectors.toList());

			for (final SpotMarket sm : markets) {
				GridColumnGroup group;
				if (sm instanceof FOBPurchasesMarket || sm instanceof DESPurchaseMarket) {
					group = lhsMarkets;
				} else {
					group = rhsMarkets;
				}

				final GridColumn gc = new GridColumn(group, SWT.CENTER | SWT.WRAP);
				final GridViewerColumn gvc = new GridViewerColumn(tableViewer, gc);
				gvc.getColumn().setHeaderRenderer(new ColumnHeaderRenderer());
				gvc.getColumn().setText(sm.getName());
				gvc.getColumn().setWordWrap(true);
				gvc.getColumn().setData(sm);

				gvc.getColumn().setWidth(60);
				gvc.setLabelProvider(new ColumnLabelProvider() {
					@Override
					public void update(final ViewerCell cell) {

						cell.setText("");

						final Object element = cell.getElement();
						if (element instanceof BreakEvenAnalysisRow row) {
							if (row.getBuyOption() != null) {
								for (final BreakEvenAnalysisResultSet resultSet : row.getRhsResults()) {
									if (resultSet.getBasedOn() == row.getRhsBasedOn()) {
										for (final BreakEvenAnalysisResult result : resultSet.getResults()) {
											if (result.getTarget() == sm) {

												final double delta = result.getPrice() - result.getReferencePrice();
												cell.setText(String.format("%s\n%.2f\n%s%.2f", format(result.getEta()), result.getPrice(), delta < 0 ? "↓" : "↑", Math.abs(delta)));
											}
										}
									}
								}
							}
							if (row.getSellOption() != null) {
								for (final BreakEvenAnalysisResultSet resultSet : row.getLhsResults()) {
									if (resultSet.getBasedOn() == row.getLhsBasedOn()) {
										for (final BreakEvenAnalysisResult result : resultSet.getResults()) {
											if (result.getTarget() == sm) {

												final double delta = result.getPrice() - result.getReferencePrice();
												cell.setText(String.format("%s\n%.2f\n%s%.2f", format(result.getEta()), result.getPrice(), delta < 0 ? "↓" : "↑", Math.abs(delta)));
											}
										}
									}
								}
							}
						}
					}
				});
				dynamicColumns.add(gvc.getColumn());
			}
		};
		inputWants.add(refreshDynamicColumns);
		tableViewer.setContentProvider(new BreakEvenAnalyticsModelContentProvider());


		final MenuManager mgr = new MenuManager();

		final MainTableContextMenuManager listener = new MainTableContextMenuManager(tableViewer, scenarioEditingLocation, mgr);
		tableViewer.getGrid().addMenuDetectListener(listener);
		inputWants.add(listener::setOptionAnalysisModel);
		
		
		inputWants.add(model -> tableViewer.setInput(model));
		inputWants.add(model -> partialCaseDiagram.setRoot(model));

		return tableViewer.getGrid();
	}

	@Override
	protected void hookDragSource(final GridTreeViewer viewer) {

		final DragSource source = new DragSource(viewer.getGrid(), DND.DROP_MOVE);
		final Transfer[] types = new Transfer[] { LocalSelectionTransfer.getTransfer() };
		source.setTransfer(types);

		source.addDragListener(new BasicDragSource(viewer) {
			@Override
			public void dragStart(final DragSourceEvent event) {
				selection = (IStructuredSelection) viewer.getSelection();
				final GridColumn column = ((GridTreeViewer) viewer).getGrid().getColumn(new Point(event.x, event.y));

				final Iterator<?> itr = selection.iterator();
				while (itr.hasNext()) {
					final BreakEvenAnalysisRow row = (BreakEvenAnalysisRow) itr.next();
					for (final BreakEvenAnalysisResultSet resultSet : row.getRhsResults()) {
						if (resultSet.getBasedOn() == null) {
							for (final BreakEvenAnalysisResult r : resultSet.getResults()) {
								if (r.getTarget() == column.getData()) {
									selection = new StructuredSelection(r);
									return;
								}
							}
						}

					}
					for (final BreakEvenAnalysisResultSet resultSet : row.getLhsResults()) {
						if (resultSet.getBasedOn() == null) {
							for (final BreakEvenAnalysisResult r : resultSet.getResults()) {
								if (r.getTarget() == column.getData()) {
									selection = new StructuredSelection(r);
									return;
								}
							}
						}

					}
				}

			}
		});
	}

	@Override
	public void refresh() {
		tableViewer.refresh();
		GridViewerHelper.recalculateRowHeights(tableViewer.getGrid());
	}

	@Override
	public void dispose() {
		if (image_generate != null) {
			image_generate.dispose();
		}

		if (image_grey_generate != null) {
			image_grey_generate.dispose();
		}
		super.dispose();
	}

	public void setValid(final boolean valid) {
		isValid = valid;
		RunnerHelper.runAsyncIfControlValid(generateButton, btn -> {
			if (isValid) {
				btn.setBackground(null);
			} else {
				btn.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_RED));
			}
		});
	}

	private String format(final LocalDate date) {
		if (date == null) {
			return "";
		}
		return String.format("%d/%s", date.getDayOfMonth(), date.getMonth().getDisplayName(TextStyle.SHORT, Locale.getDefault()));
	}
}
