/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.standard;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Supplier;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ColumnViewerToolTipSupport;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.nebula.jface.gridviewer.GridTableViewer;
import org.eclipse.nebula.jface.gridviewer.GridViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.views.properties.PropertySheet;

import com.mmxlabs.common.Pair;
import com.mmxlabs.lingo.reports.views.standard.StandardEconsRowFactory.EconsOptions;
import com.mmxlabs.lingo.reports.views.standard.StandardEconsRowFactory.EconsOptions.MarginBy;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.MarketAllocation;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.ScheduleModel;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.schedule.util.ScheduleModelUtils;
import com.mmxlabs.models.ui.tabular.GridViewerHelper;
import com.mmxlabs.rcp.common.SelectionHelper;
import com.mmxlabs.rcp.common.ServiceHelper;
import com.mmxlabs.rcp.common.ViewerHelper;
import com.mmxlabs.rcp.common.application.BindSelectionListener;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.ui.editing.IScenarioServiceEditorInput;

/**
 * The {@link CargoEconsReportComponent} is a vertical report similar in concept to the Properties View. This table is the transpose of most other tables. Columns represent the input data and rows are
 * pre-defined.
 * 
 * @author Simon Goodall
 * 
 */
public class CargoEconsReportComponent /* extends ViewPart */ {

	@Inject
	private ESelectionService selectionService;

	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "com.mmxlabs.shiplingo.platform.reports.views.CargoEconsReport";
	private GridTableViewer viewer;
	private Collection<Pair<String, org.eclipse.e4.ui.workbench.modeling.ISelectionListener>> selectionListeners = new ConcurrentLinkedQueue<>();

	/**
	 * List of dynamically generated columns to be disposed on selection changes
	 */
	private final List<GridViewerColumn> dataColumns = new LinkedList<GridViewerColumn>();
	private EconsOptions options = new EconsOptions();

	@PostConstruct
	public void createPartControl(final Composite parent) {
		viewer = new GridTableViewer(parent, SWT.H_SCROLL | SWT.V_SCROLL);
		GridViewerHelper.configureLookAndFeel(viewer);
		ColumnViewerToolTipSupport.enableFor(viewer);

		// Add the name column
		{
			final GridViewerColumn gvc = new GridViewerColumn(viewer, SWT.NONE);
			GridViewerHelper.configureLookAndFeel(gvc);
			gvc.getColumn().setText("Name");
			gvc.setLabelProvider(new FieldTypeNameLabelProvider());
			gvc.getColumn().setWidth(100);
		}

		// All other columns dynamically added.

		// Array content provider as we pass in an array of enums
		viewer.setContentProvider(new ArrayContentProvider());
		// Our input!

		List<CargoEconsReportRow> rows = new LinkedList<CargoEconsReportRow>();
		ServiceHelper.withAllServices(IEconsRowFactory.class, null, factory -> {
			rows.addAll(factory.createRows(options));
			return true;
		});
		Collections.sort(rows, (a, b) -> a.order - b.order);

		viewer.setInput(rows);
		viewer.getGrid().setHeaderVisible(true);

		PlatformUI.getWorkbench().getHelpSystem().setHelp(viewer.getControl(), "com.mmxlabs.lingo.doc.Reports_CargoEcons");
	}

	@PreDestroy
	public void dispose() {

		for (Pair<String, org.eclipse.e4.ui.workbench.modeling.ISelectionListener> p : selectionListeners) {
			if (p.getFirst() == null) {
				selectionService.removePostSelectionListener(p.getSecond());
			} else {
				selectionService.removePostSelectionListener(p.getFirst(), p.getSecond());
			}
		}

	}

	@Focus
	public void setFocus() {
		ViewerHelper.setFocus(viewer);
	}

	/**
	 * The {@link FieldType} is the row data. Each enum is a different row. Currently there is no table sorter so enum order is display order
	 * 
	 */

	/**
	 * A label provider for the "Name" column
	 * 
	 */
	private static class FieldTypeNameLabelProvider extends ColumnLabelProvider {

		@Override
		public Image getImage(final Object element) {
			return null;
		}

		@Override
		public String getText(final Object element) {
			if (element instanceof CargoEconsReportRow) {
				final CargoEconsReportRow fieldType = (CargoEconsReportRow) element;
				return fieldType.name; // + " (" + fieldType.getUnit() + ")";
			}
			return null;
		}

		// @Override
		// public void update(final ViewerCell cell) {
		// cell.setText(getText(cell.getElement()));
		//
		// }

	}

