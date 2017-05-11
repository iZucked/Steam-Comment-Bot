/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.standard;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.AbstractTreeViewer;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.jface.viewers.ViewerFilter;
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
import com.mmxlabs.lingo.reports.services.SelectedScenariosService;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.pricing.NamedIndexContainer;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.ExposureDetail;
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

public class ExposureDetailReportView extends ViewPart implements org.eclipse.e4.ui.workbench.modeling.ISelectionListener {

	private SelectedScenariosService selectedScenariosService;

	@Override
	public void createPartControl(final Composite parent) {

		viewer = new GridTreeViewer(parent);
		GridViewerHelper.configureLookAndFeel(viewer);

		viewer.getGrid().setHeaderVisible(true);
		viewer.setAutoExpandLevel(AbstractTreeViewer.ALL_LEVELS);

		final GridViewerColumn gvc0 = createColumn("Slot", SchedulePackage.Literals.SLOT_ALLOCATION__SLOT);
		// gvc0.getColumn().setTree(true);

		final GridViewerColumn gvc1 = createColumn("Index", SchedulePackage.Literals.EXPOSURE_DETAIL__INDEX);
		final GridViewerColumn gvc2 = createColumn("Month", SchedulePackage.Literals.EXPOSURE_DETAIL__DATE);
		final GridColumnGroup volumeGroup = createGroup("Volume");

		final GridViewerColumn gvc3 = createColumn("mmBtu", volumeGroup, SchedulePackage.Literals.EXPOSURE_DETAIL__VOLUME_IN_MMBTU);
		final GridViewerColumn gvc4 = createColumn("Native", volumeGroup, SchedulePackage.Literals.EXPOSURE_DETAIL__VOLUME_IN_NATIVE_UNITS);
		final GridViewerColumn gvc5 = createColumn("Unit", volumeGroup, SchedulePackage.Literals.EXPOSURE_DETAIL__VOLUME_UNIT);

		final GridColumnGroup currencyGroup = createGroup("Currency");
		final GridViewerColumn gvc6 = createColumn("Price", currencyGroup, SchedulePackage.Literals.EXPOSURE_DETAIL__UNIT_PRICE);
		final GridViewerColumn gvc7 = createColumn("Value", currencyGroup, SchedulePackage.Literals.EXPOSURE_DETAIL__NATIVE_VALUE);
		final GridViewerColumn gvc8 = createColumn("Unit", currencyGroup, SchedulePackage.Literals.EXPOSURE_DETAIL__CURRENCY_UNIT);

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

				if (element instanceof SlotAllocation) {
					final SlotAllocation slotAllocation = (SlotAllocation) element;
					return !slotAllocation.getExposures().isEmpty();
				}
				return false;
			}

			@Override
			public Object getParent(final Object element) {
				if (element instanceof ExposureDetail) {
					return ((ExposureDetail) element).eContainer();
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
				if (parentElement instanceof SlotAllocation) {
					final SlotAllocation slotAllocation = (SlotAllocation) parentElement;
					return slotAllocation.getExposures().toArray();
				}
				return new Object[0];
			}
		});

		viewer.addFilter(new ViewerFilter() {

			@Override
			public boolean select(final Viewer viewer, final Object parentElement, final Object element) {
				List<Object> selected = (selection == null) ? Collections.emptyList() : SelectionHelper.convertToList(selection, Object.class);
				selected = selected.stream().filter(s -> s instanceof Slot || s instanceof SlotAllocation || s instanceof Cargo || s instanceof CargoAllocation).collect(Collectors.toList());
				// selected = selected.stream().filter(s -> s instanceof SlotAllocation).collect(Collectors.toList());

				if (selected.isEmpty()) {
					return false;
				}
				if (element instanceof ExposureDetail) {
					return true;
				}
				if (element instanceof SlotAllocation) {
					final SlotAllocation slotAllocation = (SlotAllocation) element;
					return selected.contains(element) || selected.contains(slotAllocation.getSlot()) || selected.contains(slotAllocation.getCargoAllocation());

				}
				return true;
			}
		});

		final ESelectionService service = (ESelectionService) getSite().getService(ESelectionService.class);
		service.addPostSelectionListener(this);

		selectedScenariosService = (SelectedScenariosService) getSite().getService(SelectedScenariosService.class);
		selectedScenariosService.addListener(selectedScenariosServiceListener);

		makeActions();

		selectedScenariosService.triggerListener(selectedScenariosServiceListener, false);
	}

	private void makeActions() {
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

				final Object element = cell.getElement();
				if (element instanceof EObject) {
					final EObject eObject = (EObject) element;
					if (eObject.eClass().getEAllReferences().contains(reference)) {
						try {
							final Object o = ((EObject) element).eGet(reference);
							if (o instanceof Slot) {
								final Slot slot = (Slot) o;
								cell.setText(slot.getName());
							} else if (o instanceof NamedIndexContainer<?>) {
								final NamedIndexContainer<?> idx = (NamedIndexContainer<?>) o;
								cell.setText(idx.getName());
							} else if (element instanceof ExposureDetail) {
								final ExposureDetail detail = (ExposureDetail) element;
								if (reference == SchedulePackage.Literals.EXPOSURE_DETAIL__UNIT_PRICE) {
									cell.setText(String.format("%,.3f", (Double) o));
								} else if (reference.getEType() == EcorePackage.Literals.EDOUBLE) {
									cell.setText(String.format("%,.1f", ((Double) o).doubleValue()));
								} else {
									cell.setText(o.toString());
								}
							}
						} catch (final Throwable e) {
							cell.setText("");
						}
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

	public ExposureDetailReportView() {
		// super("com.mmxlabs.lingo.doc.Reports_IndexExposuresDetails");
	}

	@NonNull
	private final ISelectedScenariosServiceListener selectedScenariosServiceListener = new ISelectedScenariosServiceListener() {

		@Override
		public void selectionChanged(final ISelectedDataProvider selectedDataProvider, final ScenarioResult pinned, final Collection<ScenarioResult> others, final boolean block) {
			final Runnable r = new Runnable() {
				@Override
				public void run() {

					final List<SlotAllocation> slotAllocations = new LinkedList<>();

					if (pinned != null) {
						@Nullable
						final ScheduleModel scheduleModel = pinned.getTypedResult(ScheduleModel.class);
						if (scheduleModel != null) {
							final Schedule schedule = scheduleModel.getSchedule();
							if (schedule != null) {
								schedule.getCargoAllocations().forEach(ca -> slotAllocations.addAll(ca.getSlotAllocations()));
							}
						}
					}
					for (final ScenarioResult other : others) {
						@Nullable
						final ScheduleModel scheduleModel = other.getTypedResult(ScheduleModel.class);
						if (scheduleModel != null) {
							final Schedule schedule = scheduleModel.getSchedule();
							if (schedule != null) {
								schedule.getCargoAllocations().forEach(ca -> slotAllocations.addAll(ca.getSlotAllocations()));
							}
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

}