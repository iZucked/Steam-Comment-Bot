/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.standard.exposures;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.emf.ecore.EAttribute;
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
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.nebula.jface.gridviewer.GridTreeViewer;
import org.eclipse.nebula.jface.gridviewer.GridViewerColumn;
import org.eclipse.nebula.widgets.grid.Grid;
import org.eclipse.nebula.widgets.grid.GridColumn;
import org.eclipse.nebula.widgets.grid.GridColumnGroup;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.part.ViewPart;

import com.mmxlabs.lingo.reports.IReportContents;
import com.mmxlabs.lingo.reports.IReportContentsGenerator;
import com.mmxlabs.lingo.reports.ReportContents;
import com.mmxlabs.lingo.reports.services.ISelectedDataProvider;
import com.mmxlabs.lingo.reports.services.ISelectedScenariosServiceListener;
import com.mmxlabs.lingo.reports.services.ReentrantSelectionManager;
import com.mmxlabs.lingo.reports.services.ScenarioComparisonService;
import com.mmxlabs.lingo.reports.services.SelectedDataProviderImpl;
import com.mmxlabs.lingo.reports.services.SelectionServiceUtils;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.PaperDeal;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.pricing.AbstractYearMonthCurve;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.ExposureDetail;
import com.mmxlabs.models.lng.schedule.PaperDealAllocation;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.ScheduleModel;
import com.mmxlabs.models.lng.schedule.SchedulePackage;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.ui.tabular.GridViewerHelper;
import com.mmxlabs.rcp.common.SelectionHelper;
import com.mmxlabs.rcp.common.ViewerHelper;
import com.mmxlabs.rcp.common.actions.CopyGridToClipboardAction;
import com.mmxlabs.rcp.common.actions.CopyGridToHtmlStringUtil;
import com.mmxlabs.rcp.common.actions.CopyTransposedGridToJSONUtil;
import com.mmxlabs.rcp.common.actions.PackActionFactory;
import com.mmxlabs.scenario.service.ScenarioResult;

/**
 */

public class ExposureDetailReportView extends ViewPart {

	private ScenarioComparisonService selectedScenariosService;
	private ReentrantSelectionManager selectionManager;