	/**
	 * Label Provider created for each column. Returns text for the given {@link CargoEconsReportRow} enum (as the input element) for the {@link IFieldTypeMapper} object passed in during creation.
	 * 
	 */
	private static class FieldTypeMapperLabelProvider extends ColumnLabelProvider {

		private final Object columnElement;

		public FieldTypeMapperLabelProvider(final Object columnElement) {
			this.columnElement = columnElement;
		}

		@Override
		public Image getImage(final Object element) {
			return null;
		}

		@Override
		public String getText(final Object element) {
			if (element instanceof CargoEconsReportRow) {
				CargoEconsReportRow row = (CargoEconsReportRow) element;
				return row.formatter.render(columnElement);
			}
			return null;
		}

		@Override
		public String getToolTipText(Object element) {
			if (element instanceof CargoEconsReportRow) {
				CargoEconsReportRow row = (CargoEconsReportRow) element;
				Supplier<String> tooltip = row.tooltip;
				if (tooltip != null) {
					return tooltip.get();
				}
			}
			return null;
		}

		@Override
		public Color getForeground(final Object element) {
			if (element instanceof CargoEconsReportRow) {
				final CargoEconsReportRow row = ((CargoEconsReportRow) element);
				if (row.colourProvider != null) {
					return row.colourProvider.getForeground(columnElement);
				}
			}
			return null;
		}
	}

