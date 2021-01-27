/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.standard.paperdeals;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.AbstractTreeViewer;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.nebula.jface.gridviewer.GridTreeViewer;
import org.eclipse.nebula.jface.gridviewer.GridViewerColumn;
import org.eclipse.nebula.widgets.grid.GridColumn;
import org.eclipse.nebula.widgets.grid.GridColumnGroup;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.views.properties.PropertySheet;

import com.mmxlabs.lingo.reports.services.ISelectedDataProvider;
import com.mmxlabs.lingo.reports.services.ISelectedScenariosServiceListener;
import com.mmxlabs.lingo.reports.services.ScenarioComparisonService;
import com.mmxlabs.models.lng.analytics.SolutionOption;
import com.mmxlabs.models.lng.cargo.PaperDeal;
import com.mmxlabs.models.lng.schedule.PaperDealAllocation;
import com.mmxlabs.models.lng.schedule.PaperDealAllocationEntry;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.ScheduleModel;
import com.mmxlabs.models.lng.schedule.SchedulePackage;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.ui.tabular.GridViewerHelper;
import com.mmxlabs.rcp.common.RunnerHelper;
import com.mmxlabs.rcp.common.SelectionHelper;
import com.mmxlabs.rcp.common.ViewerHelper;
import com.mmxlabs.rcp.common.actions.CopyGridToClipboardAction;
import com.mmxlabs.rcp.common.actions.PackActionFactory;
import com.mmxlabs.scenario.service.ui.ScenarioResult;

/**
 */

public class PaperDealsReportView extends ViewPart implements org.eclipse.e4.ui.workbench.modeling.ISelectionListener {

	private ScenarioComparisonService selectedScenariosService;
	private boolean expand = true;

	@Override
	public void createPartControl(final Composite parent) {

		viewer = new GridTreeViewer(parent);
		GridViewerHelper.configureLookAndFeel(viewer);

		viewer.getGrid().setHeaderVisible(true);
		viewer.setAutoExpandLevel(AbstractTreeViewer.ALL_LEVELS);

		final GridViewerColumn gvc1 = createColumn("Date", SchedulePackage.Literals.PAPER_DEAL_ALLOCATION_ENTRY__DATE);
		gvc1.getColumn().setTree(true);
		final GridViewerColumn gvc2 = createColumn("Price", SchedulePackage.Literals.PAPER_DEAL_ALLOCATION_ENTRY__PRICE);
		final GridViewerColumn gvc3 = createColumn("Settled", SchedulePackage.Literals.PAPER_DEAL_ALLOCATION_ENTRY__SETTLED);
		final GridViewerColumn gvc4 = createColumn("Quantity", SchedulePackage.Literals.PAPER_DEAL_ALLOCATION_ENTRY__QUANTITY);
		final GridViewerColumn gvc5 = createColumn("Value", SchedulePackage.Literals.PAPER_DEAL_ALLOCATION_ENTRY__VALUE);

		viewer.setContentProvider(new ITreeContentProvider() {

			@Override
			public void inputChanged(final Viewer viewer, final Object oldInput, final Object newInput) {
				// TODO Auto-generated method stub

			}

			@Override
			public void dispose() {
				// TODO Auto-generated method stub

			}

			@Override
			public boolean hasChildren(final Object element) {

				if (element instanceof PaperDealAllocation) {
					return true;
				}
				return false;
			}

			@Override
			public Object getParent(final Object element) {
				if (element instanceof PaperDealAllocationEntry) {
					return ((PaperDealAllocationEntry) element).eContainer();
				}
				return null;
			}

			@Override
			public Object[] getElements(final Object inputElement) {
				if (inputElement instanceof Object[]) {
					return (Object[]) inputElement;
				}
				if (inputElement instanceof Collection<?>) {
					final Collection<?> collection = (Collection<?>) inputElement;
					return collection.toArray();
				}
				if (inputElement instanceof SlotAllocation) {
					final SlotAllocation slotAllocation = (SlotAllocation) inputElement;
					return slotAllocation.getExposures().toArray();
				}
				return new Object[0];
			}

			@Override
			public Object[] getChildren(final Object parentElement) {
				if (parentElement instanceof Object[]) {
					return (Object[]) parentElement;
				}
				if (parentElement instanceof Collection<?>) {
					final Collection<?> collection = (Collection<?>) parentElement;
					return collection.toArray();
				}
				if (parentElement instanceof PaperDealAllocation) {
					final PaperDealAllocation slotAllocation = (PaperDealAllocation) parentElement;
					return slotAllocation.getEntries().toArray();
				}
				return new Object[0];
			}
		});

		final ESelectionService service = (ESelectionService) getSite().getService(ESelectionService.class);
		service.addPostSelectionListener(this);

		selectedScenariosService = getSite().getService(ScenarioComparisonService.class);
		selectedScenariosService.addListener(selectedScenariosServiceListener);

		makeActions();

		selectedScenariosService.triggerListener(selectedScenariosServiceListener, false);

		getSite().setSelectionProvider(viewer);

	}