	@Override
	public void createPartControl(final Composite parent) {

		viewer = new GridTreeViewer(parent);
		GridViewerHelper.configureLookAndFeel(viewer);

		viewer.getGrid().setHeaderVisible(true);
		viewer.setAutoExpandLevel(AbstractTreeViewer.ALL_LEVELS);

		createDealColumn("Deal");

		createColumn("Index", SchedulePackage.Literals.EXPOSURE_DETAIL__INDEX_NAME);
		createColumn("Month", SchedulePackage.Literals.EXPOSURE_DETAIL__DATE);
		final GridColumnGroup volumeGroup = createGroup("Volume");

		createColumn("Native", volumeGroup, SchedulePackage.Literals.EXPOSURE_DETAIL__VOLUME_IN_NATIVE_UNITS);
		createColumn("Unit", volumeGroup, SchedulePackage.Literals.EXPOSURE_DETAIL__VOLUME_UNIT);

		final GridColumnGroup currencyGroup = createGroup("Currency");
		createColumn("Price", currencyGroup, SchedulePackage.Literals.EXPOSURE_DETAIL__UNIT_PRICE);
		createColumn("Value", currencyGroup, SchedulePackage.Literals.EXPOSURE_DETAIL__NATIVE_VALUE);
		createColumn("Unit", currencyGroup, SchedulePackage.Literals.EXPOSURE_DETAIL__CURRENCY_UNIT);

		viewer.setContentProvider(new ITreeContentProvider() {
 
			@Override
			public boolean hasChildren(final Object element) {

				if (element instanceof SlotAllocation slotAllocation) {
					return !slotAllocation.getExposures().isEmpty();
				}
				if (element instanceof PaperDealAllocation paperDealAllocation) {
					return !paperDealAllocation.getEntries().isEmpty();
				}
				return false;
			}

			@Override
			public Object getParent(final Object element) {
				if (element instanceof ExposureDetail detail) {
					return detail.eContainer();
				}
				return null;
			}

			@Override
			public Object[] getElements(final Object inputElement) {
				if (inputElement instanceof Object[]) {
					return (Object[]) inputElement;
				}
				if (inputElement instanceof Collection<?> collection) {
					return collection.toArray();
				}
				if (inputElement instanceof SlotAllocation slotAllocation) {
					return slotAllocation.getExposures().toArray();
				}
				if (inputElement instanceof PaperDealAllocation paperDealAllocation) {
					return paperDealAllocation.getEntries().stream().flatMap(e -> e.getExposures().stream()).toArray();
				}
				return new Object[0];
			}

			@Override
			public Object[] getChildren(final Object parentElement) {
				if (parentElement instanceof Object[]) {
					return (Object[]) parentElement;
				}
				if (parentElement instanceof Collection<?> collection) {
					return collection.toArray();
				}
				if (parentElement instanceof SlotAllocation slotAllocation) {
					return slotAllocation.getExposures().toArray();
				}
				if (parentElement instanceof PaperDealAllocation paperDealAllocation) {
					return paperDealAllocation.getEntries().stream().flatMap(e -> e.getExposures().stream()).toArray();
				}
				return new Object[0];
			}
		});

		viewer.addFilter(new ViewerFilter() {

			@Override
			public boolean select(final Viewer viewer, final Object parentElement, final Object element) {
				List<Object> selected = (selection == null) ? Collections.emptyList() : SelectionHelper.convertToList(selection, Object.class);
				selected = selected.stream().filter(
						s -> s instanceof Slot || s instanceof SlotAllocation || s instanceof Cargo || s instanceof CargoAllocation || s instanceof PaperDealAllocation || s instanceof PaperDeal)
						.toList();
				//
				if (selected.isEmpty()) {
					return false;
				}
				if (element instanceof ExposureDetail) {
					return true;
				}
				if (element instanceof SlotAllocation slotAllocation) {
					return selected.contains(element) || selected.contains(slotAllocation.getSlot()) || selected.contains(slotAllocation.getCargoAllocation());

				} else if (element instanceof PaperDeal paperDeal) {
					return selected.contains(paperDeal);
				} else if (element instanceof PaperDealAllocation paperDealAllocation) {
					return selected.contains(paperDealAllocation) || selected.contains(paperDealAllocation.getPaperDeal());
				}
				return true;
			}
		});

		selectedScenariosService = getSite().getService(ScenarioComparisonService.class);

		selectionManager = new ReentrantSelectionManager(viewer, selectedScenariosServiceListener, selectedScenariosService, false);
		makeActions();
		try {
			selectedScenariosService.triggerListener(selectedScenariosServiceListener, false);
		} catch (Exception e) {
			// Ignore any initial issues.
		}
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

	private GridViewerColumn createDealColumn(final String title) {
		final GridViewerColumn col = new GridViewerColumn(viewer, SWT.NONE);
		GridViewerHelper.configureLookAndFeel(col);
		col.getColumn().setText(title);

		col.setLabelProvider(new CellLabelProvider() {

			@Override
			public void update(final ViewerCell cell) {
				cell.setText("");
				final Object element = cell.getElement();
				if (element instanceof SlotAllocation slotAllocation) {
					cell.setText(slotAllocation.getName());
				} else if (element instanceof PaperDealAllocation paperDealAllocation) {
					cell.setText(paperDealAllocation.getPaperDeal().getName());
				}
			}
		});

		col.getColumn().setWidth(120);
		return col;
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
				if (element instanceof EObject eObject) {
					if (reference instanceof EAttribute) {
						if (eObject.eClass().getEAllAttributes().contains(reference)) {
							updateCellForEAttributeReference(cell, element, reference);
						}
					} else {
						if (eObject.eClass().getEAllReferences().contains(reference)) {
							try {
								updateCellWithEGET(cell, element, reference);
							} catch (final Exception e) {
								System.out.println(e.getMessage());
							} finally {
								cell.setText("");
							}
						}
					}
				}
			}

		});

		col.getColumn().setWidth(120);
		return col;
	}

	private void updateCellForEAttributeReference(final ViewerCell cell, final Object element, final EStructuralFeature reference) {
		final Object o = ((EObject) element).eGet(reference);
		if (o == null) {
			cell.setText("");
		} else if (reference == SchedulePackage.Literals.EXPOSURE_DETAIL__UNIT_PRICE) {
			cell.setText(String.format("%,.3f", (Double) o));
		} else if (reference.getEType() == EcorePackage.Literals.EDOUBLE) {
			cell.setText(String.format("%,.1f", (Double) o));
		} else if (reference == SchedulePackage.Literals.EXPOSURE_DETAIL__INDEX_NAME) {
			if ("physical".equalsIgnoreCase(o.toString())) {
				cell.setText(o.toString());
			} else {
				cell.setText(o.toString().toUpperCase());
			}
		} else if (reference == SchedulePackage.Literals.EXPOSURE_DETAIL__VOLUME_UNIT) {
			switch (o.toString().toLowerCase()) {
			case "mmbtu":
				cell.setText("mmBtu");
				break;
			case "mwh":
				cell.setText("MWh");
				break;
			case "bbl":
				cell.setText("bbl");
				break;
			case "therm":
				cell.setText("therm");
				break;
			default:
				cell.setText(o.toString());
			}
		} else {
			cell.setText(o.toString());
		}
	}

	private void updateCellWithEGET(final ViewerCell cell, final Object element, final EStructuralFeature reference) {
		final Object o = ((EObject) element).eGet(reference);
		if (o instanceof Slot) {
			final Slot<?> slot = (Slot<?>) o;
			cell.setText(slot.getName());
		} else if (o instanceof AbstractYearMonthCurve idx) {
			cell.setText(idx.getName());
		} else if (element instanceof ExposureDetail) {
			if (reference == SchedulePackage.Literals.EXPOSURE_DETAIL__UNIT_PRICE) {
				cell.setText(String.format("%,.3f", (Double) o));
			} else if (reference.getEType() == EcorePackage.Literals.EDOUBLE) {
				cell.setText(String.format("%,.1f", (Double) o));
			} else {
				cell.setText(o.toString());
			}
		}
	}

	private ISelection selection;

	@NonNull
	private final ISelectedScenariosServiceListener selectedScenariosServiceListener = new ISelectedScenariosServiceListener() {
		@Override
		public void selectedDataProviderChanged(@NonNull ISelectedDataProvider selectedDataProvider, boolean block) {

			final Runnable r = () -> {

				final List<EObject> slotAllocations = new LinkedList<>();
				ScenarioResult pinned = selectedDataProvider.getPinnedScenarioResult();
				if (pinned != null) {

					final @Nullable ScheduleModel scheduleModel = pinned.getTypedResult(ScheduleModel.class);
					if (scheduleModel != null) {
						final Schedule schedule = scheduleModel.getSchedule();
						if (schedule != null) {
							schedule.getCargoAllocations().forEach(ca -> slotAllocations.addAll(ca.getSlotAllocations()));
							schedule.getPaperDealAllocations().forEach(slotAllocations::add);
						}
					}
				}
				for (final ScenarioResult other : selectedDataProvider.getOtherScenarioResults()) {

					final @Nullable ScheduleModel scheduleModel = other.getTypedResult(ScheduleModel.class);
					if (scheduleModel != null) {
						final Schedule schedule = scheduleModel.getSchedule();
						if (schedule != null) {
							schedule.getCargoAllocations().forEach(ca -> slotAllocations.addAll(ca.getSlotAllocations()));
							schedule.getPaperDealAllocations().forEach(slotAllocations::add);

						}
					}
				}

				ViewerHelper.setInput(viewer, true, slotAllocations);
			
				
			};

			ViewerHelper.runIfViewerValid(viewer, block, r);
		}

		@Override
		public void selectedObjectChanged(MPart source, ISelection selection) {
			if (SelectionServiceUtils.isSelectionValid(source, selection)) {
				// Update selection before view refresh
				ExposureDetailReportView.this.selection = SelectionHelper.adaptSelection(selection);
				ViewerHelper.refreshThen(viewer, true, () -> viewer.expandAll());
			}
		}

	};
	private GridTreeViewer viewer;

	@Override
	public void setFocus() {
		ViewerHelper.setFocus(viewer);
	}

	@Override
	public <T> T getAdapter(final Class<T> adapter) {

		if (GridTreeViewer.class.isAssignableFrom(adapter)) {
			return adapter.cast(viewer);
		}
		if (Grid.class.isAssignableFrom(adapter)) {
			return adapter.cast(viewer.getGrid());
		}
		if (IReportContentsGenerator.class.isAssignableFrom(adapter)) {

			return adapter.cast(new IReportContentsGenerator() {
				public IReportContents getReportContents(final ScenarioResult pin, final ScenarioResult other, final @Nullable List<Object> selectedObjects) {
					final SelectedDataProviderImpl provider = new SelectedDataProviderImpl();
					if (pin != null) {
						provider.addScenario(pin);
						provider.setPinnedScenarioInstance(pin);
					}
					if (other != null) {
						provider.addScenario(other);
					}

					if (selectedObjects != null) {
						provider.setSelectedObjects(null, new StructuredSelection(selectedObjects));
					}

					// Request a blocking update ...
					selectedScenariosServiceListener.selectedDataProviderChanged(provider, true);
					
					ExposureDetailReportView.this.selection = SelectionHelper.adaptSelection(provider.getSelectedObjects());
					ViewerHelper.refreshThen(viewer, true, () -> viewer.expandAll());
					
					// ... so the data is ready to be read here.
					final CopyGridToHtmlStringUtil jsonUtil = new CopyGridToHtmlStringUtil(viewer.getGrid(), true, true);
					final String jsonContents = jsonUtil.convert();
					return ReportContents.makeHTML(jsonContents);
				}
			});
		}
		 
		return super.getAdapter(adapter);
	}

}