	/**
	 * This method processes an {@link ISelection} object and return a list of objects that this report knows how to process. Currently we try to find associated {@link CargoAllocation} objects.
	 * 
	 * @param selection
	 * @return
	 */
	private Collection<Object> processSelection(final IWorkbenchPart part, final ISelection selection) {
		final Collection<Object> validObjects = new LinkedHashSet<Object>();

		if (selection instanceof IStructuredSelection) {

			final Iterator<?> itr = ((IStructuredSelection) selection).iterator();
			while (itr.hasNext()) {
				Object obj = itr.next();
				if (obj instanceof Event) {
					obj = ScheduleModelUtils.getSegmentStart((Event) obj);
				}

				if (obj instanceof CargoAllocation) {
					validObjects.add(obj);
				} else if (obj instanceof SlotAllocation) {
					final SlotAllocation slotAllocation = (SlotAllocation) obj;
					if (slotAllocation.getCargoAllocation() != null) {
						validObjects.add(slotAllocation.getCargoAllocation());
					}
					if (slotAllocation.getMarketAllocation() != null) {
						validObjects.add(slotAllocation.getMarketAllocation());
					}
				} else if (obj instanceof SlotVisit) {
					validObjects.add((((SlotVisit) obj).getSlotAllocation().getCargoAllocation()));
				} else if (obj instanceof Cargo || obj instanceof Slot) {
					Cargo cargo = null;
					Slot slot = null;
					if (obj instanceof Cargo) {
						cargo = (Cargo) obj;
					} else {
						// Must be a slot
						assert obj instanceof Slot;
						if (obj instanceof LoadSlot) {
							cargo = ((LoadSlot) obj).getCargo();
							slot = (LoadSlot) obj;
						} else if (obj instanceof DischargeSlot) {
							cargo = ((DischargeSlot) obj).getCargo();
							slot = (DischargeSlot) obj;
						}
					}

					if (cargo != null) {

						// TODO: Look up ScheduleModel somehow....

						if (part instanceof IEditorPart) {
							final IEditorPart editorPart = (IEditorPart) part;
							final IEditorInput editorInput = editorPart.getEditorInput();
							if (editorInput instanceof IScenarioServiceEditorInput) {
								final IScenarioServiceEditorInput scenarioServiceEditorInput = (IScenarioServiceEditorInput) editorInput;
								final ScenarioInstance scenarioInstance = scenarioServiceEditorInput.getScenarioInstance();
								final EObject instance = scenarioInstance.getInstance();
								if (instance instanceof LNGScenarioModel) {
									final LNGScenarioModel lngScenarioModel = (LNGScenarioModel) instance;
									final ScheduleModel scheduleModel = lngScenarioModel.getScheduleModel();
									if (scheduleModel != null) {
										final Schedule schedule = scheduleModel.getSchedule();
										if (schedule != null) {
											for (final CargoAllocation cargoAllocation : schedule.getCargoAllocations()) {
												if (cargo == cargoAllocation.getInputCargo()) {
													validObjects.add(cargoAllocation);
													break;
												}
											}
										}
									}
								}
							}
						}
					} else if (slot != null) {

						// TODO: Look up ScheduleModel somehow....

						if (part instanceof IEditorPart) {
							final IEditorPart editorPart = (IEditorPart) part;
							final IEditorInput editorInput = editorPart.getEditorInput();
							if (editorInput instanceof IScenarioServiceEditorInput) {
								final IScenarioServiceEditorInput scenarioServiceEditorInput = (IScenarioServiceEditorInput) editorInput;
								final ScenarioInstance scenarioInstance = scenarioServiceEditorInput.getScenarioInstance();
								final EObject instance = scenarioInstance.getInstance();
								if (instance instanceof LNGScenarioModel) {
									final LNGScenarioModel lngScenarioModel = (LNGScenarioModel) instance;
									final ScheduleModel scheduleModel = lngScenarioModel.getScheduleModel();
									if (scheduleModel != null) {
										final Schedule schedule = scheduleModel.getSchedule();
										if (schedule != null) {
											for (final MarketAllocation marketAllocation : schedule.getMarketAllocations()) {
												if (slot == marketAllocation.getSlot()) {
													validObjects.add(marketAllocation);
													break;
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
			// Remove invalid items
			validObjects.remove(null);
		}
		return validObjects;
	}

	public GridTableViewer getViewer() {
		return viewer;
	}

	/**
	 * Adds a selection listener for the given partID. Listens to everything if null
	 */
	@BindSelectionListener
	public void listenToSelectionsFrom(@Optional @Nullable String partId) {

		org.eclipse.e4.ui.workbench.modeling.ISelectionListener selectionListener = new org.eclipse.e4.ui.workbench.modeling.ISelectionListener() {

			@Override
			public void selectionChanged(final MPart part, final Object selectedObjects) {
				final IWorkbenchPart e3part = SelectionHelper.getE3Part(part);
				{
					// TODO: Ignore navigator
					if (e3part == CargoEconsReportComponent.this) {
						return;
					}
					if (e3part instanceof PropertySheet) {
						return;
					}
				}

				final ISelection selection = SelectionHelper.adaptSelection(selectedObjects);

				// Dispose old data columns - clone list to try to avoid concurrent modification exceptions
				final List<GridViewerColumn> oldColumns = new ArrayList<GridViewerColumn>(dataColumns);
				dataColumns.clear();
				for (final GridViewerColumn gvc : oldColumns) {
					gvc.getColumn().dispose();
				}

				// Find valid, selected objects

				final Collection<Object> validObjects = CargoEconsReportComponent.this.processSelection(e3part, selection);

				for (final Object selectedObject : validObjects) {

					// Currently only CargoAllocations
					if (selectedObject instanceof CargoAllocation) {
						final CargoAllocation cargoAllocation = (CargoAllocation) selectedObject;

						final GridViewerColumn gvc = new GridViewerColumn(viewer, SWT.NONE);
						GridViewerHelper.configureLookAndFeel(gvc);
						// Mark column for disposal on selection change
						dataColumns.add(gvc);
						gvc.getColumn().setText(cargoAllocation.getName());
						gvc.setLabelProvider(new FieldTypeMapperLabelProvider(selectedObject));

						gvc.getColumn().setWidth(100);
					} else if (selectedObject instanceof MarketAllocation) {
						final MarketAllocation cargoAllocation = (MarketAllocation) selectedObject;

						final GridViewerColumn gvc = new GridViewerColumn(viewer, SWT.NONE);
						GridViewerHelper.configureLookAndFeel(gvc);

						// Mark column for disposal on selection change
						dataColumns.add(gvc);
						gvc.getColumn().setText(cargoAllocation.getSlot().getName());
						gvc.setLabelProvider(new FieldTypeMapperLabelProvider(cargoAllocation));

						gvc.getColumn().setWidth(100);
					}
				}

				// Trigger view refresh
				ViewerHelper.refresh(viewer, true);
				// ViewerHelper.setInput(viewer, true, CargoEconsReportRow.getFilteredValues());

			}
		};
		if (partId != null) {
			selectionService.addPostSelectionListener(partId, selectionListener);
		} else {
			selectionService.addPostSelectionListener(selectionListener);
		}
		selectionListeners.add(new Pair<>(partId, selectionListener));
	}

	/**
	 * Adds a selection listener for the given partID. Listens to everything if null
	 */
	@SetEconsMarginMode
	public void setMarginMode(MarginBy mode) {
		options.marginBy = mode;
		ViewerHelper.refresh(getViewer(), false);
	}

}