	private void makeActions() {
		final Action expandCollapseAll = new Action("Collapse", Action.AS_PUSH_BUTTON) {
			@Override
			public void run() {
				expand = !expand;
				viewer.setAutoExpandLevel(expand ? AbstractTreeViewer.ALL_LEVELS : 0);
				setText(expand ? "Collapse" : "Expand");
				getViewSite().getActionBars().updateActionBars();
				PaperDealsReportView.this.refresh();
			}
		};

		getViewSite().getActionBars().getToolBarManager().add(expandCollapseAll);

		final Action packColumnsAction = PackActionFactory.createPackColumnsAction(viewer);
		final Action copyTableAction = new CopyGridToClipboardAction(viewer.getGrid());
		getViewSite().getActionBars().setGlobalActionHandler(ActionFactory.COPY.getId(), copyTableAction);

		getViewSite().getActionBars().getToolBarManager().add(packColumnsAction);
		getViewSite().getActionBars().getToolBarManager().add(copyTableAction);

		getViewSite().getActionBars().getToolBarManager().update(true);

	}

	private GridViewerColumn createColumn(final String title, final GridColumnGroup colGroup, final EStructuralFeature reference) {
		final GridColumn column = new GridColumn(colGroup, SWT.NONE);
		final GridViewerColumn col = new GridViewerColumn(viewer, column);
		GridViewerHelper.configureLookAndFeel(col);

		return createColumn(col, title, reference);
	}

	private GridColumnGroup createGroup(final String title) {
		final GridColumnGroup group = new GridColumnGroup(viewer.getGrid(), SWT.NONE);
		GridViewerHelper.configureLookAndFeel(group);

		group.setText(title);
		return group;
	}

	private GridViewerColumn createColumn(final String title, final EStructuralFeature reference) {
		final GridViewerColumn col = new GridViewerColumn(viewer, SWT.NONE);
		GridViewerHelper.configureLookAndFeel(col);
		return createColumn(col, title, reference);
	}

	private GridViewerColumn createColumn(final GridViewerColumn col, final String title, final EStructuralFeature reference) {
		col.getColumn().setText(title);

		col.setLabelProvider(new CellLabelProvider() {

			@Override
			public void update(final ViewerCell cell) {
				cell.setText("");

				final Object element = cell.getElement();
				if (element instanceof PaperDealAllocation) {
					PaperDealAllocation paperDealAllocation = (PaperDealAllocation) element;
					PaperDeal paperDeal = paperDealAllocation.getPaperDeal();
					if (reference == SchedulePackage.eINSTANCE.getPaperDealAllocationEntry_Date()) {
						cell.setText(paperDeal == null ? "<Unknown deal>" : paperDeal.getName());
					} else if (reference == SchedulePackage.eINSTANCE.getPaperDealAllocationEntry_Quantity()) {
						double sum = paperDealAllocation.getEntries().stream().mapToDouble(PaperDealAllocationEntry::getQuantity).sum();
						cell.setText(String.format("%,.1f", sum));
					} else if (reference == SchedulePackage.eINSTANCE.getPaperDealAllocationEntry_Value()) {
						double sum = paperDealAllocation.getEntries().stream().mapToDouble(PaperDealAllocationEntry::getValue).sum();
						cell.setText(String.format("%,.0f", sum));
					} else if (reference == SchedulePackage.eINSTANCE.getPaperDealAllocationEntry_Price()) {
						cell.setText(paperDeal == null ? "" : paperDeal.getIndex());
					}
				} else if (element instanceof PaperDealAllocationEntry) {
					PaperDealAllocationEntry paperDealAllocationEntry = (PaperDealAllocationEntry) element;
					if (reference == SchedulePackage.eINSTANCE.getPaperDealAllocationEntry_Date()) {
						cell.setText(paperDealAllocationEntry.getDate().toString());
					} else if (reference == SchedulePackage.eINSTANCE.getPaperDealAllocationEntry_Quantity()) {
						cell.setText(String.format("%,.1f", paperDealAllocationEntry.getQuantity()));
					} else if (reference == SchedulePackage.eINSTANCE.getPaperDealAllocationEntry_Value()) {
						cell.setText(String.format("%,.1f", paperDealAllocationEntry.getValue()));
					} else if (reference == SchedulePackage.eINSTANCE.getPaperDealAllocationEntry_Settled()) {
						cell.setText(paperDealAllocationEntry.isSettled() ? "Y" : "");
					} else if (reference == SchedulePackage.eINSTANCE.getPaperDealAllocationEntry_Price()) {
						cell.setText(String.format("%.2f", paperDealAllocationEntry.getPrice()));
					}
				}
			}

		});

		col.getColumn().setWidth(120);
		return col;

	}

	@Override
	public void dispose() {
		final ESelectionService service = (ESelectionService) getSite().getService(ESelectionService.class);
		service.removePostSelectionListener(this);

		selectedScenariosService.removeListener(selectedScenariosServiceListener);
		super.dispose();
	}

	private ISelection selection;

	public PaperDealsReportView() {
		// super("com.mmxlabs.lingo.doc.Reports_IndexExposuresDetails");
	}

	@NonNull
	private final ISelectedScenariosServiceListener selectedScenariosServiceListener = new ISelectedScenariosServiceListener() {
		@Override
		public void selectedDataProviderChanged(@NonNull ISelectedDataProvider selectedDataProvider, boolean block) {
			final Runnable r = new Runnable() {
				@Override
				public void run() {

					final List<PaperDealAllocation> slotAllocations = new LinkedList<>();
					ScenarioResult pinned = selectedDataProvider.getPinnedScenarioResult();
					if (pinned != null) {
						final @Nullable ScheduleModel scheduleModel = pinned.getTypedResult(ScheduleModel.class);
						if (scheduleModel != null) {
							final Schedule schedule = scheduleModel.getSchedule();

							if (schedule != null) {
								for (final PaperDealAllocation paperDealAllocation : schedule.getPaperDealAllocations()) {
									final PaperDeal pd = paperDealAllocation.getPaperDeal();
									if (!schedule.getGeneratedPaperDeals().contains(pd)) {
										slotAllocations.add(paperDealAllocation);
									}
								}
							}
						}
					}
					for (final ScenarioResult other : selectedDataProvider.getOtherScenarioResults()) {
						boolean showingOptiResult = false;
						if (other != null && other.getResultRoot() != null && other.getResultRoot().eContainer() instanceof SolutionOption) {
							slotAllocations.clear();
							showingOptiResult = true;
						}
						final @Nullable ScheduleModel scheduleModel = other.getTypedResult(ScheduleModel.class);
						if (scheduleModel != null) {
							final Schedule schedule = scheduleModel.getSchedule();

							if (schedule != null) {
								for (final PaperDealAllocation paperDealAllocation : schedule.getPaperDealAllocations()) {
									final PaperDeal pd = paperDealAllocation.getPaperDeal();
									if (!schedule.getGeneratedPaperDeals().contains(pd)) {
										slotAllocations.add(paperDealAllocation);
									}
								}
							}
						}
						if (showingOptiResult) {
							break;
						}
					}

					ViewerHelper.setInput(viewer, true, slotAllocations);
				}
			};

			RunnerHelper.exec(r, block);
		}
	};
	private GridTreeViewer viewer;

	@Override
	public void selectionChanged(final MPart part, final Object selectionObject) {

		final IWorkbenchPart e3Part = SelectionHelper.getE3Part(part);
		if (e3Part != null) {
			if (e3Part == this) {
				return;
			}
			if (e3Part instanceof PropertySheet) {
				return;
			}
		}
		selection = SelectionHelper.adaptSelection(selectionObject);
		// viewer.setSelection(selection, true);
		ViewerHelper.refreshThen(viewer, true, () -> viewer.expandAll());
	}

	@Override
	public void setFocus() {
		ViewerHelper.setFocus(viewer);
	}

	protected void refresh() {
		selectedScenariosService.triggerListener(selectedScenariosServiceListener, false);
	}

}